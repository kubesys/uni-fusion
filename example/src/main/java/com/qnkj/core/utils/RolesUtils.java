package com.qnkj.core.utils;

import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.settings.roles.service.IRolesService;
import com.qnkj.core.base.modules.settings.users.service.IUsersService;
import com.qnkj.core.base.modules.supplier.SupplierRoles.services.ISupplierrolesService;
import com.qnkj.core.base.modules.supplier.SupplierUsers.services.ISupplierusersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 */
@Component
public class RolesUtils {
    @Autowired
    public IRolesService rolesService;
    @Autowired
    public IUsersService usersService;
    @Autowired
    public ISupplierrolesService supplierRolesService;
    @Autowired
    public ISupplierusersService supplierUsersService;

    public static RolesUtils rolesUtils;

    @PostConstruct
    public void init(){
        rolesUtils = this;
        rolesUtils.rolesService = this.rolesService;
        rolesUtils.usersService = this.usersService;
        rolesUtils.supplierUsersService = this.supplierUsersService;
        rolesUtils.supplierRolesService = this.supplierRolesService;
    }

    public static HashMap<String,Object> getAllRoles() {
        if(ProfileUtils.isSupplier()){
            return rolesUtils.supplierRolesService.list();
        } else {
            return rolesUtils.rolesService.list();
        }
    }

    public static String getRoleidByName(String rolename) {
        if(ProfileUtils.isSupplier() && !(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant())){
            return rolesUtils.supplierRolesService.getIdByName(rolename);
        }else {
            return rolesUtils.rolesService.getIdByName(rolename);
        }
    }

    public static boolean isSupperDelete() {
        return rolesUtils.rolesService.isSupperDelete() && (ProfileUtils.isAdmin() || ProfileUtils.isBoss() || ProfileUtils.isAssistant() || ProfileUtils.isSupplierAssistant());
    }

    public static List<Object> getRoleByName(String rolename) {
        if(ProfileUtils.isSupplier() && !(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant())){
            return rolesUtils.supplierRolesService.getRoleByName(rolename);
        }else {
            return rolesUtils.rolesService.getRoleByName(rolename);
        }
    }

    public static List<Object> getBossRole(String record) {
        return rolesUtils.rolesService.getRoleById(record);
    }

    public static List<Object> getRoleById(String record) {
        if(ProfileUtils.isSupplier() && !(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant())){
            return rolesUtils.supplierRolesService.getRoleById(record);
        }else {
            return rolesUtils.rolesService.getRoleById(record);
        }
    }

    public static List<Object> getRoleByProfile(String profileid) {
        String roleid;
        if(ProfileUtils.isSupplier()){
            roleid = rolesUtils.supplierUsersService.getRoleByProfile(profileid);
        }else {
            roleid = rolesUtils.usersService.getRoleByProfile(profileid);
        }
        return getRoleById(roleid);
    }

    public static Map<String,Object> getProfileByRoles(Object roles) {
        if(ProfileUtils.isSupplier()){
            return rolesUtils.supplierUsersService.getProfileByRoles(roles);
        } else {
            return rolesUtils.usersService.getProfileByRoles(roles);
        }
    }

    public static Map<String,String> getNameByRoleIds(Object roleids) {
        if(ProfileUtils.isSupplier()){
            return rolesUtils.supplierRolesService.getNameByRoleIds(roleids);
        } else {
            return rolesUtils.rolesService.getNameByRoleIds(roleids);
        }
    }
    private static final List<String> SUPPLIER_MODULES = Arrays.asList(
            "SupplierUsers","SupplierAuth","Supplierpicklists","SupplierRoles",
            "SupplierDepartments","Supplierloginlog","Supplieroperationlog");

    public static Boolean isEdit(String modulename) {
        if (SUPPLIER_MODULES.contains(modulename) && ProfileUtils.isSupplier()) {
            return ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant();
        }
        if(ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) {
            return true;
        }
        List<Object> roles = getRoleByProfile(ProfileUtils.getCurrentProfileId());
        for(Object program: roles){
            if(Utils.isEmpty(program)) {
                continue;
            }
            for(Object parenttab: ((HashMap<?,?>)program).keySet()) {
                for (Object module : ((HashMap<?,?>)(((HashMap<?,?>) program).get(parenttab))).keySet()) {
                    if (module.equals(modulename)) {
                        HashMap<String, Object> role = (HashMap) (((HashMap<?,?>)(((HashMap<?,?>) program).get(parenttab)))).get(module);
                        if (!Utils.isEmpty(role.get("isedit"))) {
                            return (Boolean) role.get("isedit");
                        }
                    }
                }
            }
        }
        return false;
    }

    public static Boolean isDelete(String modulename) {
        if (SUPPLIER_MODULES.contains(modulename) && ProfileUtils.isSupplier()) {
            if (ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()){ return true; }
            return false;
        }
        if(ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) {
            return true;
        }
        List<Object> roles = getRoleByProfile(ProfileUtils.getCurrentProfileId());
        for(Object program: roles){
            if(Utils.isEmpty(program)) {
                continue;
            }
            for(Object parenttab: ((HashMap<?,?>)program).keySet()) {
                for (Object module : ((HashMap<?,?>)(((HashMap<?,?>) program).get(parenttab))).keySet()) {
                    if (module.equals(modulename)) {
                        HashMap<String, Object> role = (HashMap) (((HashMap<?,?>)(((HashMap<?,?>) program).get(parenttab)))).get(module);
                        if (!Utils.isEmpty(role.get("isdelete"))) {
                            return (Boolean) role.get("isdelete");
                        }
                    }
                }
            }
        }
        return false;
    }
}
