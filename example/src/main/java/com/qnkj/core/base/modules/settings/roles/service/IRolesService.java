package com.qnkj.core.base.modules.settings.roles.service;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.roles.entity.Roles;
import com.qnkj.core.base.services.IBaseService;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IRolesService extends IBaseService {

    Object editDetails(Map<String, Object> httpRequest, Model model, BaseEntityUtils viewEntitys);

    void saveRoles(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;

    void saveDetails(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;

    void deleteRoles(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;

    String getIdByName(String rolename);

    boolean isSupperDelete();

    HashMap<String,Object> list();

    List<Object> getAllRoles(Roles role);

    List<Object> getRoleByName(String rolename);

    List<Object> getRoleById(String record);

    HashMap<String,String> getNameByRoleIds(Object roleids);
}
