package com.qnkj.core.base.modules.supplier.Supplieroperationlog.controller;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.supplier.Supplieroperationlog.entitys.supplieroperationlog;
import com.qnkj.core.base.modules.supplier.Supplieroperationlog.services.IsupplieroperationlogService;
import com.qnkj.core.webconfigs.annotation.Log;
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
 * create by Auto Generator
 * create date 2021-05-23
 */

@Validated
@Controller("base-supplieroperationlog")
@RequiredArgsConstructor
@Api(tags = "企业管理：操作日志")
@Scope("prototype")
@RequestMapping("supplieroperationlog")
public class BaseModuleController {
    private final IsupplieroperationlogService supplieroperationlogService;
    private BaseEntityUtils viewEntitys = null;
    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "列表请求接口")
    @GetMapping("/listview")
    @Log("显示操作日志列表视图")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String,Object> list = supplieroperationlogService.getListViewEntity(request,viewEntitys, supplieroperationlog.class);
        return viewEntitys.listEntity(request,list);
    }
}
