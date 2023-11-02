package com.qnkj.core.base.modules.settings.users.service;

import com.qnkj.core.base.services.IBaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author clubs
 */
public interface IUsersService extends IBaseService {
    HashMap<String, Object> list();

    List<String> getUserNameById(Object ids);

    List<String> getProfileById(Object ids);

    List<String> getIdByProfile(Object profiles);

    HashMap<String,String> getProfileIdsByName(String name);

    HashMap<String,String> getNameByProfiles(List<String> profileids);

    String getProfileIdByName(String name);

    String getNameByProfileid(String profileid);

    void updateUserRoles(Object profiles, String roleid);

    void chanageAccount(Map<String, Object> httpRequest) throws Exception;

    void modifyPassword(Map<String, Object> httpRequest) throws Exception;

    void save(Map<String, Object> httpRequest) throws Exception;

    void active(Map<String, Object> httpRequest) throws Exception;

    void inactive(Map<String, Object> httpRequest) throws Exception;

    HashMap<String, List<?>> getUsersByDepartment(Object departmentids);

    String getRoleByProfile(String record);

    String getDepartment(String profileid);

    String getDepartmentId(String profileid);

    String getDepartmentName(String departmentid);

    Map<String,Object> getProfileByRoles(Object roles);

    String getDirectSuperior(String profileid);

    List<String> getSuperiorLeaders(String profileid);
    List<String> getSuperiorLeaders(String profileid,Boolean isAll);

    List<String> getAllSubordinate(String profileid);
    List<String> getAllSubordinate(String profileid,Boolean isAll);

    HashMap<String, Object> getLeaderByDepartment(Object departmentids);
}
