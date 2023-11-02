package com.qnkj.core.utils;

import com.qnkj.core.base.modules.settings.importdata.services.IImportdataSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class ImportdataUtils {
    @Autowired
    public IImportdataSetService importdataSetService;
    public static ImportdataUtils importdataUtils;

    @PostConstruct
    public void init(){
        importdataUtils = this;
        importdataUtils.importdataSetService = this.importdataSetService;
    }

    public static Boolean hasExcelset(String modulename) {
        try {
            List<Object> list = importdataUtils.importdataSetService.getExcelset(modulename);
            if (list.size()>0) {
                return true;
            }
        }catch (Exception ignored){}
        return false;
    }
}
