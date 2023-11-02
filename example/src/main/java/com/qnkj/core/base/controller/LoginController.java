package com.qnkj.core.base.controller;

import com.github.restapi.models.Profile;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.*;
import com.qnkj.core.base.entitys.LoginLog;
import com.qnkj.core.base.modules.settings.loginlog.service.ILoginlogService;
import com.qnkj.core.base.services.IProfileService;
import com.qnkj.core.base.services.impl.ValidateCodeService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.annotation.Limit;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oldhand
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = "框架：登录接口")
public class LoginController {

    private final IProfileService profileService;
    private final ValidateCodeService validateCodeService;
    private final ILoginlogService loginLogService;

    @PostMapping("login")
    @ApiOperation("登录认证接口")
    @Limit(key = "login", period = 60, count = 10, name = "登录接口", prefix = "limit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "verifyCode", value = "验证码", required = false, dataType = "String", paramType = "query")
    })
    public WebResponse login(
            @RequestParam(required=true,name="username") String username,
            @RequestParam(required=true,name="password") String password,
            @RequestParam(required=false,name="verifyCode") String verifyCode,
            @RequestParam(required=false,name="rememberMe") boolean rememberMe, HttpServletRequest request) throws WebException {

        HttpSession session = request.getSession();
        if (validateCodeService.checkLoginErrorSign(request)) {
            validateCodeService.check(session.getId(), verifyCode);
        }
        SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
        try {
            username = username.replace(" ", "");
            password = password.replace(" ", "");
            ValidationUtil.checkPassword(password);
            Profile profileInfo;
            if (ValidationUtil.isMobile(username)) {
                profileInfo = profileService.findByMobile(username);
            } else {
                profileInfo = profileService.findByUsername(username);
            }
            if (!profileInfo.status) {
                throw new WebException("用户已停用！");
            }
            if (saasUtils.isSingle()) {
                if(ProfileUtils.isLogin(profileInfo.id)){
                    ProfileUtils.logout(profileInfo.id);
                    throw new WebException("检测到您的账号已经登录，请注意保护您的密码！");
                }
            }
            if (profileInfo.type.compareTo("supplier") == 0) {
                try {
                    Object result = CallbackUtils.invoke("loginCallback", profileInfo);
                    if (!Utils.isEmpty(result)) {
                        log.info("LoginCallback: {} ", result);
                        if (result instanceof Boolean && !((Boolean) result)) {
                            throw new WebException("登录失败！");
                        }
                    }
                } catch (Exception e) {
                    log.error("LoginCallback: {} ", e.getMessage());
                    throw e;
                }
            }

            String profileid = profileInfo.id;
            UsernamePasswordToken token = new UsernamePasswordToken(profileid, password, rememberMe);
            ProfileUtils.login(token);
            validateCodeService.clearLoginErrorSign(request);
            // 保存登录日志
            LoginLog loginLog = new LoginLog();
            loginLog.profileid = profileid;
            loginLog.setSystemBrowserInfo(request);
            this.loginLogService.saveLoginLog(request, loginLog);
            try {
                CallbackUtils.invoke("loginedCallback", profileInfo);
            } catch (Exception e) {
                log.error("LoginedCallback: {} ", e.getMessage());
            }
            return new WebResponse().success();
        } catch (WebException e) {
            validateCodeService.setLoginErrorSign(request);
            throw e;
        } catch (Exception e) {
            validateCodeService.setLoginErrorSign(request);
            throw new WebException(e.getMessage());
        }
    }


    @GetMapping("index/{profileid}")
    @ApiOperation("登录成功显示首页")
    @ApiImplicitParam(name = "profileid", value = "用户ID", required = true, dataType = "String", paramType = "path")
    public WebResponse index(HttpServletRequest request,@PathVariable(value="profileid") String profileid) {
        try {
            this.loginLogService.checkLoginLog(request);
            Map<String, Object> data = new HashMap<>(5);
            // 获取系统访问记录
            Long totalVisitCount = this.loginLogService.findTotalVisitCount();
            data.put("totalVisitCount", totalVisitCount);
            Long todayVisitCount = this.loginLogService.findTodayVisitCount();
            data.put("todayVisitCount", todayVisitCount);
            Long todayIp = this.loginLogService.findTodayIp();
            data.put("todayIp", todayIp);
            // 获取近期系统访问记录
            List<Map<String, Object>> lastSevenVisitCount = this.loginLogService.findLastTenDaysVisitCount(null);
            data.put("lastSevenVisitCount", lastSevenVisitCount);
            List<Map<String, Object>> lastSevenUserVisitCount = this.loginLogService.findLastTenDaysVisitCount(profileid);
            data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
            String lastLoginTime = this.loginLogService.getLastLoginTime(profileid);
            data.put("lastLoginTime", lastLoginTime);
            return new WebResponse().success().data(data);
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }

    @GetMapping("images/captcha")
    @ApiOperation("获取验证码")
    @Limit(key = "get_captcha", period = 60, count = 20, name = "获取验证码", prefix = "limit")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, WebException {
        validateCodeService.create(request, response);
    }
}
