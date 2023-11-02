package com.qnkj.core.base.modules.supplier.SupplierRoles.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.roles.entity.Roles;
import com.qnkj.core.base.modules.supplier.SupplierRoles.services.ISupplierrolesService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2021-04-02
 */

@Validated
@Controller("supplierroles")
@RequiredArgsConstructor
@Api(tags = "管理平台：权限管理")
@Scope("prototype")
@RequestMapping("supplierroles")
public class SupplierrolesController {
    private final ISupplierrolesService supplierrolesService;
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
        Map<String, Object> list = supplierrolesService.getListViewEntity(request, viewEntitys, Roles.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "权限编辑请求接口")
    @GetMapping("/editviewAction")
    public Object editDetails(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        return supplierrolesService.editDetails(httpRequest, model, viewEntitys);
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
            supplierrolesService.saveRoles(httpRequest, viewEntitys);
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
            supplierrolesService.saveDetails(httpRequest, viewEntitys);
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
            supplierrolesService.deleteRoles(httpRequest, viewEntitys);
            return new WebResponse().refresh().success(0, "删除完成");
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "用户弹窗请求接口")
    @PostMapping("/popupview")
    public Object popupView(HttpServletRequest request, Model model) {
        model.addAttribute("ENTITYURL", "supplierroles/listentity/ajax");
        model.addAttribute("ISPOPUP",true);
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        List<String> ids = new ArrayList<>(1);
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
        httpRequest.put("exclude",ids);
        return viewEntitys.popupView(httpRequest, model);
    }

}
