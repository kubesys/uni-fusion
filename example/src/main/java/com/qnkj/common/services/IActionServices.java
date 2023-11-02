package com.qnkj.common.services;

import com.qnkj.common.entitys.Action;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface IActionServices {

    void clear();
    void clear(String modulename);
    void clear(String modulename,String actionkey);

    Action get(String modulename,String actionkey);

    Action load(String actionid);

    List<Action> list(String modulename, int moduletype);

    void update(Action action) throws Exception;
}
