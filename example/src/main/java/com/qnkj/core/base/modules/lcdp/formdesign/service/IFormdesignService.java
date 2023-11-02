package com.qnkj.core.base.modules.lcdp.formdesign.service;

import com.qnkj.core.base.services.IBaseService;

import java.util.Map;

/**
 * create by 徐雁
 * create date 2021/4/30
 */

public interface IFormdesignService extends IBaseService {
    void updateModuleInfo();
    void updateDesignStatus(Map<String, Object> httpRequest);
    Object getDesignInfo(Map<String, Object> httpRequest);
    Object getDesignModuleInfo(Map<String, Object> httpRequest);
    void generateModule(Map<String, Object> httpRequest) throws Exception;
    void generateAllModule() throws Exception;
    Object getFlowform(String module) throws Exception;
    void saveDataPermission(Map<String, Object> httpRequest) throws Exception;
}
