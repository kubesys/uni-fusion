package com.qnkj.core.base.controller;

import com.github.restapi.models.Profile;
import com.qnkj.common.utils.ValidationUtil;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.entitys.ProfileSettings;
import com.qnkj.core.base.services.IProfileService;
import com.qnkj.core.base.services.IProfileSettingsService;
import com.qnkj.core.base.services.impl.ValidateCodeService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.WebViews;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oldhand
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@Api(tags = "框架：用户接口")
@Controller("Settings-Profile")
public class ProfileController {

    private final IProfileService profileService;
    private final IProfileSettingsService profileSettingsService;
    private final ValidateCodeService validateCodeService;

    @ApiOperation("显示用户修改密码")
    @GetMapping("/password/update")
    public String passwordUpdate() {
        return WebViews.view("system/user/passwordUpdate");
    }

    @ApiOperation("显示用户更换手机")
    @GetMapping("/user/mobile/update")
    public String mobileUpdate() {
        return WebViews.view("system/user/mobileUpdate");
    }

    @ApiOperation("显示用户个人中心")
    @GetMapping("/user/profile")
    public String userProfile() {
        return WebViews.view("system/user/userProfile");
    }

    @ApiOperation("显示用户修改图像")
    @GetMapping("/user/avatar")
    public String userAvatar() {
        return WebViews.view("system/user/avatar");
    }

    @ApiOperation("显示修改用户基本资料")
    @GetMapping("/user/profile/update")
    public String profileUpdate() {
        return WebViews.view("system/user/profileUpdate");
    }

    @PostMapping("/profile/password/update")
    @ApiOperation("修改用户密码")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "smsverifyCode", value = "短信验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String", paramType = "query")
    })
    public WebResponse updatePassword(
            @RequestParam(required=true,name="smsverifyCode") String smsverifyCode,
            @RequestParam(required=true,name="newPassword") String newPassword, HttpServletRequest request) {
        try {
            Profile user = ProfileUtils.getCurrentUser();
            HttpSession session = request.getSession();
            newPassword = newPassword.replace(" ","");
            ValidationUtil.checkPassword(newPassword);
            validateCodeService.checkSmsCode(session.getId(), user.mobile, smsverifyCode);
            this.profileService.updatePassword(user.id, newPassword);
            return new WebResponse().success();
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }

    @PostMapping("/profile/mobile/update")
    @ApiOperation("修改用户手机号码密码")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "smsverifyCode", value = "短信验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "newmobile", value = "新手机", required = true, dataType = "String", paramType = "query"),
    })
    public WebResponse updateMobile(
            @RequestParam(required=true,name="smsverifyCode")  String smsverifyCode,
            @RequestParam(required=true,name="newmobile")  String newmobile, HttpServletRequest request) {
        try {
            Profile user = ProfileUtils.getCurrentUser();
            HttpSession session = request.getSession();
            validateCodeService.checkSmsCode(session.getId(), newmobile, smsverifyCode);
            this.profileService.updateMobile(user.id, newmobile);
            return new WebResponse().success();
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }

    @GetMapping("/profile/avatar/{image}")
    @ApiOperation("修改用户头像")
    @ResponseBody
    @ApiImplicitParam(name = "image", value = "头像", required = true, dataType = "String", paramType = "path")
    public WebResponse updateAvatar(@PathVariable(value="image") String image) {
        try {
            Profile user = ProfileUtils.getCurrentUser();
            this.profileService.updateAvatar(user.id, image);
            user.link = image;
            ProfileUtils.updateCurrentUser(user);
            return new WebResponse().success();
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }

    @PostMapping("/profile/settings/update")
    @ApiOperation("修改用户系统配置")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "theme", value = "主题", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "isTab", value = "选项卡开关", required = true, dataType = "String", paramType = "query"),
    })
    public WebResponse updateSettings(@RequestParam(required=true,name="theme") String theme,
                                      @RequestParam(required=true,name="isTab") String isTab) {
        try {
            Profile user = ProfileUtils.getCurrentUser();
            ProfileSettings  profilesettings = this.profileSettingsService.get(user.id);
            profilesettings.theme = theme;
            profilesettings.istab = isTab;
            this.profileSettingsService.updateSettings(user.id, profilesettings);
            return new WebResponse().success();
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }

    @PostMapping("/profile/update")
    @ApiOperation("修改用户个人信息")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "givenname", value = "昵称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "province", value = "省份", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "city", value = "城市", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "gender", value = "性别", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "birthdate", value = "生日", required = false, dataType = "String", paramType = "query")
    })
    public WebResponse updateProfile(
            @RequestParam(required=true,name="givenname") String givenname,
            @RequestParam(required=false,name="province") String province,
            @RequestParam(required=false,name="city") String city,
            @RequestParam(required=false,name="gender") String gender,
            @RequestParam(required=false,name="birthdate") String birthdate) throws WebException {
        try {
            province = URLDecoder.decode(province,"UTF-8");
            city = URLDecoder.decode(city,"UTF-8");
            gender = URLDecoder.decode(gender,"UTF-8");
            givenname = URLDecoder.decode(givenname,"UTF-8");
            Profile user = ProfileUtils.getCurrentUser();
            user.givenname = givenname;
            user.gender = gender;
            user.birthdate = birthdate;
            user.province = province;
            user.city = city;
            Map<String, Object> json = new HashMap();
            json.put("givenname",givenname);
            json.put("gender",gender);
            json.put("birthdate",birthdate);
            json.put("province",province);
            json.put("city",city);
            this.profileService.updateProfile(user.id, json);
            ProfileUtils.updateCurrentUser(user);
            return new WebResponse().success();
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }

}
