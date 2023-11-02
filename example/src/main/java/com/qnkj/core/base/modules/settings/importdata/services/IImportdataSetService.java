package com.qnkj.core.base.modules.settings.importdata.services;

import com.qnkj.core.base.services.IBaseService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2021-09-02
 */
public interface IImportdataSetService extends IBaseService {
    Model getImportdataSet(String modulename, Model model)throws Exception;

    List getImportdataSetUI(List<HashMap<String,Object>> list, List linkLst)throws Exception;

    void saveExcelset(Map<String, Object> Request, List columnlist, Object fields, List linkLst)throws Exception;

    void downloadExcel(HttpServletRequest request, HttpServletResponse response)throws Exception;

    List getExcelset(String modulename)throws Exception;
}
