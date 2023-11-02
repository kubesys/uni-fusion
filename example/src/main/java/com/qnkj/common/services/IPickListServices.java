package com.qnkj.common.services;

import com.qnkj.common.entitys.PickList;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface IPickListServices {

    void clear();

    Boolean existPickList(String name);

    PickList get(String name);

    List<PickList> list();

    void update(PickList pickList) throws Exception;
}
