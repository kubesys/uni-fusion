package com.qnkj.core.base.modules.supplier.SupplierRoles.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.google.common.collect.ImmutableSet;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.ParentTab;
import com.qnkj.common.entitys.Program;
import com.qnkj.common.services.IModuleMenuServices;
import com.qnkj.common.services.IParentTabServices;
import com.qnkj.common.services.IProgramServices;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.CacheBaseEntitys;
import com.qnkj.core.base.modules.settings.roles.entity.Role2tabs;
import com.qnkj.core.base.modules.settings.roles.service.IRolesService;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.base.modules.supplier.SupplierRoles.entitys.SupplierRole2tabs;
import com.qnkj.core.base.modules.supplier.SupplierRoles.entitys.SupplierRoles;
import com.qnkj.core.base.modules.supplier.SupplierRoles.services.ISupplierrolesService;
import com.qnkj.core.base.modules.supplier.SupplierUsers.services.ISupplierusersService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.utils.RolesUtils;
import com.qnkj.core.webconfigs.WebViews;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

/**
 * create by Auto Generator
 * create date 2021-04-02
 */

@Slf4j
@Service
public class SupplierrolesServiceImpl implements ISupplierrolesService {

    private final IProgramServices programServices;
    private final IParentTabServices parentTabServices;
    private final IModuleMenuServices moduleMenuServices;
    private final ISupplierusersService supplierusersService;
    private final IRolesService rolesService;
    static Boolean cacheIsSupperDelete = null;

    public SupplierrolesServiceImpl(IProgramServices programServices,
                                    IParentTabServices parentTabServices,
                                    IModuleMenuServices moduleMenuServices,
                                    ISupplierusersService supplierusersService,
                                    IRolesService rolesService) {
        this.programServices = programServices;
        this.parentTabServices = parentTabServices;
        this.moduleMenuServices = moduleMenuServices;
        this.supplierusersService = supplierusersService;
        this.rolesService = rolesService;
    }

    @Override
    public Object editDetails(Map<String, Object> httpRequest, Model model, BaseEntityUtils viewEntitys) {
        if (!Utils.isEmpty(httpRequest.get("record"))) {
            try {
                String record = httpRequest.get("record").toString();
                Content conn = XN_Content.load(record, viewEntitys.getTabName());
                SupplierRoles roles = new SupplierRoles(conn);
                List<Object> moduleData = new ArrayList<>();
                HashMap<String, Object> role2tabs = this.getRolesData(record);
                List<Object> bossroles = getBossRole();
                for(Object item: bossroles){
                    if(Utils.isEmpty(item)) {
                        continue;
                    }
                    for(Object group: ((HashMap<?,?>)item).keySet()) {
                        List<Object> children = this.getModulesData(group.toString(), bossroles, roles.globalview, roles.globaledit, role2tabs);
                        if (!children.isEmpty()) {
                            Program program = programServices.get(group.toString());
                            Map<String, Object> infoMap = new HashMap<>(1);
                            infoMap.put("menuId", "0");
                            infoMap.put("menuName", program.label);
                            infoMap.put("order", program.order);
                            infoMap.put("children", children);
                            moduleData.add(infoMap);
                        }
                    }
                }
                role2tabs.clear();
                bossroles.clear();
                model.addAttribute("RECORD", record);
                model.addAttribute("ROLESNAME", roles.rolename);
                model.addAttribute("TABNAME", roles.rolename + "详情");
                model.addAttribute("GLOBALVIEW", roles.globalview);
                model.addAttribute("GLOBALEDIT", roles.globaledit);
                model.addAttribute("SUPERDELETED", roles.superdelete);
                model.addAttribute("MODULELISTS", moduleData);
                if(!(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()) && !RolesUtils.isEdit("roles")){
                    model.addAttribute("READONLY", true);
                }
            } catch (Exception e) {
                return WebViews.view("error/403");
            }
            return WebViews.view("modules/settings/roles/DetailsView");
        } else {
            return WebViews.view("error/403");
        }
    }

    @Override
    public void saveRoles(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        if (!Utils.isEmpty(httpRequest.get("rolename"))) {
            List<Object> list = XN_Query.create(viewEntitys.getDataType()).tag(viewEntitys.getTabName())
                    .filter("type", "eic", viewEntitys.getTabName())
                    .filter("my.rolename", "=", httpRequest.get("rolename"))
                    .filter("my.supplierid","=", SupplierUtils.getSupplierid())
                    .notDelete().end(-1).execute();
            if (!list.isEmpty()) {
                throw new Exception("权限已存在，请更换后再试。。。");
            }
            Content authorization = XN_Content.create(viewEntitys.getTabName(), "", ProfileUtils.getCurrentProfileId());
            authorization.add("allowdeleted", "1");
            authorization.add("deleted", "0");
            authorization.add("rolename", httpRequest.get("rolename"));
            authorization.add("description", httpRequest.get("description"));
            authorization.add("globalview", "1");
            authorization.add("globaledit", "0");
            authorization.add("supplierid",SupplierUtils.getSupplierid());
            authorization.save(viewEntitys.getTabName());
        } else {
            throw new Exception("保存参数错误！");
        }
    }

    private HashMap<String, Object> getRolesData(String record) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<Object> list = XN_Query.contentQuery().tag("role2tabs")
                    .filter("type", "eic", "role2tabs")
                    .filter("my.record", "=", record)
                    .notDelete().end(-1).execute();
            list.forEach(item -> {
                SupplierRole2tabs role2tabs = new SupplierRole2tabs(item);
                if (!Utils.isEmpty(result.get(role2tabs.classify))) {
                    if (!Utils.isEmpty(((HashMap<?,?>) result.get(role2tabs.classify)).get(role2tabs.parentid))) {
                        if (!Utils.isEmpty(((HashMap<?,?>) ((HashMap<?,?>) result.get(role2tabs.classify)).get(role2tabs.parentid)).get(role2tabs.moduleid))) {
                            if (!Utils.isEmpty(((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) result.get(role2tabs.classify)).get(role2tabs.parentid)).get(role2tabs.moduleid)).get(role2tabs.module))) {
                                if (Utils.isEmpty(((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) result.get(role2tabs.classify)).get(role2tabs.parentid)).get(role2tabs.moduleid)).get(role2tabs.module)).get("isview"))) {
                                    ((HashMap) ((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) result.get(role2tabs.classify)).get(role2tabs.parentid)).get(role2tabs.moduleid)).get(role2tabs.module)).put("isview", role2tabs.isview);
                                }
                                if (Utils.isEmpty(((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) result.get(role2tabs.classify)).get(role2tabs.parentid)).get(role2tabs.moduleid)).get(role2tabs.module)).get("isedit"))) {
                                    ((HashMap) ((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) result.get(role2tabs.classify)).get(role2tabs.parentid)).get(role2tabs.moduleid)).get(role2tabs.module)).put("isedit", role2tabs.isedit);
                                }
                                if (Utils.isEmpty(((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) result.get(role2tabs.classify)).get(role2tabs.parentid)).get(role2tabs.moduleid)).get(role2tabs.module)).get("isdelete"))) {
                                    ((HashMap) ((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) result.get(role2tabs.classify)).get(role2tabs.parentid)).get(role2tabs.moduleid)).get(role2tabs.module)).put("isdelete", role2tabs.isdelete);
                                }
                            } else {
                                Map<String, Object> infoMap = new HashMap<>(1);
                                infoMap.put("isview", role2tabs.isview);
                                infoMap.put("isedit", role2tabs.isedit);
                                infoMap.put("isdelete", role2tabs.isdelete);
                                ((HashMap<String,Object>) ((HashMap<?,?>) ((HashMap<?,?>) result.get(role2tabs.classify)).get(role2tabs.parentid)).get(role2tabs.moduleid)).put(role2tabs.module, infoMap);
                            }
                        } else {
                            Map<String, Object> infoMap = new HashMap<>(1);
                            Map<String, Object> moduleMap = new HashMap<>(1);
                            moduleMap.put("isview", role2tabs.isview);
                            moduleMap.put("isedit", role2tabs.isedit);
                            moduleMap.put("isdelete", role2tabs.isdelete);
                            infoMap.put(role2tabs.module, moduleMap);
                            ((HashMap) ((HashMap<?,?>) result.get(role2tabs.classify)).get(role2tabs.parentid)).put(role2tabs.moduleid, infoMap);
                        }
                    } else {
                        Map<String, Object> infoMap = new HashMap<>(1);
                        Map<String, Object> moduleidMap = new HashMap<>(1);
                        Map<String, Object> moduleMap = new HashMap<>(1);
                        moduleMap.put("isview", role2tabs.isview);
                        moduleMap.put("isedit", role2tabs.isedit);
                        moduleMap.put("isdelete", role2tabs.isdelete);
                        moduleidMap.put(role2tabs.module, moduleMap);
                        infoMap.put(role2tabs.moduleid, moduleidMap);
                        ((HashMap) result.get(role2tabs.classify)).put(role2tabs.parentid, infoMap);
                    }
                } else {
                    Map<String, Object> infoMap = new HashMap<>(1);
                    Map<String, Object> parentidMap = new HashMap<>(1);
                    Map<String, Object> moduleidMap = new HashMap<>(1);
                    Map<String, Object> moduleMap = new HashMap<>(1);
                    moduleMap.put("isview", role2tabs.isview);
                    moduleMap.put("isedit", role2tabs.isedit);
                    moduleMap.put("isdelete", role2tabs.isdelete);
                    moduleidMap.put(role2tabs.module, moduleMap);
                    parentidMap.put(role2tabs.moduleid, moduleidMap);
                    infoMap.put(role2tabs.parentid, parentidMap);
                    result.put(role2tabs.classify, infoMap);
                }
            });
            list.clear();
        } catch (Exception ignored) {
        }
        return result;
    }

    private Boolean getRole2tabs(HashMap<String, Object> data, String classify, String parentid, String moduleid, String module, String field) {
        boolean result = false;
        if (!Utils.isEmpty(data.get(classify))) {
            if (!Utils.isEmpty(((HashMap<?,?>) data.get(classify)).get(parentid))) {
                if (!Utils.isEmpty(((HashMap<?,?>) ((HashMap<?,?>) data.get(classify)).get(parentid)).get(moduleid))) {
                    if (!Utils.isEmpty(((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) data.get(classify)).get(parentid)).get(moduleid)).get(module))) {
                        if (!Utils.isEmpty(((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) data.get(classify)).get(parentid)).get(moduleid)).get(module)).get(field))) {
                            result = (Boolean) ((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) ((HashMap<?,?>) data.get(classify)).get(parentid)).get(moduleid)).get(module)).get(field);
                        }
                    }
                }
            }
        }
        return result;
    }

    private List<Object> getModulesData(String classify,List<Object> root, boolean globalview, boolean globaledit, HashMap<String, Object> role2tabs) {
        List<Object> data = new ArrayList<>();
        try {
            List<ParentTab> parentTabs = parentTabServices.list(classify);
            if(!Utils.isEmpty(parentTabs)) {
                for (ParentTab parentTab : parentTabs) {
                    Map<?,?> target = Utils.getListByMap(root,classify);
                    if(Utils.isEmpty(target)) {
                        continue;
                    }
                    List<Object> children = this.getModulesData(parentTab.name,root, globalview, globaledit, role2tabs);
                    List<Object> subParent = this.getModules(classify, parentTab.name, (HashMap<?,?>) target.get(classify), globalview, globaledit, role2tabs);
                    if(!Utils.isEmpty(subParent)){
                        children.addAll(subParent);
                    }
                    if (!children.isEmpty()) {
                        Map<String, Object> infoMap = new HashMap<>(1);
                        infoMap.put("menuId", parentTab.id);
                        infoMap.put("menuName", parentTab.label);
                        infoMap.put("order", parentTab.order);
                        infoMap.put("classify", classify);
                        infoMap.put("parentId", parentTab.programid);
                        infoMap.put("children", children);
                        data.add(infoMap);
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return data;
    }

    private List<Object> getModules(String classify, String parent,HashMap<?,?> modules, boolean globalview, boolean globaledit, HashMap<String, Object> role2tabs) {
        List<Object> data = new ArrayList<>();
        try {
            List<ModuleMenu> moduleMenus = moduleMenuServices.list(classify, parent);
            if(!Utils.isEmpty(moduleMenus)) {
                for (ModuleMenu moduleMenu : moduleMenus) {
                    String modulename = moduleMenu.modulename;
                    if(modulename.startsWith("MultipleEntry:")) {
                        modulename = modulename.substring(14);
                    }
                    if(Utils.isEmpty(modules.get(modulename))) {
                        continue;
                    }
                    Map<String, Object> infoMap = new HashMap<>(1);
                    infoMap.put("menuId", moduleMenu.id);
                    infoMap.put("menuName", moduleMenu.label);
                    infoMap.put("order", moduleMenu.order);
                    infoMap.put("classify", classify);
                    infoMap.put("parentId", moduleMenu.parentid);
                    infoMap.put("moduleName", moduleMenu.modulename);
                    infoMap.put("isview", globalview || getRole2tabs(role2tabs, classify, moduleMenu.parentid, moduleMenu.id, moduleMenu.modulename, "isview"));
                    infoMap.put("isedit", globaledit || getRole2tabs(role2tabs, classify, moduleMenu.parentid, moduleMenu.id, moduleMenu.modulename, "isedit"));
                    infoMap.put("isdelete", globaledit || getRole2tabs(role2tabs, classify, moduleMenu.parentid, moduleMenu.id, moduleMenu.modulename, "isdelete"));
                    data.add(infoMap);
                }
            }
        } catch (Exception ignored) {
        }
        return data;
    }

    private List<Object> getBossRole() {
        List<Object> result = new ArrayList<>();
        List<Object> bossroles = RolesUtils.getBossRole(supplierusersService.getBossRoleId());
        for(Object program: bossroles){
            if(Utils.isEmpty(program)) {
                continue;
            }
            for(Map.Entry<String,Object> entry: ((HashMap<String,Object>)program).entrySet()){
                HashMap<String, Object> rolemodules = (HashMap<String,Object>)entry.getValue();
                if (!Utils.isEmpty(rolemodules)) {
                    for (String modulename : rolemodules.keySet()) {
                        HashMap<String, Object> roleinfo = (HashMap<String, Object>) rolemodules.get(modulename);
                        if (!Utils.isEmpty(roleinfo) && Boolean.parseBoolean(roleinfo.getOrDefault("isview", false).toString())) {
                            Map<?,?> target = Utils.getListByMap(result,entry.getKey());
                            if(!target.isEmpty()){
                                ((HashMap<String,Object>)target.get(entry.getKey())).put(modulename, roleinfo);
                            } else {
                                Map<String,Object> infoMap = new HashMap<>(1);
                                Map<String,Object> moduleMap = new HashMap<>(1);
                                moduleMap.put(modulename,roleinfo);
                                infoMap.put(entry.getKey(),moduleMap);
                                result.add(infoMap);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public List<Object> getAllRoles(SupplierRoles role) {
        if(CacheBaseEntitys.hasRoles(role.id)) {
            return CacheBaseEntitys.getRoles(role.id);
        }
        List<Object> bossRole = getBossRole();
        List<Object> result = new ArrayList<>();
        try {
            List<Object> roletabs = XN_Query.contentQuery().tag("role2tabs")
                    .filter("type", "eic", "role2tabs")
                    .filter("my.record", "=", role.id)
                    .notDelete().end(-1).execute();
            if(roletabs.isEmpty()){
                if(role.globalview){
                    bossRole.forEach(item -> {
                        if (item instanceof Map && !Utils.isEmpty(item)) {
                            for (Map.Entry<String, Object> entry : ((Map<String, Object>) item).entrySet()) {
                                Map<String, Object> infoMap = new HashMap<>(1);
                                for(Map.Entry<String,Object> moduleEntry: ((Map<String, Object>)entry.getValue()).entrySet()) {
                                    Map<String, Object> modulenameMap = new HashMap<>(1);
                                    modulenameMap.put("isview", role.globalview);
                                    modulenameMap.put("isedit", role.globaledit);
                                    modulenameMap.put("isdelete", role.globaledit);
                                    if(infoMap.containsKey(entry.getKey())){
                                        ((Map<String,Object>)infoMap.get(entry.getKey())).put(moduleEntry.getKey(),modulenameMap);
                                    }else {
                                        Map<String, Object> classifyMap = new HashMap<>(1);
                                        classifyMap.put(moduleEntry.getKey(), modulenameMap);
                                        infoMap.put(entry.getKey(), classifyMap);
                                    }
                                }
                                result.add(infoMap);
                            }
                        }
                    });
                }
            }else {
                roletabs.forEach(item -> {
                    Role2tabs role2tabs = new Role2tabs(item);
                    String modulename = role2tabs.module;
                    if (modulename.startsWith("MultipleEntry:")) {
                        modulename = modulename.substring(14);
                    }
                    String finalModulename = modulename;
                    Map<?, ?> bosstarget = Utils.getListByMap(bossRole, role2tabs.classify);
                    if (!Utils.isEmpty(bosstarget) && !Utils.isEmpty(((HashMap<?, ?>) (bosstarget.get(role2tabs.classify))).get(finalModulename))) {
                        boolean isview = (boolean) ((HashMap<?, Object>) ((HashMap<?, ?>) (bosstarget.get(role2tabs.classify))).get(finalModulename)).get("isview");
                        boolean isedit = (boolean) ((HashMap<?, Object>) ((HashMap<?, ?>) (bosstarget.get(role2tabs.classify))).get(finalModulename)).get("isedit");
                        boolean isdelete = (boolean) ((HashMap<?, Object>) ((HashMap<?, ?>) (bosstarget.get(role2tabs.classify))).get(finalModulename)).get("isdelete");
                        if (isview) {
                            Map<?, ?> target = Utils.getListByMap(result, role2tabs.classify);
                            if (Utils.isEmpty(target)) {
                                Map<String, Object> infoMap = new HashMap<>(1);
                                Map<String, Object> classifyMap = new HashMap<>(1);
                                Map<String, Object> modulenameMap = new HashMap<>(1);
                                modulenameMap.put("isview", role2tabs.isview || role.globalview);
                                modulenameMap.put("isedit", isedit && (role2tabs.isedit || role.globaledit));
                                modulenameMap.put("isdelete", isdelete && (role2tabs.isdelete || role.globaledit));
                                classifyMap.put(modulename, modulenameMap);
                                infoMap.put(role2tabs.classify, classifyMap);
                                result.add(infoMap);
                            } else {
                                if (!Utils.isEmpty(((HashMap<?, ?>) (target.get(role2tabs.classify))).get(finalModulename))) {
                                    ((HashMap<String, Object>) ((HashMap<?, ?>) (target.get(role2tabs.classify))).get(finalModulename)).put("isview", role2tabs.isview || role.globalview);
                                    ((HashMap<String, Object>) ((HashMap<?, ?>) (target.get(role2tabs.classify))).get(finalModulename)).put("isedit", isedit && (role2tabs.isedit || role.globaledit));
                                    ((HashMap<String, Object>) ((HashMap<?, ?>) (target.get(role2tabs.classify))).get(finalModulename)).put("isdelete", isdelete && (role2tabs.isdelete || role.globaledit));
                                } else {
                                    Map<String, Object> infoMap = new HashMap<>(1);
                                    infoMap.put("isview", role2tabs.isview || role.globalview);
                                    infoMap.put("isedit", isedit && (role2tabs.isedit || role.globaledit));
                                    infoMap.put("isdelete", isdelete && (role2tabs.isdelete || role.globaledit));
                                    ((HashMap<String, Object>) (target.get(role2tabs.classify))).put(finalModulename, infoMap);
                                }
                            }
                        }
                    }
                });
                roletabs.clear();
            }
            CacheBaseEntitys.addRoles(role.id, result);
        } catch (Exception ignored) { }

        return result;
    }

    @Override
    public void saveDetails(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        if (!Utils.isEmpty(httpRequest.get("record"))) {
            String record = httpRequest.get("record").toString();
            CacheBaseEntitys.clearRoles(record);
            httpRequest.remove("record");
            try {
                Content detail = XN_Content.load(record, viewEntitys.getTabName());
                if (!Utils.isEmpty(httpRequest.get("supplierroles_superdelete"))) {
                    if ("1".equals(httpRequest.get("supplierroles_superdelete").toString())) {
                        detail.my.put("superdelete", "1");
                    } else {
                        detail.my.put("superdelete", "0");
                    }
                    detail.save(viewEntitys.getTabName());
                    cacheIsSupperDelete = null;
                    return;
                }

                boolean globalview = false, globaledit = false;
                if (!Utils.isEmpty(httpRequest.get("supplierroles_globalview")) && "1".equals(httpRequest.get("supplierroles_globalview").toString())) {
                    detail.my.put("globalview", "1");
                    httpRequest.remove("supplierroles_globalview");
                    globalview = true;
                } else {
                    detail.my.put("globalview", "0");
                }
                if (!Utils.isEmpty(httpRequest.get("supplierroles_globaledit")) && "1".equals(httpRequest.get("supplierroles_globaledit").toString())) {
                    detail.my.put("globaledit", "1");
                    httpRequest.remove("supplierroles_globaledit");
                    globaledit = true;
                } else {
                    detail.my.put("globaledit", "0");
                }
                detail.save(viewEntitys.getTabName());

                List<Object> list = XN_Query.contentQuery().notDelete().tag("role2tabs")
                        .filter("type", "eic", "role2tabs")
                        .filter("my.record", "=", record)
                        .end(-1).execute();
                List<Object> delete = new ArrayList<>();
                if (globalview && globaledit) {
                    for(Object item: list){
                        ((Content)item).my.put("isview","0");
                        ((Content)item).my.put("isedit","0");
                        ((Content)item).my.put("isdelete","0");
                        delete.add(item);
                    }
                } else {
                    List<Object> create = new ArrayList<>();
                    List<Object> update = new ArrayList<>();
                    list.forEach(item -> {
                        SupplierRole2tabs role2tabs = new SupplierRole2tabs(item);
                        String viewKey = role2tabs.classify + "_" + role2tabs.parentid + "_" + role2tabs.moduleid + "_" + role2tabs.module + "_isview";
                        String editKey = role2tabs.classify + "_" + role2tabs.parentid + "_" + role2tabs.moduleid + "_" + role2tabs.module + "_isedit";
                        String delKey = role2tabs.classify + "_" + role2tabs.parentid + "_" + role2tabs.moduleid + "_" + role2tabs.module + "_isdelete";
                        if (!Utils.isEmpty(httpRequest.get(viewKey)) || !Utils.isEmpty(httpRequest.get(editKey)) || !Utils.isEmpty(httpRequest.get(delKey))) {
                            ((Content) item).my.put("isview", !Utils.isEmpty(httpRequest.get(viewKey)) ? "1" : "0");
                            ((Content) item).my.put("isedit", !Utils.isEmpty(httpRequest.get(editKey)) ? "1" : "0");
                            ((Content) item).my.put("isdelete", !Utils.isEmpty(httpRequest.get(delKey)) ? "1" : "0");
                            update.add(item);
                            httpRequest.remove(viewKey);
                            httpRequest.remove(editKey);
                            httpRequest.remove(delKey);
                        } else {
                            ((Content) item).my.put("isview", "0");
                            ((Content) item).my.put("isedit", "0");
                            ((Content) item).my.put("isdelete", "0");
                            delete.add(item);
                        }
                    });
                    if (!httpRequest.isEmpty()) {
                        HashMap<String, Object> createData = new HashMap<>(1);
                        httpRequest.forEach((k, v) -> {
                            String[] data = k.split("_");
                            if (data.length == 5) {
                                String hashKey = data[0] + "_" + data[1] + "_" + data[2] + "_" + data[3];
                                if (!Utils.isEmpty(createData.get(hashKey))) {
                                    if (Utils.isEmpty(((HashMap<?,?>) createData.get(hashKey)).get(data[4]))) {
                                        ((HashMap<String,Object>) createData.get(hashKey)).put(data[4], v);
                                    }
                                } else {
                                    Map<String,Object> infoMap = new HashMap<>(1);
                                    infoMap.put(data[4], v);
                                    createData.put(hashKey, infoMap);
                                }
                            }
                        });
                        createData.forEach((k, v) -> {
                            String[] data = k.split("_");
                            Content newConn = XN_Content.create("role2tabs", "", ProfileUtils.getCurrentProfileId());
                            newConn.add("record", record);
                            newConn.add("deleted", "0");
                            newConn.add("classify", !Utils.isEmpty(data[0]) ? data[0] : "");
                            newConn.add("parentid", !Utils.isEmpty(data[1]) ? data[1] : "");
                            newConn.add("moduleid", !Utils.isEmpty(data[2]) ? data[2] : "");
                            newConn.add("module", !Utils.isEmpty(data[3]) ? data[3] : "");
                            newConn.add("isview", !Utils.isEmpty(((HashMap<?,?>) v).get("isview")) ? "1" : "0");
                            newConn.add("isedit", !Utils.isEmpty(((HashMap<?,?>) v).get("isedit")) ? "1" : "0");
                            newConn.add("isdelete", !Utils.isEmpty(((HashMap<?,?>) v).get("isdelete")) ? "1" : "0");
                            create.add(newConn);
                        });
                    }
                    if (!create.isEmpty()) {
                        XN_Content.batchsave(create, "role2tabs");
                        create.clear();
                    }
                    if (!update.isEmpty()) {
                        XN_Content.batchsave(update, "role2tabs");
                        update.clear();
                    }
                }
                if (!delete.isEmpty()) {
                    XN_Content.delete(delete,"role2tabs");
                    delete.clear();
                }
            } catch (Exception ignored) { }
        } else {
            throw new Exception("参数错误！请确认操作是否正确。。。");
        }
    }

    @Override
    public void deleteRoles(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        if (!Utils.isEmpty(httpRequest.get("ids"))) {
            Object ids = httpRequest.get("ids");
            if (ids instanceof String) {
                ids = new ArrayList<String>(ImmutableSet.of((String) ids));
            } else if (ids instanceof Integer) {
                ids = new ArrayList<String>(ImmutableSet.of((String) ids.toString()));
            } else if (ids instanceof List) {
                ids = new ArrayList<String>((List) ids);
            }

            if (ids instanceof List && !((List<?>) ids).isEmpty()) {
                List<Object> list = XN_Content.loadMany((List<String>) ids, viewEntitys.getTabName());
                List<String> allowdeleted = new ArrayList<>();
                for (Object item : list) {
                    if (Integer.parseInt(((Content) item).my.get("allowdeleted").toString()) != 1) {
                        allowdeleted.add(((Content) item).my.get("rolename").toString());
                    }
                }
                if (!allowdeleted.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    allowdeleted.forEach(item ->
                        sb.append("[<span style='color: blue;'>").append(item).append("</span>] 系统默认权限，不允许删除！<br>")
                    );
                    allowdeleted.clear();
                    throw new Exception(sb.toString());
                }
                List<Object> saves = new ArrayList<>();
                list.forEach(item -> {
                    CacheBaseEntitys.clearRoles(((Content) item).id);
                    ((Content) item).my.put("deleted", DateTimeUtils.gettimeStamp());
                    saves.add(item);
                });
                if (!saves.isEmpty()) {
                    XN_Content.batchsave(saves, viewEntitys.getTabName());
                    saves.clear();
                    int page = 0;
                    List<Object> role2tabs;
                    do {
                        role2tabs = XN_Query.contentQuery().tag("role2tabs")
                                .filter("type", "eic", "role2tabs")
                                .filter("my.record", "in", ids)
                                .order("published", "A_N")
                                .begin(page * 100).end((page + 1) * 100)
                                .notDelete().execute();
                        role2tabs.forEach(item -> {
                            ((Content) item).my.put("deleted", DateTimeUtils.gettimeStamp());
                            saves.add(item);
                        });
                        page++;
                    } while (!role2tabs.isEmpty());

                    if (!saves.isEmpty()) {
                        XN_Content.batchsave(saves, "role2tabs");
                        saves.clear();
                    }
                }
                ((List<?>) ids).clear();
            }
        }
    }

    @Override
    public String getIdByName(String rolename) {
        if (!rolename.isEmpty()) {
            try {
                List<Object> list = XN_Query.contentQuery().tag("supplier_roles")
                        .filter("type", "eic", "supplier_roles")
                        .filter("my.rolename", "=", rolename)
                        .notDelete().end(1).execute();
                if (!list.isEmpty()) {
                    return ((Content) list.get(0)).id;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @Override
    public HashMap<String, Object> list() {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<Object> list = XN_Query.contentQuery().tag("supplier_roles")
                    .filter("type", "eic", "supplier_roles")
                    .notDelete().end(-1).execute();
            for(Object item: list){
                result.put(((Content)item).id,((Content)item).get("rolename"));
            }
        } catch (Exception ignored) {
        }

        return result;
    }

    @Override
    public List<Object> getRoleByName(String rolename) {
        List<Object> result = new ArrayList<>();
        try {
            List<Object> list = XN_Query.contentQuery().tag("supplier_roles")
                    .filter("type", "eic", "supplier_roles")
                    .filter("my.rolename", "=", rolename)
                    .notDelete().end(1).execute();
            if (!list.isEmpty()) {
                SupplierRoles roles = new SupplierRoles(list.get(0));
                result = getAllRoles(roles);
            }
        } catch (Exception ignored) {
        }

        return result;
    }

    @Override
    public List<Object> getRoleById(String record) {
        List<Object> result = new ArrayList<>();
        try {
            Content Conn = XN_Content.load(record, "supplier_roles");
            SupplierRoles roles = new SupplierRoles(Conn);
            result = getAllRoles(roles);
        } catch (Exception ignored) {
        }
        return result;
    }

    @Override
    public HashMap<String, String> getNameByRoleIds(Object roleids) {
        HashMap<String,String> result = new HashMap<>(1);
        if(Utils.isNotEmpty(roleids)){
            if(roleids instanceof String){
                roleids = Collections.singletonList(roleids);
            }
            if(roleids instanceof List){
                try {
                    List<Object> query = XN_Content.loadMany((List<String>)roleids,"supplier_roles");
                    for(Object item: query){
                        SupplierRoles roles = new SupplierRoles(item);
                        result.put(roles.id,roles.rolename);
                    }
                }catch (Exception ignored) {}
            }
        }
        return result;
    }
}
