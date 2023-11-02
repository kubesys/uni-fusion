package com.qnkj.core.utils;

import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.settings.users.service.IUsersService;
import com.qnkj.core.base.modules.supplier.SupplierUsers.services.ISupplierusersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("unchecked")
public class UserUtils {
    @Autowired
    public IUsersService usersService;
    @Autowired
    public ISupplierusersService supplierusersService;

    private static UserUtils userUtils;

    @PostConstruct
    public void init() {
        userUtils = this;
        userUtils.usersService = this.usersService;
        userUtils.supplierusersService = this.supplierusersService;
    }

    public static HashMap<String, Object> getAllUsers() {
        if(ProfileUtils.isSupplier()){
            return userUtils.supplierusersService.list();
        } else {
            return userUtils.usersService.list();
        }
    }

    public static HashMap<String,String> getProfileidsByName(String name){
        return userUtils.usersService.getProfileIdsByName(name);
    }

    public static String getProfileIdByName(String name){
        return userUtils.usersService.getProfileIdByName(name);
    }

    public static String getNameByProfileid(String profileId) {
        return userUtils.usersService.getNameByProfileid(profileId);
    }

    public static HashMap<String,String> getNameByProfiles(List<String> profileids) {
        if(ProfileUtils.isSupplier()){
            return userUtils.supplierusersService.getNameByProfiles(profileids);
        } else {
            return userUtils.usersService.getNameByProfiles(profileids);
        }
    }

    /**
     * 获取用户的ID
     * @param name 用户的名称(模糊查询，可能返回多个数据)
     * @return 用户的ID
     */
    public static HashMap<String,String> getProfilesByName(String name) {
        if(ProfileUtils.isSupplier()){
            return userUtils.supplierusersService.getProfileIdsByName(name);
        } else {
            return userUtils.usersService.getProfileIdsByName(name);
        }
    }

    public static String getNameByProfile(String profileId) {
        if(ProfileUtils.isSupplier()) {
            return userUtils.supplierusersService.getNameByProfileid(profileId);
        } else {
            return userUtils.usersService.getNameByProfileid(profileId);
        }
    }

    /**
     * 获取企业用户的ID
     * @param name 企业用户名称(模糊查询，可能返回多个数据)
     * @return 用户的ID
     */
    public static HashMap<String,String> getSupplierProfileidsByName(String name){
        return userUtils.supplierusersService.getProfileIdsByName(name);
    }

    /**
     * 获取企业用户的名称
     * @param profileId 企业用户的ID
     * @return 用户的名称
     */
    public static String getNameBySupplierProfileid(String profileId) {
        return userUtils.supplierusersService.getNameByProfileid(profileId);
    }

    /**
     * 获取指定用户的直接上级
     */
    public static String getDirectLeader(String profileid) {
        if(ProfileUtils.isSupplier()){
            return userUtils.supplierusersService.getDirectSuperior(profileid);
        } else {
            return userUtils.usersService.getDirectSuperior(profileid);
        }
    }

    public static String getDirectSuperior(String profileid) {
        return userUtils.usersService.getDirectSuperior(profileid);
    }

    public static String getSupplierDirectSuperior(String profileid) {
        return userUtils.supplierusersService.getDirectSuperior(profileid);
    }

    /**
     * 通过ProfileID获取所在部门ID
     */
    public static String getDepartmentId(String profileid) {
        return userUtils.usersService.getDepartmentId(profileid);
    }

    public static String getSupplierDepartmentId(String profileid) {
        return userUtils.supplierusersService.getDepartmentId(profileid);
    }

    /**
     * 通过ProfileID获取所在部门名称
     */
    public static String getDepartmentName(String profileid) {
        return userUtils.usersService.getDepartment(profileid);
    }
    public static String getSupplierDepartmentName(String profileid) {
        return userUtils.supplierusersService.getDepartment(profileid);
    }

    /**
     * 根据部门ID获取部门名称
     */
    public static String getDepartmentNameById(String departmentid) {
        return userUtils.usersService.getDepartmentName(departmentid);
    }
    public static String getSupplierDepartmentNameById(String departmentid) {
        return userUtils.supplierusersService.getDepartmentName(departmentid);
    }
    /**
     * 根据部门ID获取部门的所有用户
     * @param departmentids 部门ID或部门ID集合；String或List<String>
     * @return { departmentid : {profileid:"",username:""}}
     */
    public static HashMap<String, List<?>> getUsersByDepartments(Object departmentids) {
        if(ProfileUtils.isSupplier()) {
            return userUtils.supplierusersService.getUsersByDepartment(departmentids);
        } else {
            return userUtils.usersService.getUsersByDepartment(departmentids);
        }
    }

    public static HashMap<String, List<?>> getUsersByDepartment(Object departmentids) {
        return userUtils.usersService.getUsersByDepartment(departmentids);
    }

    public static HashMap<String, List<?>> getUsersBySupplierDepartment(Object departmentids) {
        return userUtils.supplierusersService.getUsersByDepartment(departmentids);
    }

    /**
     * 获取指定用户的部门领导和主管领导
     * @return { profileid : {leadership:[],mainleader:[]}}
     */
    public static HashMap<String, Object> getLeaderByProfile(Object profileids) {
        HashMap<String, String> depids = new HashMap<>();
        if(profileids instanceof String){
            String depid = getDepartmentId(profileids.toString());
            if(!Utils.isEmpty(depid)){
                depids.put(profileids.toString(),depid);
            }
        }else if(profileids instanceof List){
            for(Object item: (List<?>)profileids){
                String depid = getDepartmentId(item.toString());
                if(!Utils.isEmpty(depid)){
                    depids.put(item.toString(),depid);
                }
            }
        }
        HashMap<String, Object> result = new HashMap<>();
        if(!depids.isEmpty()) {
            HashMap<String, Object> Leaders = getLeaderByDepartment(depids.values());
            for (Object item : depids.keySet()) {
                String depid = depids.get(item.toString());
                String Superior = getDirectSuperior(item.toString());
                if (!Utils.isEmpty(Leaders) && Leaders.containsKey(depid)) {
                    ((HashMap<String,String>)Leaders.get(depid)).put("superior",Superior);
                    result.put(item.toString(), Leaders.get(depid));
                }else if(!Utils.isEmpty(Superior)){
                    Map<String,Object> info = new HashMap<>(1);
                    info.put("superior",Superior);
                    result.put(item.toString(), info);
                }
            }
        } else {
            if(profileids instanceof String){
                String Superior = getDirectSuperior(profileids.toString());
                if(Utils.isEmpty(Superior)){
                    Map<String,Object> info = new HashMap<>(1);
                    info.put("superior",Superior);
                    result.put(profileids.toString(), info);
                }
            }else if(profileids instanceof List){
                for(Object item: (List<?>)profileids){
                    String Superior = getDirectSuperior(item.toString());
                    if(Utils.isEmpty(Superior)){
                        Map<String,Object> info = new HashMap<>(1);
                        info.put("superior",Superior);
                        result.put(item.toString(), info);
                    }
                }
            }
        }
        return result;
    }

    public static HashMap<String, Object> getLeaderBySupplierProfile(Object profileids) {
        HashMap<String, String> depids = new HashMap<>();
        if(profileids instanceof String){
            String depid = getSupplierDepartmentId(profileids.toString());
            if(!Utils.isEmpty(depid)){
                depids.put(profileids.toString(),depid);
            }
        }else if(profileids instanceof List){
            for(Object item: (List<?>)profileids){
                String depid = getSupplierDepartmentId(item.toString());
                if(!Utils.isEmpty(depid)){
                    depids.put(item.toString(),depid);
                }
            }
        }
        HashMap<String, Object> result = new HashMap<>();
        if(!depids.isEmpty()) {
            HashMap<String, Object> leaders = getLeaderBySupplierDepartment(depids.values());
            for (Object item : depids.keySet()) {
                String depid = depids.get(item.toString());
                String superior = getSupplierDirectSuperior(item.toString());
                if (!Utils.isEmpty(leaders) && leaders.containsKey(depid)) {
                    ((HashMap<String,String>)leaders.get(depid)).put("superior",superior);
                    result.put(item.toString(), leaders.get(depid));
                }else if(!Utils.isEmpty(superior)){
                    Map<String, Object> info = new HashMap<>(1);
                    info.put("superior",superior);
                    result.put(item.toString(), info);
                }
            }
            return result;
        } else {
            if(profileids instanceof String){
                String superior = getSupplierDirectSuperior(profileids.toString());
                if(Utils.isEmpty(superior)){
                    Map<String, Object> info = new HashMap<>(1);
                    info.put("superior",superior);
                    result.put(profileids.toString(), info);
                }
            }else if(profileids instanceof List){
                for(Object item: (List<?>)profileids){
                    String superior = getSupplierDirectSuperior(item.toString());
                    if(Utils.isEmpty(superior)){
                        Map<String, Object> info = new HashMap<>(1);
                        info.put("superior",superior);
                        result.put(item.toString(), info);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取指定部门的领导
     * @return { departmentid : {leadership:[],mainleader:[]}}
     */
    public static HashMap<String, Object> getLeaderByDepartments(Object departmentids) {
        if(ProfileUtils.isSupplier()){
            return userUtils.supplierusersService.getLeaderByDepartment(departmentids);
        } else {
            return userUtils.usersService.getLeaderByDepartment(departmentids);
        }
    }

    public static HashMap<String, Object> getLeaderByDepartment(Object departmentids) {
        return userUtils.usersService.getLeaderByDepartment(departmentids);
    }

    public static HashMap<String, Object> getLeaderBySupplierDepartment(Object departmentids) {
        return userUtils.supplierusersService.getLeaderByDepartment(departmentids);
    }

    /**
     * 获取指定用户的所有上级领导
     */
    public static List<String> getSuperiorLeaders(String profileid) {
        return userUtils.usersService.getSuperiorLeaders(profileid);
    }
    public static List<String> getSuperiorLeaders(String profileid,Boolean isAll) {
        return userUtils.usersService.getSuperiorLeaders(profileid,isAll);
    }

    /**
     * 获取指定用户的所有下级
     */
    public static List<String> getAllSubordinate(String profileid) {
        return userUtils.usersService.getAllSubordinate(profileid);
    }
    public static List<String> getAllSubordinate(String profileid,Boolean isAll) {
        return userUtils.usersService.getAllSubordinate(profileid,isAll);
    }
}
