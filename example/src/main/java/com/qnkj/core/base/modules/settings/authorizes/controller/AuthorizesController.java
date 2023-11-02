package com.qnkj.core.base.modules.settings.authorizes.controller;

import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.authorizes.entity.Authorizes;
import com.qnkj.core.base.modules.settings.authorizes.service.IAuthorizesService;
import com.qnkj.core.base.modules.settings.users.entity.Users;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@Controller("Settings-Authorizations")
@RequiredArgsConstructor
@Api(tags = "系统设置：管理授权")
@Scope("prototype")
@RequestMapping("authorizes")
public class AuthorizesController {
    private final IAuthorizesService authorizationsService;
    private BaseEntityUtils viewEntitys = null;
    private static final String EXCLUDE_KEY = "exclude";
    private static final String SELECT_KEY = "select";

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "权限表单请求接口")
    @GetMapping("/listview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("TABNAME", "管理授权");
        model.addAttribute("NOCUSTOMVIEW", true);
        return viewEntitys.listView(request, model);
    }

    @ApiOperation(value = "权限列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Object ajaxIndex(HttpServletRequest request) {
        Map<String, Object> result = authorizationsService.getListViewEntity(request, viewEntitys, Authorizes.class);
        List<Object> records = new ArrayList<>();
        for (Object item : (List<?>) result.get("list")) {
            Map<String, Object> recordMap = new HashMap<>();
            String authorize = ((Authorizes) item).authorize;
            String authorizename = "";
            SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
            Object authorizess = saasUtils.getPlatformAuthorizes();
            if (!Utils.isEmpty(authorizess)) {
                for (Object authorizes : (List<?>) authorizess) {
                    if (((HashMap<String,String>) authorizes).getOrDefault("authorize","").equals(authorize)) {
                        authorizename = ((HashMap<String,String>) authorizes).getOrDefault("authlabel","");
                        break;
                    }
                }
            }

            recordMap.put("id", authorize);
            recordMap.put("authorize", authorizename);
            recordMap.put("userlist", String.join(",", ((Authorizes) item).userlist));
            recordMap.put("author",((Authorizes) item).author);
            recordMap.put("published",((Authorizes) item).published);
            recordMap.put("updated",((Authorizes) item).updated);
            records.add(recordMap);
        }
        return new WebResponse().code(0).data(records).count(result.get("total"));
    }

    @ApiOperation(value = "授权请求接口")
    @PostMapping("/authorizeAction/ajax")
    @SuppressWarnings("unchecked")
    public void authorize(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            Object authorizeids = httpRequest.get("authorizeids");
            List<Users> authusers = new ArrayList<>();
            if(authorizeids instanceof String) {
                List<Users> tmp = authorizationsService.getUsersByAuthorize(authorizeids.toString());
                authusers.addAll(tmp);
            }else if(authorizeids instanceof List){
                for(Object auth: (List<?>)authorizeids){
                    List<Users> tmp = authorizationsService.getUsersByAuthorize(auth.toString());
                    authusers.addAll(tmp);
                }
            }
            if(!authusers.isEmpty()) {
                List<String> ids = new ArrayList<>(1);
                for(Users users: authusers){
                    if(!ids.contains(users.profileid)) {
                        ids.add(users.profileid);
                    }
                }
                if(!ids.isEmpty()){
                    request.setAttribute(SELECT_KEY,ids);
                }
            }
            request.getRequestDispatcher("/departments/popupview/users").forward(request, response);
        } catch (ServletException | IOException e) {
            log.error(e.getMessage());
        }
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
        model.addAttribute("ENTITYURL", "authorizes/listentity/ajax");
        model.addAttribute("ISPOPUP",true);
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        List<String> ids = new ArrayList<>(1);
        if(!Utils.isEmpty(httpRequest.getOrDefault(SELECT_KEY,""))){
            if(httpRequest.get(SELECT_KEY) instanceof String) {
                ids.add(httpRequest.get(SELECT_KEY).toString());
            }else if(httpRequest.get(SELECT_KEY) instanceof List){
                ids.addAll((List<String>)httpRequest.get(SELECT_KEY));
            }
        }
        if (!Utils.isEmpty(httpRequest.getOrDefault(EXCLUDE_KEY, ""))) {
            if(httpRequest.get(EXCLUDE_KEY) instanceof String) {
                ids.add(httpRequest.get(EXCLUDE_KEY).toString());
            }else if(httpRequest.get(EXCLUDE_KEY) instanceof List){
                ids.addAll((List<String>)httpRequest.get(EXCLUDE_KEY));
            }
        }
        httpRequest.put(EXCLUDE_KEY,ids);
        return viewEntitys.popupView(httpRequest, model);
    }

}
