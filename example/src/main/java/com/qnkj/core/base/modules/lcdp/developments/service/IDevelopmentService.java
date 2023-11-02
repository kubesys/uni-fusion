package com.qnkj.core.base.modules.lcdp.developments.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2020/11/9
 * @author clubs
 */

public interface IDevelopmentService {
    Object getTree();
    Object getGroup(Map<String, Object> httpRequest);
    Object getModule(String modulename);
    Object getAllModule();
    Object getModuleList();
    Object getAllModule(List<String> authorize);
    Object getConfigModule();
    String getModuleMenuAuthorize(String modulename);
    void delete(Map<String, Object> httpRequest) throws Exception;
    void update(Map<String, Object> httpRequest) throws Exception;
    void saveModule(HashMap<String, Object> httpRequest) throws Exception;
    void createModule(String modulename) throws Exception;
    void createModule(String modulename,Boolean isUpdateMenu,Boolean isUpdatePicklist,Boolean isAutoCompile) throws Exception;
    void createAllModule() throws Exception;
}
