package com.qnkj.core.base.modules.management.notices.services;

import com.qnkj.core.base.modules.management.notices.entitys.NoticeLevel;
import com.qnkj.core.base.modules.management.notices.entitys.NoticeType;
import com.qnkj.core.base.services.IBaseService;

import java.util.HashMap;

/**
 * create by Auto Generator
 * create date 2020-11-17
 */
public interface INoticesService extends IBaseService {

    /**
     * 获取通知信息
     *
     */
    HashMap<String,Object> get(String profileid, int page, int limit) throws Exception;

    /**
     * 获取客户的通知信息
     *
     */
    HashMap<String,Object> getSupplierNotices(String supplierid, String profileid, int page, int limit) throws Exception;


    /**
     * 获取个人与审核的通知信息
     *
     */
    HashMap<String,Object> getApprovalNotices(String profileid, int page, int limit) throws Exception;


    /**
     * 获取个人的通知信息
     *
     */
    HashMap<String,Object> getProfileNotices(String profileid, int page, int limit) throws Exception;

    /**
     * 获取系统的通知信息
     *
     */
    HashMap<String,Object> getSystemNotices(String profileid,int page, int limit) throws Exception;

    /**
     * 获取未读通知数量
     *
     */
    long getSupplierUnReadCount(String supplierid, String profileid) throws Exception;

    /**
     * 获取未读通知数量
     *
     */
    long getApprovalUnReadCount(String profileid) throws Exception;


    /**
     * 获取未读通知数量
     *
     */
    long getProfileUnReadCount(String profileid) throws Exception;


    /**
     * 获取未读通知数量
     *
     */
    long getSystemUnReadCount(String profileid) throws Exception;

    /**
     * 通知修改为已读操作
     *
     */
    void update(String noticeid) throws Exception;

    /**
     * 创建通知
     *
     */
    void create(String supplierid, String profileid, String publisher,NoticeType noticetype, NoticeLevel noticelevel, String title, String body, String md5) throws Exception;

    /**
     * 通过MD5唯一键来查询通知是否已经创建
     *
     */
    Boolean exist(String md5) throws Exception;
}
