package com.qnkj.core.utils;

import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.settings.departments.service.IDepartmentsService;
import com.qnkj.core.base.modules.supplier.SupplierDepartments.services.ISupplierdepartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * create by 徐雁
 * create date 2021/8/16
 */

@Component
public class DepartmentUtils {
    @Autowired
    public IDepartmentsService departmentsService;
    @Autowired
    public ISupplierdepartmentsService supplierdepartmentsService;

    public static DepartmentUtils utils;

    @PostConstruct
    public void init() {
        utils = this;
        utils.departmentsService = this.departmentsService;
        utils.supplierdepartmentsService = this.supplierdepartmentsService;
    }

    public static HashMap<String,Object> getAllDepartments() {
        if(ProfileUtils.isSupplier()){
            return utils.supplierdepartmentsService.list();
        } else {
            return utils.departmentsService.list();
        }
    }

    public static HashMap<String,Object> getAllDepartmentLeader() {
        if(ProfileUtils.isSupplier()){
            return utils.supplierdepartmentsService.getAllDepartmentLeader();
        } else {
            return utils.departmentsService.getAllDepartmentLeader();
        }
    }

    public static String getCurrentDepartmentId() {
        String id = "";
        if(ProfileUtils.isSupplier()){
            id = UserUtils.getSupplierDepartmentId(ProfileUtils.getCurrentProfileId());
        }else {
            id = UserUtils.getDepartmentId(ProfileUtils.getCurrentProfileId());
        }
        return Utils.isEmpty(id) ? "" : id;
    }
    public static String getCurrentDepartmentName() {
        String name = "";
        if(ProfileUtils.isSupplier()){
            name = UserUtils.getSupplierDepartmentName(ProfileUtils.getCurrentProfileId());
        } else {
            name = UserUtils.getDepartmentName(ProfileUtils.getCurrentProfileId());
        }
        return Utils.isEmpty(name) ? "" : name;
    }

    public static HashMap<String, Object> getNameByDepartmentIds(Object departmentids) {
        if(ProfileUtils.isSupplier()) {
            return utils.supplierdepartmentsService.getNameByDepartmentIds(departmentids);
        } else {
            return utils.departmentsService.getNameByDepartmentIds(departmentids);
        }
    }
}
