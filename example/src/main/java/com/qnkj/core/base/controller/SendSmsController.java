package com.qnkj.core.base.controller;

import com.github.restapi.models.Profile;
import com.qnkj.common.utils.SmsUtils;
import com.qnkj.common.utils.ValidationUtil;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.services.IProfileService;
import com.qnkj.core.base.services.impl.ValidateCodeService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;

/**
 * @author Oldhand
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = "框架：短信接口")
public class SendSmsController {

    private final ValidateCodeService validateCodeService;
    private final IProfileService profileService;

    /**
     * make 生成六位随机验证码
     */
    public String make() {
        return String.valueOf(new SecureRandom().nextInt(899999) + 100000);
    }

    @PostMapping("sendsms")
    @ApiOperation("发送短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "verifycode", value = "验证码", required = true, dataType = "String", paramType = "query")
    })
    public WebResponse sendsms(@RequestParam(required=true,name="mobile") String mobile,
                               @RequestParam(required=true,name="verifycode") String verifycode, HttpServletRequest request) throws WebException {
        HttpSession session = request.getSession();
        validateCodeService.check(session.getId(), verifycode);

        if (validateCodeService.existSmsCode(session.getId())) {
            throw new WebException("短信发送太频繁，请稍候再试!");
        }

        try {
            String smsvVerifyCode = make();
            log.warn("短信验证码: {} ", smsvVerifyCode);

            validateCodeService.saveSmsCode(session.getId(), mobile, smsvVerifyCode);
            SmsUtils.sendSms(mobile,smsvVerifyCode);

            return new WebResponse().success();
        } catch(Exception e) {
            throw new WebException(e.getMessage());
        }
    }

    @PostMapping("user/mobile/sendsms")
    @ApiOperation("为修改手机号码发送短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "verifycode", value = "验证码", required = true, dataType = "String", paramType = "query")
    })
    public WebResponse userMobileSendsms(@RequestParam(required=true,name="mobile") String mobile,
                                         @RequestParam(required=true,name="verifycode") String verifycode, HttpServletRequest request) throws WebException {
        HttpSession session = request.getSession();
        validateCodeService.check(session.getId(), verifycode);

        if (validateCodeService.existSmsCode(session.getId())) {
            throw new WebException("短信发送太频繁，请稍候再试!");
        }

        try {
            if (!ValidationUtil.isMobile(mobile)) {
                throw new WebException("手机号码不符合规范!");
            }
            Profile user = ProfileUtils.getCurrentUser();
            if (mobile.compareTo(user.mobile) == 0) {
                throw new WebException("新手机号码与原手机号码相同!");
            }
            if (!profileService.allowChangeMobile(user.id,mobile)) {
                throw new WebException("新手机号码已经使用，请更换!");
            }
            String smsvVerifyCode = make();
            log.warn("短信验证码: {} ", smsvVerifyCode);

            validateCodeService.saveSmsCode(session.getId(), mobile, smsvVerifyCode);
            SmsUtils.sendSms(mobile,smsvVerifyCode);

            return new WebResponse().success();
        } catch(Exception e) {
            throw new WebException(e.getMessage());
        }
    }

}
