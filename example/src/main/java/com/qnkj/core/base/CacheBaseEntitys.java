package com.qnkj.core.base;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * create by 徐雁
 * create date 2021/7/20
 */

@Component
public class CacheBaseEntitys {
    private CacheBaseEntitys() {}

    static HashMap<String,BaseEntityUtils> cacheEntitys = new LinkedHashMap<>(1);
    static HashMap<String,List<Object>> cacheRoles = new LinkedHashMap<>(1);
    static HashMap<String, String> cacheProfiles = new LinkedHashMap<>(1);

    public static void setEntitys(BaseEntityUtils entity) {
        cacheEntitys.put(entity.getModuleName(),entity);
    }

    public static BaseEntityUtils getEntitys(String modulename) {
        return cacheEntitys.getOrDefault(modulename,null);
    }

    public static void clear() {
        cacheEntitys.clear();
        cacheRoles.clear();
    }
    public static void clear(String modulename) {
        cacheEntitys.remove(modulename);
    }

    public static void addRoles(String roleid, List<Object> roles) {
        cacheRoles.put(roleid,roles);
    }

    public static List<Object> getRoles(String roleid) {
        if(cacheRoles.containsKey(roleid)){
            return cacheRoles.get(roleid);
        } else {
            return new ArrayList<>();
        }
    }

    public static void clearRoles(String roleid) {
        cacheRoles.remove(roleid);
    }

    public static Boolean hasRoles(String roleid) {
        return cacheRoles.containsKey(roleid);
    }

    public static String getProfileName(String profileid) {
        return cacheProfiles.getOrDefault(profileid, null);
    }

    public static void addProfileName(String profileid, String profilename) {
        cacheProfiles.put(profileid,profilename);
    }

    public static Boolean hasProfileName(String profileid) {
        return cacheProfiles.containsKey(profileid);
    }
}
