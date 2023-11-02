package com.qnkj.core.base.modules.supplier.SupplierUsers.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.supplier.SupplierUsers.entitys.Supplierusers;
import com.qnkj.core.base.modules.supplier.SupplierUsers.services.ISupplierusersService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.WebViews;
import com.qnkj.core.webconfigs.annotation.Log;
import com.qnkj.core.webconfigs.exception.WebException;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2021-03-31
 */

@Validated
@Controller("supplierusers")
@RequiredArgsConstructor
@Api(tags = "管理平台：用户管理")
@Scope("prototype")
@RequestMapping("supplierusers")
public class SupplierusersController {
    private final ISupplierusersService supplierusersService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "列表请求接口")
    @GetMapping("/listview")
    @Log("显示用户管理列表视图")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String,Object> list = supplierusersService.getListViewEntity(request,viewEntitys, Supplierusers.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "编辑请求接口")
    @GetMapping("/editviewAction")
    @Log("显示用户管理编辑视图")
    public Object editView(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        Supplierusers result = (Supplierusers) supplierusersService.getObjectByRecord(httpRequest.get("record"),viewEntitys,Supplierusers.class);
        if(result != null) {
            model.addAttribute("TABNAME", result.getEditState() ? viewEntitys.getModuleLabel() + "详情" : "新建" + viewEntitys.getModuleLabel());
            model.addAttribute("ISEDIT", result.getEditState());
            model.addAttribute("RECORD", result.id);
            model.addAttribute("AUTHOR", ProfileUtils.getCurrentUser().givenname);
            model.addAttribute("PUBLISHED", result.published);
            return viewEntitys.editView(httpRequest, model, result);
        } else {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation(value = "保存请求接口")
    @PostMapping("/save")
    @ResponseBody
    @Log("保存用户管理")
    public Object editViewSave(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            viewEntitys.formatRequest(httpRequest);
            supplierusersService.save(httpRequest);
            return new WebResponse().refresh().record(httpRequest.get("record")).success(0,"保存完成");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "删除请求接口")
    @PostMapping("/deleteAction/ajax")
    @ResponseBody
    @Log("删除用户管理")
    public Object delete(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            supplierusersService.delete(httpRequest,viewEntitys);
            return new WebResponse().refresh().success(0,"删除成功");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "列表弹窗请求接口")
    @PostMapping("/popupview")
    public Object popupView(HttpServletRequest request, Model model) {
        model.addAttribute("ENTITYURL", "supplierusers/listentity/ajax");
        model.addAttribute("ISPOPUP",true);
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        return viewEntitys.popupView(httpRequest, model);
    }

    @ApiOperation(value = "用户弹窗请求接口")
    @PostMapping("/popupview/users")
    public void popuUsers(HttpServletRequest request, HttpServletResponse response, Model model) {
        try {
            request.getRequestDispatcher("/supplierdepartments/popupview/users").forward(request, response);
        } catch (ServletException | IOException ignored) {}
    }

    @ApiOperation(value = "用户启用请求接口")
    @PostMapping("/activeAction/ajax")
    @ResponseBody
    public Object active(HttpServletRequest request) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            supplierusersService.active(httpRequest);
            return new WebResponse().success("启用成功！").refresh();
        }catch (Exception e){
            return new WebResponse().fail("启用失败，请稍候再试。。。");
        }
    }

    @ApiOperation(value = "用户禁用请求接口")
    @PostMapping("/inactiveAction/ajax")
    @ResponseBody
    public Object inactive(HttpServletRequest request) {
        try {
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            supplierusersService.inactive(httpRequest);
            return new WebResponse().success(0,"停用成功！").put("refresh",true);
        }catch (WebException e){
            return new WebResponse().fail(e.getMessage());
        }catch (Exception e){
            return new WebResponse().fail("停用失败，请稍候再试。。。");
        }
    }


    @ApiOperation(value = "修改用户账号")
    @PostMapping("/chanageaccountAction/ajax")
    public Object chanageAccount(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            Object ids = httpRequest.get("ids");
            if(ids instanceof List){
                ids = ((List<?>) ids).get(0);
            }
            Supplierusers result = (Supplierusers)supplierusersService.getObjectByRecord(ids,viewEntitys,Supplierusers.class);
            model.addAttribute("MODULE", viewEntitys.getModuleName());
            model.addAttribute("RECORD",result.id);
            model.addAttribute("ACCOUNT",result.account);
            return WebViews.view("/modules/settings/users/chanageaccount");
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation(value = "修改用户账号")
    @PostMapping("/chanageaccountAction/save")
    @ResponseBody
    public Object chanageAccountSave(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            supplierusersService.chanageAccount(httpRequest);
            return new WebResponse().success("修改完成").refresh();
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }


    @ApiOperation(value = "修改用户密码")
    @PostMapping("/modifypasswordAction/ajax")
    public Object modifyPassword(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            Object ids = httpRequest.get("ids");
            if(ids instanceof List){
                ids = ((List<?>) ids).get(0);
            }
            Supplierusers result = (Supplierusers)supplierusersService.getObjectByRecord(ids,viewEntitys,Supplierusers.class);
            model.addAttribute("MODULE", viewEntitys.getModuleName());
            model.addAttribute("RECORD",result.id);
            model.addAttribute("ACCOUNT",result.account);
            return WebViews.view("/modules/settings/users/modifypassword");
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }
    @ApiOperation(value = "保存用户密码")
    @PostMapping("/modifypasswordAction/save")
    @ResponseBody
    public Object modifypasswordSave(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            supplierusersService.modifyPassword(httpRequest);
            return new WebResponse().success("修改完成").refresh();
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }
}
