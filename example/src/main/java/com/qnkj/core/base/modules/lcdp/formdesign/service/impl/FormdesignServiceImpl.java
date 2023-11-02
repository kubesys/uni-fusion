package com.qnkj.core.base.modules.lcdp.formdesign.service.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.google.common.collect.ImmutableSet;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.common.services.*;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.RedisUtils;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.CacheBaseEntitys;
import com.qnkj.core.base.modules.lcdp.developments.service.IDevelopmentService;
import com.qnkj.core.base.modules.lcdp.formdesign.entity.FormDesign;
import com.qnkj.core.base.modules.lcdp.formdesign.service.IFormdesignService;
import com.qnkj.core.plugins.compiler.CompilerUtils;
import com.qnkj.core.plugins.feign.FeignApi;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.exception.WebException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * create by 徐雁
 * create date 2021/4/30
 * @author clubs
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class FormdesignServiceImpl implements IFormdesignService {
    private final IProgramServices programServices;
    private final IParentTabServices parentTabServices;
    private final IModuleMenuServices moduleMenuServices;
    private final ITabServices tabServices;
    private final IDevelopmentService developmentService;
    private final IModuleServices moduleServices;
    private final ITabFieldServices fieldServices;
    private final IDataPermissionServices dataPermissionServices;

    private List<String> cachePrograms = new ArrayList<>();
    private List<String> cacheParents = new ArrayList<>();


    @Override
    public Object addDataSearch(HashMap<String, Object> httpRequest) {
        try{
            if (cachePrograms.isEmpty() || cacheParents.isEmpty()) {
                List<Object> formdesigns = XN_Query.contentQuery().tag("formdesigns")
                        .filter("type", "eic", "formdesigns")
                        .notDelete()
                        .end(-1).execute();
                for(Object item: formdesigns) {
                    Content formdesign = (Content)item;
                    String program = formdesign.my.get("program").toString();
                    String parent = formdesign.my.get("parent").toString();
                    if (!cachePrograms.contains(program)) {
                        cachePrograms.add(program);
                    }
                    if (!cacheParents.contains(parent)) {
                        cacheParents.add(parent);
                    }
                }
            }
            String currentSessionId = (String) SecurityUtils.getSubject().getSession().getId();
            Object redisProgram = RedisUtils.get("formdesigns_program_" + currentSessionId);
            List<SelectOption> options = new ArrayList<>();
            for(String item: cachePrograms){
                if (Utils.isNotEmpty(redisProgram) && redisProgram.toString().compareTo(item) == 0) {
                    options.add(new SelectOption(item,item,true));
                } else {
                    options.add(new SelectOption(item,item,false));
                }
            }
            CustomDataSearch programDataSearch = new CustomDataSearch().searchtype("select").colspan(1).fieldname("program").fieldlabel("菜单组").options(options);

            Object redisParent = RedisUtils.get("formdesigns_parent_" + currentSessionId);
            options = new ArrayList<>();
            for(String item: cacheParents){
                if (Utils.isNotEmpty(redisParent) && redisParent.toString().compareTo(item) == 0) {
                    options.add(new SelectOption(item,item,true));
                } else {
                    options.add(new SelectOption(item,item,false));
                }
            }
            CustomDataSearch parentDataSearch = new CustomDataSearch().searchtype("select").colspan(1).fieldname("parent").fieldlabel("模块组").options(options);

            return Arrays.asList(programDataSearch,parentDataSearch);

        }catch (Exception ignored){ }
        return new CustomDataSearch();
    }

    @Override
    public void addQueryFilter(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys, XN_Query query) {
        query.order("published","D_N");
        String currentSessionId = (String) SecurityUtils.getSubject().getSession().getId();
        if (httpRequest.containsKey("program") && !Utils.isEmpty(httpRequest.get("program"))) {
            String program = httpRequest.get("program").toString();
            query.filter("my.program","=",program);
            RedisUtils.set("formdesigns_program_"  + currentSessionId, program);
        } else if (!httpRequest.containsKey("program")) {
            Object program = RedisUtils.get("formdesigns_program_" + currentSessionId);
            if (program != null && program.toString().compareTo("") != 0) {
                query.filter("my.program","=",program.toString());
            }
        } else if (httpRequest.containsKey("program") && Utils.isEmpty(httpRequest.get("program"))) {
            RedisUtils.del("formdesigns_program_" + currentSessionId);
        }

        if (httpRequest.containsKey("parent") && !Utils.isEmpty(httpRequest.get("parent"))) {
            String parent = httpRequest.get("parent").toString();
            query.filter("my.parent","=",parent);
            RedisUtils.set("formdesigns_parent_"  + currentSessionId, parent);
        } else if (!httpRequest.containsKey("parent")) {
            Object parent = RedisUtils.get("formdesigns_parent_" + currentSessionId);
            if (parent != null && parent.toString().compareTo("") != 0) {
                query.filter("my.parent","=",parent.toString());
            }
        } else if (httpRequest.containsKey("parent") && Utils.isEmpty(httpRequest.get("parent"))) {
            RedisUtils.del("formdesigns_parent_" + currentSessionId);
        }
    }

    private List<FormDesign> getAllDesign() {
        List<FormDesign> result = new ArrayList<>();
        try{
            List<Object> query;
            int page = 1;
            do {
                query = XN_Query.contentQuery().tag("formdesigns")
                        .filter("type", "eic", "formdesigns")
                        .order("updated","A_N")
                        .begin((page - 1) * 100).end(page * 100).execute();
                for(Object item: query){
                    result.add(new FormDesign(item));
                }
                page++;
            }while (query.size() == 100);
        }catch (Exception ignored) {}
        return result;
    }

    private FormDesign getDesign(List<FormDesign> dblist,String modulename) {
        for(FormDesign item: dblist){
            if(item.modulename.equals(modulename)) {
                return item;
            }
        }
        return null;
    }

    private List<ModuleMenu> getSubParentTab(ParentTab parenttab) {
        List<ModuleMenu> moduleMenus = new ArrayList<>();
        List<ParentTab> subTabs = parentTabServices.list(parenttab.name);
        if(!Utils.isEmpty(subTabs)) {
            for (ParentTab subtab :subTabs) {
                List<ModuleMenu> menus = moduleMenuServices.list(parenttab.name, subtab.name);
                if(!Utils.isEmpty(menus)) {
                    for (ModuleMenu moduleMenu : menus) {
                        if (!Utils.isEmpty(moduleMenu.modulename) && !"developments".equals(moduleMenu.modulename)) {
                            moduleMenus.add(moduleMenu);
                        }
                    }
                }
                moduleMenus.addAll(getSubParentTab(subtab));
            }
        }
        return moduleMenus;
    }



    @Override
    public void updateModuleInfo() {
        List<ModuleMenu> moduleMenus = new ArrayList<>();
        List<Program> programs = programServices.list();
        if(!Utils.isEmpty(programs)) {
            for (Program program : programs) {
                if (program.builtin) {
                    continue;
                }
                try {
                    List<ParentTab> parentTabs = parentTabServices.list(program.group);
                    if (!Utils.isEmpty(parentTabs)) {
                        for (ParentTab parenttab : parentTabs) {
                            moduleMenus.addAll(getSubParentTab(parenttab));
                            List<ModuleMenu> menus = moduleMenuServices.list(program.group, parenttab.name);
                            if (Utils.isEmpty(menus)) {
                                continue;
                            }
                            for (ModuleMenu moduleMenu : menus) {
                                if (!Utils.isEmpty(moduleMenu.modulename) && !"developments".equals(moduleMenu.modulename)) {
                                    moduleMenus.add(moduleMenu);
                                }
                            }
                        }
                    }
                } catch (Exception ignored) { }
            }
        }
        List<FormDesign> dblist = getAllDesign();
        List<Object> create = new ArrayList<>();
        List<Object> update = new ArrayList<>();
        List<FormDesign> exist = new ArrayList<>();
        if(!moduleMenus.isEmpty()){
            for(ModuleMenu moduleMenu: moduleMenus){
                CacheBaseEntitys.clear(moduleMenu.modulename);
                FeignApi.clearModuleCacheInfo(moduleMenu.modulename);
                Tab tab = tabServices.get(moduleMenu.modulename);
                FormDesign design = getDesign(dblist,moduleMenu.modulename);
                if(!Utils.isEmpty(design)) {
                    exist.add(design);
                }
                if(!Utils.isEmpty(tab) && tab.moduletype == 0) {
                    ParentTab parentTab = parentTabServices.get(moduleMenu.program, moduleMenu.parent);
                    Program program = programServices.get(parentTab.program);
                    String parentLabel = parentTab.label;
                    while (Utils.isEmpty(program) && !Utils.isEmpty(parentTab)) {
                        parentTab = parentTabServices.load(parentTab.programid);
                        if(!Utils.isEmpty(parentTab)) {
                            program = programServices.get(parentTab.program);
                        }
                    }
                    String progLabel = !Utils.isEmpty(program) ? program.label : "";
                    if(Utils.isEmpty(design)){
                        FormDesign newDesign = new FormDesign();
                        newDesign.modulename = moduleMenu.modulename;
                        newDesign.program = progLabel;
                        newDesign.parent = parentLabel;
                        newDesign.module = moduleMenu.label;
                        newDesign.generate = false;
                        Content content = XN_Content.create("formdesigns","",ProfileUtils.getCurrentProfileId());
                        newDesign.toContent(content);
                        create.add(content);
                    } else {
                        design.program = progLabel;
                        design.parent = parentLabel;
                        design.module = moduleMenu.label;
                        design.deleted = 0;
                        update.add(design);
                    }
                }else if(design != null && design.deleted == 0){
                    design.deleted = DateTimeUtils.gettimeStamp();
                    update.add(design);
                }
            }
        }
        dblist.removeAll(exist);
        if(!dblist.isEmpty()) {
            for(FormDesign item: dblist){
                if(item.deleted == 0) {
                    item.deleted = DateTimeUtils.gettimeStamp();
                    update.add(item);
                }
            }
        }
        try {
            if (!create.isEmpty()) {
                XN_Content.batchsave(create, "formdesigns");
            }
            if (!update.isEmpty()) {
                List<Object> updateConn = new ArrayList<>();
                for(Object item: update){
                    try{
                        if(!Utils.isEmpty(((FormDesign) item).id)){
                            Content content = XN_Content.load(((FormDesign) item).id,"formdesigns");
                            ((FormDesign) item).toContent(content);
                            updateConn.add(content);
                        }
                    }catch (Exception ignored) {}
                }
                if(!updateConn.isEmpty()) {
                    XN_Content.batchsave(updateConn, "formdesigns");
                    updateConn.clear();
                    update.clear();
                }
            }
        }catch (Exception ignored) {}
    }

    @Override
    public Object getDesignInfo(Map<String, Object> httpRequest) {
        String record = httpRequest.getOrDefault("record","").toString();
        if(!Utils.isEmpty(record)){
            try {
                Content content = XN_Content.load(record,"formdesigns");
                FormDesign design = new FormDesign(content);
                if(!Utils.isEmpty(design.modulename)){
                    HashMap<String,Object> result = new HashMap<>();
                    Object moduleinfo = developmentService.getModule(design.modulename);
                    if (!Utils.isEmpty(moduleinfo)) {
                        result.put("MODULEINFO",moduleinfo);
                    }
                    SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
                    Object authorizess = saasUtils.getPlatformAuthorizes();
                    result.put("AUTHORIZE",authorizess);
                    result.put("SERVICECLASS",developmentService.getAllModule());
                    return result;
                }
            }catch (Exception ignored) {}
        }
        return null;
    }

    @Override
    public Object getDesignModuleInfo(Map<String, Object> httpRequest) {
        String modulename = httpRequest.getOrDefault("modulename","").toString();
        if(Utils.isEmpty(modulename)) {
            return null;
        }
        return developmentService.getModule(modulename);
    }

    @Override
    public void updateDesignStatus(Map<String, Object> httpRequest) {
        String record = httpRequest.getOrDefault("record", "").toString();
        if (!Utils.isEmpty(record)) {
            try {
                Content content = XN_Content.load(record, "formdesigns");
                FormDesign design = new FormDesign(content);
                design.generate = false;
                design.toContent(content);
                content.save("formdesigns");
            } catch (Exception ignored) {
            }
        }
    }

    private void createModule(String modulename) throws Exception {
        createModule(modulename,false,false,true);
    }

    private void createModule(String modulename,Boolean isUpdateMenu,Boolean isUpdatePicklist,Boolean isAutoCompile) throws Exception {
        if(Utils.isEmpty(modulename)) {
            throw new Exception("模块名不能为空");
        }
        CacheBaseEntitys.clear(modulename);
        FeignApi.clearModuleCacheInfo(modulename);
        Module info = moduleServices.get(modulename);
    }

    @Override
    public void generateModule(Map<String, Object> httpRequest) throws Exception {
        try {
            Object record = httpRequest.getOrDefault("ids", null);
            if (!Utils.isEmpty(record)) {
                if (record instanceof String) {
                    record = new ArrayList<>(ImmutableSet.of(record.toString()));
                }
            } else {
                throw new WebException("参数错误");
            }
        } catch (Exception e){
            log.error("GenerateModule : {}",e.getMessage());
            throw e;
        }
    }

    @Override
    public void generateAllModule() throws Exception {
        List<Object> modules = moduleServices.getMenuGroup();
        List<String> modulenames = new ArrayList<>();
        for(Object item: modules){
            if(item instanceof ParentTab){
                List<ModuleMenu> moduleMenus = moduleMenuServices.list(((ParentTab) item).program,((ParentTab) item).name);
                if(!Utils.isEmpty(moduleMenus)) {
                    for (ModuleMenu menu : moduleMenus) {
                        if (!menu.builtin && !Utils.isEmpty(menu.modulename)) {
                            modulenames.add(menu.modulename);
                            createModule(menu.modulename,false,false,false);
                        }
                    }
                }
            }
        }
        if(!modulenames.isEmpty()) {
            try {
                List<Object> query = XN_Query.contentQuery().tag("formdesigns")
                        .filter("type", "eic", "formdesigns")
                        .filter("my.modulename","in",modulenames)
                        .end(-1).execute();
                if(!query.isEmpty()){
                    for(Object item: query){
                        ((Content)item).set("generate","1");
                    }
                    XN_Content.batchsave(query,"formdesigns");
                }
            } catch (Exception ignored) {
            }
        }
        try {
            CompilerUtils.autoCompiler();
        }catch (Exception ignored) {}
    }

    @Override
    public Object getFlowform(String module) throws Exception {
        List<Object> result = new ArrayList<>();
        if(Utils.isNotEmpty(module)){
            List<TabField> tabFields = fieldServices.list(module);
            if (Utils.isEmpty(tabFields)) {
                return result;
            }
            for(TabField field: tabFields){
                if(!Arrays.asList(3,4,22,30,31).contains(field.uitype)){
                    Map<String,Object> infoMap = new HashMap<>(1);
                    infoMap.put("fieldname",field.fieldname);
                    infoMap.put("fieldlabel",field.fieldlabel);
                    infoMap.put("uitype", field.uitype);
                    infoMap.put("uitypename",field.uitype);
                    infoMap.put("picklist",field.picklist);
                    infoMap.put("isarray",field.isarray?"1":"0");
                    result.add(infoMap);
                }
            }
        }
        return result;
    }

    @Override
    public void saveDataPermission(Map<String, Object> httpRequest) throws Exception {
        String record = httpRequest.getOrDefault("record", "").toString();
        Object datapermission = httpRequest.getOrDefault("datapermission",null);
        if (Utils.isNotEmpty(record)) {
            Content content = XN_Content.load(record,"formdesigns");
            FormDesign design = new FormDesign(content);
            if(Utils.isNotEmpty(design.modulename)) {
                if(Utils.isNotEmpty(datapermission)) {
                    if (datapermission instanceof List) {
                        Map<String, List<Expression>> datapermissions = new HashMap<>(1);
                        for (Object item : (List<?>) datapermission) {
                            if (item instanceof Map) {
                                for (Object authorizes : ((Map<?, ?>) item).keySet()) {
                                    Object conditions = ((Map<?, ?>) item).get(authorizes);
                                    if (conditions instanceof List) {
                                        List<Expression> expressions = new ArrayList<>(1);
                                        for (Object condition : (List<?>) conditions) {
                                            Expression expression = new Expression(condition);
                                            expressions.add(expression);
                                        }
                                        if (!expressions.isEmpty()) {
                                            datapermissions.put(authorizes.toString(), expressions);
                                        }
                                    }
                                }
                            }
                        }
                        if (!datapermissions.isEmpty()) {
                            DataPermission dataPermission = new DataPermission();
                            dataPermission.setModulename(design.modulename).setExpressions(datapermissions);
                            dataPermissionServices.delete(design.modulename);
                            dataPermissionServices.updateAll(dataPermission);
                        }
                    }
                } else {
                    dataPermissionServices.delete(design.modulename);
                }
            }
        }
    }
}
