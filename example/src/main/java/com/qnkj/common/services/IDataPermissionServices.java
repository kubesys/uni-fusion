package com.qnkj.common.services;

import com.qnkj.common.entitys.DataPermission;

import java.util.List;

/**
 * create by 徐雁
 * create date 2021/12/14
 * create time 3:45 下午
 */

public interface IDataPermissionServices {
    void clear();

    void clear(String modulename);

    DataPermission get(String modulename);

    void updateAll(DataPermission validationRole) throws Exception;

    void update(DataPermission validationRole) throws Exception;

    void update(List<DataPermission> validationRoles) throws Exception;

    void delete(String modulename);
}
