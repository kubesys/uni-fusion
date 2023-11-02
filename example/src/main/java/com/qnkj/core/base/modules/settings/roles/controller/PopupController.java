package com.qnkj.core.base.modules.settings.roles.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.roles.service.IRolesService;
import com.qnkj.core.utils.ProfileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Validated
@Controller("Settings-Roles-Popup")
@RequiredArgsConstructor
@Api(tags = "系统设置：权限管理")
@Scope("prototype")
@RequestMapping("roles")
public class PopupController {
    private final IRolesService rolesService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init(){
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "用户弹窗请求接口")
    @PostMapping("/popupview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("ENTITYURL", "roles/listentity/ajax");
        model.addAttribute("ISPOPUP",true);
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        String supperID = rolesService.getIdByName("超级删除");
        if(!Utils.isEmpty(supperID)){
            List<String> ids = new ArrayList<>(1);
            ids.add(supperID);
            if(!Utils.isEmpty(httpRequest.getOrDefault("select",""))){
                if(httpRequest.get("select") instanceof String) {
                    ids.add(httpRequest.get("select").toString());
                }else if(httpRequest.get("select") instanceof List){
                    ids.addAll((List<String>)httpRequest.get("select"));
                }
            }
            if (!Utils.isEmpty(httpRequest.getOrDefault("exclude", ""))) {
                if(httpRequest.get("exclude") instanceof String) {
                    ids.add(httpRequest.get("exclude").toString());
                }else if(httpRequest.get("exclude") instanceof List){
                    ids.addAll((List<String>)httpRequest.get("exclude"));
                }
            }
            if(ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) {
                String excludeId = rolesService.getIdByName("未审核企业权限");
                if(Utils.isNotEmpty(excludeId)){
                    ids.add(excludeId);
                }
                excludeId = rolesService.getIdByName("企业权限");
                if(Utils.isNotEmpty(excludeId)){
                    ids.add(excludeId);
                }
            }
            httpRequest.put("exclude",ids);
        }
        return viewEntitys.popupView(httpRequest, model);
    }

}
