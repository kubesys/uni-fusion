package com.qnkj.core.base.services.impl;


import com.github.restapi.models.Profile;
import com.qnkj.core.base.entitys.ActiveUser;
import com.qnkj.core.base.modules.settings.loginlog.utils.AddressUtil;
import com.qnkj.core.base.services.ISessionService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Oldhand
 */
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements ISessionService {

    private final SessionDAO sessionDAO;

    @Override
    public List<ActiveUser> list() {
        String currentSessionId = (String) SecurityUtils.getSubject().getSession().getId();

        List<ActiveUser> list = new ArrayList<>();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            ActiveUser activeUser = new ActiveUser();
            Profile user;
            SimplePrincipalCollection principalCollection;
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            } else {
                principalCollection = (SimplePrincipalCollection) session
                        .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                user = (Profile) principalCollection.getPrimaryPrincipal();
                activeUser.setUsername(user.givenname);
                activeUser.setUserId(user.id);
            }
            activeUser.setId((String) session.getId());
            activeUser.setHost(session.getHost());
            activeUser.setStartTimestamp(session.getStartTimestamp().toString());
            activeUser.setLastAccessTime(session.getLastAccessTime().toString());
            long timeout = session.getTimeout();
            activeUser.setStatus(timeout == 0L ? "0" : "1");
            String address = AddressUtil.getCityInfo(activeUser.getHost());
            activeUser.setLocation(address);
            activeUser.setTimeout(timeout);
            if (currentSessionId.equals(activeUser.getId())) {
                activeUser.setCurrent(true);
            }
            list.add(activeUser);
        }
        return list;
    }

    @Override
    public void forceLogout(Serializable sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        session.setTimeout(0);
        session.stop();
        sessionDAO.delete(session);
    }

    @Override
    public List<Session> getProfileSessions(String profileid) {
        List<Session> result = new ArrayList<>();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        for (Session session : sessions) {
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            }
            SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            Profile profile = (Profile) principalCollection.getPrimaryPrincipal();
            if(profile.id.equals(profileid)) {
                result.add(session);
            }
        }
        return result;
    }

    @Override
    public void profileLogout(String profileid) {
        List<Session> sessions = getProfileSessions(profileid);
        if(!sessions.isEmpty()){
            for(Session session: sessions){
                forceLogout(session.getId());
            }
        }
    }
}
