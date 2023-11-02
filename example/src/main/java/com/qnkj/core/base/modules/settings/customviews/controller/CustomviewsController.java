package com.qnkj.core.base.modules.settings.customviews.controller;

import com.qnkj.common.entitys.CustomView;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.modules.settings.customviews.service.ICustomviewsService;
import com.qnkj.core.utils.PickListUtils;
import com.qnkj.core.webconfigs.WebViews;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author clubs
 */
@Validated
@Controller("Settings-Customviews")
@RequiredArgsConstructor
@Api(tags = "系统设置：用户管理")
@RequestMapping("customviews")
public class CustomviewsController {
    private final ICustomviewsService customviewsService;

    @ApiOperation("获取模块的视图列表")
    @PostMapping("/getviews/ajax")
    @ResponseBody
    public Object getModuleViews(HttpServletRequest request) {
        return new WebResponse().success().data(Utils.classToData(customviewsService.getModuleViews(request)));
    }

    @ApiOperation("获取模块的视图列表")
    @PostMapping("/getheader/ajax")
    @ResponseBody
    public Object getListViewHeader(HttpServletRequest request) {
        return new WebResponse().code(0).data(Utils.classToData(customviewsService.getListViewHeader(request)));
    }

    @ApiOperation("编辑模块的视图")
    @PostMapping("/editview/ajax")
    public Object editCustomviews(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("FIELDS", Utils.classToData(customviewsService.getListFields(httpRequest)));
        model.addAttribute("modulename", "Customviews");
        model.addAttribute("VIEWMODULENAME", httpRequest.getOrDefault("modulename", ""));
        model.addAttribute("AUTHORIZE", PickListUtils.getPickList("authorize"));
        if (!Utils.isEmpty(httpRequest.get("viewid"))) {
            CustomView customView = customviewsService.getCustomView(httpRequest.get("viewid").toString());
            model.addAttribute("CUSTOMVIEW", Utils.classToData(customView));
            if(Utils.isEmpty(customView.privateuser)){
                model.addAttribute("ISPUBLIC", true);
            } else {
                model.addAttribute("ISPUBLIC", false);
            }
        } else {
            model.addAttribute("ISPUBLIC", false);
        }
        return WebViews.view("/modules/settings/customviews/EditView");
    }

    @ApiOperation("保存模块的视图")
    @PostMapping("/editview/save")
    @ResponseBody
    public Object saveCustomviews(HttpServletRequest request) {
        try{
            String data = customviewsService.saveCustomView(request);
            return new WebResponse().success().data(data);
        }catch (Exception e){
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation("删除模块的视图")
    @PostMapping("/delview/ajax")
    @ResponseBody
    public Object delCustomviews(HttpServletRequest request) {
        try{
            String data = customviewsService.delCustomviews(request);
            return new WebResponse().success().data(data);
        }catch (Exception e){
            return new WebResponse().fail(e.getMessage());
        }
    }
}
