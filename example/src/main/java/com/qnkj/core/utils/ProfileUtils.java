package com.qnkj.core.utils;

import com.github.restapi.models.Profile;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.entitys.ProfileSettings;
import com.qnkj.core.base.modules.supplier.SupplierUsers.services.ISupplierusersService;
import com.qnkj.core.base.services.IProfileService;
import com.qnkj.core.base.services.IProfileSettingsService;
import com.qnkj.core.base.services.ISessionService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProfileUtils {

    @Autowired
    public IProfileService profileService;
    @Autowired
    public ISupplierusersService supplierusersService;
    @Autowired
    public ISessionService sessionService;
    @Autowired
    public IProfileSettingsService profileSettingsService;

    private static ProfileUtils profileutils;

    @PostConstruct
    public void init() {
        profileutils = this;
        profileutils.profileService = this.profileService;
        profileutils.supplierusersService = this.supplierusersService;
        profileutils.sessionService = this.sessionService;
        profileutils.profileSettingsService = this.profileSettingsService;
    }


    public static int getPageLimit() {
        String profileid = getCurrentProfileId();
        if (!Utils.isEmpty(profileid)) {
            try {
                return profileutils.profileSettingsService.get(profileid).pageLimit;
            }catch (Exception ignored){}
        }
        return 20;
    }
    public static void updatePageLimit(int pagelimit) {
        if (pagelimit < 10) {
            return ;
        }
        if (pagelimit > 200) {
            return ;
        }
        String profileid = getCurrentProfileId();
        if (!Utils.isEmpty(profileid)) {
            try {
                ProfileSettings ps = profileutils.profileSettingsService.get(profileid);
                if (ps.pageLimit != pagelimit) {
                    ps.pageLimit = pagelimit;
                    profileutils.profileSettingsService.updateSettings(profileid,ps);
                }
            }catch (Exception ignored){}
        }
    }

    protected static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    protected static Session getSession() { return getSubject().getSession(); }

    public static Profile getCurrentUser() {
        return (Profile)getSubject().getPrincipal();
    }

    public static Boolean isLogoned() {
        try {
            Profile profile = (Profile) getSubject().getPrincipal();
            if (Utils.isNotEmpty(profile) && Utils.isNotEmpty(profile.id)) {
                return true;
            }
        }catch (Exception ignored) { }
        return false;
    }
    public static String getCurrentProfileId() {
        try {
            Profile profile = (Profile) getSubject().getPrincipal();
            return profile != null ? profile.id : "";
        }catch (Exception e) {
            return "";
        }
    }
    public static String getCurrentProfileName() {
        try {
            Profile profile = (Profile) getSubject().getPrincipal();
            return profile != null ? profile.givenname : "";
        }catch (Exception e) {
            return "";
        }
    }

    public static Boolean isBoss() {
        return profileutils.supplierusersService.isBossUser(getCurrentProfileId());
    }

    public static Boolean isAdmin() {
        Profile profile = (Profile)getSubject().getPrincipal();
        return profile != null && "admin".equals(profile.type);
    }
    public static Boolean isManager() {
        Profile profile = (Profile)getSubject().getPrincipal();
        return profile != null && "pt".equals(profile.type);
    }
    public static Boolean isSupplier() {
        Profile profile = (Profile)getSubject().getPrincipal();
        return profile != null && "supplier".equals(profile.type);
    }
    public static Boolean isAssistant() {
        return AuthorizeUtils.isAuthorizes(getCurrentProfileId(),"adminassistant");
    }
    public static Boolean isSupplierAssistant() {
        return AuthorizeUtils.isAuthorizes(getCurrentProfileId(),"supplierassistant");
    }

    private static Map<String,String> caches = new HashMap<>();
    /**
     * 通过用户ID查找用户名称
     *
     * @param profileid 用户ID
     * @return 用户
     */
    public static String getNameByProfileid(String profileid) {
        try {
            if (Utils.isEmpty(profileid)) {
                return "";
            }
            if (caches.containsKey(profileid)) {
                return caches.get(profileid);
            }
            Profile profile = profileutils.profileService.load(profileid);
            if (Utils.isNotEmpty(profile)) {
                if (Utils.isNotEmpty(profile.givenname)) {
                    caches.put(profileid,profile.givenname);
                    return profile.givenname;
                }
                if (Utils.isNotEmpty(profile.fullname)) {
                    caches.put(profileid,profile.fullname);
                    return profile.fullname;
                }
                caches.put(profileid,profile.mobile);
                return profile.mobile;
            }
        }catch (Exception ignored) {}
        return "";
    }

    /**
     * 通过手机号码查找用户
     *
     * @param mobile 用户ID
     * @return 用户
     */
    public static Profile findByMobile(String mobile) throws Exception {
        return profileutils.profileService.findByMobile(mobile);
    }

    /**
     * 通过用户名查找用户
     *
     * @param username 用户ID
     * @return 用户
     */
    public static Profile findByUsername(String username) throws Exception {
        return profileutils.profileService.findByUsername(username);
    }

    /**
     * 通过用户ID查找用户
     *
     * @param profileid 用户ID
     * @return 用户
     */
    public static Profile load(String profileid) throws Exception {
        return profileutils.profileService.load(profileid);
    }

    public static Object getProfileName(Object profileids) {
        return profileutils.profileService.getProfileNameById(profileids);
    }

    public static Object getProfileGivenNames(List<String> ids) {
        return profileutils.profileService.getGivenNames(ids);
    }

    public static void updateCurrentUser(Profile profile) {
        Subject subject = getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(profile, realmName);
        subject.runAs(newPrincipalCollection);
    }

    public static void login(AuthenticationToken token) {
        getSubject().login(token);
    }

    public static void logout(String profileid) {
        profileutils.sessionService.profileLogout(profileid);
    }

    public static Boolean isLogin(String profileid) {
        return !profileutils.sessionService.getProfileSessions(profileid).isEmpty();
    }
}
