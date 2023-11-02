package com.qnkj.core.base.controller;


import com.github.restapi.models.Profile;
import com.qnkj.common.utils.ContextUtils;
import com.qnkj.common.utils.RedisUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.entitys.MenuTree;
import com.qnkj.core.base.entitys.ProfileSettings;
import com.qnkj.core.base.services.IMenuService;
import com.qnkj.core.base.services.IProfileSettingsService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.WebViews;
import com.qnkj.core.webconfigs.configure.WebConstant;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Oldhand
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("menu")
@Api(tags = "框架：菜单接口")
public class MenuController {
    private final IMenuService menuService;
    private final IProfileSettingsService profileSettingsService;

    @GetMapping("{profileid}")
    @ApiOperation("获取当前用户菜单")
    @ApiImplicitParam(name = "profileid", value = "用户ID", required = true, dataType = "String", paramType = "path")
    public WebResponse getUserMenus(HttpServletRequest request, @PathVariable(value="profileid") String profileid) throws WebException {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        Profile currentUser = ProfileUtils.getCurrentUser();
        if (!StringUtils.equalsIgnoreCase(profileid, currentUser.id)) {
            throw new WebException("您无权获取别人的菜单");
        }
        try {
            String selectMenu = "";
            if (httpRequest.containsKey("menu")) {
                selectMenu = httpRequest.get("menu").toString();
            } else {
                ProfileSettings profilesettings = profileSettingsService.get(currentUser.id);
                if(!(!ContextUtils.isJar() && "lcdp".equals(profilesettings.menu) && profilesettings.isDev && (ProfileUtils.isAdmin() || ProfileUtils.isBoss() || ProfileUtils.isAssistant() || ProfileUtils.isSupplierAssistant()))) {
                    List<Map<String, Object>> menugroup = menuService.findMenuGroups(currentUser.id, currentUser.type,false);
                    if (!menugroup.isEmpty()) {
                        boolean isFind = false;
                        for (Object menu : menugroup) {
                            if (((HashMap) menu).getOrDefault("name", "").equals(profilesettings.menu)) {
                                isFind = true;
                                break;
                            }
                        }
                        if (!isFind) {
                            profilesettings.menu = menugroup.get(0).getOrDefault("name", profilesettings.menu).toString();
                        }
                    }
                }
                log.info("profilesettings: {}",profilesettings);
                selectMenu = profilesettings.menu;
            }
            MenuTree<Object> userMenus;
            if("report".equals(selectMenu)) {
                userMenus = this.menuService.findReportMenus(currentUser.id, currentUser.type, selectMenu);
            } else {
                userMenus = this.menuService.findUserMenus(currentUser.id, currentUser.type,selectMenu);
            }
            return new WebResponse().data(userMenus);
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }

    }
    @PostMapping("setmenu")
    @ApiOperation("修改默认菜单配置")
    @ApiImplicitParam(name = "menu", value = "菜单", required = true, dataType = "String", paramType = "query")
    public Object updateSettings(HttpServletResponse response, @RequestParam(required=true,name="menu") String menu) throws IOException {
        if (!Utils.isEmpty(RedisUtils.get(WebConstant.INIT_STATUS))){
            return WebViews.body(response,new WebResponse().alert().message("正在初始化系统，请稍候。。。"));
        }
        try {
            Profile currentUser = ProfileUtils.getCurrentUser();
            ProfileSettings profilesettings = profileSettingsService.get(currentUser.id);
            profilesettings.menu = menu;
            this.profileSettingsService.updateSettings(currentUser.id, profilesettings);
            return new WebResponse().success();
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }
}
