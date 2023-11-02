package com.qnkj.common.services;

import com.qnkj.common.entitys.Tab;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface ITabServices {

    void clear();

    void clear(String modulename);

    Tab get(String modulename);

    Tab load(String tabid);

    List<Tab> load();

    void update(Tab tab) throws Exception;
}
