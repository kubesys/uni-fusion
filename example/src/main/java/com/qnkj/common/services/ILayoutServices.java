package com.qnkj.common.services;

import com.qnkj.common.entitys.Layout;

import java.util.List;

/**
 * create by 徐雁
 * create date 2021/5/11
 */

public interface ILayoutServices {
    void clear();
    void clear(String modulename);
    List<Layout> list(String modulename);
    void update(Layout layout) throws Exception;
}
