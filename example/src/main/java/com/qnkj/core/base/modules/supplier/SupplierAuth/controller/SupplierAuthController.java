package com.qnkj.core.base.modules.supplier.SupplierAuth.controller;

import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.supplier.SupplierAuth.entity.SupplierAuth;
import com.qnkj.core.base.modules.supplier.SupplierAuth.service.ISupplierAuthService;
import com.qnkj.core.base.modules.supplier.SupplierUsers.entitys.Supplierusers;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@Controller("Supplier-Authorizations")
@RequiredArgsConstructor
@Api(tags = "系统设置：管理授权")
@Scope("prototype")
@RequestMapping("supplierauth")
public class SupplierAuthController {
    private final ISupplierAuthService authorizationsService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "部门表单请求接口")
    @GetMapping("/listview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("TABNAME", "管理授权");
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "权限列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String, Object> result = authorizationsService.getListViewEntity(request, viewEntitys, SupplierAuth.class);
        List<Object> records = new ArrayList<Object>();
        for (Object item : (List) result.get("list")) {
            Map<String, Object> record = new HashMap<>();
            String authorize = ((SupplierAuth) item).authorize;
            String authorizename = "";
            SaaSUtils saaSUtils = new SaaSUtils(BaseSaasConfig.getDomain());
            Object authorizess = saaSUtils.getUserAuthorizes();
            if (!Utils.isEmpty(authorizess)) {
                for (Object authorizes : (List) authorizess) {
                    if (((HashMap) authorizes).getOrDefault("authorize","").equals(authorize)) {
                        authorizename = ((HashMap) authorizes).getOrDefault("authlabel","").toString();
                        break;
                    }
                }
            }

            record.put("id", authorize);
            record.put("authorize", authorizename);
            record.put("userlist", String.join(",", ((SupplierAuth) item).userlist));
            records.add(record);
        }
        return new WebResponse().code(0).data(records).count(result.get("total"));
    }

    @ApiOperation(value = "授权请求接口")
    @PostMapping("/authorizeAction/ajax")
    public void authorize(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            if(!Utils.isEmpty(httpRequest.get("authorizeids"))){
                Object authorizeids = httpRequest.get("authorizeids");
                List<Supplierusers> authusers = new ArrayList<>();
                if(authorizeids instanceof String) {
                    Object tmp = authorizationsService.getUsersByAuthorize(authorizeids.toString());
                    if(!Utils.isEmpty(tmp)) {
                        authusers.addAll((List)tmp);
                    }
                }else if(authorizeids instanceof List){
                    for(Object auth: (List<?>)authorizeids){
                        Object tmp = authorizationsService.getUsersByAuthorize(auth.toString());
                        if(!Utils.isEmpty(tmp)) {
                            authusers.addAll((List)tmp);
                        }
                    }
                }
                if(!authusers.isEmpty()) {
                    List<String> ids = new ArrayList<>();
                    for(Supplierusers users: authusers){
                        if(!ids.contains(users.profileid)) {
                            ids.add(users.profileid);
                        }
                    }
                    if(!ids.isEmpty()){
                        request.setAttribute("select",ids);
                    }
                }
            }
            request.getRequestDispatcher("/supplierdepartments/popupview/users").forward(request, response);
        } catch (ServletException | IOException ignored) {}
    }


    @ApiOperation(value = "取消授权请求接口")
    @PostMapping("/cancelauthorizeAction/ajax")
    @ResponseBody
    public Object cancelAuthorize(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            authorizationsService.cancel(httpRequest, viewEntitys);
            return new WebResponse().code(0).refresh();
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "授权请求接口")
    @PostMapping("/authorizeAction/save")
    @ResponseBody
    public Object save(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            authorizationsService.save(httpRequest, viewEntitys);
            return new WebResponse().code(0).refresh();
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "用户弹窗请求接口")
    @PostMapping("/popupview")
    public Object popupView(HttpServletRequest request, Model model) {
        model.addAttribute("ENTITYURL", "supplierauth/listentity/ajax");
        model.addAttribute("ISPOPUP",true);
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        List<String> ids = new ArrayList<>(1);
        if(!Utils.isEmpty(httpRequest.getOrDefault("select",""))){
            if(httpRequest.get("select") instanceof String) {
                ids.add(httpRequest.get("select").toString());
            }else if(httpRequest.get("select") instanceof List){
                ids.addAll((List<String>)httpRequest.get("select"));
            }
        }
        if (!Utils.isEmpty(httpRequest.getOrDefault("exclude", ""))) {
            if(httpRequest.get("exclude") instanceof String) {
                ids.add(httpRequest.get("exclude").toString());
            }else if(httpRequest.get("exclude") instanceof List){
                ids.addAll((List<String>)httpRequest.get("exclude"));
            }
        }
        httpRequest.put("exclude",ids);
        return viewEntitys.popupView(httpRequest, model);
    }

}
