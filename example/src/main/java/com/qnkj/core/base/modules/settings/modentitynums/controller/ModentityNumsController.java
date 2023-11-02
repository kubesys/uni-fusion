package com.qnkj.core.base.modules.settings.modentitynums.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.modentitynums.entity.ModentityNums;
import com.qnkj.core.base.modules.settings.modentitynums.service.IModentityNumsService;
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
import java.util.Map;

/**
 * create by 徐雁
 * @author clubs
 */
@Validated
@Controller("Settings-ModentityNums")
@RequiredArgsConstructor
@Api(tags = "系统设置：系统编号定制")
@Scope("prototype")
@RequestMapping("modentitynums")
public class ModentityNumsController {
    private final IModentityNumsService modentityNumsService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "系统编号列表请求接口")
    @GetMapping("/listview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("TABNAME", "系统编号定制");
        return viewEntitys.listView(request, model, "modules/settings/modentitynums/ListView");
    }

    @ApiOperation(value = "系统编号列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String, Object> list = modentityNumsService.getListViewEntity(request, viewEntitys, ModentityNums.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "系统编号编辑请求接口")
    @PostMapping("/editview")
    public Object editView(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        ModentityNums modentityNums = modentityNumsService.getNumInfoById(httpRequest.get("record").toString(), viewEntitys);
        model.addAttribute("RECORD", modentityNums.id);
        model.addAttribute("MODULENAME", modentityNums.semodule);
        model.addAttribute("PREFIX", modentityNums.prefix);
        model.addAttribute("INCLUDE_DATE", modentityNums.include_date);
        model.addAttribute("LENGTH", modentityNums.length);
        return WebViews.view("modules/settings/modentitynums/EditView");
    }

    @ApiOperation(value = "系统编号列表Ajax请求接口")
    @PostMapping("/editview/save")
    @ResponseBody
    public Object save(HttpServletRequest request) {
        try {
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            modentityNumsService.save(httpRequest, viewEntitys);
            return new WebResponse().refresh().success(0, "保存完成");
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "刷新系统编号请求接口")
    @PostMapping("/refreshnumsAction/ajax")
    @ResponseBody
    public Object refreshNums(HttpServletRequest request) {
        try {
            return new WebResponse().refresh().success(0, "保存完成");
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

}
