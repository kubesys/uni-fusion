package com.qnkj.core.base.modules.management.announcements.services;

import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.management.announcements.entitys.Announcements;
import com.qnkj.core.base.services.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * create by Auto Generator
 * create date 2020-11-17
 * @author generator
 */
public interface IAnnouncementsService extends IBaseService {

    /**
     * 获取公告信息
     * @return 返回List<Announcements>对像
     * @exception Exception
     */
    List<Announcements> get() throws Exception;

    /**
     * 获取公告详情
     *
     */
    Announcements get(String announcementid) throws Exception;


    void setTop(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;

    void cancelRelease(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception;

}
