package com.qnkj.core.base.modules.baseservices.modifylog.services.impl;

import com.github.restapi.XN_Query;
import com.qnkj.common.entitys.CustomDataSearch;
import com.qnkj.common.entitys.SelectOption;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.baseservices.modifylog.entitys.modifylog;
import com.qnkj.core.base.modules.baseservices.modifylog.services.ImodifylogService;
import com.qnkj.core.base.modules.lcdp.developments.service.IDevelopmentService;
import com.qnkj.core.base.services.IPublicService;
import com.qnkj.core.utils.ProfileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * create by Auto Generator
 * create date 2021-09-18
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class modifylogServiceImpl implements ImodifylogService {
    private final IDevelopmentService developmentService;

    @Override
    public void customListViewEntity(ArrayList<Object> Entitys) throws Exception {
        List<Object> allModule = (List<Object>) developmentService.getAllModule();
        for(Object item: Entitys){
            modifylog modifylog = (modifylog)item;
            for(Object module: allModule) {
                if(((HashMap)module).get("modulename").equals(modifylog.belongmodule)){
                    modifylog.belongmodule = ((HashMap)module).get("modulelabel").toString();
                    break;
                }
            }
            modifylog.body = modifylog.body.replaceAll("\n","<br>");
        }
    }

    @Override
    public Object addDataSearch(HashMap<String, Object> Request) {
        List<String> authorize = new ArrayList<>(Collections.singleton("general"));
        if(ProfileUtils.isSupplier()) {
            authorize.add("supplier");
        }else if(ProfileUtils.isAdmin()){
            authorize.add("system");
        }
        List<Object> allModule = (List<Object>) developmentService.getAllModule(authorize);
        List<SelectOption> options = new ArrayList<>();
        for(Object item: allModule) {
            if("lcdp".equals(((HashMap)item).get("group"))) {
                continue;
            }
            if("settings".equals(((HashMap)item).get("group"))) {
                continue;
            }
            if("supplier".equals(((HashMap)item).get("group"))) {
                continue;
            }
            if(((HashMap)item).get("builtin").equals(true)) {
                continue;
            }
            options.add(new SelectOption(((HashMap)item).get("modulename").toString(),((HashMap)item).get("modulelabel").toString(),false));
        }
        return new CustomDataSearch().searchtype("select").colspan(2).fieldname("belongmodule").fieldlabel("审批模块").options(options);
    }

    @Override
    public void addQueryFilter(Map<String, Object> Request, BaseEntityUtils viewEntitys, XN_Query query) {
        IPublicService.addSupplierFilter(query);
        if (Request.containsKey("belongmodule")) {
            query.filter("my.belongmodule", "=", Request.get("belongmodule").toString());
        }
        ImodifylogService.super.addQueryFilter(Request, viewEntitys, query);
    }
}
