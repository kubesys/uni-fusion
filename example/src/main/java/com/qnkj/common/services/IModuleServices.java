package com.qnkj.common.services;

import com.qnkj.common.entitys.Module;
import com.qnkj.common.entitys.PickList;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/6
 */

public interface IModuleServices {
    Module get(String modulename) throws Exception;

    List<Object> getMenuGroup();

    List<PickList> getAllPickLists();
}
