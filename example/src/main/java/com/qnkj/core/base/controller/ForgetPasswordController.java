package com.qnkj.core.base.controller;

import com.github.restapi.models.Profile;
import com.qnkj.common.utils.RedisUtils;
import com.qnkj.common.utils.ValidationUtil;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.services.IProfileService;
import com.qnkj.core.base.services.impl.ValidateCodeService;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Oldhand
 */
@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = "框架：密码找回接口")
public class ForgetPasswordController {

    private final IProfileService profileService;
    private final ValidateCodeService validateCodeService;

    @PostMapping("forgetPassword")
    @ApiOperation("提交忘记密码接口")
    @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
    public WebResponse forgetPassword(@RequestParam(required=true,name="password") String password, HttpServletRequest request) throws WebException {
        try {
            ValidationUtil.checkPassword(password);
            HttpSession session = request.getSession();

            Object mobile = RedisUtils.get("web_forgetpassword_" + session.getId());
            if (mobile == null) {
                throw new WebException("忘记密码操作失败");
            }

            Profile profileInfo  = profileService.findByMobile(mobile.toString());
            profileService.updatePassword(profileInfo.id, password);
            return new WebResponse().success();
        } catch(Exception e) {
            throw new WebException(e.getMessage());
        }

    }

    @PostMapping("saveforgetPassword")
    @ApiOperation("保存忘记密码信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "verifyCode", value = "验证码", required = true, dataType = "String", paramType = "query")
    })
    public WebResponse saveforgetPassword(
            @RequestParam(required=true,name="mobile")  String mobile,
            @RequestParam(required=true,name="smsverifycode") String smsverifycode, HttpServletRequest request) throws WebException {
        HttpSession session = request.getSession();
        validateCodeService.checkSmsCode(session.getId(), mobile, smsverifycode);
        RedisUtils.set("web_forgetpassword_"  + session.getId(), mobile, 1800L);
        return new WebResponse().success();
    }


}
