package com.qnkj.core.base.modules.management.notices.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.management.notices.entitys.Notices;
import com.qnkj.core.base.modules.management.notices.services.INoticesService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2020-11-17
 */

@Validated
@Controller("notices")
@RequiredArgsConstructor
@Api(tags = "管理平台：通知")
@Scope("prototype")
@RequestMapping("notices")
public class BaseModuleController {
    private final INoticesService noticesService;
    private BaseEntityUtils viewEntitys = null;
    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "列表请求接口")
    @GetMapping("/listview")
    public Object index(HttpServletRequest request, Model model) {
        if (ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) {
            model.addAttribute("MODULE", viewEntitys.getModuleName());
            model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
            return viewEntitys.listView(request, model);
        } else {
            return "redirect:/builtinnotices/index";
        }
    }

    @ApiOperation(value = "列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String,Object> list = noticesService.getListViewEntity(request,viewEntitys, Notices.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "编辑请求接口")
    @GetMapping("/editviewAction")
    public Object editView(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        Notices result = (Notices) noticesService.getObjectByRecord(httpRequest.get("record"),viewEntitys,Notices.class);
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
            noticesService.save(httpRequest,viewEntitys,Notices.class);
            return new WebResponse().put("record",httpRequest.getOrDefault("record","")).success(0,"保存完成");
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
            noticesService.delete(httpRequest,viewEntitys);
            Map<String, Object> infoMap = new HashMap<>(1);
            infoMap.put("code", 0);
            infoMap.put("msg", "删除成功");
            infoMap.put("refresh",true);
            return infoMap;
        }catch (Exception e) {
            Map<String, Object> infoMap = new HashMap<>(1);
            infoMap.put("code", 100);
            infoMap.put("msg",e.getMessage());
            return infoMap;
        }
    }
}
