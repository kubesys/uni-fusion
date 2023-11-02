package com.qnkj.core.base.modules.lcdp.systemsetting.service;

import com.qnkj.core.base.services.IBaseService;

import java.util.Map;
import java.util.Properties;

/**
 * create by 徐雁
 * create date 2021/3/5
 */

public interface ISystemSettingService extends IBaseService {
    Properties getSetting(String domain);
    Object getTree();
    Object getPlatformAuthorizes(String domain);
    Object getUserAuthorizes(String domain);
    void save(Map<String, Object> httpRequest) throws Exception;
    void delete(Map<String, Object> httpRequest) throws Exception;
}
