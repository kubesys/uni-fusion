package com.qnkj.common.services;

import com.qnkj.common.entitys.EntityLink;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public interface IEntityLinkServices {

    void clear();

    void clear(String modulename);

    EntityLink get(String modulename);

    EntityLink load(String entityid);

    void update(EntityLink entityLink) throws Exception;
}
