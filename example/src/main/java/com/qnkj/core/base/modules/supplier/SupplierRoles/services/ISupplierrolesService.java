package com.qnkj.core.base.modules.supplier.SupplierRoles.services;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.services.IBaseService;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2021-04-02
 */
public interface ISupplierrolesService extends IBaseService {

    Object editDetails(Map<String, Object> httpRequest, Model model, BaseEntityUtils viewEntitys);

    void saveRoles(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;

    void saveDetails(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;

    void deleteRoles(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;

    String getIdByName(String rolename);

    HashMap<String, Object> list();

    List<Object> getRoleByName(String rolename);

    List<Object> getRoleById(String record);

    HashMap<String,String> getNameByRoleIds(Object roleids);
}
