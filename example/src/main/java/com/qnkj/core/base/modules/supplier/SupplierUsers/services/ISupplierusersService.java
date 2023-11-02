package com.qnkj.core.base.modules.supplier.SupplierUsers.services;

import com.qnkj.core.base.services.IBaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2021-03-31
 */
public interface ISupplierusersService extends IBaseService {

    HashMap<String, Object> list();

    void save(Map<String, Object> httpRequest) throws Exception;

    void active(Map<String, Object> httpRequest) throws Exception;

    void inactive(Map<String, Object> httpRequest) throws Exception;

    String getRoleByProfile(String record);

    HashMap<String, List<?>> getUsersByDepartment(Object departmentids);

    HashMap<String, Object> getUserNamesByDepartment(Object departmentids);

    String getBossRoleId();

    HashMap<String,String> getProfileIdsByName(String name);

    String getNameByProfileid(String profileid);

    HashMap<String,String> getNameByProfiles(List<String> profileids);

    Boolean isBossUser(String profileid);

    String getDepartmentId(String profileid);

    String getDepartment(String profileid);

    String getDepartmentName(String departmentid);

    Map<String, Object> getProfileByRoles(Object roles);

    String getDirectSuperior(String profileid);

    HashMap<String, Object> getLeaderByDepartment(Object departmentids);

    void chanageAccount(Map<String, Object> httpRequest) throws Exception;

    void modifyPassword(Map<String, Object> httpRequest) throws Exception;
}
