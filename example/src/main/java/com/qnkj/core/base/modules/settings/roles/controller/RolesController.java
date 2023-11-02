package com.qnkj.core.base.modules.settings.roles.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.roles.entity.Roles;
import com.qnkj.core.base.modules.settings.roles.service.IRolesService;
import com.qnkj.core.webconfigs.WebViews;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author clubs
 */
@Validated
@Controller("Settings-Roles")
@RequiredArgsConstructor
@Api(tags = "系统设置：权限管理")
@Scope("prototype")
@RequestMapping("roles")
public class RolesController {
    private final IRolesService rolesService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "权限列表请求接口")
    @GetMapping("/listview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("TABNAME", "权限管理");
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "权限列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String, Object> list = rolesService.getListViewEntity(request, viewEntitys, Roles.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "权限编辑请求接口")
    @GetMapping("/editviewAction")
    public Object editDetails(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        return rolesService.editDetails(httpRequest, model, viewEntitys);
    }

    @ApiOperation(value = "权限新增请求接口")
    @PostMapping("/editviewAction/ajax")
    public Object addView(HttpServletRequest request, Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        return WebViews.view("modules/settings/roles/AddView");
    }

    @ApiOperation(value = "权限信息保存接口")
    @PostMapping("/editviewAction/save")
    @ResponseBody
    public Object save(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            rolesService.saveRoles(httpRequest, viewEntitys);
            return new WebResponse().refresh().success(0, "保存完成");
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "权限信息保存接口")
    @PostMapping("/editviewAction/savedetails")
    @ResponseBody
    public Object saveDetails(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            rolesService.saveDetails(httpRequest, viewEntitys);
            return new WebResponse().refresh().code(0);
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "权限信息删除接口")
    @PostMapping("/deleteAction/ajax")
    @ResponseBody
    public Object delete(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            rolesService.delete(httpRequest, viewEntitys);
            return new WebResponse().refresh().success(0, "删除完成");
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

}
