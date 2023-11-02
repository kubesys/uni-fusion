package com.qnkj.common.services;

import com.qnkj.common.entitys.CustomView;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface ICustomViewServices {

    void clear();
    void clear(String modulename);
    void clear(String modulename, String viewid);

    CustomView get(String modulename, String viewname);

    CustomView load(String customid);

    List<CustomView> list(String modulename);

    List<CustomView> list(String modulename,String profileid);

    void update(CustomView customView) throws Exception;
}
