package com.qnkj.core.base.modules.lcdp.pagedesign.services;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.lcdp.pagedesign.entitys.PageDesign;
import com.qnkj.core.base.services.IBaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2021-06-25
 */
public interface IpagedesignService extends IBaseService {

    default PageDesign load(String record) throws Exception {
        return (PageDesign)IBaseService.super.load(record, "pagedesigns", 0, PageDesign.class);
    }

    HashMap<String,Object> getModuleInfo(String module) throws Exception;

    void saveDesignDetail(List<Object> list)throws Exception;
    void generateModule(Map<String, Object> httpRequest) throws Exception;
    void exportAssigned(Map<String, Object> requestParams, BaseEntityUtils viewEntitys) throws Exception;
    void exportAll(BaseEntityUtils viewEntitys) throws Exception;
    void clearModuleDesignData(Map<String, Object> httpRequest) throws Exception;

    void verifyDesignList();

    Object getFlowform(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys,Boolean isTypename) throws Exception;


}
