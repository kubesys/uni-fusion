package com.qnkj.core.utils;

import com.qnkj.core.base.modules.settings.authorizes.service.IAuthorizesService;
import com.qnkj.core.base.modules.supplier.SupplierAuth.service.ISupplierAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * create by 徐雁
 */
@Component
public class AuthorizeUtils {
    @Autowired
    private IAuthorizesService authorizesService;
    @Autowired
    private ISupplierAuthService supplierauthService;
    private static AuthorizeUtils authorizeUtils;

    @PostConstruct
    public void init(){
        authorizeUtils = this;
        authorizeUtils.authorizesService = this.authorizesService;
        authorizeUtils.supplierauthService = this.supplierauthService;
    }

    public static boolean isAuthorizes(String profileid,String authorize) {
        if (authorize.contains(",")) {
            List<String> authorizes = Arrays.asList(authorize.split(","));
            for (String item : authorizes) {
                if (authorizeUtils.authorizesService.isAuthorizes(profileid, item)) {
                    return true;
                }
            }
            return false;
        } else {
            return authorizeUtils.authorizesService.isAuthorizes(profileid, authorize);
        }
    }
    public static boolean isSupplierAssistant(String profileid,String authorize) {
        return authorizeUtils.supplierauthService.isAuthorizes(profileid,authorize);
    }
    public static Object getUsersByAuthorize(String authorize) {
        if(Boolean.TRUE.equals(ProfileUtils.isSupplier())){
            return authorizeUtils.supplierauthService.getUsersByAuthorize(authorize);
        }else {
            return authorizeUtils.authorizesService.getUsersByAuthorize(authorize);
        }
    }
    public static Map<String, Object> getProfileByAuthorizes(Object authorize) {
        if(Boolean.TRUE.equals(ProfileUtils.isSupplier())){
            return authorizeUtils.supplierauthService.getProfileByAuthorizes(authorize);
        } else {
            return authorizeUtils.authorizesService.getProfileByAuthorizes(authorize);
        }
    }
    public static Map<String, Object> getAllAuth() {
        if(Boolean.TRUE.equals(ProfileUtils.isSupplier())){
            return authorizeUtils.supplierauthService.getAuths();
        } else {
            return authorizeUtils.authorizesService.getAuths();
        }
    }
    public static Map<String, Object> getAuthorizesByType(String type) {
        if("supplier".equals(type)){
            return authorizeUtils.supplierauthService.getAuths();
        } else if("system".equals(type)){
            return authorizeUtils.authorizesService.getAuths();
        } else if("general".equals(type)){
            HashMap<String, Object> supplierAuthorizes = authorizeUtils.supplierauthService.getAuths();
            Map<String, String> newSupplierAuthorizes = supplierAuthorizes.entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().toString() + " (企业)"
            ));
            HashMap<String, Object> systemAuthorizes = authorizeUtils.authorizesService.getAuths();
            systemAuthorizes.putAll(newSupplierAuthorizes);
            return systemAuthorizes;
        } else {
            return new HashMap<>();
        }
    }

    public static List<String> getAuthorizesByProfileId(String profileid) {
        if(Boolean.TRUE.equals(ProfileUtils.isSupplier())) {
            return authorizeUtils.supplierauthService.getAuthorizesByProfile(profileid);
        } else {
            return authorizeUtils.authorizesService.getAuthorizesByProfile(profileid);
        }
    }

    public static Map<String, String> getNameByAuthorizes(Object authorizes){
        if(Boolean.TRUE.equals(ProfileUtils.isSupplier())) {
            return authorizeUtils.supplierauthService.getNameByAuthorizes(authorizes);
        }else {
            return authorizeUtils.authorizesService.getNameByAuthorizes(authorizes);
        }
    }
}
