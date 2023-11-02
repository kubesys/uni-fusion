package com.qnkj.core.base.services;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public interface IBaseEntityService {
    List<Object> getEntityUtils(String tabname);

    HashMap<String,Object> getExportExcelData(HttpServletRequest request) throws Exception;

    void superDelete(HttpServletRequest request) throws Exception;
}
