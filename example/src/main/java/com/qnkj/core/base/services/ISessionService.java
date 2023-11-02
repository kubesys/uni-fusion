package com.qnkj.core.base.services;

import com.qnkj.core.base.entitys.ActiveUser;
import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.List;

/**
 * @author Oldhand
 */
public interface ISessionService {

    /**
     * 获取在线用户列表
     *
     * @return List<ActiveUser>
     */
    List<ActiveUser> list();

    /**
     * 踢出用户
     *
     * @param sessionId sessionId
     */
    void forceLogout(Serializable sessionId);

    /**
     * 获取指定用户ID的Session
     */
    List<Session> getProfileSessions(String profileid);

    /**
     * 踢出指定的用户ID
     */
    void profileLogout(String profileid);
}
