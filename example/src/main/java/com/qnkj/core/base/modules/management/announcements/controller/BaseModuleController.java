package com.qnkj.core.base.modules.management.announcements.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.management.announcements.entitys.Announcements;
import com.qnkj.core.base.modules.management.announcements.services.IAnnouncementsService;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2020-11-17
 */

@Validated
@Controller("baseannouncements")
@RequiredArgsConstructor
@Api(tags = "管理平台：公告")
@Scope("prototype")
@RequestMapping("announcements")
public class BaseModuleController {
    private final IAnnouncementsService announcementsService;
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
            return "redirect:/builtinannouncements/index";
        }
    }

    @ApiOperation(value = "列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String,Object> list = announcementsService.getListViewEntity(request,viewEntitys, Announcements.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "编辑请求接口")
    @GetMapping("/editviewAction")
    public Object editView(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        Announcements result = (Announcements) announcementsService.getObjectByRecord(httpRequest.get("record"),viewEntitys,Announcements.class);
        if(result != null) {
            model.addAttribute("TABNAME", Boolean.TRUE.equals(result.getEditState()) ? viewEntitys.getModuleLabel() + "详情" : "新建" + viewEntitys.getModuleLabel());
            model.addAttribute("ISEDIT", result.getEditState());
            model.addAttribute("RECORD", result.id);
            model.addAttribute("AUTHOR", ProfileUtils.getCurrentUser().givenname);
            model.addAttribute("PUBLISHED", result.published);
            if(Arrays.asList(1,2,4).contains(result.approvalstatus)) {
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
            announcementsService.save(httpRequest,viewEntitys,Announcements.class);
            return new WebResponse().refresh().put("record",httpRequest.getOrDefault("record","")).success(0,"保存完成");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }
    @ApiOperation(value = "提交请求接口")
    @PostMapping("/submitonlineAction/ajax")
    @ResponseBody
    public Object submitOnline(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            announcementsService.submitOnline(httpRequest,viewEntitys);
            Map<String, Object> infoMap = new HashMap<>(1);
            infoMap.put("code", 0);
            infoMap.put("msg", "提交成功");
            infoMap.put("close",true);
            return infoMap;
        }catch (Exception e) {
            Map<String, Object> infoMap = new HashMap<>(1);
            infoMap.put("code", 100);
            infoMap.put("msg","提交失败，请稍候再试。。。");
            return infoMap;
        }
    }

    @ApiOperation(value = "删除请求接口")
    @PostMapping("/deleteAction/ajax")
    @ResponseBody
    public Object delete(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            announcementsService.delete(httpRequest,viewEntitys);
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
