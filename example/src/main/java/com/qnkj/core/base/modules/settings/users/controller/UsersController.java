package com.qnkj.core.base.modules.settings.users.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.users.entity.Users;
import com.qnkj.core.base.modules.settings.users.service.IUsersService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.WebViews;
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
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Oldhand
 */
@Validated
@Controller("Settings-Users")
@RequiredArgsConstructor
@Api(tags = "系统设置：用户管理")
@Scope("prototype")
@RequestMapping("users")
public class UsersController {
    private final IUsersService usersService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "用户列表请求接口")
    @GetMapping("/listview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("TABNAME", "用户管理");
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "用户列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String,Object> list = usersService.getListViewEntity(request,viewEntitys, Users.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "用户编辑请求接口")
    @GetMapping("/editviewAction")
    public Object editView(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        Users result = (Users)usersService.getObjectByRecord(httpRequest.get("record"),viewEntitys,Users.class);
        if(result != null) {
            model.addAttribute("TABNAME", result.getEditState() ? "用户详情" : "新建");
            model.addAttribute("ISEDIT", result.getEditState());
            model.addAttribute("RECORD", result.id);
            model.addAttribute("AUTHOR", ProfileUtils.getCurrentUser().givenname);
            model.addAttribute("PUBLISHED", result.published);
            if ("admin".equals(result.account) && "admin".equals(result.is_admin)) {
                result.approvalstatus = 2;
            }
            if(result.approvalstatus.equals(1) || result.approvalstatus.equals(2) || result.approvalstatus.equals(4)){
                model.addAttribute("READONLY",true);
            }
            return viewEntitys.editView(httpRequest, model, result);
        } else {
            return WebViews.view("error/403");
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
            Users result = (Users)usersService.getObjectByRecord(ids,viewEntitys,Users.class);
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
            usersService.chanageAccount(httpRequest);
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
            Users result = (Users)usersService.getObjectByRecord(ids,viewEntitys,Users.class);
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
            usersService.modifyPassword(httpRequest);
            return new WebResponse().success("修改完成").refresh();
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "用户保存请求接口")
    @PostMapping("/save")
    @ResponseBody
    public Object editViewSave(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            viewEntitys.formatRequest(httpRequest);
            usersService.save(httpRequest);
            return new WebResponse().success("保存完成！").refresh().record(httpRequest.getOrDefault("record",""));
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "用户启用请求接口")
    @PostMapping("/activeAction/ajax")
    @ResponseBody
    public Object active(HttpServletRequest request) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            usersService.active(httpRequest);
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
            usersService.inactive(httpRequest);
            return new WebResponse().success("停用成功！").refresh();
        }catch (WebException e){
            return new WebResponse().fail(e.getMessage());
        }catch (Exception e) {
            return new WebResponse().fail("停用失败，请稍候再试。。。");
        }
    }

}
