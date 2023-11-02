package com.qnkj.core.base.modules.settings.operationlog.controller;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.operationlog.entity.Operationlog;
import com.qnkj.core.base.modules.settings.operationlog.service.IOperationlogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * create by 徐雁
 */
@Validated
@Controller("Settings-Operationlog")
@RequiredArgsConstructor
@Api(tags = "系统设置：操作日志")
@Scope("prototype")
@RequestMapping("operationlog")
public class OperationlogController {
    private final IOperationlogService operationlogService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "日志列表请求接口")
    @GetMapping("/listview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("TABNAME", "操作日志");
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "日志列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String, Object> list = operationlogService.getListViewEntity(request, viewEntitys, Operationlog.class);
        return viewEntitys.listEntity(request,list);
    }

}
