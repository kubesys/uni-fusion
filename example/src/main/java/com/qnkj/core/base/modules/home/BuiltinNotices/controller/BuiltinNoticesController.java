package com.qnkj.core.base.modules.home.BuiltinNotices.controller;

import com.github.restapi.models.Profile;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.modules.management.notices.services.INoticesService;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.utils.AuthorizeUtils;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.WebViews;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2020-11-17
 */

@Slf4j
@Validated
@Controller("builtinnotices")
@RequiredArgsConstructor
@Api(tags = "内置：通知")
@Scope("prototype")
@RequestMapping("builtinnotices")
public class BuiltinNoticesController {
    private final INoticesService noticesService;

    @ApiOperation("显示通知")
    @GetMapping("index")
    public String notices(Model model) {
        return WebViews.view("modules/home/notices/index");
    }

    @ApiOperation("分页显示通知")
    @GetMapping("view/page")
    @ResponseBody
    public Object noticespage(HttpServletRequest request) {
        Profile currentUser = ProfileUtils.getCurrentUser();
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            HashMap<String,Object> lists;
            int pageIndex = Integer.parseInt(httpRequest.getOrDefault("pageIndex",1).toString(),10);
            int pageSize = Integer.parseInt(httpRequest.getOrDefault("pageSize",100).toString(),10);

            if (ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) {
                lists = noticesService.getSystemNotices(currentUser.getId(),pageIndex,pageSize);
            } else if (Boolean.TRUE.equals(ProfileUtils.isManager())) {
                if (AuthorizeUtils.isAuthorizes(ProfileUtils.getCurrentProfileId(),"auditor")) {
                    lists = noticesService.getApprovalNotices(currentUser.getId(),pageIndex,pageSize);
                } else {
                    lists = noticesService.getProfileNotices(currentUser.getId(),pageIndex,pageSize);
                }
            } else {
                lists = noticesService.getSupplierNotices(SupplierUtils.getSupplierid(),currentUser.getId(),pageIndex,pageSize);
            }
            return new WebResponse().data(lists).code(0);
        }catch(Exception e) {
            log.error("noticespage Error: {}",e.getMessage());
        }
        return new WebResponse().fail();
    }

    @PostMapping("view/{noticeid}")
    @ApiOperation("通知修改为已读操作")
    @ResponseBody
    @ApiImplicitParam(name = "noticeid", value = "通知ID", required = true, dataType = "String", paramType = "path")
    public WebResponse readNotice(@PathVariable(value="noticeid") String noticeid, Model model) {
        try {
            noticesService.update(noticeid);
            return new WebResponse().success();
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }


}
