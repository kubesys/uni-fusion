package com.qnkj.core.base.modules.settings.loginlog.service;

import com.qnkj.core.base.entitys.LoginLog;
import com.qnkj.core.base.services.IBaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * create by 徐雁
 */
public interface ILoginlogService extends IBaseService {

    /**
     * 检查当日登录日志，适合记忆密码，自动产生登录日志
     *
     * @param request 登录日志
     */
    void checkLoginLog(HttpServletRequest request);

    /**
     * 保存登录日志
     *
     * @param loginLog 登录日志
     */
    void saveLoginLog(HttpServletRequest request, LoginLog loginLog);

    /**
     * 获取系统总访问次数
     *
     * @return Long
     */
    Long findTotalVisitCount() throws Exception;

    /**
     * 获取系统今日访问次数
     *
     * @return Long
     */
    Long findTodayVisitCount() throws Exception;

    /**
     * 获取系统今日访问 IP数
     *
     * @return Long
     */
    Long findTodayIp() throws Exception;

    /**
     * 获取系统近七天来的访问记录
     *
     * @param profileid 用户
     * @return 系统近七天来的访问记录
     */
    List<Map<String, Object>> findLastTenDaysVisitCount(String profileid) throws Exception;

    /**
     * 获取上一次登录时间
     *
     * @param profileid 用户
     * @return 时间
     */
    String  getLastLoginTime(String profileid) throws Exception;
}
