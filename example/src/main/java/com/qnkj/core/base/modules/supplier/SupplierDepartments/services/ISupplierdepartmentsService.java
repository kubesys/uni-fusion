package com.qnkj.core.base.modules.supplier.SupplierDepartments.services;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.services.IBaseService;

import java.util.HashMap;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2021-03-31
 */
public interface ISupplierdepartmentsService extends IBaseService {
    HashMap<String,Object> list();
    Object getTree(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys);
    Object getTree(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys, Boolean isSelect);
    void save(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;
    @Override
    void delete(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;

    HashMap<String,Object> getAllDepartmentLeader();
    HashMap<String,Object> getNameByDepartmentIds(Object departmentids);
}
