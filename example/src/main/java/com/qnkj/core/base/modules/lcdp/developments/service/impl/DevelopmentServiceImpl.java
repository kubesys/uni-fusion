package com.qnkj.core.base.modules.lcdp.developments.service.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.configs.BaseMenuConfig;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.common.services.*;
import com.qnkj.common.utils.*;
import com.qnkj.core.base.CacheBaseEntitys;
import com.qnkj.core.base.modules.lcdp.developments.service.IDevelopmentService;
import com.qnkj.core.base.services.IBaseService;
import com.qnkj.core.plugins.compiler.CompilerUtils;
import com.qnkj.core.plugins.feign.FeignApi;
import com.qnkj.core.utils.ProfileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * create by 徐雁
 * create date 2020/11/9
 */

@Slf4j
@Service
public class DevelopmentServiceImpl implements IDevelopmentService {
    private final IProgramServices programServices;
    private final IParentTabServices parentTabServices;
    private final IModuleMenuServices moduleMenuServices;
    private final IModuleServices moduleServices;
    private final IBlockServices blockServices;
    private final ITabFieldServices fieldServices;
    private final ITabServices tabServices;
    private final ICustomViewServices customViewServices;
    private final ISearchColumnServices searchColumnServices;
    private final IActionServices actionServices;
    private final IOutsideLinkServices outsideLinkServices;
    private final IPopupDialogServices popupDialogServices;
    private final IModentityNumServices modentityNumServices;
    private final IEntityLinkServices entityLinkServices;
    private final IPickListServices pickListServices;
    private final ILayoutServices layoutServices;

    public DevelopmentServiceImpl(IProgramServices programServices, IParentTabServices parentTabServices, IModuleMenuServices moduleMenuServices, IModuleServices moduleServices, IBlockServices blockServices, ITabFieldServices fieldServices, ITabServices tabServices, ICustomViewServices customViewServices, ISearchColumnServices searchColumnServices, IActionServices actionServices, IOutsideLinkServices outsideLinkServices, IPopupDialogServices popupDialogServices, IModentityNumServices modentityNumServices, IEntityLinkServices entityLinkServices, IPickListServices pickListServices,ILayoutServices layoutServices) {
        this.programServices = programServices;
        this.parentTabServices = parentTabServices;
        this.moduleMenuServices = moduleMenuServices;
        this.moduleServices = moduleServices;
        this.blockServices = blockServices;
        this.fieldServices = fieldServices;
        this.tabServices = tabServices;
        this.customViewServices = customViewServices;
        this.searchColumnServices = searchColumnServices;
        this.actionServices = actionServices;
        this.outsideLinkServices = outsideLinkServices;
        this.popupDialogServices = popupDialogServices;
        this.modentityNumServices = modentityNumServices;
        this.entityLinkServices = entityLinkServices;
        this.pickListServices = pickListServices;
        this.layoutServices = layoutServices;
    }

    private List<Object> getParentTab(List<Object> parenttabs,String parentname, String parentid) {
        List<Object> result = new ArrayList<>();
        List<ParentTab> parentTabs = new ArrayList<>();
        for(Object item: parenttabs){
            if(Utils.isEmpty(((Content)item).my.get("modulename")) && ((Content)item).my.get("program").equals(parentname)){
                parentTabs.add(new ParentTab(item));
            }
        }
        if(!Utils.isEmpty(parentTabs)) {
            for (ParentTab tab : parentTabs) {
                if("developmentmanager".equals(tab.name) || tab.builtin) {
                    continue;
                }
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("pId", parentid);
                infoMap.put("id", tab.id);
                infoMap.put("title", tab.label);
                infoMap.put("name", tab.label);
                infoMap.put("key",tab.name);
                infoMap.put("builtin",false);
                infoMap.put("menulevel", "parent");
                infoMap.put("iconSkin","catalogue");
                result.add(infoMap);
                List<ModuleMenu> moduleMenus = new ArrayList<>();
                for(Object item: parenttabs){
                    if(!Utils.isEmpty(((Content)item).my.get("parent")) && ((Content)item).my.get("parent").equals(tab.name) && ((Content)item).my.get("program").equals(parentname)){
                        moduleMenus.add(new ModuleMenu(item));
                    }
                }
                if(!Utils.isEmpty(moduleMenus)) {
                    for (ModuleMenu menu : moduleMenus) {
                        if("developments".equals(menu.modulename) || menu.builtin) {
                            continue;
                        }
                        Map<String,Object> deveMap = new HashMap<>(1);
                        deveMap.put("pId", tab.id);
                        deveMap.put("id", menu.id);
                        deveMap.put("title", menu.label);
                        deveMap.put("name", menu.label);
                        deveMap.put("key",menu.modulename);
                        deveMap.put("builtin",false);
                        deveMap.put("menulevel", "module");
                        deveMap.put("iconSkin","module");
                        result.add(deveMap);
                    }
                }
                List<Object> sub = getParentTab(parenttabs,tab.name, tab.id);
                if(sub.size()>0){
                    result.addAll(sub);
                }
            }
        }
        return result;
    }

    @Override
    public Object getTree() {
        List<Object> result = new ArrayList<>();
        List<Program> programs = programServices.list();
        if(!Utils.isEmpty(programs)) {
            List<Object> parenttabs = new ArrayList<>();
            try {
                parenttabs = XN_Query.contentQuery().tag("parenttabs")
                        .filter("type","eic","parenttabs")
                        .notDelete()
                        .order("my.order","A_N")
                        .end(-1).execute();
            }catch (Exception ignored){}

            for (Program item : programs) {
                if(item.builtin) {
                    continue;
                }
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("pId", "");
                infoMap.put("id", item.id);
                infoMap.put("title", item.label);
                infoMap.put("name", item.label);
                infoMap.put("key",item.group);
                infoMap.put("builtin",false);
                infoMap.put("menulevel", "program");
                infoMap.put("iconSkin","catalogue");
                result.add(infoMap);

                List<Object> sub = getParentTab(parenttabs,item.group, item.id);
                if(sub.size()>0){
                    result.addAll(sub);
                }
            }
        }
        if(result.size() <= 0){
            try {
                SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
                String departmentRoot = saasUtils.getCompanyNickname();
                Program program = new Program().group("module").label(departmentRoot).order(1).authorize("general").icon("lcdp-icon-wangzhanshezhi");
                programServices.update(program);
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("pId", "");
                infoMap.put("id", program.id);
                infoMap.put("title", program.label);
                infoMap.put("name", program.label);
                infoMap.put("key",program.group);
                infoMap.put("builtin",program.builtin);
                infoMap.put("menulevel", "program");
                infoMap.put("iconSkin","catalogue");
                result.add(infoMap);
            }catch (Exception ignored) {}
        }
        return result;
    }

    @Override
    public void delete(Map<String, Object> httpRequest) throws Exception {
        if(Utils.isEmpty(httpRequest.get("nodeid"))) {
            throw new Exception("删除ID不能为空");
        }
        long deleted = DateTimeUtils.gettimeStamp();
        String tag = "parenttabs";
        if("program".equals(httpRequest.getOrDefault("level",""))){
            tag = "programs";
        }
        Content conn = XN_Content.load(httpRequest.get("nodeid").toString(), tag);
        conn.add("deleted",deleted).save(tag);
        Tab tabinfo = tabServices.get(conn.my.getOrDefault("modulename","").toString());
        if(!Utils.isEmpty(tabinfo)) {
            tabinfo.deleted = deleted;
            tabServices.update(tabinfo);
            List<Block> blocks = blockServices.list(tabinfo.modulename);
            if(!Utils.isEmpty(blocks)) {
                List<Block> tmp = new ArrayList<>(blocks);
                for (Block block : tmp) {
                    block.deleted = deleted;
                    blockServices.update(block);
                }
                tmp.clear();
                blocks.clear();
            }
            List<Layout> layouts = layoutServices.list(tabinfo.modulename);
            if(!Utils.isEmpty(layouts)) {
                List<Layout> tmp = new ArrayList<>(layouts);
                for (Layout layout : tmp) {
                    layout.deleted = deleted;
                    layoutServices.update(layout);
                }
                tmp.clear();
                layouts.clear();
            }
            List<TabField> fields = fieldServices.list(tabinfo.modulename);
            if(!Utils.isEmpty(fields)) {
                List<TabField> tmp = new ArrayList<>(fields);
                for (TabField field : tmp) {
                    field.deleted = deleted;
                    fieldServices.update(field);
                }
                tmp.clear();
                fields.clear();
            }
            List<CustomView> customViews = customViewServices.list(tabinfo.modulename);
            if(!Utils.isEmpty(customViews)) {
                List<CustomView> tmp = new ArrayList<>(customViews);
                for (CustomView customView : tmp) {
                    customView.deleted = deleted;
                    customViewServices.update(customView);
                }
                tmp.clear();
                customViews.clear();
            }
            List<SearchColumn> searchColumns = searchColumnServices.list(tabinfo.modulename);
            if(!Utils.isEmpty(searchColumns)) {
                List<SearchColumn> tmp = new ArrayList<>(searchColumns);
                for (SearchColumn searchColumn : tmp) {
                    searchColumn.deleted = deleted;
                    searchColumnServices.update(searchColumn);
                }
                tmp.clear();
                searchColumns.clear();
            }
            List<Action> actions = actionServices.list(tabinfo.modulename, -1);
            if(!Utils.isEmpty(actions)) {
                List<Action> tmp = new ArrayList<>(actions);
                for (Action action : tmp) {
                    action.deleted = deleted;
                    actionServices.update(action);
                }
                tmp.clear();
                actions.clear();
            }
            List<OutsideLink> outsideLinks = outsideLinkServices.list(tabinfo.modulename);
            if(!Utils.isEmpty(outsideLinks)) {
                List<OutsideLink> tmp = new ArrayList<>(outsideLinks);
                for (OutsideLink outsideLink : tmp) {
                    outsideLink.deleted = deleted;
                    outsideLinkServices.update(outsideLink);
                }
                tmp.clear();
                outsideLinks.clear();
            }
            PopupDialog popupDialog = popupDialogServices.get(tabinfo.modulename);
            if(!Utils.isEmpty(popupDialog)) {
                popupDialog.deleted = deleted;
                popupDialogServices.update(popupDialog);
            }
            ModentityNum modentityNum = modentityNumServices.get(tabinfo.modulename);
            if(!Utils.isEmpty(modentityNum)){
                modentityNum.deleted = deleted;
                modentityNumServices.update(modentityNum);
            }
            EntityLink entityLink = entityLinkServices.get(tabinfo.modulename);
            if(!Utils.isEmpty(entityLink)){
                entityLink.deleted = deleted;
                entityLinkServices.update(entityLink);
            }
            CacheBaseEntitys.clear(tabinfo.modulename);
            FeignApi.clearModuleCacheInfo(tabinfo.modulename);
            try{
                List<Object> invoicePrints = XN_Query.contentQuery().tag("invoice_print")
                        .filter("type","eic","invoice_print")
                        .notDelete()
                        .filter("my.printmodule","=",tabinfo.modulename)
                        .end(-1).execute();
                XN_Content.delete(invoicePrints,"invoice_print");
                invoicePrints.clear();

                List<Object> reportsettings = XN_Query.contentQuery().tag("reportsettings")
                        .filter("type","eic","reportsettings")
                        .notDelete()
                        .filter("my.modulestabid","=",tabinfo.modulename)
                        .end(-1).execute();
                XN_Content.delete(reportsettings,"reportsettings");
                reportsettings.clear();

                List<Object> approvalflows = XN_Query.contentQuery().tag("approvalflows")
                        .filter("type","eic","approvalflows")
                        .notDelete()
                        .filter("my.flowmodule","=",tabinfo.modulename)
                        .end(-1).execute();
                XN_Content.delete(approvalflows,"approvalflows");
                approvalflows.clear();

                List<String> wfs = new ArrayList<>();
                List<Object> workflowsettings = XN_Query.contentQuery().tag("workflowsettings")
                        .filter("type","eic","workflowsettings")
                        .notDelete()
                        .filter("my.flowmodule","=",tabinfo.modulename)
                        .end(-1).execute();
                if(!workflowsettings.isEmpty()) {
                    for (Object item : workflowsettings) {
                        wfs.add(((Content)item).id);
                    }
                    XN_Content.delete(workflowsettings, "workflowsettings");
                }
                workflowsettings.clear();
                if(!wfs.isEmpty()) {
                    List<Object> workflows = null;
                    List<Object> entityids = new ArrayList<>();
                    int page = 0;
                    do {
                        workflows = XN_Query.contentQuery().tag("workflows")
                                .filter("type", "eic", "workflows")
                                .filter("my.suoshuliucheng", "in", wfs)
                                .begin(page*100).end((page+1)*100).execute();
                        if(!workflows.isEmpty()) {
                            for(Object item: workflows){
                                entityids.add(((Content)item).id);
                            }
                            XN_Content.delete(workflows,"workflows");
                        }
                        page++;
                    } while (!Utils.isEmpty(workflows) && workflows.size() == 100);
                    workflows.clear();
                    if(!entityids.isEmpty()) {
                        page = 0;
                        do {
                            workflows = XN_Query.contentQuery().tag("workflowentitys")
                                    .filter("type", "eic", "workflowentitys")
                                    .filter("my.record", "in", entityids)
                                    .begin(page * 100).end((page + 1) * 100).execute();
                            if (!workflows.isEmpty()) {
                                for (Object item : workflows) {
                                    entityids.add(((Content) item).id);
                                }
                                XN_Content.delete(workflows, "workflowentitys");
                            }
                            page++;
                        } while (!Utils.isEmpty(workflows) && workflows.size() == 100);
                        workflows.clear();
                    }
                }
            }catch (Exception ignored){ }
        }
        programServices.clear();
        parentTabServices.clear();
        moduleMenuServices.clear();
    }

    @Override
    public void update(Map<String, Object> httpRequest) throws Exception {
        if(!Utils.isEmpty(httpRequest.get("edit")) && "true".equals(httpRequest.get("edit"))){
            if ("parent".equals(httpRequest.get("level"))) {
                ParentTab parentTab = new ParentTab();
                parentTab.fromRequest(httpRequest);
                parentTab.id = httpRequest.get("id").toString();
                parentTabServices.update(parentTab);
            } else if ("program".equals(httpRequest.get("level"))) {
                Program program = new Program();
                program.fromRequest(httpRequest);
                program.id = httpRequest.get("id").toString();
                programServices.update(program);
            }else if("module".equals(httpRequest.get("level"))){
                ModuleMenu moduleMenu = new ModuleMenu();
                moduleMenu.fromRequest(httpRequest);
                if(moduleMenu.url.isEmpty()) {
                    moduleMenu.url("/" + moduleMenu.modulename + "/listview");
                }
                moduleMenu.id = httpRequest.get("id").toString();
                moduleMenuServices.update(moduleMenu);
                CacheBaseEntitys.clear(moduleMenu.modulename);
                FeignApi.clearModuleCacheInfo(moduleMenu.modulename);
            }
        }else if(!Utils.isEmpty(httpRequest.get("level"))) {
            if ("parent".equals(httpRequest.get("level"))) {
                if (!Utils.isEmpty(httpRequest.get("nodeid"))) {
                    String programid = httpRequest.get("nodeid").toString();
                    ParentTab parentTab = new ParentTab();
                    parentTab.fromRequest(httpRequest);
                    parentTab.programid = programid;
                    if (!Utils.isEmpty(httpRequest.get("pid"))) {
                        parentTabServices.updateSubtab(parentTab);
                    } else {
                        parentTabServices.update(parentTab);
                    }
                } else {
                    throw new Exception("菜单组不存在");
                }
            } else if ("program".equals(httpRequest.get("level"))) {
                Program program = new Program();
                program.fromRequest(httpRequest);
                programServices.update(program);
            }else if("module".equals(httpRequest.get("level"))) {
                if(!Utils.isEmpty(httpRequest.get("pid")) && !Utils.isEmpty(httpRequest.get("nodeid"))){
                    ParentTab parentTab = parentTabServices.load(httpRequest.get("nodeid").toString());
                    ModuleMenu moduleMenu = new ModuleMenu();
                    moduleMenu.fromRequest(httpRequest);
                    if(moduleMenuServices.isModulenameExist(moduleMenu.modulename)) {
                        throw new Exception("模块名称已经存在，请更换后再试！");
                    }
                    moduleMenu.parent(parentTab.name).program(parentTab.program).parentid = parentTab.id;
                    if(moduleMenu.url.isEmpty()){
                        moduleMenu.url("/"+moduleMenu.modulename+"/listview");
                    }
                    Tab tabinfo = new Tab();
                    tabinfo.modulename(moduleMenu.modulename);
                    tabinfo.modulelabel(moduleMenu.label);
                    tabinfo.tabname(moduleMenu.modulename.toLowerCase());
                    moduleMenuServices.update(moduleMenu);
                    tabServices.update(tabinfo);
                }
            }
        }
        programServices.clear();
        parentTabServices.clear();
        moduleMenuServices.clear();
    }

    @Override
    public Object getGroup(Map<String, Object> httpRequest) {
        if(!Utils.isEmpty(httpRequest.get("nodeid"))){
            if("program".equals(httpRequest.get("level"))){
                return programServices.load(httpRequest.get("nodeid").toString());
            }else if("parent".equals(httpRequest.get("level"))){
                return parentTabServices.load(httpRequest.get("nodeid").toString());
            }else if("module".equals(httpRequest.get("level"))){
                return moduleMenuServices.load(httpRequest.get("nodeid").toString());
            }
        }
        return null;
    }

    @Override
    public Object getModule(String modulename) {
        try {
            if(!Utils.isEmpty(modulename)) {
                return moduleServices.get(modulename);
            }
        }catch (Exception ignored){}
        return null;
    }

    @Override
    public Object getAllModule() {
        return this.getAllModule(null);
    }

    @Override
    public Object getAllModule(List<String> authorize) {
        List<Object> result = new ArrayList<>();
        List<ModuleMenu> moduleMenus = new ArrayList<>();
        List<Program> programs = programServices.list();
        for (Program program : programs) {
            if(Utils.isEmpty(authorize)) {
                if (ProfileUtils.isSupplier() && !("general".equals(program.authorize) || "supplier".equals(program.authorize))) {
                    continue;
                }
                if (ProfileUtils.isManager() && !program.authorize.contains("general")) {
                    continue;
                }
            } else {
                if(!authorize.contains(program.authorize)) {
                    continue;
                }
            }
            try {
                List<ParentTab> parentTabs = parentTabServices.list(program.group);
                if(!Utils.isEmpty(parentTabs)) {
                    for (ParentTab parenttab : parentTabs) {
                        List<ParentTab> subTabs = parentTabServices.list(parenttab.name);
                        if(!Utils.isEmpty(subTabs)) {
                            for (ParentTab subtab :subTabs) {
                                List<ModuleMenu> menus = moduleMenuServices.list(parenttab.name, subtab.name);
                                if(Utils.isEmpty(menus)) {
                                    continue;
                                }
                                for (ModuleMenu moduleMenu : menus) {
                                    if (!Utils.isEmpty(moduleMenu.modulename) && !"developments".equals(moduleMenu.modulename)) {
                                        moduleMenus.add(moduleMenu);
                                    }
                                }
                            }
                        }
                        List<ModuleMenu> menus = moduleMenuServices.list(program.group, parenttab.name);
                        if(Utils.isEmpty(menus)) {
                            continue;
                        }
                        for (ModuleMenu moduleMenu : menus) {
                            if (!Utils.isEmpty(moduleMenu.modulename) && !"developments".equals(moduleMenu.modulename)) {
                                moduleMenus.add(moduleMenu);
                            }
                        }
                    }
                }
            }catch (Exception ignored) { }
        }
        if(!moduleMenus.isEmpty()){
            for(ModuleMenu moduleMenu: moduleMenus){
                Tab tab = tabServices.get(moduleMenu.modulename);
                if(!Utils.isEmpty(tab) && tab.moduletype != 1) {
                    Class<T> serviceClazz = CallbackUtils.getService(tab.modulename, IBaseService.class);
                    String serviceclass = "";
                    if(!Utils.isEmpty(serviceClazz)) {
                        Class<?>[] interfaces = serviceClazz.getInterfaces();
                        if(!Utils.isEmpty(interfaces)){
                            serviceclass = interfaces[0].getName();
                        }
                    }
                    ParentTab parentTab = parentTabServices.get(moduleMenu.program, moduleMenu.parent);
                    Program program = programServices.get(parentTab.program);
                    String parentLabel = parentTab.label;
                    while (Utils.isEmpty(program) && !Utils.isEmpty(parentTab)) {
                        parentTab = parentTabServices.load(parentTab.programid);
                        if(!Utils.isEmpty(parentTab)) {
                            program = programServices.get(parentTab.program);
                        }
                    }
                    if(serviceclass.isEmpty()){
                        try{
                            serviceclass = getWebServicePackageName(!Utils.isEmpty(program) ? program.group : moduleMenu.program,moduleMenu.modulename);
                        }catch (Exception ignored){}
                    }
                    String finalServiceclass = serviceclass;

                    String proglabel = !Utils.isEmpty(program) ? program.label : "";
                    EntityLink entityLink = entityLinkServices.get(moduleMenu.modulename);
                    Map<String,Object> infoMap = new HashMap<>(1);
                    infoMap.put("modulename",tab.modulename);
                    infoMap.put("moduletype",tab.moduletype);
                    infoMap.put("modulelabel", proglabel + "：" + parentLabel + "：" + tab.modulelabel);
                    infoMap.put("serviceclass", finalServiceclass);
                    infoMap.put("disabled",finalServiceclass.isEmpty());
                    infoMap.put("entitylink",entityLink != null && !entityLink.fieldname.isEmpty());
                    infoMap.put("builtin", moduleMenu.builtin);
                    infoMap.put("group", program.group);
                    infoMap.put("issupplier",tab.datarole == 1);
                    result.add(infoMap);
                }
            }
        }
        return result;
    }

    @Override
    public Object getModuleList() {
        List<Object> result = new ArrayList<>();
        List<ModuleMenu> moduleMenus = new ArrayList<>();
        List<Program> programs = programServices.list();
        for (Program program : programs) {
            try {
                List<ParentTab> parentTabs = parentTabServices.list(program.group);
                if(!Utils.isEmpty(parentTabs)) {
                    for (ParentTab parenttab : parentTabs) {
                        List<ParentTab> subTabs = parentTabServices.list(parenttab.name);
                        if(!Utils.isEmpty(subTabs)) {
                            for (ParentTab subtab :subTabs) {
                                List<ModuleMenu> menus = moduleMenuServices.list(parenttab.name, subtab.name);
                                if(Utils.isEmpty(menus)) {
                                    continue;
                                }
                                for (ModuleMenu moduleMenu : menus) {
                                    if (!Utils.isEmpty(moduleMenu.modulename) && !"developments".equals(moduleMenu.modulename)) {
                                        moduleMenus.add(moduleMenu);
                                    }
                                }
                            }
                        }
                        List<ModuleMenu> menus = moduleMenuServices.list(program.group, parenttab.name);
                        if(Utils.isEmpty(menus)) {
                            continue;
                        }
                        for (ModuleMenu moduleMenu : menus) {
                            if (!Utils.isEmpty(moduleMenu.modulename) && !"developments".equals(moduleMenu.modulename)) {
                                moduleMenus.add(moduleMenu);
                            }
                        }
                    }
                }
            }catch (Exception ignored) { }
        }
        if(!moduleMenus.isEmpty()){
            for(ModuleMenu moduleMenu: moduleMenus){
                Tab tab = tabServices.get(moduleMenu.modulename);
                if(!Utils.isEmpty(tab)) {
                    Class<T> serviceClazz = CallbackUtils.getService(tab.modulename, IBaseService.class);
                    String serviceclass = "";
                    if(!Utils.isEmpty(serviceClazz)) {
                        Class<?>[] interfaces = serviceClazz.getInterfaces();
                        if(!Utils.isEmpty(interfaces)){
                            serviceclass = interfaces[0].getName();
                        }
                    }
                    ParentTab parentTab = parentTabServices.get(moduleMenu.program, moduleMenu.parent);
                    Program program = programServices.get(parentTab.program);
                    String parentLabel = parentTab.label;
                    while (Utils.isEmpty(program) && !Utils.isEmpty(parentTab)) {
                        parentTab = parentTabServices.load(parentTab.programid);
                        if(!Utils.isEmpty(parentTab)) {
                            program = programServices.get(parentTab.program);
                        }
                    }
                    if(serviceclass.isEmpty()){
                        try{
                            serviceclass = getWebServicePackageName(!Utils.isEmpty(program) ? program.group : moduleMenu.program,moduleMenu.modulename);
                        }catch (Exception ignored){}
                    }
                    String finalServiceclass = serviceclass;

                    String proglabel = !Utils.isEmpty(program) ? program.label : "";
                    EntityLink entityLink = entityLinkServices.get(moduleMenu.modulename);
                    Map<String,Object> infoMap = new HashMap<>(1);
                    infoMap.put("modulename",tab.modulename);
                    infoMap.put("moduletype",tab.moduletype);
                    infoMap.put("modulelabel", proglabel + "：" + parentLabel + "：" + tab.modulelabel);
                    infoMap.put("serviceclass", finalServiceclass);
                    infoMap.put("disabled",finalServiceclass.isEmpty());
                    infoMap.put("entitylink",entityLink != null && !entityLink.fieldname.isEmpty());
                    infoMap.put("builtin", moduleMenu.builtin);
                    infoMap.put("group", program.group);
                    infoMap.put("issupplier",tab.datarole == 1);
                    result.add(infoMap);
                }
            }
        }
        return result;
    }

    @Override
    public String getModuleMenuAuthorize(String modulename) {
        ModuleMenu moduleMenu = moduleMenuServices.get(modulename);
        if(Utils.isEmpty(moduleMenu)) {
            return "";
        }
        ParentTab parentTab = parentTabServices.get(moduleMenu.program, moduleMenu.parent);
        if(Utils.isEmpty(parentTab)) {
            return "";
        }
        Program program = programServices.get(parentTab.program);
        while (Utils.isEmpty(program) && !Utils.isEmpty(parentTab)) {
            parentTab = parentTabServices.load(parentTab.programid);
            if(!Utils.isEmpty(parentTab)) {
                program = programServices.get(parentTab.program);
            }
        }
        if(Utils.isEmpty(program)) {
            return "";
        }
        return program.authorize;
    }

    private Program getProgram(String program) {
        List<Program> programs = BaseMenuConfig.getProgramSettings();
        for (Program prog : programs) {
            if(prog.group.equals(program)){
                return prog;
            }
        }
        return new Program();
    }
    private List<ParentTab> getParentTabs(String program) {
        List<ParentTab> result = new ArrayList<>();
        List<ParentTab> parentTabs = BaseMenuConfig.getParentMenus();
        for (ParentTab parenttab : parentTabs) {
            if(parenttab.program.equals(program)){
                result.add(parenttab);
            }
        }
        return result;
    }
    private ParentTab getParentTab(String program, String parent) {
        List<ParentTab> parentTabs = BaseMenuConfig.getParentMenus();
        for (ParentTab parenttab : parentTabs) {
            if(parenttab.program.equals(program) && parenttab.name.equals(parent)){
                return parenttab;
            }
        }
        return new ParentTab();
    }
    private  ParentTab getSubParentTab(String parent) {
        List<ParentTab> parentTabs = BaseMenuConfig.getParentMenus();
        for (ParentTab parenttab : parentTabs) {
            if(parenttab.name.equals(parent)){
                return parenttab;
            }
        }
        return new ParentTab();
    }

    private List<ModuleMenu> getModuleMenus(String program, String parent) {
        List<ModuleMenu> result = new ArrayList<>();
        List<Class<T>> initClass = FindClassUtils.getSonClass(BaseDataConfig.class);
        if(Utils.isEmpty(initClass)) {
            return result;
        }
        for (Class<?> clazz : initClass) {
            try {
                BaseDataConfig dataConfig = (BaseDataConfig) clazz.getConstructor().newInstance();
                if(dataConfig.moduleMenu.program.equals(program) && dataConfig.moduleMenu.parent.equals(parent)){
                    dataConfig.moduleMenu.modulename(dataConfig.tabs.modulename);
                    result.add(dataConfig.moduleMenu);
                }
            }catch (Exception ignored) {}
        }
        result.sort((o1, o2) -> {
            int diff = o1.order - o2.order;
            return Integer.compare(diff, 0);
        });
        return result;
    }
    private Tab getModuleTab(String modulename) {
        List<Class<T>> initClass = FindClassUtils.getSonClass(BaseDataConfig.class);
        if(Utils.isEmpty(initClass)) {
            return new Tab();
        }
        for (Class<?> clazz : initClass) {
            try {
                BaseDataConfig dataConfig = (BaseDataConfig) clazz.getConstructor().newInstance();
                if(dataConfig.tabs.modulename.equals(modulename)){
                    return dataConfig.tabs;
                }
            }catch (Exception ignored) {}
        }
        return new Tab();
    }

    private List<ModuleMenu> getSubTabModule(ParentTab parent) {
        List<ModuleMenu> moduleMenus = new ArrayList<>();
        List<ParentTab> subTabs = getParentTabs(parent.name);
        if(!Utils.isEmpty(subTabs)) {
            for (ParentTab subtab :subTabs) {
                List<ModuleMenu> menus = getModuleMenus(parent.name, subtab.name);
                if(!Utils.isEmpty(menus)) {
                    for (ModuleMenu moduleMenu : menus) {
                        if (!Utils.isEmpty(moduleMenu.modulename) && !"developments".equals(moduleMenu.modulename)) {
                            moduleMenus.add(moduleMenu);
                        }
                    }
                }
                moduleMenus.addAll(getSubTabModule(subtab));
            }
        }
        return moduleMenus;
    }

    @Override
    public Object getConfigModule() {
        List<Object> result = new ArrayList<>();
        List<ModuleMenu> moduleMenus = new ArrayList<>();
        List<Program> programs = BaseMenuConfig.getProgramSettings();
        programs.sort((o1, o2) -> {
            int diff = o1.order - o2.order;
            return Integer.compare(diff, 0);
        });
        for (Program program : programs) {
            try {
                List<ParentTab> parentTabs = getParentTabs(program.group);
                if(!Utils.isEmpty(parentTabs)) {
                    for (ParentTab parenttab : parentTabs) {
                        moduleMenus.addAll(getSubTabModule(parenttab));
                        List<ModuleMenu> menus = getModuleMenus(program.group, parenttab.name);
                        if(Utils.isEmpty(menus)) {
                            continue;
                        }
                        for (ModuleMenu moduleMenu : menus) {
                            if (!Utils.isEmpty(moduleMenu.modulename) && !"developments".equals(moduleMenu.modulename)) {
                                moduleMenus.add(moduleMenu);
                            }
                        }
                    }
                }
            }catch (Exception ignored) { }
        }
        if(!moduleMenus.isEmpty()){
            for(ModuleMenu moduleMenu: moduleMenus){
                Tab tab = getModuleTab(moduleMenu.modulename);
                if(!Utils.isEmpty(tab) && tab.moduletype != 1) {
                    Class<T> serviceClazz = CallbackUtils.getService(tab.modulename, IBaseService.class);
                    String serviceclass = "";
                    if(!Utils.isEmpty(serviceClazz)) {
                        Class<?>[] interfaces = serviceClazz.getInterfaces();
                        if(!Utils.isEmpty(interfaces)){
                            serviceclass = interfaces[0].getName();
                        }
                    }
                    ParentTab parentTab = getParentTab(moduleMenu.program, moduleMenu.parent);
                    Program program = getProgram(parentTab.program);
                    String parentLabel = parentTab.label;
                    while (Utils.isEmpty(program.group) && !Utils.isEmpty(parentTab.program)) {
                        parentTab = getSubParentTab(parentTab.program);
                        if(!Utils.isEmpty(parentTab)) {
                            program = getProgram(parentTab.program);
                        }
                    }
                    if(serviceclass.isEmpty()){
                        try{
                            serviceclass = getWebServicePackageName(!Utils.isEmpty(program) ? program.group : moduleMenu.program,moduleMenu.modulename);
                        }catch (Exception ignored){}
                    }
                    String finalServiceclass = serviceclass;

                    String proglabel = !Utils.isEmpty(program) ? program.label : "";
                    Map<String,Object> infoMap = new HashMap<>(1);
                    infoMap.put("modulename",tab.modulename);
                    infoMap.put("modulelabel",proglabel + "：" + parentLabel + "：" + tab.modulelabel);
                    infoMap.put("serviceclass", finalServiceclass);
                    infoMap.put("disabled",finalServiceclass.isEmpty());
                    infoMap.put("issupplier",tab.datarole == 1);
                    result.add(infoMap);
                }
            }
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void saveModule(HashMap<String, Object> httpRequest) throws Exception {
        if(Utils.isEmpty(httpRequest) || Utils.isEmpty(httpRequest.get("Tabinfo")) || Utils.isEmpty(httpRequest.get("moduleMenu"))) {
            throw new Exception("参数错误");
        }
        ModuleMenu moduleMenu = new ModuleMenu();
        moduleMenu.fromRequest(httpRequest.get("moduleMenu"));
        long deleted = DateTimeUtils.gettimeStamp();
        //region 保存Tab
        HashMap<String, Object> tabinfo = (HashMap)httpRequest.get("Tabinfo");
        Tab tab = new Tab();
        tab.hasoperator(false).requireleft(false).saveiscloseeditview(false).searchcolumnflow(false).blockcard(false);
        tab.fromRequest(tabinfo);
        tabServices.update(tab);
        String modulename = tab.modulename;
        CacheBaseEntitys.clear(modulename);
        FeignApi.clearModuleCacheInfo(modulename);
        //endregion

        if (tab.moduletype == 1) {
            if (moduleMenu.url.compareTo("/"+modulename+"/") != 0) {
                moduleMenu.url = "/"+modulename+"/";
                moduleMenuServices.update(moduleMenu);
            }
        } else {
            if(moduleMenu.url.isEmpty()){
                moduleMenu.url = "/"+modulename+"/listview";
                moduleMenuServices.update(moduleMenu);
            }
            if(tab.moduletype != 1 && moduleMenu.url.equals("/"+modulename+"/")){
                moduleMenu.url = "/"+modulename+"/listview";
                moduleMenuServices.update(moduleMenu);
            }
        }

        boolean hasFields = !Utils.isEmpty(httpRequest.get("Fields")) && !((List<Object>) httpRequest.get("Fields")).isEmpty();
        boolean hasBlock = !Utils.isEmpty(httpRequest.get("Blocks")) && !((List<Object>) httpRequest.get("Blocks")).isEmpty();
        boolean hasLayout = !Utils.isEmpty(httpRequest.get("Layouts")) && !((List<Object>) httpRequest.get("Layouts")).isEmpty();

        //region 保存区块
        List<Block> dbblocks = new ArrayList<>();
        List<Object> update = new ArrayList<>();

        if(!Utils.isEmpty(blockServices.list(modulename))) {
            dbblocks.addAll(blockServices.list(modulename));
        }
        if(hasFields && !Utils.isEmpty(httpRequest.get("Blocks"))){
            List<Object> blocks = (List<Object>) httpRequest.get("Blocks");
            if(!Utils.isEmpty(blocks)){
                int index = 0;
                for(Object block : blocks){
                    if(Utils.isEmpty(((HashMap<String, Object>)block).get("id"))){
                        Block bk = new Block();
                        bk.fromRequest(block);
                        bk.modulename(modulename);
                        bk.sequence(index);
                        blockServices.update(bk);
                    } else {
                        for(Block bk : dbblocks){
                            if(bk.id.equals(((HashMap<String, Object>)block).get("id"))){
                                bk.fromRequest(block);
                                bk.sequence(index);
                                blockServices.update(bk);
                                update.add(bk);
                                break;
                            }
                        }
                    }
                    index++;
                }
            }
        }
        if(!Utils.isEmpty(dbblocks)) {
            dbblocks.removeAll(update);
            if (!dbblocks.isEmpty()) {
                for (Block block : dbblocks) {
                    blockServices.delete(block.id);
                }
            }
        }
        //endregion

        //region 保存布局信息
        update.clear();
        List<Layout> dblayout = new ArrayList<>();
        if(!Utils.isEmpty(layoutServices.list(modulename))) {
            dblayout.addAll(layoutServices.list(modulename));
        }

        if(hasFields && !Utils.isEmpty(httpRequest.get("Layouts"))){
            List<Object> layouts = (List<Object>) httpRequest.get("Layouts");
            if(!Utils.isEmpty(layouts)){
                int index = 0;
                for(Object layout: layouts){
                    if(Utils.isEmpty(((HashMap<String,Object>)layout).get("id"))){
                        Layout lo = new Layout(layout);
                        lo.modulename(modulename);
                        lo.sequence(index);
                        layoutServices.update(lo);
                    } else {
                        for(Layout lo: dblayout){
                            if(lo.id.equals(((HashMap<String,Object>)layout).get("id"))){
                                lo.fromRequest(layout);
                                lo.sequence(index);
                                layoutServices.update(lo);
                                update.add(lo);
                                break;
                            }
                        }
                    }
                    index++;
                }
            }
        }
        if(!Utils.isEmpty(dblayout)) {
            dblayout.removeAll(update);
            if (!dblayout.isEmpty()) {
                for (Layout layout : dblayout) {
                    layout.deleted = deleted;
                    layoutServices.update(layout);
                }
            }
        }
        //endregion

        //region 保存字段
        update.clear();
        List<TabField> dbfields = new ArrayList<>();
        if(!Utils.isEmpty(fieldServices.list(modulename))) {
            dbfields.addAll(fieldServices.list(modulename));
        }

        if(!Utils.isEmpty(httpRequest.get("Fields"))){
            List<Object> fields = (List<Object>) httpRequest.get("Fields");
            if(!Utils.isEmpty(fields)){
                int sequence = 1;
                for(Object field : fields){
                    if(Utils.isEmpty(((HashMap<String,Object>)field).get("id"))){
                        TabField fd = new TabField();
                        fd.fromRequest(field);
                        fd.modulename(modulename).sequence(sequence);
                        fieldServices.update(fd);
                    } else {
                        for(TabField fd : dbfields){
                            if(fd.id.equals(((HashMap<String,Object>)field).get("id"))){
                                fd.fromRequest(field);
                                fd.sequence(sequence);
                                if(!hasBlock && !hasLayout){
                                    fd.displaytype(2);
                                }
                                fieldServices.update(fd);
                                update.add(fd);
                                break;
                            }
                        }
                    }
                    sequence++;
                }
            }
        }
        if(!Utils.isEmpty(dbfields)) {
            dbfields.removeAll(update);
            if (!dbfields.isEmpty()) {
                for (TabField field : dbfields) {
                    field.deleted = deleted;
                    fieldServices.update(field);
                }
            }
        }

        //endregion

        //region 保存视图
        update.clear();
        List<CustomView> dbviews = new ArrayList<>();
        if(!Utils.isEmpty(customViewServices.list(modulename,""))) {
            dbviews.addAll(customViewServices.list(modulename,""));
        }
        if(!Utils.isEmpty(httpRequest.get("CustomViews"))){
            List<Object> views = (List<Object>) httpRequest.get("CustomViews");
            for(Object view : views){
                if(Utils.isEmpty(((HashMap<String,Object>)view).get("id"))){
                    CustomView vi = new CustomView(view);
                    if(!hasFields && !(vi.columnlist.contains("author") || vi.columnlist.contains("updated") || vi.columnlist.contains("published"))) {
                        continue;
                    }
                    vi.modulename(modulename);
                    customViewServices.update(vi);
                } else {
                    for(CustomView vi : dbviews){
                        if(vi.id.equals(((HashMap<String,Object>)view).get("id"))){
                            vi.fromRequest(view);
                            if(!hasFields && !(vi.columnlist.contains("author") || vi.columnlist.contains("updated") || vi.columnlist.contains("published"))) {
                                continue;
                            }
                            customViewServices.update(vi);
                            update.add(vi);
                            break;
                        }
                    }
                }
            }
        }
        if(!Utils.isEmpty(dbviews)) {
            dbviews.removeAll(update);
            if (!dbviews.isEmpty()) {
                for (CustomView view : dbviews) {
                    view.deleted = deleted;
                    customViewServices.update(view);
                }
            }
        }
        //endregion

        //region 保存筛选条件
        update.clear();
        List<SearchColumn> dbsearchs = new ArrayList<>();
        if(!Utils.isEmpty(searchColumnServices.list(modulename))) {
            dbsearchs.addAll(searchColumnServices.list(modulename));
        }
        if(hasFields && !Utils.isEmpty(httpRequest.get("SearchColumns"))){
            List<Object> searchs = (List<Object>) httpRequest.get("SearchColumns");
            for(Object search : searchs){
                if(Utils.isEmpty(((HashMap<String,Object>)search).get("id"))){
                    SearchColumn sh = new SearchColumn(search);
                    sh.modulename(modulename);
                    if(sh.colspan < 1) {
                        sh.colspan(1);
                    }
                    searchColumnServices.update(sh);
                } else {
                    for(SearchColumn sh : dbsearchs){
                        if(sh.id.equals(((HashMap<String,Object>)search).get("id"))){
                            sh.fromRequest(search);
                            searchColumnServices.update(sh);
                            update.add(sh);
                            break;
                        }
                    }
                }
            }
        }
        if(!Utils.isEmpty(dbsearchs)) {
            dbsearchs.removeAll(update);
            if (!dbsearchs.isEmpty()) {
                for (SearchColumn search : dbsearchs) {
                    search.deleted = deleted;
                    searchColumnServices.update(search);
                }
            }
        }
        //endregion

        //region 保存按钮信息
        update.clear();
        List<Action> dbactions = new ArrayList<>();
        if(!Utils.isEmpty(actionServices.list(modulename, tab.moduletype))) {
            dbactions.addAll(actionServices.list(modulename, tab.moduletype));
        }
        if(Utils.isEmpty(httpRequest.get("actions"))) {
            httpRequest.put("actions",new ArrayList<>());
        }
        if (tab.isenabledisable) {
            boolean ise = false, isd = false;
            List<Object> actions = (List<Object>) httpRequest.get("actions");
            for(Object action : actions) {
                String ak = ((HashMap<String,Object>)action).getOrDefault("actionkey","").toString();
                if("Active".equals(ak)) {
                    ise = true;
                }
                if("Inactive".equals(ak)) {
                    isd = true;
                }
                if(ise && isd) {
                    break;
                }
            }
            if(!ise){
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("actionkey", "Active");
                infoMap.put("actionlabel", "启用");
                infoMap.put("group", "ids");
                infoMap.put("icon", "lcdp-icon-qiyong");
                infoMap.put("toggle", "ajax");
                infoMap.put("funpara", "Active");
                infoMap.put("securitycheck", false);
                infoMap.put("confirm", "确定要启用吗？");
                actions.add(infoMap);
            }
            if(!isd){
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("actionkey", "Inactive");
                infoMap.put("actionlabel", "停用");
                infoMap.put("group", "ids");
                infoMap.put("icon", "lcdp-icon-tingyong");
                infoMap.put("actionclass", "layui-btn-grey");
                infoMap.put("toggle", "ajax");
                infoMap.put("funpara", "Inactive");
                infoMap.put("securitycheck", false);
                infoMap.put("confirm", "确定要停用吗？");
                actions.add(infoMap);
            }
        }
        if (tab.moduletype==1) {
            List<Object> list = XN_Query.contentQuery().tag("pagedesigns")
                    .filter("type","eic","pagedesigns")
                    .filter("my.modulename","=",tab.modulename)
                    .end(1).execute();
            if (!list.isEmpty()) {
                    Content settingInfo = ((Content)list.get(0));
                    if (!"0".equals(settingInfo.get("deleted").toString())) {
                        settingInfo.my.put("deleted","0");
                        settingInfo.my.put("parent", getParentTab(moduleMenu.program,moduleMenu.parent).label);
                        settingInfo.my.put("program",getProgram(moduleMenu.program).label);
                        settingInfo.save("pagedesigns");
                    }
            }else {
                Content newcontent = XN_Content.create("pagedesigns","",ProfileUtils.getCurrentProfileId(),0);
                newcontent.add("deleted","0");
                newcontent.add("parent", getParentTab(moduleMenu.program,moduleMenu.parent).label);
                newcontent.add("module", tab.modulelabel);
                newcontent.add("program", getProgram(moduleMenu.program).label);
                newcontent.add("modulename", tab.modulename);
                newcontent.add("generate","0");
                newcontent.add("status","0");
                newcontent.save("pagedesigns");
            }
        }else {
            List<Object> list = XN_Query.contentQuery().tag("pagedesigns")
                    .filter("type","eic","pagedesigns")
                    .notDelete()
                    .filter("my.modulename","=",tab.modulename)
                    .end(1).execute();
            if (!list.isEmpty()) {
                Content settingInfo = ((Content)list.get(0));
                settingInfo.my.put("deleted","1");
                settingInfo.save("pagedesigns");
            }
        }
        if (tab.isapproval) {
            boolean isa = false, isma = false;
            List<Object> actions = (List<Object>) httpRequest.get("actions");
            for(Object action : actions) {
                String ak = ((HashMap<String,Object>)action).getOrDefault("actionkey","").toString();
                if("SubmitOnline".equals(ak)) {
                    isa = true;
                }
                if("BatchSubmitOnline".equals(ak)) {
                    isma = true;
                }
                if(isa && isma) {
                    break;
                }
            }
            if(!isa){
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("actionkey", "SubmitOnline");
                infoMap.put("actionlabel", "提交");
                infoMap.put("actiontype", "editview");
                infoMap.put("toggle", "ajax");
                infoMap.put("funpara", "SubmitOnline");
                infoMap.put("securitycheck", false);
                infoMap.put("submit", true);
                infoMap.put("confirm", "提交后，您将没有权限再进行修改，是否确定提交?");
                actions.add(infoMap);
            }
            if(!isma){
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("actionkey", "BatchSubmitOnline");
                infoMap.put("actionlabel", "批量提交");
                infoMap.put("group", "ids");
                infoMap.put("toggle", "ajax");
                infoMap.put("funpara", "BatchSubmitOnline");
                infoMap.put("securitycheck", false);
                infoMap.put("confirm", "提交后，您将没有权限再进行修改，是否确定提交?");
                actions.add(infoMap);
            }
        }
        if (tab.popupeditview) {
            boolean hasPopupEditView = false;
            List<Object> actions = (List<Object>) httpRequest.get("actions");
            for(Object action : actions) {
                String ak = ((HashMap<String,Object>)action).getOrDefault("actionkey","").toString();
                if("PopupEditView".equals(ak)) {
                    hasPopupEditView = true;
                }
            }
            if(!hasPopupEditView){
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("actionkey", "PopupEditView");
                infoMap.put("actionlabel", "编辑");
                infoMap.put("group", "ids");
                infoMap.put("toggle", "dialog");
                infoMap.put("funpara", "PopupEditview");
                infoMap.put("securitycheck", false);
                infoMap.put("multiselect", false);
                actions.add(infoMap);
            }
        }
        if (tab.batcheditview) {
            boolean hasBatchEditView = false;
            List<Object> actions = (List<Object>) httpRequest.get("actions");
            for(Object action : actions) {
                String ak = ((HashMap<String,Object>)action).getOrDefault("actionkey","").toString();
                if("BatchEditView".equals(ak)) {
                    hasBatchEditView = true;
                }
            }
            if(!hasBatchEditView){
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("actionkey", "BatchEditView");
                infoMap.put("actionlabel", "批量编辑");
                infoMap.put("group", "ids");
                infoMap.put("toggle", "dialog");
                infoMap.put("funpara", "BatchEditView");
                infoMap.put("securitycheck", false);
                infoMap.put("multiselect", true);
                actions.add(infoMap);
            }
        }
        if(!Utils.isEmpty(httpRequest.get("actions"))){
            List<Object> actions = (List<Object>) httpRequest.get("actions");
            for(Object action : actions){
                if(Utils.isEmpty(((HashMap<String, Object>)action).get("id"))){
                    Action ac = new Action();
                    ac.fromRequest(action);
                    ac.modulename(modulename);
                    try {
                        if (Boolean.TRUE.equals(ac.securitycheck)) {
                            ac.funclass = getModuleServicePackageName(moduleMenu.modulename);
                        }
                    }catch (Exception e){
                        ac.funclass = "";
                    }
                    actionServices.update(ac);
                } else {
                    for(Action ac : dbactions){
                        if(ac.id.equals(((HashMap<String, Object>)action).get("id"))){
                            ac.fromRequest(action);
                            if(!tab.isenabledisable && ("Active".equals(ac.actionkey) || "Inactive".equals(ac.actionkey))) {
                                continue;
                            }
                            if(!tab.isapproval && ("SubmitOnline".equals(ac.actionkey) || "BatchSubmitOnline".equals(ac.actionkey))) {
                                continue;
                            }
                            if(!tab.popupeditview && "PopupEditView".equals(ac.actionkey) ) {
                                continue;
                            }
                            if(!tab.batcheditview && "BatchEditView".equals(ac.actionkey) ) {
                                continue;
                            }
                            try {
                                if (ac.securitycheck) {
                                    ac.funclass = getModuleServicePackageName(moduleMenu.modulename);
                                }
                            }catch (Exception e){
                                ac.funclass = "";
                            }
                            actionServices.update(ac);
                            update.add(ac);
                            break;
                        }
                    }
                }
            }
        }
        if(!Utils.isEmpty(dbactions)) {
            dbactions.removeAll(update);
            if (!dbactions.isEmpty()) {
                for (Action action : dbactions) {
                    action.deleted = deleted;
                    actionServices.update(action);
                }
            }
        }
        //endregion

        //region 保存关联信息
        update.clear();
        List<OutsideLink> dboutsides = new ArrayList<>();
        if(!Utils.isEmpty(outsideLinkServices.list(modulename))) {
            dboutsides.addAll(outsideLinkServices.list(modulename));
        }
        if(hasFields && !Utils.isEmpty(httpRequest.get("OutsideLinks"))){
            List<Object> outsidelinks = (List) httpRequest.get("OutsideLinks");
            for(Object outsidelink : outsidelinks){
                if(Utils.isEmpty(((HashMap<?,?>)outsidelink).get("id"))){
                    OutsideLink ol = new OutsideLink();
                    ol.fromRequest(outsidelink);
                    ol.modulename(modulename);
                    if("selfservice".equals(ol.serviceclass)){
                        ol.serviceclass = getWebServicePackageName(moduleMenu.program,modulename);
                    }
                    outsideLinkServices.update(ol);
                } else {
                    for(OutsideLink ol : dboutsides){
                        if(ol.id.equals(((HashMap<?,?>)outsidelink).get("id"))){
                            ol.fromRequest(outsidelink);
                            if("selfservice".equals(ol.serviceclass)){
                                ol.serviceclass = getWebServicePackageName(moduleMenu.program,modulename);
                            }
                            outsideLinkServices.update(ol);
                            update.add(ol);
                            break;
                        }
                    }
                }
            }
        }
        if(!Utils.isEmpty(dboutsides)) {
            dboutsides.removeAll(update);
            if (!dboutsides.isEmpty()) {
                for (OutsideLink ol : dboutsides) {
                    ol.deleted = deleted;
                    outsideLinkServices.update(ol);
                }
            }
        }
        //endregion

        //region 保存弹窗信息
        update.clear();
        List<PopupDialog> dbpopups = new ArrayList<>();
        if(!Utils.isEmpty(popupDialogServices.get(modulename))) {
            dbpopups.add(popupDialogServices.get(modulename));
        }
        if(Utils.isEmpty(httpRequest.get("Popupdialog")) && tab.moduletype == 2) {
            Map<String,Object> infoMap = new HashMap<>(1);
            infoMap.put("columns", Arrays.asList("name","sequence"));
            infoMap.put("search", Collections.singletonList("name"));
            httpRequest.put("Popupdialog",infoMap);
        }
        if(!Utils.isEmpty(httpRequest.get("Popupdialog")) && tab.moduletype != 2) {
            HashMap<String,Object> popups = (HashMap<String,Object>) httpRequest.get("Popupdialog");
            List<?> columns = (List<?>) popups.getOrDefault("columns",new ArrayList<>());
            if(columns.size() <= 0 || (columns.contains("name") && columns.contains("sequence"))){
                httpRequest.remove("Popupdialog");
            }
        }
        if(!Utils.isEmpty(httpRequest.get("Popupdialog"))){
            HashMap<String,Object> popups = (HashMap<String,Object>) httpRequest.get("Popupdialog");
            if(Utils.isEmpty(popups.get("id"))){
                PopupDialog po = new PopupDialog();
                po.modulename(modulename)
                        .columns((List<String>) popups.getOrDefault("columns",new ArrayList<>()))
                        .search((List<String>) popups.getOrDefault("search",new ArrayList<>()));
                popupDialogServices.update(po);
            } else {
                for(PopupDialog po : dbpopups){
                    if(po.id.equals(popups.get("id"))){
                        po.columns((List<String>) popups.getOrDefault("columns",new ArrayList<>()))
                                .search((List<String>) popups.getOrDefault("search",new ArrayList<>()));
                        popupDialogServices.update(po);
                        update.add(po);
                        break;
                    }
                }
            }
        }
        if(!Utils.isEmpty(dbpopups)) {
            dbpopups.removeAll(update);
            if (!dbpopups.isEmpty()) {
                for (PopupDialog ol : dbpopups) {
                    ol.deleted = deleted;
                    popupDialogServices.update(ol);
                }
            }
        }
        //endregion

        //region 保存编号信息
        update.clear();
        List<ModentityNum> dbnums = new ArrayList<>();
        if(!Utils.isEmpty(modentityNumServices.get(modulename))) {
            dbnums.add(modentityNumServices.get(modulename));
        }
        if(!Utils.isEmpty(httpRequest.get("Modentitynum"))){
            HashMap<String,Object> nums = (HashMap<String,Object>) httpRequest.get("Modentitynum");
            if(Utils.isEmpty(nums.get("id"))){
                ModentityNum num = new ModentityNum();
                num.fromRequest(nums);
                num.modulename(modulename);
                modentityNumServices.update(num);
            } else {
                for(ModentityNum num : dbnums){
                    if(num.id.equals(nums.get("id"))){
                        num.fromRequest(nums);
                        modentityNumServices.update(num);
                        update.add(num);
                        break;
                    }
                }
            }
        }
        if(!Utils.isEmpty(dbnums)) {
            dbnums.removeAll(update);
            if (!dbnums.isEmpty()) {
                for (ModentityNum num : dbnums) {
                    num.deleted = deleted;
                    modentityNumServices.update(num);
                }
            }
        }
        //endregion

        //region 保存内链字段信息
        update.clear();
        List<EntityLink> dbentitylinks = new ArrayList<>();
        if(!Utils.isEmpty(entityLinkServices.get(modulename))) {
            dbentitylinks.add(entityLinkServices.get(modulename));
        }
        if(hasFields && !Utils.isEmpty(httpRequest.get("Entitylink"))){
            HashMap<String,Object> entitylinks = (HashMap<String,Object>) httpRequest.get("Entitylink");
            if(Utils.isEmpty(entitylinks.get("id"))){
                EntityLink entitylink = new EntityLink();
                entitylink.fromRequest(entitylinks);
                entitylink.modulename(modulename);
                entityLinkServices.update(entitylink);
            } else {
                for(EntityLink entitylink : dbentitylinks){
                    if(entitylink.id.equals(entitylinks.get("id"))){
                        entitylink.fromRequest(entitylinks);
                        entityLinkServices.update(entitylink);
                        update.add(entitylink);
                        break;
                    }
                }
            }
        }
        if(!Utils.isEmpty(dbentitylinks)) {
            dbentitylinks.removeAll(update);
            if (!dbentitylinks.isEmpty()) {
                for (EntityLink entitylink : dbentitylinks) {
                    entitylink.deleted = deleted;
                    entityLinkServices.update(entitylink);
                }
            }
        }
        //endregion

        //region 保存字典信息
        update.clear();
        List<PickList> dbPickLists = pickListServices.list();
        if(!Utils.isEmpty(httpRequest.get("picklists"))){
            List<?> picklists = (List<?>) httpRequest.get("picklists");
            for(Object obj: picklists){
                if (obj instanceof HashMap){
                    PickList pickList = new PickList(obj);
                    pickListServices.update(pickList);
                    update.add(pickList);
                }
            }
        }
        if(!Utils.isEmpty(dbPickLists)){
            update.forEach(upd -> dbPickLists.removeIf(dbp -> dbp.name.equals(((PickList)upd).name)));
            if(!dbPickLists.isEmpty()){
                for(PickList pickList: dbPickLists){
                    pickList.entitys = null;
                    pickListServices.update(pickList);
                }
            }
        }
        //endregion
    }

    //获取模块的Service包名
    private String getModuleServicePackageName(String moduleName) throws Exception {
        List<Object> result = (List<Object>)getAllModule();
        for(Object item: result){
            if(((HashMap<String,Object>)item).getOrDefault("modulename","").equals(moduleName)){
                return ((HashMap<String,Object>)item).getOrDefault("serviceclass","").toString();
            }
        }
        return "";
    }
    private String getWebServicePackageName(String moduleGroup,String moduleName) throws Exception {
        String packagename = ContextUtils.findPackageNameByBootClass();
        return packagename + ".modules." + moduleGroup + "." + moduleName + ".services." +"I"+ moduleName + "Service";
    }

    @Override
    public void createModule(String modulename) throws Exception {
        createModule(modulename,false,false,true);
    }

    @Override
    public void createModule(String modulename,Boolean isUpdateMenu,Boolean isUpdatePicklist,Boolean isAutoCompile) throws Exception {
        if(Utils.isEmpty(modulename)) {
            throw new Exception("模块名不能为空");
        }
        try {
            CacheBaseEntitys.clear(modulename);
            FeignApi.clearModuleCacheInfo(modulename);
            Module info = moduleServices.get(modulename);
            FeignApi.generatorApiCode(modulename);
        }catch (Exception e) {
            log.error("autoCompiler : {}",e.getMessage());
            throw e;
        }
    }
    public void autoCompiler()  {
        try {
            CompilerUtils.autoCompiler();
        } catch (Exception e) {
            log.error("autoCompiler : {}",e.getMessage());
        }
    }
    @Override
    public void createAllModule() throws Exception {
        List<Object> modules = moduleServices.getMenuGroup();
        for(Object item: modules){
            if(item instanceof ParentTab){
                List<ModuleMenu> moduleMenus = moduleMenuServices.list(((ParentTab) item).program,((ParentTab) item).name);
                if(!Utils.isEmpty(moduleMenus)) {
                    for (ModuleMenu menu : moduleMenus) {
                        if (!menu.builtin && !Utils.isEmpty(menu.modulename)) {
                            createModule(menu.modulename,false,false,false);
                        }
                    }
                }
            }
        }
        autoCompiler();
    }
}
