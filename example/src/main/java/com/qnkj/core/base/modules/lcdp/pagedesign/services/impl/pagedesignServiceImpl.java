package com.qnkj.core.base.modules.lcdp.pagedesign.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.google.common.collect.ImmutableSet;
import com.qnkj.common.configs.BasePageDesignConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.common.services.IModuleServices;
import com.qnkj.common.services.ITabFieldServices;
import com.qnkj.common.utils.Base64Util;
import com.qnkj.common.utils.RedisUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.CacheBaseEntitys;
import com.qnkj.core.base.modules.lcdp.developments.service.IDevelopmentService;
import com.qnkj.core.base.modules.lcdp.formdesign.entity.FormDesign;
import com.qnkj.core.base.modules.lcdp.formdesign.service.IFormdesignService;
import com.qnkj.core.base.modules.lcdp.pagedesign.Util.PageDesignUtils;
import com.qnkj.core.base.modules.lcdp.pagedesign.Util.PagedesignConfigGenerator;
import com.qnkj.core.base.modules.lcdp.pagedesign.entitys.PageDesign;
import com.qnkj.core.base.modules.lcdp.pagedesign.services.IpagedesignService;
import com.qnkj.core.plugins.compiler.CompilerUtils;
import com.qnkj.core.webconfigs.exception.WebException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * create by Auto Generator
 * create date 2021-06-25
 * @author Generator
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class pagedesignServiceImpl implements IpagedesignService {
    private final IFormdesignService formdesignService;
    private final IModuleServices moduleServices;
    private final ITabFieldServices fieldServices;
    private final IDevelopmentService developmentService;

    private List<String> cachePrograms = new ArrayList<>();
    private List<String> cacheParents = new ArrayList<>();



    @Override
    public Object addDataSearch(HashMap<String, Object> httpRequest) {
        try{
            if (cachePrograms.isEmpty() || cacheParents.isEmpty()) {
                List<Object> pagedesigns = XN_Query.contentQuery().tag("pagedesigns")
                        .filter("type", "eic", "pagedesigns")
                        .notDelete()
                        .end(-1).execute();
                for(Object item: pagedesigns) {
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
            Object redisProgram = RedisUtils.get("pagedesigns_program_" + currentSessionId);
            List<SelectOption> options = new ArrayList<>();
            for(String item: cachePrograms){
                if (Utils.isNotEmpty(redisProgram) && redisProgram.toString().compareTo(item) == 0) {
                    options.add(new SelectOption(item,item,true));
                } else {
                    options.add(new SelectOption(item,item,false));
                }
            }
            CustomDataSearch programDataSearch = new CustomDataSearch().searchtype("select").colspan(1).fieldname("program").fieldlabel("菜单组").options(options);

            Object redisParent = RedisUtils.get("pagedesigns_parent_" + currentSessionId);
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
        String currentSessionId = (String) SecurityUtils.getSubject().getSession().getId();
        if (httpRequest.containsKey("program") && !Utils.isEmpty(httpRequest.get("program"))) {
            String program = httpRequest.get("program").toString();
            query.filter("my.program","=",program);
            RedisUtils.set("pagedesigns_program_"  + currentSessionId, program);
        } else if (!httpRequest.containsKey("program")) {
            Object program = RedisUtils.get("pagedesigns_program_" + currentSessionId);
            if (program != null && program.toString().compareTo("") != 0) {
                query.filter("my.program","=",program.toString());
            }
        } else if (httpRequest.containsKey("program") && Utils.isEmpty(httpRequest.get("program"))) {
            RedisUtils.del("pagedesigns_program_" + currentSessionId);
        }
        if (httpRequest.containsKey("parent") && !Utils.isEmpty(httpRequest.get("parent"))) {
            String parent = httpRequest.get("parent").toString();
            query.filter("my.parent","=",parent);
            RedisUtils.set("pagedesigns_parent_"  + currentSessionId, parent);
        } else if (!httpRequest.containsKey("parent")) {
            Object parent = RedisUtils.get("pagedesigns_parent_" + currentSessionId);
            if (parent != null && parent.toString().compareTo("") != 0) {
                query.filter("my.parent","=",parent.toString());
            }
        } else if (httpRequest.containsKey("parent") && Utils.isEmpty(httpRequest.get("parent"))) {
            RedisUtils.del("pagedesigns_parent_" + currentSessionId);
        }
    }


    @Override
    public HashMap<String, Object> getModuleInfo(String module) throws Exception {
         return PageDesignUtils.getModulePageDesignFields(module);
    }

    @Override
    public void verifyDesignList() {
        try {
            List<HashMap<String,Object>> allModuleObject = (List<HashMap<String, Object>>)developmentService.getModuleList();
            List<String > allowLists = allModuleObject.stream().filter(v -> {
                        Boolean disabled = (Boolean) v.get("disabled");
                        Boolean builtin = (Boolean) v.get("builtin");
                        Integer moduletype = (Integer) v.get("moduletype");
                        return !disabled && !builtin && moduletype == 1;
                    }
            ).map(v ->  v.get("modulename").toString() ).collect(Collectors.toList());
            List<Object> pagedesigns = XN_Query.contentQuery().tag("pagedesigns")
                    .filter("type", "eic", "pagedesigns")
                    .notDelete()
                    .end(-1).execute();
            if (!pagedesigns.isEmpty()) {
                for(Object item : pagedesigns) {
                    PageDesign design = new PageDesign(item);
                    if (!allowLists.contains(design.modulename)) {
                        Content pagedesign = (Content) item;
                        pagedesign.my.put("deleted","1");
                        pagedesign.save("pagedesigns");
                    }
                }
            }
        }catch (Exception e) {}
    }

    /**
     * 保存页面设计信息
     * @param list　页面设计列表信息（以JSON字符串保存）
     * @throws Exception
     */
    @Override
    public void saveDesignDetail(List<Object> list)throws Exception {
        if (Utils.isEmpty(list)) { return; }
        String jsonStr = JSONArray.toJSONString(list);
        String record = ((Map)list.get(0)).get("record").toString();
        Content pagedesign = XN_Content.load(record, "pagedesigns");
        pagedesign.my.put("template_editor", Base64Util.base64Encode(jsonStr));
        pagedesign.save("pagedesigns");
    }

    @Override
    public void generateModule(Map<String, Object> httpRequest) throws Exception {
        Object record = httpRequest.getOrDefault("ids", null);
        if (!Utils.isEmpty(record)) {
            if (record instanceof String) {
                record = new ArrayList<>(ImmutableSet.of(record.toString()));
            }
            if (record instanceof List) {
                List<?> query = XN_Content.loadMany((List) record, "pagedesigns");
                if (!query.isEmpty()) {
                    for (Object item : query) {
                        FormDesign pagedesign = new FormDesign(item);
                        createModule(pagedesign.modulename, false, false, false);
                        pagedesign.generate = true;
                        pagedesign.toContent(item);
                        ((Content) item).save("pagedesigns");
                    }
                }
                try {
                    CompilerUtils.autoCompiler();
                } catch (Exception ignored) {
                }
            }
        } else {
            throw new WebException("参数错误");
        }
    }

    private void createModule(String modulename,Boolean isUpdateMenu,Boolean isUpdatePicklist,Boolean isAutoCompile) throws Exception {
        if(Utils.isEmpty(modulename)) {
            throw new Exception("模块名不能为空");
        }
        CacheBaseEntitys.clear(modulename);
        Module info = moduleServices.get(modulename);
    }

    @Override
    public void exportAssigned(Map<String, Object> requestParams, BaseEntityUtils viewEntitys) throws Exception {
        List<String> ids;
        if (requestParams.get("ids") instanceof String) {
            ids = Arrays.asList((String) requestParams.get("ids"));
        } else {
            ids = (List<String>)requestParams.get("ids");
        }
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        BasePageDesignConfig baseConfig = PagedesignConfigGenerator.getBasePageDesignConfig();
        if (Objects.isNull(baseConfig)) {
            exportAll(viewEntitys);
            return;
        }
        List<PageDesignInitInfo> list = getPageDesignInitInfo(ids, viewEntitys);
        BasePageDesignConfig.pageDesignList.addAll(list);
        PagedesignConfigGenerator.generate(list, "Auto Generator");
    }

    @Override
    public void exportAll(BaseEntityUtils viewEntitys) throws Exception {
        List<PageDesignInitInfo> listOfPrint = getPageDesignInitInfo(null, viewEntitys);
        PagedesignConfigGenerator.generate(listOfPrint, "Auto Generator");
    }

    private List<PageDesignInitInfo> getPageDesignInitInfo(List<String> ids, BaseEntityUtils viewEntitys) throws Exception {
        XN_Query query = XN_Query.create(viewEntitys.getDataType()).tag(viewEntitys.getTabName())
                .filter("type", "eic", viewEntitys.getTabName())
                .notDelete().end(-1);
        if (!CollectionUtils.isEmpty(ids)) {
            query.filter("id", "in", ids);
        }
        List<Object> resultList = query.execute();
        if (resultList.isEmpty()) {
            throw new RuntimeException("可导出数据为空");
        }
        List<PageDesignInitInfo> list = new ArrayList<>(resultList.size());
        resultList.forEach(item -> {
            PageDesignInitInfo pageDesignInitInfo = new PageDesignInitInfo(item);
            list.add(pageDesignInitInfo);
        });
        return list;
    }

    /**
     * 清空页面设计信息
     * @param httpRequest
     * @throws Exception
     */
    @Override
    public void clearModuleDesignData(Map<String, Object> httpRequest) throws Exception {
        Object record = httpRequest.getOrDefault("ids", null);
        if (!Utils.isEmpty(record)) {
            if (record instanceof String) {
                record = new ArrayList<>(ImmutableSet.of(record.toString()));
            }
            if (record instanceof List) {
                List<String> lst = (List<String>)record;
                for (String id:lst) {
                    Content conn = XN_Content.load(id, "pagedesigns");
                    conn.my.put("template_editor","");
                    conn.save("pagedesigns");
                }
            }
        } else {
            throw new WebException("参数错误");
        }
    }

    @Override
    public Object getFlowform(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys,Boolean isTypename) throws Exception {
        String module = httpRequest.getOrDefault("module","").toString();
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
                    infoMap.put("uitypename",isTypename? Utils.getUitypeName(field.uitype) : field.uitype);
                    infoMap.put("picklist",field.picklist);
                    infoMap.put("isarray",field.isarray?"1":"0");
                    result.add(infoMap);
                }
            }
        }
        return result;
    }
}
