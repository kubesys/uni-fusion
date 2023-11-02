package com.qnkj.core.base.modules.lcdp.developments.controller;

import com.qnkj.common.entitys.Module;
import com.qnkj.common.entitys.Program;
import com.qnkj.common.services.IProgramServices;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.lcdp.developments.service.IDevelopmentService;
import com.qnkj.core.utils.AuthorizeUtils;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * create by 徐雁
 * create date 2020/11/9
 */

@Validated
@Controller("lcdp-Developments")
@RequiredArgsConstructor
@Scope("prototype")
@Api(tags = "低代码开发：菜单设计")
@RequestMapping("/developments")
public class DevelopmentController {
    private final IDevelopmentService developmentService;
    private final IProgramServices programServices;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "开发页面请求接口")
    @GetMapping("/listview")
    public Object index(Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        return WebViews.view("modules/settings/developments/TreeView");
    }

    @ApiOperation(value = "开发页面树结构请求接口")
    @PostMapping("/tree")
    @ResponseBody
    public Object getTreeData() {
        return developmentService.getTree();
    }

    //region 菜单部份
    @ApiOperation(value = "新增菜单表单")
    @PostMapping("/addmenuview")
    public Object addMenu(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TREENODE", httpRequest.getOrDefault("title", ""));
        model.addAttribute("ADDLEVEL",httpRequest.get("level"));
        return WebViews.view("modules/settings/developments/AddMenuView");
    }

    @ApiOperation(value = "编辑菜单表单")
    @PostMapping("/editmenuview")
    public Object editMenu(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("EDITLEVEL", httpRequest.get("level"));
        if (!"program".equals(httpRequest.get("level"))) {
            model.addAttribute("TREENODE", httpRequest.getOrDefault("title", ""));
        }
        model.addAttribute("RESULT", Utils.objectToJson(developmentService.getGroup(httpRequest)));
        return WebViews.view("modules/settings/developments/EditMenuView");
    }

    @ApiOperation(value = "菜单组保存表单")
    @PostMapping("/menuviewsave")
    @ResponseBody
    public Object menuViewSave(HttpServletRequest request) {
        try {
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            developmentService.update(httpRequest);
            return new WebResponse().refresh().code(0);
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "菜单组删除")
    @PostMapping("/menuviewdelete")
    @ResponseBody
    public Object menuViewDelete(HttpServletRequest request) {
        try {
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            developmentService.delete(httpRequest);
            return new WebResponse().refresh().code(0);
        } catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
        }
    }

    @ApiOperation(value = "编辑菜单表单")
    @PostMapping("/editmoduleview")
    public Object editModule(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        if (!Utils.isEmpty(httpRequest.get("modulename"))) {
            Module moduleinfo = (Module)developmentService.getModule(httpRequest.get("modulename").toString());
            if (!Utils.isEmpty(moduleinfo)) {
                if (Utils.isNotEmpty( moduleinfo.Fields)) {
                    moduleinfo.Fields = moduleinfo.Fields.stream().map(v -> { v.html = ""; return v;}).collect(Collectors.toList());
                }
                model.addAttribute("MODULEINFO", Utils.objectToJson(moduleinfo));
            }
            Program program = programServices.get(moduleinfo.moduleMenu.program);
            Map<String, Object> authorizes = AuthorizeUtils.getAuthorizesByType(program.authorize);
            List<String> selectAuthorize = new ArrayList<>();
            if (Utils.isNotEmpty(moduleinfo.Tabinfo.dataauthorize)) {
                selectAuthorize = Arrays.asList(moduleinfo.Tabinfo.dataauthorize.split(","));
            }
            List<String> finalSelectAuthorize = selectAuthorize;
            List<HashMap<String, Object>> lists =  authorizes.entrySet().stream().map(v -> {
                HashMap<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("name",v.getKey());
                infoMap.put("value",v.getValue());
                if (finalSelectAuthorize.contains(v.getKey())) {
                    infoMap.put("selected",true);
                } else {
                    infoMap.put("selected",false);
                }
                return infoMap;
            }).collect(Collectors.toList());

            model.addAttribute("AUTHORIZE", Utils.objectToJson(lists));
            model.addAttribute("SERVICECLASS", Utils.objectToJson(developmentService.getAllModule()));
        }
        return WebViews.view("modules/settings/developments/EditModuleView");
    }
    //endregion

    //region 保存模块信息
    @ApiOperation(value = "保存模块信息")
    @PostMapping("/savemoduleinfo")
    @ResponseBody
    public Object saveModule(HttpServletRequest request, Model model) {
        try {
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            developmentService.saveModule((HashMap)httpRequest.get("moduleinfo"));
            return new WebResponse().success("保存完成").refresh();
        }catch (Exception e){
            return new WebResponse().fail(e.getMessage());
        }
    }
    //endregion

    //region 创建模块文件
    @ApiOperation(value = "创建模块文件")
    @PostMapping("/createmodulefile")
    @ResponseBody
    public Object createModule(HttpServletRequest request) {
        try {
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            developmentService.createModule(httpRequest.get("modulename").toString(),true,true,true);
            return new WebResponse().success("创建模块完成");
        }catch (Exception e){
            return new WebResponse().fail(e.getMessage());
        }
    }
    //endregion

    //region 一键生成
    @ApiOperation(value = "创建模块文件")
    @PostMapping("/createallmodule")
    @ResponseBody
    public Object createAllModule(HttpServletRequest request) {
        try {
            developmentService.createAllModule();
            return new WebResponse().success("创建模块完成");
        }catch (Exception e){
            return new WebResponse().fail(e.getMessage());
        }
    }
    //endregion
}
