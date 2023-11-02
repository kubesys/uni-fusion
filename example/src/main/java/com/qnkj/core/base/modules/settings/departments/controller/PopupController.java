package com.qnkj.core.base.modules.settings.departments.controller;

import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.departments.service.IDepartmentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * create by 徐雁
 */
@Validated
@Controller("Settings-Departments-Popup")
@RequiredArgsConstructor
@Scope("prototype")
@Api(tags = "系统设置：部门管理")
@RequestMapping("departments/popupview")
public class PopupController {
    private final IDepartmentsService departmentsService;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "部门树结构请求接口")
    @PostMapping("/tree")
    @ResponseBody
    public Object getTreeData(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        if (!Utils.isEmpty(httpRequest.get("departmentid"))) {
            return departmentsService.getRoleTree(Utils.objectToString(httpRequest.get("departmentid")));
        } else {
            return departmentsService.getRoleTree(null);
        }
    }

    @ApiOperation(value = "部门弹窗请求接口")
    @PostMapping("/trees")
    public Object getTreesData(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        Object result;
        if (!Utils.isEmpty(httpRequest.get("departmentid"))) {
            result = departmentsService.getRoleTree(Utils.objectToString(httpRequest.get("departmentid")));
        } else {
            result = departmentsService.getRoleTree(null);
        }
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("SELECTTREE", true);
        model.addAttribute("TREEDATA", Utils.objectToJson(result));
        return viewEntitys.popupTreeView(httpRequest, model);
    }

    @ApiOperation(value = "用户弹窗请求接口")
    @PostMapping("/users")
    public Object getUserTreeData(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        Object result;
        if (!Utils.isEmpty(httpRequest.get("departmentid"))) {
            result = departmentsService.getRoleTreeByUsers(Utils.objectToString(httpRequest.get("departmentid")));
        } else {
            result = departmentsService.getRoleTreeByUsers(null);
        }
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TREEDATA", result);
        return viewEntitys.popupTreeView(httpRequest, model);
    }
}
