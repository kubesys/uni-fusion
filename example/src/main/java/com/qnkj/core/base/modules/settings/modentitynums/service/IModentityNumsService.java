package com.qnkj.core.base.modules.settings.modentitynums.service;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.modentitynums.entity.ModentityNums;
import com.qnkj.core.base.services.IBaseService;

import java.util.Map;

/**
 * create by 徐雁
 * @author clubs
 */
public interface IModentityNumsService extends IBaseService {

    ModentityNums getNumInfoById(String record, BaseEntityUtils viewEntitys);

    void save(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;
}
