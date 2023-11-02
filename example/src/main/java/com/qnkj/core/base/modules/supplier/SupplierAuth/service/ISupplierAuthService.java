package com.qnkj.core.base.modules.supplier.SupplierAuth.service;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.supplier.SupplierUsers.entitys.Supplierusers;
import com.qnkj.core.base.services.IBaseService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 * create date 2021/4/9
 * @author clubs
 */

public interface ISupplierAuthService  extends IBaseService {
    void save(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;
    void cancel(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;
    boolean isAuthorizes(String profileid,String authorize);
    List<Supplierusers> getUsersByAuthorize(String authorize);
    Map<String,Object> getProfileByAuthorizes(Object authorize);
    HashMap<String, Object> getAuths();
    HashMap<String, String> getNameByAuthorizes(Object authorizes);
    List<String> getAuthorizesByProfile(String profileid);
}
