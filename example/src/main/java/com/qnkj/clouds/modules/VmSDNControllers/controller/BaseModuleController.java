package com.qnkj.clouds.modules.VmSDNControllers.controller;

import com.qnkj.clouds.modules.VmSDNControllers.entitys.VmSDNControllers;
import com.qnkj.clouds.modules.VmSDNControllers.services.IVmSDNControllersService;
import com.qnkj.clouds.modules.VmZones.entitys.VmZones;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.webconfigs.WebViews;
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
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2023-07-09
 * @author Auto Generator
 */

@Slf4j
@Validated
@Controller("base-VmSDNControllers")
@RequiredArgsConstructor
@Api(tags = "低代码演示：SDN控制器")
@Scope("prototype")
@RequestMapping("VmSDNControllers")
public class BaseModuleController {
    private final IVmSDNControllersService vmsdncontrollersService;
    private BaseEntityUtils viewEntitys = null;
    @PostConstruct
    public void init() { viewEntitys = BaseEntityUtils.init(this); }

    @ApiOperation(value = "列表请求接口")
    @GetMapping("/listview")
    @Log("显示SDN控制器列表视图")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String,Object> list = vmsdncontrollersService.getListViewEntity(request,viewEntitys, VmSDNControllers.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "编辑请求接口")
    @GetMapping("/editviewAction")
    @Log("显示SDN控制器编辑视图")
    public Object editView(HttpServletRequest request,HttpServletResponse response, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        VmSDNControllers result = (VmSDNControllers) vmsdncontrollersService.getObjectByRecord(httpRequest.get("record"),viewEntitys,VmSDNControllers.class);
        if(result != null) {
            if(!Utils.isEmpty(httpRequest.get("remoteurl"))) {
                model.addAttribute("REMOTEURL",httpRequest.get("remoteurl"));
            }
            if(Utils.isNotEmpty(httpRequest.get("forwardmodule"))){
                model.addAttribute("FORWARDMODULE",httpRequest.get("forwardmodule"));
            }
            return viewEntitys.editView(httpRequest, model, result);
        }else{
            return WebViews.view("error/403");
        }
    }



    @ApiOperation(value = "保存请求接口")
    @PostMapping("/save")
    @ResponseBody
    @Log("保存SDN控制器")
    public Object editViewSave(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            viewEntitys.formatRequest(httpRequest);
            vmsdncontrollersService.save(httpRequest,viewEntitys,VmSDNControllers.class);
            String record = httpRequest.getOrDefault("record","").toString();
            if(viewEntitys.getWorkflowStatus().isDefineflow() && (viewEntitys.getWorkflowStatus().isDealwith() || !viewEntitys.getWorkflowStatus().isStartflow())){
                return new WebResponse().refresh().record(record).success(0,"保存完成");
            }else{
                return new WebResponse().close().success(0,"保存完成");
            }
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }


    @ApiOperation(value = "删除请求接口")
    @PostMapping("/deleteAction/ajax")
    @ResponseBody
    public Object delete(HttpServletRequest request, Model model) {
        try {
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            vmsdncontrollersService.delete(httpRequest,viewEntitys);
            return new WebResponse().refresh().success(0,"删除成功");
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "启用请求接口")
    @PostMapping("/activeAction/ajax")
    @ResponseBody
    @Log("启用区域")
    public Object active(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            vmsdncontrollersService.active(httpRequest,viewEntitys);
            return new WebResponse().refresh().success("启用成功");
        }catch (Exception e) {
            return new WebResponse().fail("启用失败，请稍候再试。。。");
        }
    }

    @ApiOperation(value = "停用请求接口")
    @PostMapping("/inactiveAction/ajax")
    @ResponseBody
    @Log("停用区域")
    public Object inactive(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            vmsdncontrollersService.inactive(httpRequest,viewEntitys);
            return new WebResponse().refresh().success("停用成功");
        } catch (Exception e) {
            return new WebResponse().fail("停用失败，请稍候再试。。。");
        }
    }

    @ApiOperation(value = "弹窗请求接口")
    @PostMapping("/popupview")
    public Object popupView(HttpServletRequest request, Model model) {
        model.addAttribute("ENTITYURL", "VmSDNControllers/popupentity/ajax");
        model.addAttribute("ISPOPUP",true);
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        return viewEntitys.popupView(httpRequest, model);
    }

    @ApiOperation(value = "弹窗请求数据接口")
    @GetMapping("/popupentity/ajax")
    @ResponseBody
    public Object popupEntity(HttpServletRequest request, Model model) {
        Map<String,Object> list = vmsdncontrollersService.getListViewEntity(request,viewEntitys, VmZones.class);
        return viewEntitys.popupEntity(request,list);
    }
}
