package com.qnkj.core.base.modules.baseservices.modifylog.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.baseservices.modifylog.entitys.modifylog;
import com.qnkj.core.base.modules.baseservices.modifylog.services.ImodifylogService;
import com.qnkj.core.webconfigs.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * create date 2021-09-18
 */

@Slf4j
@Validated
@Controller("base-modifylog")
@RequiredArgsConstructor
@Api(tags = "系统专属模块演示：编辑日志")
@Scope("prototype")
@RequestMapping("modifylog")
public class BaseModuleController {
    private final ImodifylogService modifylogService;
    private BaseEntityUtils viewEntitys = null;
    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }

    @ApiOperation(value = "列表请求接口")
    @GetMapping("/listview")
    @Log("显示编辑日志列表视图")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String,Object> list = modifylogService.getListViewEntity(request,viewEntitys, modifylog.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "保存请求接口")
    @PostMapping("/save")
    @ResponseBody
    @Log("保存编辑日志")
    public Object editViewSave(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            viewEntitys.formatRequest(httpRequest);
            modifylogService.save(httpRequest,viewEntitys,modifylog.class);
            return new WebResponse().close().success(0,"保存完成");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

}
