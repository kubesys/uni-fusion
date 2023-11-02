package com.qnkj.common.services;

import com.qnkj.common.entitys.Api;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface IApiServices {

    void clear();
    void clear(String modulename);
    Api get(String modulename);
    void update(Api api) throws Exception;
}
