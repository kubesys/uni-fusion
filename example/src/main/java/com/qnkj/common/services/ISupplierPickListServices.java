package com.qnkj.common.services;

import com.qnkj.common.entitys.SupplierPickList;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface ISupplierPickListServices {

    void clear();

    void clear(String supplierid);

    SupplierPickList get(String supplierid,String name);

    List<SupplierPickList> list(String supplierid);

    void update(SupplierPickList pickList) throws Exception;

    void init(String supplierid);
}
