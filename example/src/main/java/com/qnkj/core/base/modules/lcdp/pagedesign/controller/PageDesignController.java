package com.qnkj.core.base.modules.lcdp.pagedesign.controller;

import com.qnkj.common.entitys.SelectOption;
import com.qnkj.common.utils.Base64Util;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.lcdp.developments.service.IDevelopmentService;
import com.qnkj.core.base.modules.lcdp.pagedesign.entitys.PageDesign;
import com.qnkj.core.base.modules.lcdp.pagedesign.services.IpageEngineService;
import com.qnkj.core.base.modules.lcdp.pagedesign.services.IpagedesignService;
import com.qnkj.core.utils.PickListUtils;
import com.qnkj.core.utils.UserUtils;
import com.qnkj.core.webconfigs.WebViews;
import com.qnkj.core.webconfigs.annotation.Log;
import com.qnkj.core.webconfigs.exception.WebException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * create by Auto Generator
 * create date 2021-06-25
 */
@Slf4j
@Validated
@Controller("pagedesign")
@RequiredArgsConstructor
@Api(tags = "框架：页面设计")
@Scope("prototype")
@RequestMapping("pagedesign")
public class PageDesignController {
    private final IpageEngineService pageEngineService;
    private final IpagedesignService pagedesignService;
    private final IDevelopmentService developmentService;

    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "列表请求接口")
    @GetMapping("/listview")
    @Log("显示页面设计列表视图")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        model.addAttribute("EDITMODE", "PUPUPDIALOG");
        pagedesignService.verifyDesignList();
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String,Object> list = pagedesignService.getListViewEntity(request, viewEntitys, PageDesign.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "编辑请求接口")
    @GetMapping("/editviewAction")
    @Log("显示测试模块编辑视图")
    public Object editView(HttpServletRequest request,HttpServletResponse response, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        model.addAttribute("record", httpRequest.get("record"));
        try {
            PageDesign pagedesign = pagedesignService.load(httpRequest.get("record").toString());
            model.addAttribute("TEMPLATE", Base64Util.base64Decode(pagedesign.template_editor));
            List<HashMap<String,Object>> allModuleObject = (List<HashMap<String, Object>>)developmentService.getAllModule();
            List<Map<String, Object>> mapList = allModuleObject.stream().filter(v -> {
                Boolean disabled = (Boolean) v.get("disabled");
                Boolean builtin = (Boolean) v.get("builtin");
                return !disabled && !builtin;
            }
            ).collect(Collectors.toList());
            List<Object> moduleOptionList = new ArrayList<>();
            for (Map<String, Object> optionMap:mapList) {
                String label = optionMap.get("modulelabel").toString();
                String[] labelAry = label.split("：");
                label = labelAry[2]+"（"+labelAry[0]+"："+labelAry[1]+"）";
                SelectOption option = new SelectOption(optionMap.get("modulename").toString(), label, false);
                moduleOptionList.add(Utils.classToData(option));
            }
            model.addAttribute("SOURCELIST", moduleOptionList);
        }catch (Exception e) {
            log.error("pagedesignService EditView : {}",e.getMessage());
        }
        return WebViews.view("modules/settings/pagedesigns/DesignFormPage");
    }


    @ApiOperation(value = "获取模块字段信息接口")
    @GetMapping("/getModuleInfo/ajax")
    @ResponseBody
    public Object getModuleInfo(HttpServletRequest request) {
        try {
            String modulename = request.getParameter("modulename");
            HashMap<String, Object> info = pagedesignService.getModuleInfo(modulename);
            return new WebResponse().success().data(info);
        }catch (Exception e) {
            return new WebResponse().fail("获取数据失败");
        }
    }


    @ApiOperation(value = "保存页面设计请求接口")
    @PostMapping("/save")
    @ResponseBody
    @Log("保存页面设计")
    public Object editViewSave(@RequestBody List<Object> objList) {
        try{
            pagedesignService.saveDesignDetail(objList);
            return new WebResponse().close().success(0,"保存完成");
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "生成模块页面设计接口")
    @PostMapping("/generateAction/ajax")
    @ResponseBody
    public Object generateAjax(HttpServletRequest request) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            pagedesignService.generateModule(httpRequest);
            return new WebResponse().success("生成模块页面设计完成").refresh();
        }catch (Exception e) {
            return new WebResponse().fail("生成模块页面设计失败");
        }
    }

    @ApiOperation(value = "导出指定数据至配置文件")
    @PostMapping("/exportassignedAction/ajax")
    @ResponseBody
    public Object exportAssigned(HttpServletRequest request){
        try{
            Map<String,Object> requestParams=Utils.getRequestQuery(request);
            pagedesignService.exportAssigned(requestParams,viewEntitys);
            return new WebResponse().success(0,"导出完成");
        }catch (Exception e){
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "导出所有数据至配置文件")
    @PostMapping("/exportallAction/ajax")
    @ResponseBody
    public Object exportAll(){
        try{
            pagedesignService.exportAll(viewEntitys);
            return new WebResponse().success(0,"导出完成");
        }catch (Exception e){
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "清除模块页面设计接口")
    @PostMapping("/deleteAction/ajax")
    @ResponseBody
    public Object deleteAjax(HttpServletRequest request) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            pagedesignService.clearModuleDesignData(httpRequest);
            return new WebResponse().success("清除模块页面设计完成").refresh();
        }catch (Exception e) {
            return new WebResponse().fail("删除模块页面设计失败");
        }
    }

    @ApiOperation(value = "重启JAVA虚似机")
    @PostMapping("/reloadjavaAction/ajax")
    @ResponseBody
    public Object reloadJavaAjax(HttpServletRequest request, HttpServletResponse response) {
        try{
            request.getRequestDispatcher("/base/triggerReload").forward(request, response);
            return new WebResponse().success();
        }catch (Exception e) {
            return new WebResponse().fail("重启JAVA虚似机失败");
        }
    }


    @ApiOperation(value = "预览请求接口")
    @GetMapping("/preview")
    @Log("预览请求接口")
    public Object preview(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try{
            PageDesign pagedesign = pagedesignService.load(httpRequest.get("record").toString());
            BaseEntityUtils baseEntity = BaseEntityUtils.init(pagedesign.modulename);
            if (Utils.isEmpty(baseEntity)) {
                throw new Exception("无法获得模块信息");
            }
            model.addAttribute("MODULE",  pagedesign.modulename);
            model.addAttribute("TABNAME", baseEntity.getModuleLabel());
            pageEngineService.execute(pagedesign.modulename, model);
            return WebViews.view("modules/settings/pagedesigns/DisplayPage");
        }catch (Exception e){
            return WebViews.view("error/403");
        }
    }


    @ApiOperation(value = "获取流转条件设置接口")
    @GetMapping("/setConditionExpressionAction/ajax")
    public Object setConditionExpression(HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            List<Object> fields = new ArrayList<>(1);
            Map<String,Object> fieldMap = new HashMap<>(1);
            Map<String,Object> publishedMap = new HashMap<>(1);
            Map<String,Object> authorMap = new HashMap<>(1);
            publishedMap.put("fieldname","published");
            publishedMap.put("fieldlabel","创建时间");
            publishedMap.put("uitype",7);
            authorMap.put("fieldname","author");
            authorMap.put("fieldlabel","创建人");
            authorMap.put("uitype",25);
            fieldMap.put("系统字段", Arrays.asList(publishedMap,authorMap));
            fieldMap.put("表单字段", pagedesignService.getFlowform(httpRequest,viewEntitys,false));
            fields.add(fieldMap);
            model.addAttribute("MODULE",viewEntitys.getModuleName());
            model.addAttribute("ALLUSERS", UserUtils.getAllUsers());
            model.addAttribute("Fields",fields);
            return WebViews.view("modules/settings/pagedesigns/ExpressionSettings");
        }catch (WebException e) {
            return WebViews.body(response,new WebResponse().fail(e.getMessage()));
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation(value = "获取字典数据接口")
    @PostMapping("/getpicklist/ajax")
    @ResponseBody
    public Object getPicklist(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            String picklistname = httpRequest.getOrDefault("name","").toString();
            Object data = null;
            if(!picklistname.isEmpty()) {
                Map<String, Object> picklists = PickListUtils.getAllPicklists();
                if(picklists.containsKey(picklistname)){
                    data = ((Map<?,?>)picklists.get(picklistname)).getOrDefault("picklist",null);
                }
            }
            if(!Utils.isEmpty(data)) {
                return new WebResponse().success().data(data);
            } else {
                return new WebResponse().success();
            }
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }
}
