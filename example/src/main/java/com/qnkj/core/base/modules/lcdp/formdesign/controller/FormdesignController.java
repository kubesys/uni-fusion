package com.qnkj.core.base.modules.lcdp.formdesign.controller;

import com.qnkj.common.entitys.*;
import com.qnkj.common.services.IProgramServices;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.WebResponse;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.lcdp.formdesign.entity.FormDesign;
import com.qnkj.core.base.modules.lcdp.formdesign.service.IFormdesignService;
import com.qnkj.core.utils.AuthorizeUtils;
import com.qnkj.core.utils.PickListUtils;
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
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * create by 徐雁
 * create date 2021/4/30
 */

@Validated
@Controller("lcdp-Formdesign")
@RequiredArgsConstructor
@Scope("prototype")
@Api(tags = "低代码开发：表单设计")
@RequestMapping("/formdesign")
public class FormdesignController {
    private final IFormdesignService formdesignService;
    private final IProgramServices programServices;
    private BaseEntityUtils viewEntitys = null;

    @PostConstruct
    public void init() {
        viewEntitys = BaseEntityUtils.init(this);
    }

    @ApiOperation(value = "开发页面请求接口")
    @GetMapping("/listview")
    public Object index(HttpServletRequest request, Model model) {
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        return viewEntitys.listView(request,model,"modules/settings/formdesigns/ListView");
    }


    @ApiOperation(value = "列表Ajax请求接口")
    @GetMapping("/listentity/ajax")
    @ResponseBody
    public Object ajaxIndex(HttpServletRequest request) {
        formdesignService.updateModuleInfo();
        Map<String, Object> list = formdesignService.getListViewEntity(request, viewEntitys, FormDesign.class);
        return viewEntitys.listEntity(request,list);
    }

    @ApiOperation(value = "开发页面请求接口")
    @PostMapping("/editviewAction")
    public Object editView(HttpServletRequest request, Model model) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        model.addAttribute("MODULE", viewEntitys.getModuleName());
        model.addAttribute("TABNAME", viewEntitys.getModuleLabel());
        Object formDesigns = formdesignService.getDesignInfo(httpRequest);
        if(!Utils.isEmpty(formDesigns)){
            model.addAttribute("MODULEINFO", Utils.objectToJson(((HashMap<?,?>)formDesigns).get("MODULEINFO")));
            model.addAttribute("AUTHORIZE", Utils.objectToJson(((HashMap<?,?>)formDesigns).get("AUTHORIZE")));
            model.addAttribute("SERVICECLASS", Utils.objectToJson(((HashMap<?,?>)formDesigns).get("SERVICECLASS")));
        }
        return WebViews.view("modules/settings/formdesigns/DesignFormPage");
    }

    @ApiOperation(value = "开发页面请求接口")
    @PostMapping("/designmodule/ajax")
    @ResponseBody
    public Object getDesignModule(HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            Object moduleInfo = formdesignService.getDesignModuleInfo(httpRequest);
            if(!Utils.isEmpty(moduleInfo)){
                return new WebResponse().success().data(moduleInfo);
            } else {
                return new WebResponse().fail("获取模块信息失败");
            }
        }catch (Exception e) {
            return new WebResponse().fail("获取模块信息失败");
        }
    }

    @ApiOperation(value = "开发页面请求接口")
    @PostMapping("/design/save")
    @ResponseBody
    public void designSave(HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            request.getRequestDispatcher("/developments/savemoduleinfo").forward(request, response);
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            formdesignService.updateDesignStatus(httpRequest);
        }catch (Exception ignored) { }
    }

    @ApiOperation(value = "生成模块接口")
    @PostMapping("/generateAction/ajax")
    @ResponseBody
    public Object generateAjax(HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            formdesignService.generateModule(httpRequest);
            return new WebResponse().success("生成模块完成").refresh();
        }catch (Exception e) {
            return new WebResponse().fail("生成模块失败");
        }
    }

    @ApiOperation(value = "生成所有模块接口")
    @PostMapping("/generateallAction/ajax")
    @ResponseBody
    public Object generateAllAjax() {
        try{
            formdesignService.generateAllModule();
            return new WebResponse().success("生成模块完成").refresh();
        }catch (Exception e) {
            return new WebResponse().fail("生成模块失败");
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

    @ApiOperation(value = "获取模块信息请求接口")
    @PostMapping("/design/getmoduleinfo")
    @ResponseBody
    public Object designModuleinfo(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            Object formDesigns = formdesignService.getDesignInfo(httpRequest);
            if(!Utils.isEmpty(formDesigns)){
                return new WebResponse().success().data(Utils.classToData(formDesigns));
            }
            return new WebResponse().fail("获取模块信息失败");
        }catch (Exception e) {
            return new WebResponse().fail("获取模块信息失败");
        }
    }

    @ApiOperation(value = "交互请求接口")
    @PostMapping("/design/actionslist")
    public Object designActionsList(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            Object formDesigns = formdesignService.getDesignInfo(httpRequest);
            if(!Utils.isEmpty(formDesigns)){
                Module module = (Module)((HashMap<?,?>)formDesigns).get("MODULEINFO");
                model.addAttribute("MODULE",module.Tabinfo.modulename);
                model.addAttribute("RECORD",httpRequest.get("record"));
            }
            return WebViews.view("modules/settings/formdesigns/ActionsList");
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation(value = "数据权限请求接口")
    @PostMapping("/design/datapermission")
    public Object designDataPermission(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            Object formDesigns = formdesignService.getDesignInfo(httpRequest);
            if(!Utils.isEmpty(formDesigns)) {
                Module module = (Module)((HashMap<?,?>)formDesigns).get("MODULEINFO");
                model.addAttribute("MODULE",module.Tabinfo.modulename);
                model.addAttribute("RECORD",httpRequest.get("record"));
                if(Utils.isNotEmpty(module.dataPermission)) {
                    model.addAttribute("DATAPERMISSION", Utils.objectToJson(module.dataPermission.getExpressions()));
                }
                List<Object> fields = new ArrayList<>();
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
                fieldMap.put("表单字段", formdesignService.getFlowform(module.Tabinfo.modulename));
                fields.add(fieldMap);
                model.addAttribute("Fields",fields);
                Program program = programServices.get(module.moduleMenu.program);
                Map<String, Object> authorizes = AuthorizeUtils.getAuthorizesByType(program.authorize);
                List<Map<String, Object>> lists =  authorizes.entrySet().stream().map(v -> {
                    Map<String,Object> infoMap = new HashMap<>(1);
                    infoMap.put("name",v.getKey());
                    infoMap.put("value",v.getValue());
                    infoMap.put("selected",false);
                    return infoMap;
                }).collect(Collectors.toList());
                model.addAttribute("AUTHORIZES", lists);
            }
            return WebViews.view("modules/settings/formdesigns/DataPermission");
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation(value = "保存数据权限请求接口")
    @PostMapping("/design/datapermission/save")
    @ResponseBody
    public Object designSaveDataPermission(HttpServletRequest request) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            formdesignService.saveDataPermission(httpRequest);
            return new WebResponse().success();
        }catch (Exception e) {
            return new WebResponse().fail(e.getMessage());
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

    @ApiOperation(value = "视图编辑请求接口")
    @PostMapping("/design/actions")
    public Object designActions(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            Object formDesigns = formdesignService.getDesignInfo(httpRequest);
            if(!Utils.isEmpty(formDesigns)){
                Module module = (Module)((HashMap<?,?>)formDesigns).get("MODULEINFO");
                model.addAttribute("MODULE",module.Tabinfo.modulename);
                if(!Utils.isEmpty(httpRequest.get("actionkey")) && !Utils.isEmpty(module.actions)) {
                    for(Action item: module.actions){
                        if(item.actionkey.equals(httpRequest.get("actionkey"))){
                            model.addAttribute("ACTION", Utils.objectToJson(item));
                            List<String> selectAuthorize = new ArrayList<>();
                            if (Utils.isNotEmpty(item.authorizes)) {
                                selectAuthorize = Arrays.asList(item.authorizes.split(","));
                            }
                            model.addAttribute("ISEDIT", true);
                            Program program = programServices.get(module.moduleMenu.program);
                            Map<String, Object> authorizes = AuthorizeUtils.getAuthorizesByType(program.authorize);
                            List<String> finalSelectAuthorize = selectAuthorize;
                            List<Map<String, Object>> lists =  authorizes.entrySet().stream().map(v -> {
                                Map<String,Object> infoMap = new HashMap<>(1);
                                infoMap.put("name",v.getKey());
                                infoMap.put("value",v.getValue());
                                if (finalSelectAuthorize.contains(v.getKey())) {
                                    infoMap.put("selected",true);
                                } else {
                                    infoMap.put("selected",false);
                                }
                                return infoMap;
                            }).collect(Collectors.toList());
                            model.addAttribute("AUTHORIZES", lists);
                            break;
                        }
                    }
                }

            }
            return WebViews.view("modules/settings/formdesigns/Actions");
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation(value = "视图列表请求接口")
    @PostMapping("/design/customviewlist")
    public Object designCustomviewList(HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            Object formDesigns = formdesignService.getDesignInfo(httpRequest);
            if(!Utils.isEmpty(formDesigns)){
                Module module = (Module)((HashMap<?,?>)formDesigns).get("MODULEINFO");
                if(!Utils.isEmpty(module.Fields) && !module.Fields.isEmpty()) {
                    model.addAttribute("MODULE", module.Tabinfo.modulename);
                    model.addAttribute("RECORD", httpRequest.get("record"));
                    return WebViews.view("modules/settings/formdesigns/CustomViewsList");
                }
            }
            return WebViews.body(response,new WebResponse().alert().message("请先设计表单后再试"));
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation(value = "视图编辑请求接口")
    @PostMapping("/design/customview")
    public Object designCustomView(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            Object formDesigns = formdesignService.getDesignInfo(httpRequest);
            if(!Utils.isEmpty(formDesigns)){
                Module module = (Module)((HashMap<?,?>)formDesigns).get("MODULEINFO");
                model.addAttribute("MODULE",module.Tabinfo.modulename);
                List<TabField> fields = new ArrayList<>();
                List<String> fieldNames = new ArrayList<>();
                if(!Utils.isEmpty(module.Fields)) {
                    for (TabField field : module.Fields) {
                        fieldNames.add(field.fieldname);
                        if (field.uitype != 3 && field.uitype != 18  && field.uitype != 22 && field.uitype != 24 && field.uitype != 30 && field.uitype != 31 && field.uitype != 33) {
                            fields.add(field);
                        }
                    }
                }
                if(!fieldNames.contains("author")){
                    fields.add(new TabField().fieldname("author").fieldlabel("创建人"));
                }
                if(!fieldNames.contains("updated")){
                    fields.add(new TabField().fieldname("updated").fieldlabel("更新时间"));
                }
                if(!fieldNames.contains("published")){
                    fields.add(new TabField().fieldname("published").fieldlabel("创建时间"));
                }
                if(!fieldNames.contains("approvalstatus")){
                    fields.add(new TabField().fieldname("approvalstatus").fieldlabel("状态"));
                }
                if(!Utils.isEmpty(httpRequest.get("viewname")) && !Utils.isEmpty(module.CustomViews)) {
                    for(CustomView item: module.CustomViews){
                        if(item.viewname.equals(httpRequest.get("viewname"))){
                            model.addAttribute("CUSTOMVIEW", Utils.objectToJson(item));
                            model.addAttribute("ISEDIT", true);
                        }
                    }
                }
                model.addAttribute("FIELDS", Utils.objectToJson(fields));
                model.addAttribute("AUTHORIZE", Utils.objectToJson(((HashMap<?,?>)formDesigns).get("AUTHORIZE")));
            }
            return WebViews.view("modules/settings/formdesigns/CustomViews");
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation(value = "筛选配置请求接口")
    @PostMapping("/design/searchcolumnslist")
    public Object designSearchColumnsList(HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            Object formDesigns = formdesignService.getDesignInfo(httpRequest);
            if(!Utils.isEmpty(formDesigns)){
                Module module = (Module)((HashMap<?,?>)formDesigns).get("MODULEINFO");
                if(!Utils.isEmpty(module.Fields) && !module.Fields.isEmpty()) {
                    model.addAttribute("MODULE", module.Tabinfo.modulename);
                    model.addAttribute("RECORD", httpRequest.get("record"));
                    return WebViews.view("modules/settings/formdesigns/SearchColumnsList");
                }
            }
            return WebViews.body(response,new WebResponse().alert().message("请先设计表单后再试"));
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation(value = "筛选编辑请求接口")
    @PostMapping("/design/searchcolumn")
    public Object designSearchColumn(HttpServletRequest request, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            Object formDesigns = formdesignService.getDesignInfo(httpRequest);
            if(!Utils.isEmpty(formDesigns)){
                Module module = (Module)((HashMap<?,?>)formDesigns).get("MODULEINFO");
                model.addAttribute("MODULE",module.Tabinfo.modulename);
                List<TabField> fields = new ArrayList<>();
                List<String> fieldNames = new ArrayList<>();
                if(!Utils.isEmpty(module.Fields)) {
                    for (TabField field : module.Fields) {
                        fieldNames.add(field.fieldname);
                        if (field.uitype != 3 && field.uitype != 18 && field.uitype != 17 && field.uitype != 22 && field.uitype != 30 && field.uitype != 31) {
                            fields.add(field);
                        }
                    }
                }
                if(!fieldNames.contains("author")){
                    fields.add(new TabField().fieldname("author").fieldlabel("创建人"));
                }
                if(!fieldNames.contains("updated")){
                    fields.add(new TabField().fieldname("updated").fieldlabel("更新时间"));
                }
                if(!fieldNames.contains("published")){
                    fields.add(new TabField().fieldname("published").fieldlabel("创建时间"));
                }
                if(!Utils.isEmpty(httpRequest.get("searchfield")) && !Utils.isEmpty(module.SearchColumns)) {
                    for(SearchColumn item: module.SearchColumns){
                        if(httpRequest.get("searchfield") instanceof List){
                            httpRequest.put("searchfield",String.join(",",(List)httpRequest.get("searchfield")));
                        }
                        if(item.fieldname.equals(httpRequest.get("searchfield"))){
                            model.addAttribute("SEARCHCOLUMN", Utils.objectToJson(item));
                            model.addAttribute("ISEDIT", true);
                            model.addAttribute("ISFLOW", module.Tabinfo.searchcolumnflow);
                        }
                    }
                }
                model.addAttribute("FIELDS", Utils.objectToJson(fields));
            }
            return WebViews.view("modules/settings/formdesigns/SearchColumns");
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation(value = "弹窗编辑请求接口")
    @PostMapping("/design/popudialogs")
    public Object designPopuDialogs(HttpServletRequest request, HttpServletResponse response, Model model) {
        try{
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            Object formDesigns = formdesignService.getDesignInfo(httpRequest);
            if(!Utils.isEmpty(formDesigns)){
                Module module = (Module)((HashMap<?,?>)formDesigns).get("MODULEINFO");
                if(!Utils.isEmpty(module.Fields) && !module.Fields.isEmpty()) {
                    model.addAttribute("MODULE", module.Tabinfo.modulename);
                    model.addAttribute("MODULEINFO", Utils.objectToJson(module));
                    List<TabField> fields = new ArrayList<>();
                    List<String> fieldNames = new ArrayList<>();
                    if (!Utils.isEmpty(module.Fields)) {
                        for (TabField field : module.Fields) {
                            fieldNames.add(field.fieldname);
                            if (field.uitype != 3 && field.uitype != 18 && field.uitype != 17 && field.uitype != 22 && field.uitype != 30 && field.uitype != 31) {
                                fields.add(field);
                            }
                        }
                    }
                    if (!fieldNames.contains("author")) {
                        fields.add(new TabField().fieldname("author").fieldlabel("创建人"));
                    }
                    if (!fieldNames.contains("updated")) {
                        fields.add(new TabField().fieldname("updated").fieldlabel("更新时间"));
                    }
                    if (!fieldNames.contains("published")) {
                        fields.add(new TabField().fieldname("published").fieldlabel("创建时间"));
                    }
                    model.addAttribute("POPUPDIALOG", Utils.classToData(module.Popupdialog));
                    model.addAttribute("FIELDS", Utils.classToData(fields));
                    return WebViews.view("modules/settings/formdesigns/PopupDialogs");
                }
            }
            return WebViews.body(response,new WebResponse().alert().message("请先设计表单后再试"));
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

}
