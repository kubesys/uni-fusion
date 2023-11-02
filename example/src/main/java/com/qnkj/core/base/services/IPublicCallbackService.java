package com.qnkj.core.base.services;

import com.github.restapi.models.Profile;
import com.qnkj.core.base.modules.settings.supplier.entitys.Supplier;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.utils.ProfileUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * create by 徐雁
 * create date 2020/11/20
 * @author clubs
 */

public interface IPublicCallbackService {

    default Boolean getManagementMenuIsHidden() {
        return false;
    }

    default Object loginCallback(Profile profile) throws Exception {
        return true;
    }

    default void loginedCallback(Profile profile) throws Exception {}

    default String getHomePage(Profile profile) throws Exception {
        return  "/base/index";
    }

    default String menuCallback(HashMap<String, String> params) throws Exception {
        if(ProfileUtils.isSupplier()){
            if(Objects.requireNonNull(SupplierUtils.getSupplierInfo()).approvalstatus.equals(2)) {
                return "企业权限";
            } else {
                return "未审核企业权限";
            }
        }
        return "标准读写权限";
    }

    default void registerCallback(Supplier info) throws Exception {}

    /**
     * 部门修改回调事件
     * @param params Object：HashMap类型;
     *               profileid：用户ID;
     *               source：原部门ID，如没有为空字符串;
     *               target：新部门ID，如没有为空字符串；
     */
    default void updateDepartments(ArrayList<Object> params) {}


    default HashMap<String,String> shiroCallback() throws Exception {
        return new HashMap<>(1);
    }

    default List<HashMap<String,String>> getFriendlyLinks() throws Exception {
        return new ArrayList<>(1);
    }

}
