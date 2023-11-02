package com.qnkj.core.base.controller;

import com.qnkj.common.utils.RedisUtils;
import com.qnkj.common.utils.ValidationUtil;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.entitys.RegisterInfo;
import com.qnkj.core.base.services.IProfileService;
import com.qnkj.core.base.services.IRegisterInfo;
import com.qnkj.core.base.services.impl.ValidateCodeService;
import com.qnkj.core.utils.IpUtil;
import com.qnkj.core.webconfigs.annotation.Limit;
import com.qnkj.core.webconfigs.configure.WebConstant;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Oldhand
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("register")
@Api(tags = "框架：注册接口")
public class RegisterController {

    private final IProfileService profileService;
    private final IRegisterInfo registerInfo;
    private final ValidateCodeService validateCodeService;

    private final List<String> registerProcessing = new ArrayList<>();

    @GetMapping("getsession")
    @ApiOperation("获取保存的注册信息接口")
    public WebResponse getSession(HttpServletRequest request) throws WebException {
        try {
            HttpSession session = request.getSession();
            Object profileInRedis = RedisUtils.get(WebConstant.REGISTER_PREFIX + session.getId());
            if (profileInRedis == null) {
                throw new WebException("获取保存的注册信息接口失败");
            }
            RegisterInfo registerinfo = new RegisterInfo(profileInRedis.toString());
            return new WebResponse().success().data(registerinfo);
        } catch(Exception e) {
            RegisterInfo registerinfo = new RegisterInfo();
            return new WebResponse().success().data(registerinfo);
        }

    }

    @PostMapping("submituserinfo")
    @ApiOperation("提交账号信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "givenname", value = "昵称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "browser", value = "浏览器", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "system", value = "操作系统", required = true, dataType = "String", paramType = "query")
    })
    public WebResponse submituserinfo(
            @RequestParam(required=true,name="username") String username,
            @RequestParam(required=true,name="email") String email,
            @RequestParam(required=true,name="givenname") String givenname,
            @RequestParam(required=true,name="password") String password,
            @RequestParam(required=true,name="browser")String browser,
            @RequestParam(required=true,name="system") String system, HttpServletRequest request) throws WebException {
        try {
            username = username.replace(" ","");
            email = email.replace(" ","");
            givenname = givenname.replace(" ","");
            password = password.replace(" ","");
            givenname = givenname.replace(" ","");
            system = system.replace(" ","");
            ValidationUtil.checkPassword(password);
            HttpSession session = request.getSession();

            Object profileInRedis = RedisUtils.get(WebConstant.REGISTER_PREFIX + session.getId());
            if (profileInRedis == null) {
                throw new WebException("提交账号信息失败");
            }
            RegisterInfo registerinfo = new RegisterInfo(profileInRedis.toString());
            registerinfo.username = username.toLowerCase();
            registerinfo.email = email;
            registerinfo.givenname = givenname;
            registerinfo.password = password;
            registerinfo.browser = browser;
            registerinfo.system = system;
            registerinfo.regip = IpUtil.getIpAddr(request);
            log.info("提交账号信息: {}",registerinfo.toString());
            RedisUtils.set(WebConstant.REGISTER_PREFIX  + session.getId(), registerinfo.toString(), 1800L);
            registerInfo.update(registerinfo);

            return new WebResponse().success();
        } catch(Exception e) {
            throw new WebException(e.getMessage());
        }

    }

    private final Pattern IS_SUBMIT_PATTERN = Pattern.compile("^(.*)/(.*)$");
    @PostMapping("submit")
    @ApiOperation("立即注册接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "suppliername", value = "客户全称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "legalpersonname", value = "企业法人", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "legalpersonidentitycard", value = "法人身份证号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "citypicker", value = "省份/城市", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "businesslicense", value = "营业执照", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "businesslicenseimg", value = "营业执照图片", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "legalpersoncertificate", value = "法人身份证正面", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "legalpersoncertificatereverse", value = "法人身份证反面", required = true, dataType = "String", paramType = "query")
    })
    public WebResponse submit(
            @RequestParam(required=true,name="suppliername")  String suppliername,
            @RequestParam(required=true,name="legalpersonname")  String legalpersonname,
            @RequestParam(required=true,name="legalpersonidentitycard")  String legalpersonidentitycard,
            @RequestParam(required=true,name="citypicker")  String citypicker,
            @RequestParam(required=true,name="businesslicense")  String businesslicense,
            @RequestParam(required=true,name="businesslicenseimg")  String businesslicenseimg,
            @RequestParam(required=true,name="legalpersoncertificate")  String legalpersoncertificate,
            @RequestParam(required=true,name="legalpersoncertificatereverse")  String legalpersoncertificatereverse, HttpServletRequest request) throws WebException {
        try {
            suppliername = suppliername.replace(" ","");
            legalpersonname = legalpersonname.replace(" ","");
            legalpersonidentitycard = legalpersonidentitycard.replace(" ","");
            citypicker = citypicker.replace(" ","");
            businesslicense = businesslicense.replace(" ","");
            legalpersoncertificate = legalpersoncertificate.replace(" ","");
            HttpSession session = request.getSession();
            if (registerProcessing.contains( session.getId())) {
                throw new WebException("请不要重复提交！");
            }
            registerProcessing.add(session.getId());
            Object profileInRedis = RedisUtils.get(WebConstant.REGISTER_PREFIX + session.getId());
            if (profileInRedis == null) {
                throw new WebException("立即注册失败");
            }
            Matcher matcher = IS_SUBMIT_PATTERN.matcher(citypicker);
            String province = "";
            String city = "";
            if(matcher.find()){
                province = matcher.group(1);
                city = matcher.group(2);
            }
            RegisterInfo registerinfo = new RegisterInfo(profileInRedis.toString());
            registerinfo.suppliers_name = suppliername;
            registerinfo.legal_person_name = legalpersonname;
            registerinfo.legal_person_identity_card = legalpersonidentitycard;
            registerinfo.province = province;
            registerinfo.city = city;
            registerinfo.business_license = businesslicense;
            registerinfo.business_license_img_url = businesslicenseimg;
            registerinfo.legal_person_certificate_img_url = legalpersoncertificate;
            registerinfo.legal_person_certificate_reverse_img_url = legalpersoncertificatereverse;
            log.info("立即注册: {}" , registerinfo.toString());

            RedisUtils.set(WebConstant.REGISTER_PREFIX  + session.getId(), registerinfo.toString(), 1800L);
            registerInfo.update(registerinfo);
            registerInfo.submit(registerinfo.mobile);
            if (registerProcessing.contains( session.getId())) {
                registerProcessing.remove(session.getId());
            }
            return new WebResponse().success();
        } catch(Exception e) {
            throw new WebException(e.getMessage());
        }

    }

    @PostMapping("submitmobile")
    @ApiOperation("提交注册手机接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "verifycode", value = "验证码", required = true, dataType = "String", paramType = "query")
    })
    public WebResponse submitmobile(
            @RequestParam(required=true,name="mobile") String mobile,
            @RequestParam(required=true,name="smsverifycode") String smsverifycode, HttpServletRequest request) throws WebException {
        mobile = mobile.replace(" ","");
        HttpSession session = request.getSession();
        validateCodeService.checkSmsCode(session.getId(), mobile, smsverifycode);
        RegisterInfo registerinfo;
        try {
            registerinfo = registerInfo.get(mobile);
        } catch(Exception e) {
            registerinfo = new RegisterInfo();
            registerinfo.regioncode = "86";
            registerinfo.mobile = mobile;
            registerInfo.update(registerinfo);
        }
        RedisUtils.set(WebConstant.REGISTER_PREFIX  + session.getId(), registerinfo.toString(), 1800L);
        return new WebResponse().success();
    }

    @PostMapping("profileisexist")
    @ApiOperation("检测用户是否已经注册")
    @Limit(key = "profileisexist", period = 60, count = 30, name = "检测用户是否已经注册", prefix = "limit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "关键词", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "value", value = "值", required = true, dataType = "String", paramType = "query")
    })
    public WebResponse profileisexist( @RequestParam(required=true,name="type") String type,
                                       @RequestParam(required=true,name="value") String value,HttpServletRequest request) throws WebException {

        try {
            Map<String, Object> result = new HashMap<>();
            if (type.compareTo("mobile") == 0) {
                if (profileService.isExist(type,value)) {
                    result.put("code", 0);
                    result.put("message", value + "已经注册");
                    return new WebResponse().success().data(result);
                } else {
                    result.put("code", 1);
                    result.put("message", value + "没有注册");
                    return new WebResponse().success().data(result);
                }
            } else {
                HttpSession session = request.getSession();
                Object profileInRedis = RedisUtils.get(WebConstant.REGISTER_PREFIX + session.getId());
                if (profileInRedis == null) {
                    throw new WebException("检测企业是否已经注册失败");
                }
                RegisterInfo registerinfo = new RegisterInfo(profileInRedis.toString());
                if (profileService.isExist(type,value)) {
                    result.put("code", 0);
                    result.put("message", value + "已经注册");
                    return new WebResponse().success().data(result);
                } else {
                    if ( registerInfo.isExist(registerinfo.mobile,type,value)) {
                        result.put("code", 0);
                        result.put("message", value + "已经占用");
                        return new WebResponse().success().data(result);
                    } else {
                        result.put("code", 1);
                        result.put("message", value + "没有注册");
                        return new WebResponse().success().data(result);
                    }
                }
            }
        } catch(Exception e) {
            throw new WebException(e.getMessage());
        }
    }
    @PostMapping("supplieisexist")
    @ApiOperation("检测企业是否已经注册")
    @Limit(key = "supplieisexist", period = 60, count = 30, name = "检测企业是否已经注册", prefix = "limit")
    @ApiImplicitParam(name = "value", value = "名称", required = true, dataType = "String", paramType = "query")
    public WebResponse supplieisexist(@RequestParam(required=true,name="value") String value,HttpServletRequest request) throws WebException {
        HttpSession session = request.getSession();
        Object profileInRedis = RedisUtils.get(WebConstant.REGISTER_PREFIX + session.getId());
        if (profileInRedis == null) {
            throw new WebException("检测企业是否已经注册失败!");
        }
        RegisterInfo registerinfo = new RegisterInfo(profileInRedis.toString());
        try {
            Map<String, Object> result = new HashMap<>();
            if ( registerInfo.isExistSupplier(registerinfo.mobile, value)) {
                result.put("code", 0);
                result.put("message", value + "已经注册");
                return new WebResponse().success().data(result);
            } else {
                result.put("code", 1);
                result.put("message", value + "没有注册");
                return new WebResponse().success().data(result);
            }
        } catch(Exception e) {
            throw new WebException(e.getMessage());
        }
    }

}
