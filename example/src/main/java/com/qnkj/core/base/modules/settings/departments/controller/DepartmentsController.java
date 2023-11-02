package com.qnkj.core.base.modules.settings.departments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.modules.settings.departments.service.IDepartmentsService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.utils.RolesUtils;
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
@Controller("Settings-Departments")
@RequiredArgsConstructor
@Api(tags = "系统设置：部门管理")
@RequestMapping("departments")
public class DepartmentsController {
    private final IDepartmentsService departmentsService;

    @ApiOperation(value = "部门表单请求接口")
    @GetMapping("/listview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("MODULE", "departments");
        model.addAttribute("TABNAME", "部门管理");
        model.addAttribute("TREEDATA", departmentsService.getRoleTree(null,true));
        if(!(ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) && !RolesUtils.isEdit("departments")){
            model.addAttribute("READONLY", true);
        }
        return WebViews.view("modules/settings/departments/DepartmentsTreeView");
    }

    @ApiOperation(value = "部门树结构请求接口")
    @PostMapping("/tree")
    @ResponseBody
    public Object getTreeData(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        if (!Utils.isEmpty(httpRequest.get("departmentid"))) {
            return departmentsService.getTreeNodes(Utils.objectToString(httpRequest.get("departmentid")));
        } else {
            return departmentsService.getRoleTree(null,true);
        }
    }

    @ApiOperation(value = "部门操作请求接口（新增、修改、删除）")
    @PostMapping("/operate")
    public Object operate(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        if (!Utils.isEmpty(httpRequest.get("operate"))) {
            model.addAttribute("MODULE", "departments");
            if ("edit".equals(httpRequest.get("operate").toString())) {
                if (!Utils.isEmpty(httpRequest.get("leadership"))) {
                    List<?> names = (List<?>) ProfileUtils.getProfileName(httpRequest.get("leadership"));
                    if (!Utils.isEmpty(names)) {
                        model.addAttribute("LEADERSHIPNAME", Utils.objectToString(names));
                        names.clear();
                    }
                }
                if (!Utils.isEmpty(httpRequest.get("mainleadership"))) {
                    List<?> names = (List<?>) ProfileUtils.getProfileName(httpRequest.get("mainleadership"));
                    if (!Utils.isEmpty(names)) {
                        model.addAttribute("MAINLEADERSHIPNAME", Utils.objectToString(names));
                        names.clear();
                    }
                }
                model.addAttribute("OPERATE", "修改");
                model.addAttribute("RECORD", Utils.objectToString(httpRequest.get("departmentid")));
                model.addAttribute("NAME", Utils.objectToString(httpRequest.get("departmentname")));
                model.addAttribute("SEQUENCE", Utils.objectToString(httpRequest.get("sequence")));
                model.addAttribute("ISHIDE", Utils.objectToString(httpRequest.get("ishide")));
                model.addAttribute("LEADERSHIP", Utils.objectToString(httpRequest.get("leadership")));
                model.addAttribute("MAINLEADERSHIP", Utils.objectToString(httpRequest.get("mainleadership")));
                model.addAttribute("PARENTNAME", Utils.objectToString(httpRequest.get("parentname")));
                model.addAttribute("PARENTROLEID", Utils.objectToString(httpRequest.get("parentid")));
                model.addAttribute("ENTITYURL", "users/listentity/ajax");
                Map<String, Object> usernameMap = new HashMap<>(1);
                usernameMap.put("field", "username");
                usernameMap.put("title", "姓名");
                Map<String, Object> accountMap = new HashMap<>(1);
                accountMap.put("field", "account");
                accountMap.put("title", "账户名称");
                Map<String, Object> mobileMap = new HashMap<>(1);
                mobileMap.put("field", "mobile");
                mobileMap.put("title", "手机号码");
                Map<String, Object> mailboxMap = new HashMap<>(1);
                mailboxMap.put("field", "mailbox");
                mailboxMap.put("title", "邮箱");
                List<Object> list = new ArrayList<>(ImmutableSet.of(
                        usernameMap, accountMap, mobileMap, mailboxMap
                ));
                model.addAttribute("USERSLISTHEADER", Utils.objectToJson(list));
            } else if ("add".equals(httpRequest.get("operate").toString())) {
                model.addAttribute("OPERATE", "新建");
                model.addAttribute("RECORD", "-1");
                model.addAttribute("PARENTNAME", Utils.objectToString(httpRequest.get("departmentname")));
                model.addAttribute("PARENTROLEID", Utils.objectToString(httpRequest.get("departmentid")));
            } else if ("remove".equals(httpRequest.get("operate").toString())) {
                model.addAttribute("RECORD", Utils.objectToString(httpRequest.get("departmentid")));
                model.addAttribute("NAME", Utils.objectToString(httpRequest.get("departmentname")));
                model.addAttribute("TREEDATA", departmentsService.getRoleTree(Utils.objectToString(httpRequest.get("departmentid"))));
                return WebViews.view("modules/settings/departments/DepartmentsTransferView");
            }
            return WebViews.view("modules/settings/departments/DepartmentsOperateView");
        }
        return null;
    }

    @ApiOperation(value = "部门信息保存接口")
    @PostMapping("/save")
    @ResponseBody
    public Object saveTreeData(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            Object node = departmentsService.save(httpRequest);
            ObjectMapper mapper = new ObjectMapper();
            String json = "{}";
            try {
                json = mapper.writeValueAsString(node);
            } catch (Exception ignored) {
            }

            String finalJson = json;
            return new WebResponse().success(0, "保存完成").put("node", finalJson);
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "部门信息删除接口")
    @PostMapping("/delete")
    @ResponseBody
    public Object deleteTreeData(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            departmentsService.delete(httpRequest);
            return new WebResponse().success(0, "保存完成");
        } catch (Exception e) {
            return new WebResponse().fail("删除失败，请稍后再试。。。");
        }
    }

    @ApiOperation(value = "部门信息移动接口")
    @PostMapping("/dropmove")
    @ResponseBody
    public Object dropMove(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            departmentsService.dropMove(httpRequest);
            return new WebResponse().code(0);
        } catch (Exception e) {
            return new WebResponse().fail("部门移动失败");
        }
    }

}
