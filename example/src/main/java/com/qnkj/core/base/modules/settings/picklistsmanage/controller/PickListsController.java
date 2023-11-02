package com.qnkj.core.base.modules.settings.picklistsmanage.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.modules.settings.picklistsmanage.service.IMyPickListsService;
import com.qnkj.core.webconfigs.WebViews;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@Controller("Settings-PickListsManage")
@RequiredArgsConstructor
@Api(tags = "系统设置：数据字典")
@RequestMapping("picklistsmanage")
public class PickListsController {
    private final IMyPickListsService mypickListsService;

    @ApiOperation(value = "字典列表请求接口")
    @GetMapping("/listview")
    public Object index(Model model) {
        HashMap<String, Object> picklists = mypickListsService.getAllPickLists();
        List<Object> result = new ArrayList<>();
        for (String key : picklists.keySet()) {
            Map<String,Object> infoMap = new HashMap<>(1);
            infoMap.put("name", key);
            infoMap.put("label", ((HashMap<?,?>) picklists.get(key)).get("label"));
            result.add(infoMap);
        }

        model.addAttribute("MODULE", "picklists");
        model.addAttribute("TABNAME", "数据字典");
        model.addAttribute("PICKLISTS", result);
        return WebViews.view("modules/settings/picklistsmanage/ListView");
    }

    @ApiOperation(value = "字典请求接口")
    @GetMapping("/picklist")
    @ResponseBody
    public Object picklist(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        if (!Utils.isEmpty(httpRequest.get("picklist"))) {
            Map<String,Object> infoMap = new HashMap<>(1);
            Map<String,Object> resultMap = new HashMap<>(1);
            resultMap.put("picklist", httpRequest.get("picklist"));
            resultMap.put("data", mypickListsService.getPickList(httpRequest.get("picklist").toString()));
            infoMap.put("code", 0);
            infoMap.put("result", resultMap);
            return infoMap;
        } else {
            return new WebResponse().fail(200).put("result", new HashMap<>());
        }
    }

    @ApiOperation(value = "字典编辑请求接口")
    @PostMapping("/editview")
    public Object editView(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("MODULE", "picklists");
        if (!Utils.isEmpty(httpRequest.get("options")) && !Utils.isEmpty(httpRequest.get("picklist"))) {
            model.addAttribute("PICKLIST", httpRequest.get("picklist"));
            model.addAttribute("IDEDIT", true);
            model.addAttribute("RECORD", ((List<?>) httpRequest.get("options")).get(5));
            model.addAttribute("STRVAL", ((List<?>) httpRequest.get("options")).get(0));
            model.addAttribute("LABEL", ((List<?>) httpRequest.get("options")).get(1));
            model.addAttribute("INTVAL", ((List<?>) httpRequest.get("options")).get(2));
            model.addAttribute("STYCLASS", ((List<?>) httpRequest.get("options")).get(3));
            return WebViews.view("modules/settings/picklistsmanage/EditView");
        } else {
            return WebViews.view("error/500");
        }

    }

    @ApiOperation(value = "字典编辑请求接口")
    @PostMapping("/addoptions")
    public Object addView(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("MODULE", "picklists");
        if (!Utils.isEmpty(httpRequest.get("picklist"))) {
            model.addAttribute("IDEDIT", false);
            model.addAttribute("PICKLIST", httpRequest.get("picklist"));
            model.addAttribute("INTVAL", mypickListsService.getMaxIntValue(httpRequest.get("picklist").toString()) + 1);
            return WebViews.view("modules/settings/picklistsmanage/EditView");
        } else {
            return WebViews.view("error/500");
        }
    }

    @ApiOperation(value = "字典保存请求接口")
    @PostMapping("/save")
    @ResponseBody
    public Object save(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            mypickListsService.save(httpRequest);
            return new WebResponse().refresh().code(0);
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "字典删除请求接口")
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            mypickListsService.delete(httpRequest);
            return new WebResponse().refresh().code(0);
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "字典上移请求接口")
    @PostMapping("/moveup")
    @ResponseBody
    public Object moveUp(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            mypickListsService.moveUp(httpRequest);
            return new WebResponse().refresh().code(0);
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "字典下移请求接口")
    @PostMapping("/movedown")
    @ResponseBody
    public Object moveDown(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            mypickListsService.moveDown(httpRequest);
            return new WebResponse().refresh().code(0);
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "新建数据字典请求接口")
    @PostMapping("/newpicklist")
    public Object newPicklist(HttpServletRequest request, Model model) {
        model.addAttribute("MODULE", "picklists");
        return WebViews.view("modules/settings/picklistsmanage/NewEditView");
    }


    @ApiOperation(value = "新建数据字典请求接口")
    @PostMapping("/saveNewPicklist")
    @ResponseBody
    public Object saveNewPicklist(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            mypickListsService.saveNewPicklist(httpRequest);
            return new WebResponse().refresh().code(0);
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }
}
