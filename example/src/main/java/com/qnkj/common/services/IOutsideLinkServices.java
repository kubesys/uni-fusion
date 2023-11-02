package com.qnkj.common.services;

import com.qnkj.common.entitys.OutsideLink;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface IOutsideLinkServices {

    void clear();
    void clear(String modulename);
    void clear(String modulename,String fieldname);

    OutsideLink get(String modulename, String field);

    OutsideLink load(String linkid);

    List<OutsideLink> list(String modulename);

    void update(OutsideLink outsideLink) throws Exception;

    List<OutsideLink> getRelMeLinkModules(String moduleName);
}
