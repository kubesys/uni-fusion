package com.qnkj.core.base.modules.settings.operationlog.service;

import com.qnkj.core.base.modules.settings.operationlog.entity.Operationlog;
import com.qnkj.core.base.services.IBaseService;
import org.springframework.scheduling.annotation.Async;

/**
 * create by 徐雁
 */
public interface IOperationlogService extends IBaseService {

    @Async
    void writeLog(String application,Operationlog log);
}
