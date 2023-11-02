package com.qnkj.core.base.modules.supplier.SupplierDepartments.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.supplier.SupplierDepartments.entitys.Supplierdepartments;
import com.qnkj.core.base.modules.supplier.SupplierDepartments.services.ISupplierdepartmentsService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.utils.RolesUtils;
import com.qnkj.core.webconfigs.WebViews;
import com.qnkj.core.webconfigs.annotation.Log;
import com.qnkj.core.webconfigs.exception.WebException;
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
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2021-03-31
 */

@Validated
@Controller("supplierdepartments")
@RequiredArgsConstructor
@Api(tags = "管理平台：部门管理")
@Scope("prototype")
@RequestMapping("supplierdepartments")
public class SupplierdepartmentsController {
    private final ISupplierdepartmentsService supplierdepartmentsService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "树页面请求接口")
    @GetMapping("/listview")
    @Log("显示部门管理树视图")
    public Object Index(HttpServletRequest request, Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        model.addAttribute("ISDEPARTUSER", true);
        if(!(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()) && !RolesUtils.isEdit(viewEntitys.getModuleName())){
            model.addAttribute("READONLY", true);
        }
        return viewEntitys.treeView(request, model);
    }

    @ApiOperation(value = "树结构数据请求接口")
    @PostMapping("/tree")
    @ResponseBody
    public Object getTreeData(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        return supplierdepartmentsService.getTree(httpRequest,viewEntitys);
    }

    @ApiOperation(value = "树结构数据请求接口")
    @PostMapping("/treeevent")
    public Object treeEvent(HttpServletRequest request,Model model, HttpServletResponse response) throws Exception {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        if(!Utils.isEmpty(httpRequest.get("eventtype"))) {
            if("add".equals(httpRequest.get("eventtype")) || "edit".equals(httpRequest.get("eventtype"))) {
                Supplierdepartments result = null;
                if("add".equals(httpRequest.get("eventtype"))){
                    result = (Supplierdepartments)supplierdepartmentsService.getObjectByRecord(null,viewEntitys,Supplierdepartments.class);
                    result.parentid = httpRequest.getOrDefault("id","").toString();
                }else if("edit".equals(httpRequest.get("eventtype"))){
                    result = (Supplierdepartments)supplierdepartmentsService.getObjectByRecord(httpRequest.get("id"),viewEntitys,Supplierdepartments.class);
                }
                if(result != null) {
                    model.addAttribute("TABNAME", result.getEditState() ? httpRequest.getOrDefault("name","").toString() + "详情" : "新增");
                    model.addAttribute("ISEDIT", result.getEditState());
                    model.addAttribute("RECORD", result.id);
                    return viewEntitys.treeViewEdit(httpRequest,model,result);
                } else {
                    return WebViews.view("error/403");
                }
            }else if("del".equals(httpRequest.get("eventtype"))) {
                try {
                    supplierdepartmentsService.delete(httpRequest,viewEntitys);
                    return WebViews.body(response,new WebResponse().refresh().success("删除完成"));
                }catch (Exception e){
                    return WebViews.body(response,new WebResponse().fail("删除失败"));
                }
            }else if("click".equals(httpRequest.get("eventtype"))) {
                return WebViews.body(response,new WebResponse().success("点击事件"));
            } else {
                return WebViews.view("error/403");
            }
        } else {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation(value = "保存请求接口")
    @PostMapping("/save")
    @ResponseBody
    public Object save(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            supplierdepartmentsService.save(httpRequest, viewEntitys);
            return new WebResponse().refresh().success("保存完成");
        }catch (WebException e) {
            return new WebResponse().fail(e.getMessage());
        }catch (Exception e){
            return new WebResponse().fail("保存失败");
        }
    }

}
