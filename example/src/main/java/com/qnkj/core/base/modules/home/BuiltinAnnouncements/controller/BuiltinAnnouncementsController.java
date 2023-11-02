package com.qnkj.core.base.modules.home.BuiltinAnnouncements.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.modules.management.announcements.entitys.Announcements;
import com.qnkj.core.base.modules.management.announcements.services.IAnnouncementsService;
import com.qnkj.core.webconfigs.WebViews;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2020-11-17
 */

@Validated
@Controller("builtinannouncements")
@RequiredArgsConstructor
@Api(tags = "内置：公告")
@Scope("prototype")
@RequestMapping("builtinannouncements")
public class BuiltinAnnouncementsController {
    private final IAnnouncementsService announcementsservice;

    @ApiOperation("显示公告")
    @GetMapping("index")
    public String notices(Model model) {
        return WebViews.view("modules/home/announcements/index");
    }

    @ApiOperation("获取公告列表")
    @GetMapping("list")
    @ResponseBody
    public WebResponse get() {
        try {
            List<Announcements> lists = announcementsservice.get();
            Map<String, Object> data = new HashMap<>(2);
            data.put("rows", lists);
            data.put("total", lists.size());
            return new WebResponse().success().data(data);
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }

    @GetMapping("view/{announcementid}")
    @ApiOperation("显示公告详情")
    @ApiImplicitParam(name = "announcementid", value = "公告ID", required = true, dataType = "String", paramType = "path")
    public String viewAnnouncement(@PathVariable(value="announcementid") String announcementid, Model model) {
        try {
            Announcements announcements = announcementsservice.get(announcementid);
            model.addAttribute("announcement", Utils.classToData(announcements));
        }catch(Exception e) {
            model.addAttribute("announcement", new Announcements());
        }
        return WebViews.view("modules/home/announcements/view");
    }

}
