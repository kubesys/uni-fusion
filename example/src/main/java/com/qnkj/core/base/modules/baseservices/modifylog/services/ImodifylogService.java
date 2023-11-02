package com.qnkj.core.base.modules.baseservices.modifylog.services;

import com.qnkj.core.base.modules.baseservices.modifylog.entitys.modifylog;
import com.qnkj.core.base.services.IBaseService;

/**
 * create by Auto Generator
 * create date 2021-09-18
 */
public interface ImodifylogService extends IBaseService {

   default modifylog load(String record) throws Exception {
        return (modifylog)IBaseService.super.load(record, "modifylog", 7, modifylog.class);
   }
}
