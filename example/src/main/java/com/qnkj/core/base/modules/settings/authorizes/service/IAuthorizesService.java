package com.qnkj.core.base.modules.settings.authorizes.service;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.users.entity.Users;
import com.qnkj.core.base.services.IBaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IAuthorizesService extends IBaseService {
    void save(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;
    void cancel(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;
    boolean isAuthorizes(String profileid,String authorize);
    List<Users> getUsersByAuthorize(String authorize);
    Map<String,Object> getProfileByAuthorizes(Object authorize);
    HashMap<String, Object> getAuths();
    HashMap<String, String> getNameByAuthorizes(Object authorizes);
    List<String> getAuthorizesByProfile(String profileid);
}
