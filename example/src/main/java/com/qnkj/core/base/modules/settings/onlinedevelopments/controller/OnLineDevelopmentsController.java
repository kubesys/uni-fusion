package com.qnkj.core.base.modules.settings.onlinedevelopments.controller;

import com.qnkj.common.services.IProgramServices;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.onlinedevelopments.service.IOnLineDevelopmentsService;
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

/**
 * create by 徐雁
 * create date 2020/11/9
 */

@Validated
@Controller("online-Developments")
@RequiredArgsConstructor
@Scope("prototype")
@Api(tags = "低代码开发：在线开发")
@RequestMapping("/onlinedevelopments")
public class OnLineDevelopmentsController {
    private final IOnLineDevelopmentsService onlinedevelopmentService;
    private final IProgramServices programServices;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "在线开发页面请求接口")
    @GetMapping("/listview")
    public Object index(Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        return WebViews.view("modules/settings/onlinedevelopments/TreeView");
    }

    @ApiOperation(value = "在线开发页面树结构请求接口")
    @PostMapping("/tree")
    @ResponseBody
    public Object getTreeData() {
        return onlinedevelopmentService.getTree();
    }

}
