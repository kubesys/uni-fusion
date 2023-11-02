package com.qnkj.core.base.modules.settings.operationlog.service.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Rest;
import com.github.restapi.models.Content;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.settings.operationlog.entity.Operationlog;
import com.qnkj.core.base.modules.settings.operationlog.service.IOperationlogService;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import org.springframework.stereotype.Service;

/**
 * create by 徐雁
 */

@Service
public class OperationlogServiceImpl implements IOperationlogService {
    @Override
    public void writeLog(String application,Operationlog log) {
        try {
            if(Utils.isEmpty(log.supplierid)){
                log.supplierid = SupplierUtils.getSupplierid();
            }
            XN_Rest.setApplication(application);
            Content newcontent = XN_Content.create("operlogs","", log.getProfileid(),9);
            log.toContent(newcontent);
            newcontent.save("operlogs");
        } catch(Exception ignored) {
        }
    }
}
