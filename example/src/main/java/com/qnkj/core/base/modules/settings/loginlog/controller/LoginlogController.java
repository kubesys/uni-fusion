package com.qnkj.core.base.modules.settings.loginlog.controller;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.loginlog.entity.Loginlog;
import com.qnkj.core.base.modules.settings.loginlog.service.ILoginlogService;
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
@Controller("Settings-Loginlog")
@RequiredArgsConstructor
@Api(tags = "系统设置：登录日志")
@Scope("prototype")
@RequestMapping("loginlog")
public class LoginlogController {
    private final ILoginlogService loginlogService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "日志列表请求接口")
    @GetMapping("/listview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("TABNAME", "登录日志");
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "日志列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String, Object> list = loginlogService.getListViewEntity(request, viewEntitys, Loginlog.class);
        return viewEntitys.listEntity(request,list);
    }

}
