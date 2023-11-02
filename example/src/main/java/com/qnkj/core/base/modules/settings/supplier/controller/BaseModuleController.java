package com.qnkj.core.base.modules.settings.supplier.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.supplier.entitys.Supplier;
import com.qnkj.core.base.modules.settings.supplier.services.ISupplierService;
import com.qnkj.core.utils.ProfileUtils;
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
 * create by Auto Generator
 * create date 2020-11-23
 */

@Validated
@Controller("basesupplier")
@RequiredArgsConstructor
@Api(tags = "系统设置：子平台")
@Scope("prototype")
@RequestMapping("supplier")
public class BaseModuleController {
    private final ISupplierService supplierService;
    private BaseEntityUtils viewEntitys = null;
    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "列表请求接口")
    @GetMapping("/listview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String,Object> list = supplierService.getListViewEntity(request,viewEntitys, Supplier.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "编辑请求接口")
    @GetMapping("/editviewAction")
    public Object editView(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        Supplier result = (Supplier) supplierService.getObjectByRecord(httpRequest.get("record"),viewEntitys,Supplier.class);
        if(result != null) {
            model.addAttribute("TABNAME", Boolean.TRUE.equals(result.getEditState()) ? viewEntitys.getModuleLabel() + "详情" : "新建" + viewEntitys.getModuleLabel());
            model.addAttribute("ISEDIT", result.getEditState());
            model.addAttribute("RECORD", result.id);
            model.addAttribute("AUTHOR", ProfileUtils.getCurrentUser().givenname);
            model.addAttribute("PUBLISHED", result.published);
            if(result.approvalstatus.equals(1) || result.approvalstatus.equals(2) || result.approvalstatus.equals(4)){
                model.addAttribute("READONLY",true);
            }
            return viewEntitys.editView(httpRequest, model, result);
        } else {
            return WebViews.view("error/403");
        }
    }
    @ApiOperation(value = "保存请求接口")
    @PostMapping("/save")
    @ResponseBody
    public Object editViewSave(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            supplierService.save(httpRequest,viewEntitys,Supplier.class);
            return new WebResponse().refresh().success(0,"保存完成").record(httpRequest.get("record"));
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }
    @ApiOperation(value = "删除请求接口")
    @PostMapping("/deleteAction/ajax")
    @ResponseBody
    public Object delete(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            supplierService.delete(httpRequest,viewEntitys);
            return new WebResponse().refresh().success(0,"删除成功");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }
}
