package com.qnkj.core.base.services.impl;

import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.RedisUtils;
import com.qnkj.core.base.entitys.SendSms;
import com.qnkj.core.webconfigs.configure.ImageType;
import com.qnkj.core.webconfigs.configure.WebConstant;
import com.qnkj.core.webconfigs.exception.WebException;
import com.qnkj.core.webconfigs.properties.ValidateCodeProperties;
import com.qnkj.core.webconfigs.properties.WebProperties;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 验证码服务
 *
 * @author Oldhand
 */
@Service
@RequiredArgsConstructor
public class ValidateCodeService {

    private final WebProperties properties;


    public void saveSmsCode(String key,String mobile,String smsVerifycode) throws IOException {
        SendSms sms = new SendSms(mobile,smsVerifycode, DateTimeUtils.gettimeStamp());
        RedisUtils.set(WebConstant.SMSCODE_PREFIX  + key, sms.toString(), 1800L);
    }

    public boolean existSmsCode(String key) throws WebException {
        Object codeInRedis = RedisUtils.get(WebConstant.SMSCODE_PREFIX + key);
        if (codeInRedis == null) {
            return false;
        }
        SendSms sms = new SendSms(codeInRedis.toString());
        if (DateTimeUtils.gettimeStamp() > sms.time + 59L) {
            return false;
        }

        return true;
    }

    public void checkSmsCode(String key, String mobile, String value) throws WebException {
        Object codeInRedis = RedisUtils.get(WebConstant.SMSCODE_PREFIX + key);
        if (StringUtils.isBlank(value)) {
            throw new WebException("请输入短信验证码");
        }
        if (codeInRedis == null) {
            throw new WebException("没有短信验证码，或已经过期");
        }
        SendSms sms = new SendSms(codeInRedis.toString());
        if (mobile.compareTo(sms.mobile) != 0) {
            throw new WebException("手机号码与短信验证码不匹配");
        }
        if (!StringUtils.equalsIgnoreCase(value, sms.verifyCode)) {
            throw new WebException("短信验证码不正确");
        }
    }
    public void setLoginErrorSign(HttpServletRequest request) {
        HttpSession session = request.getSession();
        RedisUtils.set(WebConstant.LOGIN_SIGN_PREFIX  + session.getId(), "1");
    }
    public Boolean checkLoginErrorSign(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (RedisUtils.hasKey(WebConstant.LOGIN_SIGN_PREFIX  + session.getId())) {
            Object codeInRedis = RedisUtils.get(WebConstant.LOGIN_SIGN_PREFIX + session.getId());
            if (codeInRedis == null) {
                return false;
            }
            if ("1".equals(codeInRedis.toString())) {
                return true;
            }
        }
        return false;
    }
    public void clearLoginErrorSign(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (RedisUtils.hasKey(WebConstant.LOGIN_SIGN_PREFIX  + session.getId())) {
            RedisUtils.del(WebConstant.LOGIN_SIGN_PREFIX  + session.getId());
        }
    }

    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String key = session.getId();
        ValidateCodeProperties code = properties.getCode();
        setHeader(response, code.getType());

        Captcha captcha = createCaptcha(code);
        RedisUtils.set(WebConstant.CODE_PREFIX  + key, StringUtils.lowerCase(captcha.text()), code.getTime());
        captcha.out(response.getOutputStream());
    }


    public void check(String key, String value) throws WebException {
        Object codeInRedis = RedisUtils.get(WebConstant.CODE_PREFIX + key);
        if (StringUtils.isBlank(value)) {
            throw new WebException("请输入验证码");
        }
        if (codeInRedis == null) {
            throw new WebException("验证码已过期");
        }
        if (!StringUtils.equalsIgnoreCase(value, String.valueOf(codeInRedis))) {
            throw new WebException("验证码不正确");
        }
    }

    private Captcha createCaptcha(ValidateCodeProperties code) {
        Captcha captcha;
        if (StringUtils.equalsIgnoreCase(code.getType(), ImageType.GIF)) {
            captcha = new GifCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        } else {
            captcha = new SpecCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        }
        captcha.setCharType(code.getCharType());
        return captcha;
    }

    private void setHeader(HttpServletResponse response, String type) {
        if (StringUtils.equalsIgnoreCase(type, ImageType.GIF)) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
    }
}
