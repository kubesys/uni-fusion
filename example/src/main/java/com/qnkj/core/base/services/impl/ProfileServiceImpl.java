package com.qnkj.core.base.services.impl;

import com.github.restapi.XN_Profile;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.github.restapi.models.Profile;
import com.google.common.collect.ImmutableSet;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.CacheBaseEntitys;
import com.qnkj.core.base.entitys.RegisterInfo;
import com.qnkj.core.base.modules.home.BuiltinNotices.utils.MyNoticeUtils;
import com.qnkj.core.base.modules.management.notices.entitys.NoticeLevel;
import com.qnkj.core.base.services.IProfileService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.exception.WebException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Oldhand
 */
@Slf4j
@Service
public class ProfileServiceImpl implements IProfileService {
//
//    private final ShiroRealm shiroRealm;
//
//    public ProfileServiceImpl(ShiroRealm shiroRealm) {
//        this.shiroRealm = shiroRealm;
//    }
    /**
     * 缓存标签
     */
    private  final String tag = "profile";
    /**
     * 类型
     */
    private final  String contentType = "Profile";

    @Override
    public Profile load(String profileid) throws Exception {
        try {
            return XN_Profile.load(profileid, "id", "profile");
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean sign(String profileid, String password) throws Exception {
        return XN_Profile.sign(profileid, password);
    }

    @Override
    public boolean isExist(String type, String value) throws Exception {
        try {
            XN_Query query = XN_Query.create("Profile").tag("profile")
                    .begin(0)
                    .end(1);
            if (type.compareTo("mobile") == 0) {
                query.filter("type", "in", Arrays.asList("admin", "pt","supplier"));
                query.filter("regioncode", "=", "86");
                query.filter("mobile", "=", value);
            } else if (type.compareTo("username") == 0) {
                query.filter("username", "=", value.toLowerCase());
            } else if (type.compareTo("email") == 0) {
                query.filter("type", "in", Arrays.asList("admin", "pt","supplier"));
                query.filter("email", "=", value);
            } else {
                throw new WebException("检测类型输入错误");
            }
            List<Object> contentList = query.execute();
            if (!contentList.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean allowChangeMobile(String profileid, String mobile) throws Exception {
        try {
            XN_Query query = XN_Query.create("Profile").tag("profile")
                    .filter("mobile", "=", mobile)
                    .filter("id", "!=", profileid)
                    .begin(0)
                    .end(1);
            List<Object> contentList = query.execute();
            if (!contentList.isEmpty()) {
                return false;
            }
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
        return true;
    }

    @Override
    public Profile create(RegisterInfo info) throws Exception {
        try {
            Profile profile = XN_Profile.create(info.email, info.password);
            profile.fullname = info.username;
            profile.type = "supplier";
            profile.regioncode = info.regioncode;
            profile.mobile = info.mobile;
            profile.status = true;
            profile.givenname = info.givenname;
            profile.link = "";
            profile.system = info.system;
            profile.browser = info.browser;
            profile.reg_ip = info.regip;
            profile = profile.save("profile");
            CacheBaseEntitys.addProfileName(profile.id,profile.givenname);
            return profile;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Profile findByMobile(String mobile) throws Exception {
        try {
            XN_Query query = XN_Query.create("Profile").tag("profile")
                    .filter("type", "in", Arrays.asList("admin", "pt"))
                    .filter("mobile", "=", mobile)
                    .begin(0)
                    .end(1);
            List<Object> contentList = query.execute();
            if (!contentList.isEmpty()) {
                return (Profile) contentList.get(0);
            }
            query = XN_Query.create("Profile").tag("profile")
                    .filter("type", "=", "supplier")
                    .filter("regioncode", "=", "86")
                    .filter("mobile", "=", mobile)
                    .begin(0)
                    .end(1);
            contentList = query.execute();
            if (contentList.isEmpty()) {
                throw new Exception("找不到用户");
            }
            Profile profileInfo = (Profile) contentList.get(0);
            return profileInfo;
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }


    @Override
    public Profile findByUsername(String username) throws Exception {
        try {
            XN_Query query = XN_Query.create("Profile").tag("profile")
                    .filter("type", "in", Arrays.asList("admin", "pt"))
                    .filter("username", "=", username)
                    .begin(0)
                    .end(1);
            List<Object> contentList = query.execute();
            if (!contentList.isEmpty()) {
                return (Profile) contentList.get(0);
            }
            query = XN_Query.create("Profile").tag("profile")
                    .filter("type", "=", "supplier")
                    .filter("username", "=", username)
                    .begin(0)
                    .end(1);
            contentList = query.execute();
            if (contentList.isEmpty()) {
                throw new Exception("找不到用户");
            }
            return (Profile) contentList.get(0);
        } catch (Exception e) {
            throw new WebException(e.getMessage());
        }
    }

    @Override
    public void updateAvatar(String profileid, String avatar) throws Exception {
        Profile profileInfo = XN_Profile.load(profileid, "id", "profile");
        profileInfo.link = avatar;
        profileInfo.save("profile");
    }

    @Override
    public void updateProfile(String profileid, Map<?,?> info) throws Exception {
        Profile profileInfo = XN_Profile.load(profileid, "id", "profile");
        if (info.containsKey("mobile")) {
            profileInfo.mobile = info.get("mobile").toString();
        }
        if (info.containsKey("regioncode")) {
            profileInfo.regioncode = info.get("regioncode").toString();
        }
        if (info.containsKey("email")) {
            profileInfo.email = info.get("email").toString();
        }
        if (info.containsKey("givenname")) {
            profileInfo.givenname = info.get("givenname").toString();
        }
        if (info.containsKey("link")) {
            profileInfo.link = info.get("link").toString();
        }
        if (info.containsKey("gender")) {
            profileInfo.gender = info.get("gender").toString();
        }
        if (info.containsKey("birthdate")) {
            profileInfo.birthdate = info.get("birthdate").toString();
        }
        if (info.containsKey("province")) {
            profileInfo.province = info.get("province").toString();
        }
        if (info.containsKey("city")) {
            profileInfo.city = info.get("city").toString();
        }
        if (info.containsKey("region")) {
            profileInfo.region = info.get("region").toString();
        }
        if (info.containsKey("country")) {
            profileInfo.country = info.get("country").toString();
        }
        if (info.containsKey("password")) {
            profileInfo.password = info.get("password").toString();
        }
        profileInfo.save("profile");
        if(Boolean.TRUE.equals(ProfileUtils.isSupplier())){
            List<Object> query = XN_Query.contentQuery().tag("supplier_users")
                    .filter("type","eic","supplier_users")
                    .notDelete()
                    .filter("my.profileid","=",profileid)
                    .end(1).execute();
            if(!query.isEmpty()) {
                Content user = (Content) query.get(0);
                user.add("username",profileInfo.givenname)
                        .add("mobile",profileInfo.mobile)
                        .add("mailbox",profileInfo.email)
                        .save("supplier_users");
            }
        } else {
            List<Object> query = XN_Query.contentQuery().tag("users")
                    .filter("type","eic","users")
                    .notDelete()
                    .filter("my.profileid","=",profileid)
                    .end(1).execute();
            if(!query.isEmpty()) {
                Content user = (Content) query.get(0);
                user.add("username",profileInfo.givenname)
                        .add("mobile",profileInfo.mobile)
                        .add("mailbox",profileInfo.email)
                        .save("users");
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getProfileNameById(Object profileids) {
        List<Object> result = new ArrayList<>();
        if(Utils.isEmpty(profileids)) {
            return result;
        }
        List<String> ids = null;
        if (profileids instanceof String) {
            ids = new ArrayList<>(ImmutableSet.of(profileids.toString()));
        } else if (profileids instanceof String[]) {
            ids = new ArrayList<>(Arrays.asList((String[]) profileids));
        } else if (profileids instanceof List) {
            ids = new ArrayList<>((List<String>) profileids);
        }
        if (ids != null) {
            List<String> noFinds = new ArrayList<>(1);
            for(String id: ids) {
                if(Boolean.TRUE.equals(CacheBaseEntitys.hasProfileName(id))) {
                    result.add(CacheBaseEntitys.getProfileName(id));
                } else {
                    noFinds.add(id);
                }
            }
            if(!noFinds.isEmpty()) {
                try {
                    List<Object> list = XN_Profile.loadMany(noFinds, "profile");
                    list.forEach(item -> {
                        result.add(((Profile) item).givenname);
                        CacheBaseEntitys.addProfileName(((Profile) item).id, ((Profile) item).givenname);
                    });
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        return result;
    }

    @Override
    public void updatePassword(String profileid, String password) throws Exception {
        Profile profileInfo = XN_Profile.load(profileid, "id", "profile");
        profileInfo.password = password;
        profileInfo.save("profile");

        String title = "恭喜你成功修改登录密码!";
        String body = "恭喜你成功修改登录密码，下次登录请使用该账号的新密码登录。";
        MyNoticeUtils.profileCreate(profileid,title,body, NoticeLevel.warn);

    }

    @Override
    public void updateMobile(String profileid, String mobile) throws Exception {
        Profile profileInfo = XN_Profile.load(profileid, "id", "profile");
        String oldMobile = profileInfo.mobile;
        profileInfo.mobile = mobile;
        profileInfo.save("profile");
        List<Object> users = XN_Query.contentQuery().tag("users")
                .filter("type", "eic", "users")
                .filter("my.profileid", "=", profileid)
                .notDelete().end(1).execute();
        if(!users.isEmpty()){
            Content user = (Content)users.get(0);
            user.my.put("mobile",mobile);
            user.save("users");
        }
        String title = "恭喜你成功修改手机号码!";
        String body = "恭喜你成功修改手机号码，";
        body += "<br>\n原手机号码：" + oldMobile;
        body += "<br>\n新手机号码：" + mobile;
        MyNoticeUtils.profileCreate(profileid,title,body, NoticeLevel.warn);

    }

    @Override
    public Profile saveOrUpdate(Profile profile) throws Exception {
        Profile result = null;
        if(StringUtils.isEmpty(profile.getId())){
            result = this.insert(profile);
        } else {
            result = this.update(profile);
        }
        return result;
    }

    @Override
    public Profile insert(Profile profile) throws Exception {
        if(null == profile){
            throw new WebException("系统错误:profile is null");
        }
        if("driver".equals(profile.type)){
            profile.fullname = profile.fullname + System.currentTimeMillis();
        }
        String result = this.checkProfile(profile);
        if(StringUtils.isNotBlank(result)){
            throw new WebException(result);
        }
        profile.status = true;
        profile = profile.save(tag);
        return profile;
    }

    @Override
    public Profile update(Profile profile) throws Exception {
        if(null == profile){
            throw new WebException("系统错误:profile is null");
        }
        String result = this.checkProfile(profile);
        if(StringUtils.isNotBlank(result)){
            throw new WebException(result);
        }
        profile = profile.save(tag);
        return profile;
    }


    @Override
    public String checkProfile(Profile profile) throws Exception{
        String result = "";
        String type = profile.type;
        if(StringUtils.isEmpty(type)){
            result = "系统错误:type is null";
            return result;
        }
        //检查项
        String fullname = profile.fullname;
        String email = profile.email;
        String mobile = profile.mobile;
        if(StringUtils.isBlank(profile.id)){
            if(StringUtils.isEmpty(mobile)){
                result = "系统错误:mobile is null";
                return result;
            }
            List<Object> profiles = XN_Query.create(contentType).tag(tag)
                    .filter("type", "=", type)
                    .filter("mobile", "=", mobile)
                    .begin(0)
                    .end(1)
                    .execute();
            if (!profiles.isEmpty() ) {
                result = "手机号码：" + mobile  + "已经注册，不允许重复注册！";
                return result;
            }
            if(StringUtils.isEmpty(fullname)){
                result = "系统错误:fullname is null";
                return result;
            }
            profiles = XN_Query.create(contentType).tag(tag)
                    .filter("type", "=", type)
                    .filter("username", "=", fullname)
                    .begin(0)
                    .end(1)
                    .execute();
            if (!profiles.isEmpty()) {
                result = "账户名称：" + fullname  + "已经注册，不允许重复注册！";
                return result;
            }
            if(StringUtils.isEmpty(email)){
                result = "系统错误:email is null";
                return result;
            }
            profiles = XN_Query.create(contentType).tag(tag)
                    .filter("type", "=", type)
                    .filter("email", "=", email)
                    .begin(0)
                    .end(1)
                    .execute();
            if (!profiles.isEmpty()) {
                result = "账户邮箱：" + email  + "已经注册，不允许重复注册！";
                return result;
            }
        }else {
            if(StringUtils.isEmpty(mobile)){
                result = "系统错误:mobile is null";
                return result;
            }
            List<Object> profiles = XN_Query.create(contentType).tag(tag)
                    .filter("type", "=", type)
                    .filter("mobile", "=", mobile)
                    .filter("id", "!=", profile.id)
                    .begin(0)
                    .end(1)
                    .execute();
            if (!profiles.isEmpty()) {
                result = "手机号码：" + mobile  + "已经注册，不允许重复注册！";
                return result;
            }
            if(StringUtils.isEmpty(fullname)){
                result = "系统错误:fullname is null";
                return result;
            }
            profiles = XN_Query.create(contentType).tag(tag)
                    .filter("type", "=", type)
                    .filter("username", "=", fullname)
                    .filter("id", "!=", profile.id)
                    .begin(0)
                    .end(1)
                    .execute();
            if (!profiles.isEmpty()) {
                result = "账户名称：" + fullname  + "已经注册，不允许重复注册！";
                return result;
            }
            if(StringUtils.isEmpty(email)){
                result = "系统错误:email is null";
                return result;
            }
            profiles = XN_Query.create(contentType).tag(tag)
                    .filter("type", "=", type)
                    .filter("email", "=", email)
                    .filter("id", "!=", profile.id)
                    .begin(0)
                    .end(1)
                    .execute();
            if (!profiles.isEmpty()) {
                result = "账户邮箱：" + email  + "已经注册，不允许重复注册！";
                return result;
            }
        }
        return "";
    }

    @Override
    public String getGivenName(String id) {
        if(Boolean.TRUE.equals(CacheBaseEntitys.hasProfileName(id))) {
            return CacheBaseEntitys.getProfileName(id);
        }
        String givenname = "";
        try {
            Profile profile = XN_Profile.load(id, tag);
            givenname = profile.givenname;
        } catch (Exception e) {
            return  givenname;
        }
        return givenname;
    }

    @Override
    public Map<String, Object> getGivenNames(List<String> ids) {
        Map<String, Object> result = new HashMap<>();
        if (ids != null && !ids.isEmpty()) {
            List<String> noFinds = new ArrayList<>(1);
            for(String id: ids) {
                if(Boolean.TRUE.equals(CacheBaseEntitys.hasProfileName(id))) {
                    result.put(id,CacheBaseEntitys.getProfileName(id));
                } else {
                    noFinds.add(id);
                }
            }
            if(!noFinds.isEmpty()) {
                try {
                    List<Object> list = XN_Profile.loadMany(noFinds, "profile");
                    list.forEach(item -> {
                        result.put(((Profile) item).id, ((Profile) item).givenname);
                        CacheBaseEntitys.addProfileName(((Profile) item).id, ((Profile) item).givenname);
                    });
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        return result;
    }
}
