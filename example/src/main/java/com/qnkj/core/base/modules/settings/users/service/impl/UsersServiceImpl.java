package com.qnkj.core.base.modules.settings.users.service.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Profile;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.github.restapi.models.Profile;
import com.google.common.collect.ImmutableSet;
import com.qnkj.common.utils.CallbackUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.common.utils.ValidationUtil;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.CacheBaseEntitys;
import com.qnkj.core.base.modules.settings.users.entity.Users;
import com.qnkj.core.base.modules.settings.users.service.IUsersService;
import com.qnkj.core.base.services.IPublicService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.utils.RolesUtils;
import com.qnkj.core.webconfigs.exception.WebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UsersServiceImpl implements IUsersService {
    @Override
    public HashMap<String, Object> list() {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<Object> list = XN_Query.contentQuery().tag("users")
                    .filter("type", "eic", "users")
                    .filter("my.status", "=", "Active")
                    .notDelete().end(-1).execute();
            for(Object conn :list) {
                result.put(((Content)conn).get("profileid").toString(),((Content)conn).get("username").toString());
                CacheBaseEntitys.addProfileName(((Content)conn).get("profileid").toString(),((Content)conn).get("username").toString());
            }
        } catch (Exception ignored) {}

        return result;
    }

    private List<String> checkIds(Object ids) {
        List<String> records = null;
        if(ids instanceof String){
            if(!((String)ids).isEmpty()) {
                if (((String) ids).contains(",")) {
                    records = new ArrayList<>(Arrays.asList(((String) ids).split(",")));
                } else {
                    records = new ArrayList<>(ImmutableSet.of((String) ids));
                }
            } else {
                return new ArrayList<>(1);
            }
        }else if(ids instanceof String[]){
            records = new ArrayList<>(Arrays.asList((String[]) ids));
        }else if(ids instanceof List){
            records = new ArrayList<String>((List) ids);
        }
        return records;
    }

    @Override
    public List<String> getUserNameById(Object ids) {
        List<String> records = checkIds(ids);
        if(records != null && !records.isEmpty()){
            try {
                List<?> list = XN_Content.loadMany(records, "users");
                List<String> names = new ArrayList<>();
                list.forEach(item ->
                    names.add(((Content)item).my.get("username").toString())
                );
                records.clear();
                return names;
            }catch (Exception ignored) {}
        }
        return null;
    }

    @Override
    public List<String> getProfileById(Object ids) {
        List<String> records = checkIds(ids);
        if(records != null && !records.isEmpty()){
            try {
                List<?> list = XN_Content.loadMany(records, "users");
                List<String> profiles = new ArrayList<>();
                list.forEach(item ->
                    profiles.add(((Content)item).my.get("profileid").toString())
                );
                records.clear();
                return profiles;
            }catch (Exception ignored) {}
        }
        return new ArrayList<>(1);
    }

    @Override
    public List<String> getIdByProfile(Object profiles) {
        List<String> records = checkIds(profiles);
        if(records != null && !records.isEmpty()){
            try {
                List<Object> list = XN_Query.contentQuery().tag("users")
                        .filter("type", "eic", "users")
                        .filter("my.profileid", "in", records)
                        .notDelete().execute();

                List<String> ids = new ArrayList<>();
                list.forEach(item -> {
                    ids.add(((Content)item).id);
                });
                records.clear();
                return ids;
            }catch (Exception ignored) {}
        }
        return new ArrayList<>(1);
    }

    @Override
    public HashMap<String,String> getProfileIdsByName(String name) {
        if(!Utils.isEmpty(name)) {
            try {
                List<Object> list = XN_Query.contentQuery().tag("users")
                        .filter("type", "eic", "users")
                        .filter("my.username", "like", name)
                        .begin(0).end(20).notDelete()
                        .execute();
                if (!list.isEmpty()) {
                    HashMap<String, String> result = new HashMap<>();
                    for (Object item : list) {
                        if (!Utils.isEmpty(((Content) item).my.get("profileid"))) {
                            result.put(((Content) item).my.get("profileid").toString(), ((Content) item).my.get("username").toString());
                        }
                    }
                    return result;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @Override
    public String getProfileIdByName(String name) {
        if(!Utils.isEmpty(name)) {
            try {
                List<Object> list = XN_Query.contentQuery().tag("users")
                        .filter("type", "eic", "users")
                        .filter("my.username", "=", name)
                        .begin(0).end(20).notDelete()
                        .execute();
                if (!list.isEmpty()) {
                    String profileid = null;
                    for (Object item : list) {
                        if (!Utils.isEmpty(((Content) item).my.get("profileid"))) {
                            profileid = ((Content) item).my.get("profileid").toString();
                        }
                    }
                    return profileid;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @Override
    public String getNameByProfileid(String profileid) {
        if(CacheBaseEntitys.hasProfileName(profileid)) {
            return CacheBaseEntitys.getProfileName(profileid);
        }
        if(!Utils.isEmpty(profileid)) {
            try {
                List<Object> list = XN_Query.contentQuery().tag("users")
                        .filter("type", "eic", "users")
                        .filter("my.profileid", "=", profileid)
                        .notDelete().end(1).execute();
                if (!list.isEmpty()) {
                    if (!Utils.isEmpty(((Content) list.get(0)).my.get("username"))) {
                        CacheBaseEntitys.addProfileName(profileid,((Content) list.get(0)).my.get("username").toString());
                        return ((Content) list.get(0)).my.get("username").toString();
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @Override
    public HashMap<String, String> getNameByProfiles(List<String> profileids) {
        HashMap<String, String> result = new HashMap<>();
        if(!Utils.isEmpty(profileids) && !profileids.isEmpty()) {
            List<String> noFinds = new ArrayList<>(1);
            for(String id: profileids) {
                if(CacheBaseEntitys.hasProfileName(id)) {
                    result.put(id,CacheBaseEntitys.getProfileName(id));
                } else {
                    noFinds.add(id);
                }
            }
            if(!noFinds.isEmpty()) {
                try {
                    List<Object> list = XN_Query.contentQuery().tag("users")
                            .filter("type", "eic", "users")
                            .filter("my.profileid", "in", noFinds)
                            .notDelete().end(-1).execute();
                    if (!list.isEmpty()) {
                        for (Object item : list) {
                            Users users = new Users(item);
                            result.put(users.profileid, users.username);
                            CacheBaseEntitys.addProfileName(users.profileid,users.username);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return result;
    }

    @Override
    public void updateUserRoles(Object profiles, String roleid) {
        List<String> Profiles = null;
        if(profiles != null){
            if (profiles instanceof String) {
                if (((String) profiles).indexOf(",") > 0) {
                    Profiles = Arrays.asList(((String) profiles).split(","));
                } else {
                    Profiles = new ArrayList<>(ImmutableSet.of(profiles.toString()));
                }
            }else if(profiles instanceof String[]){
                Profiles = Arrays.asList((String[]) profiles);
            }else if(profiles instanceof List){
                Profiles = (List<String>) profiles;
            }
            if(Profiles != null && !Profiles.isEmpty()){
                try {
                    List<Object> query = XN_Query.contentQuery().tag("users")
                            .filter("type", "eic", "users")
                            .filter("my.profileid", "in", Profiles)
                            .notDelete().execute();
                    List<Object> Saves = new ArrayList<>();
                    query.forEach(item -> {
                        ((Content)item).my.put("roleid",roleid != null? roleid : "");
                        Saves.add(item);
                    });
                    if(!Saves.isEmpty()){
                        XN_Content.batchsave(Saves,"users");
                    }
                    Saves.clear();
                }catch (Exception ignored) {}
                Profiles.clear();
            }
        }
    }

    @Override
    public void chanageAccount(Map<String, Object> httpRequest) throws Exception {
        try {
            String record = httpRequest.getOrDefault("record", "").toString();
            String account = httpRequest.getOrDefault("account", "").toString();
            List<Object> list = XN_Query.contentQuery().tag("users")
                    .filter("type", "eic", "users")
                    .filter("id", "!=", record)
                    .filter("my.account", "=", account)
                    .notDelete().end(1).execute();
            if (!list.isEmpty()) {
                throw new WebException("账户名称已经存在，请更换后再试。。。");
            }
            Content userConn = XN_Content.load(record, "users");
            Users users = new Users(userConn);
            users.account = account;
            users.deleted = 0;
            users.createnew = 0;
            list = XN_Query.create("Profile").tag("profile")
                    .filter("username", "=", account)
                    .filter("id", "!=", users.profileid)
                    .filter("type", "in", Arrays.asList("admin", "pt", "supplier"))
                    .end(1).execute();
            if (!list.isEmpty()) {
                throw new WebException("账户名称已经存在，请更换后再试。。。");
            }
            Profile profile = XN_Profile.load(users.profileid, "profile");
            profile.fullname = users.account;
            profile.save("profile");
            users.toContent(userConn);
            userConn.save("users");
            ProfileUtils.logout(users.profileid);
        }catch (WebException e) {
            throw e;
        }catch (Exception e) {
            throw new Exception("修改失败");
        }
    }

    @Override
    public void modifyPassword(Map<String, Object> httpRequest) throws Exception {
        try {
            String record = httpRequest.getOrDefault("record", "").toString();
            String password = httpRequest.getOrDefault("password", "").toString();
            ValidationUtil.checkPassword(password);
            Content userConn = XN_Content.load(record, "users");
            Users users = new Users(userConn);
            Profile profile = XN_Profile.load(users.profileid, "profile");
            profile.password = password;
            profile.save("profile");
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void save(Map<String, Object> httpRequest) throws Exception {
        String record = httpRequest.getOrDefault("record","").toString();

        if(!Utils.isEmpty(record)){
            String account = httpRequest.getOrDefault("account","").toString();
            String mobile = httpRequest.getOrDefault("mobile","").toString();
            if(Utils.isEmpty(account)) {
                throw new Exception("账户名称不能为空！");
            }
            if(Utils.isEmpty(mobile)) {
                throw new Exception("手机号码不能为空！");
            }
            List<Object> list = XN_Query.contentQuery().tag("users")
                    .filter("type","eic","users")
                    .notDelete()
                    .filter("id","!=",record)
                    .filter("my.account","=",account)
                    .end(1).execute();
            if(!list.isEmpty()){
                throw new Exception("账户名称已经存在，请更换后再试。。。");
            }
            list = XN_Query.contentQuery().tag("users")
                    .filter("type","eic","users")
                    .notDelete()
                    .filter("id","!=",record)
                    .filter("my.mobile","=",mobile)
                    .end(1).execute();
            if(!list.isEmpty()){
                throw new Exception("手机号码已经存在，请更换后再试。。。");
            }

            List<Object> callback = new ArrayList<>();
            Content userConn = XN_Content.load(record,"users");
            Users users = new Users(userConn);
            String source = users.departmentid;
            users.fromRequest(httpRequest);
            users.deleted = 0;
            users.createnew = 0;
            if(Utils.isEmpty(users.profileid)) {
                String password = httpRequest.getOrDefault("password","").toString();
                if(Utils.isEmpty(password)){
                    throw new Exception("账号密码不能为空");
                }
                password = password.replace(" ","");
                ValidationUtil.checkPassword(password);

                list = XN_Query.create("Profile").tag("profile")
                        .filter("username", "=", account)
                        .filter("type","in",Arrays.asList("admin","pt","supplier"))
                        .end(1).execute();
                if(!list.isEmpty()){
                    throw new Exception("账户名称已经存在，请更换后再试。。。");
                }

                list = XN_Query.create("Profile").tag("profile")
                        .filter("mobile", "=", mobile)
                        .filter("type","in",Arrays.asList("admin","pt","supplier"))
                        .end(1).execute();
                if(!list.isEmpty()){
                    throw new Exception("手机号码已经存在，请更换后再试。。。");
                }
                try {
                    Profile profile = XN_Profile.create(password);
                    profile.fullname = users.account;
                    profile.type = users.is_admin;
                    profile.regioncode = "86";
                    profile.mobile = users.mobile;
                    profile.status = "Active".equals(users.status);
                    profile.givenname = users.username;
                    profile.link = "";
                    profile.system = "pc";
                    profile.browser = "";
                    profile.reg_ip = "";
                    profile.save("profile");
                    users.profileid = profile.id;
                }catch (com.github.restapi.models.WebException e){
                    throw e;
                } catch (Exception e) {
                    throw new Exception("创建用户失败，请稍后再试。。。");
                }
            } else {
                list = XN_Query.create("Profile").tag("profile")
                        .filter("username", "=", account)
                        .filter("id","!=",users.profileid)
                        .filter("type","in",Arrays.asList("admin","pt","supplier"))
                        .end(1).execute();
                if(!list.isEmpty()){
                    throw new Exception("账户名称已经存在，请更换后再试。。。");
                }

                list = XN_Query.create("Profile").tag("profile")
                        .filter("mobile", "=", mobile)
                        .filter("id","!=",users.profileid)
                        .filter("type","in",Arrays.asList("admin","pt","supplier"))
                        .end(1).execute();
                if(!list.isEmpty()){
                    throw new Exception("手机号码已经存在，请更换后再试。。。");
                }

                Profile profile = XN_Profile.load(users.profileid,"profile");
                profile.type = users.is_admin;
                profile.mobile = users.mobile;
                profile.status = "Active".equals(users.status);
                profile.givenname = users.username;
                profile.save("profile");
                CacheBaseEntitys.addProfileName(profile.id,profile.givenname);
                Profile currentUser = ProfileUtils.getCurrentUser();
                if(currentUser.id.equals(profile.id)){
                    ProfileUtils.updateCurrentUser(profile);
                }
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("profileid",users.profileid);
                infoMap.put("source",source);
                infoMap.put("target",users.departmentid);
                callback.add(infoMap);
            }
            users.toContent(userConn);
            userConn.save("users");
            ProfileUtils.logout(users.profileid);
            if(!callback.isEmpty()){
                try {
                    CallbackUtils.invoke("updateDepartments", callback);
                }catch (Exception e){
                    log.error("==========================================================");
                    log.error("回调错误 ： Class：UsersServiceImpl");
                    log.error("回调错误 ： Module：Save");
                    log.error("回调错误 ： invoke：UpdateDepartments");
                    log.error("回调错误 ： param：{}", callback);
                    log.error("回调错误 ： Error：{}", e.getMessage());
                    log.error("==========================================================");
                }
            }
        } else {
            throw new Exception("保存失败，参数错误");
        }
    }

    @Override
    public void active(Map<String, Object> httpRequest) throws Exception {
        Object record = httpRequest.getOrDefault("ids",null);
        if(!Utils.isEmpty(record)){
            if(record instanceof  String){
                record = new ArrayList<>(ImmutableSet.of(record.toString()));
            }
            if(record instanceof List){
                try{
                    List<?> query = XN_Content.loadMany((List<String>) record,"users");
                    if(!query.isEmpty()){
                        List<Object> Saved = new ArrayList<>();
                        List<String> profieids = new ArrayList<>();
                        query.forEach(item -> {
                            if(!("admin".equals(((Content)item).my.get("account")) && "admin".equals(((Content)item).my.get("is_admin")))) {
                                ((Content) item).my.put("status", "Active");
                                profieids.add(((Content) item).my.get("profileid").toString());
                                Saved.add(item);
                            }
                        });
                        if (!profieids.isEmpty()) {
                            query = XN_Profile.loadMany(profieids, "profile");
                            for(Object item : query){
                                ((Profile) item).status = true;
                                ((Profile) item).save("profile");
                            }
                            profieids.clear();
                        }
                        if(!Saved.isEmpty()){
                            XN_Content.batchsave(Saved,"users");
                            Saved.clear();
                        }
                    }
                }catch (Exception e){
                    throw new Exception("启用失败");
                }
            } else {
                throw new Exception("参数错误！启用失败");
            }
        } else {
            throw new Exception("参数错误！启用失败");
        }
    }

    @Override
    public void inactive(Map<String, Object> httpRequest) throws Exception {
        Object record = httpRequest.getOrDefault("ids",null);
        if(!Utils.isEmpty(record)){
            if(record instanceof  String){
                record = new ArrayList<>(ImmutableSet.of(record.toString()));
            }
            if(record instanceof List){
                try {
                    List query = XN_Content.loadMany((List<String>) record, "users");
                    if (!query.isEmpty()) {
                        List<Object> Saved = new ArrayList<>();
                        List<String> profieids = new ArrayList<>();
                        for (Object item : query) {
                            if (!("admin".equals(((Content) item).my.get("account")) && "admin".equals(((Content) item).my.get("is_admin")))) {
                                ((Content) item).my.put("status", "Inactive");
                                profieids.add(((Content) item).my.get("profileid").toString());
                                Saved.add(item);
                            } else {
                                throw new WebException("【" + ((Content) item).my.get("username") + "】不能被停用！");
                            }
                        }
                        if (!profieids.isEmpty()) {
                            query = XN_Profile.loadMany(profieids, "profile");
                            for (Object item : query) {
                                ((Profile) item).status = false;
                                ((Profile) item).save("profile");
                                ProfileUtils.logout(((Profile) item).id);
                            }
                            profieids.clear();
                        }
                        if (!Saved.isEmpty()) {
                            XN_Content.batchsave(Saved, "users");
                            Saved.clear();
                        }
                    }
                }catch (WebException e){
                    throw e;
                }catch (Exception e){
                    throw new Exception("参数错误！启用失败");
                }
            } else {
                throw new Exception("参数错误！启用失败");
            }
        } else {
            throw new Exception("参数错误！启用失败");
        }
    }

    @Override
    public HashMap<String, List<?>> getUsersByDepartment(Object departmentids) {
        List<String> records = null;
        if(departmentids instanceof String){
            if(!((String)departmentids).isEmpty()) {
                if (((String) departmentids).indexOf(",") > 0) {
                    records = new ArrayList<>(Arrays.asList(((String) departmentids).split(",")));
                } else {
                    records = new ArrayList<>(ImmutableSet.of((String) departmentids));
                }
            } else {
                return null;
            }
        }else if(departmentids instanceof String[]){
            records = new ArrayList<>(Arrays.asList((String[]) departmentids));
        }else if(departmentids instanceof List){
            records = new ArrayList<>((List<String>) departmentids);
        }
        if(records != null && !records.isEmpty()) {
            try{
                int page = 0;
                List<Object> query;
                HashMap<String, List<?>> result = new HashMap<>();
                do {
                    query = XN_Query.contentQuery().tag("users")
                            .filter("type", "eic", "users")
                            .filter("my.departmentid", "in", records)
                            .order("my.sequence", "A_N").notDelete()
                            .begin(page * 100).end((page + 1) * 100).execute();
                    query.forEach(item -> {
                        String departmentid = ((Content)item).my.get("departmentid").toString();
                        if(!Utils.isEmpty(result.get(departmentid))){
                            List<Object> users = (List<Object>) result.get(departmentid);
                            Map<String,Object> infoMap = new HashMap<>(1);
                            infoMap.put(((Content)item).my.get("profileid").toString(),((Content)item).my.get("username").toString());
                            users.add(infoMap);
                        } else {
                            Map<String,Object> infoMap = new HashMap<>(1);
                            infoMap.put(((Content)item).my.get("profileid").toString(),((Content)item).my.get("username").toString());
                            result.put(((Content)item).my.get("departmentid").toString(),new ArrayList<>(ImmutableSet.of(infoMap)));
                        }
                    });
                    page++;
                }while (query.size() >= 100);
                return result;
            }catch (Exception ignored) {}
        }
            return null;
    }

    @Override
    public String getRoleByProfile(String profileid) {
        try {
            List<Object> list = XN_Query.contentQuery().tag("users")
                    .filter("type", "eic", "users")
                    .filter("my.profileid", "=", profileid)
                    .notDelete().end(1).execute();
            if(!list.isEmpty()){
                return ((Content)list.get(0)).my.getOrDefault("roleid","").toString();
            }
        } catch (Exception ignored) { }
        return "";
    }

    @Override
    public String getDepartment(String profileid) {
        try {
            List<Object> list = XN_Query.contentQuery().tag("users")
                    .filter("type", "eic", "users")
                    .filter("my.profileid", "=", profileid)
                    .notDelete().end(1).execute();
            if(!list.isEmpty()){
                String departmentid = ((Content)list.get(0)).my.getOrDefault("departmentid","").toString();
                return this.getDepartmentName(departmentid);
            }
        } catch (Exception ignored) { }
        return null;
    }

    @Override
    public String getDepartmentId(String profileid) {
        try {
            List<Object> list = XN_Query.contentQuery().tag("users")
                    .filter("type", "eic", "users")
                    .filter("my.profileid", "=", profileid)
                    .notDelete().end(1).execute();
            if(!list.isEmpty()){
                return ((Content)list.get(0)).my.getOrDefault("departmentid","").toString();
            }
        } catch (Exception ignored) { }
        return null;
    }

    @Override
    public String getDepartmentName(String departmentid) {
        try{
            if(!Utils.isEmpty(departmentid)) {
                Content conn = XN_Content.load(departmentid, "departments");
                return conn.my.get("departmentname").toString();
            }
        }catch (Exception ignored) {}
        return null;
    }

    @Override
    public Map<String, Object> getProfileByRoles(Object roles) {
        Map<String, Object> result = new HashMap<>();
        if(roles instanceof String){
            roles = Arrays.asList(((String) roles).split(","));
        }
        if(roles instanceof List){
            try {
                List<Object> list = XN_Query.contentQuery().tag("users")
                        .filter("type", "eic", "users")
                        .filter("my.roleid", "in", roles)
                        .notDelete().end(-1).execute();
                if(!list.isEmpty()){
                    for(Object item: list){
                        Users users = new Users(item);
                        if(Utils.isEmpty(users.profileid) || Utils.isEmpty(users.username)) {
                            continue;
                        }
                        if(result.containsKey(users.roleid)){
                            if(!((Map)result.get(users.roleid)).containsKey(users.profileid)){
                                ((Map)result.get(users.roleid)).put(users.profileid,users.username);
                            }
                        } else {
                            Map<String,Object> infoMap = new HashMap<>(1);
                            infoMap.put(users.profileid,users.username);
                            result.put(users.roleid, infoMap);
                        }
                    }
                }
            } catch (Exception ignored) { }
        }
        return result;
    }

    @Override
    public String getDirectSuperior(String profileid) {
        try {
            List<Object> list = XN_Query.contentQuery().tag("users")
                    .filter("type", "eic", "users")
                    .filter("my.status","=","Active")
                    .filter("my.profileid", "=", profileid)
                    .notDelete().end(1).execute();
            if(!list.isEmpty()){
                return ((Content)list.get(0)).my.getOrDefault("directsuperior","").toString();
            }
        } catch (Exception ignored) { }
        return null;
    }

    @Override
    public HashMap<String, Object> getLeaderByDepartment(Object departmentids) {
        List<String> ids = new ArrayList<>();
        if(departmentids instanceof String){
            ids.add(departmentids.toString());
        }else if(departmentids instanceof Collection) {
            ids.addAll((Collection) departmentids);
        }
        if(ids.size() <= 0) {
            return null;
        }
        try{
            List<Object> query = XN_Content.loadMany(ids,"departments");
            if(!query.isEmpty()){
                HashMap<String, Object> result = new HashMap<>();
                for(Object item: query){
                    Map<String,Object> leader = new HashMap<>();
                    Object leadership = ((Content)item).get("leadership");
                    Object mainleader = ((Content)item).get("mainleadership");
                    if(Utils.isEmpty(leadership)){
                        leader.put("leadership",new ArrayList<>());
                    } else {
                        if(leadership instanceof String) {
                            leader.put("leadership",new ArrayList<>(Collections.singletonList(leadership)));
                        } else if(leadership instanceof List) {
                            leader.put("leadership",leadership);
                        } else {
                            leader.put("leadership",new ArrayList<>());
                        }
                    }
                    if(Utils.isEmpty(mainleader)){
                        leader.put("mainleader",new ArrayList<>());
                    } else {
                        if(mainleader instanceof String) {
                            leader.put("mainleader",new ArrayList<>(Collections.singletonList(mainleader)));
                        } else if(mainleader instanceof List) {
                            leader.put("mainleader",mainleader);
                        } else {
                            leader.put("mainleader",new ArrayList<>());
                        }
                    }
                    result.put(((Content)item).id,leader);
                }
                return result;
            }
        }catch (Exception ignored) {}
        return null;
    }

    @Override
    public List<String> getSuperiorLeaders(String profileid) {
        return getSuperiorLeaders(profileid,true);
    }

    @Override
    public List<String> getSuperiorLeaders(String profileid,Boolean isAll) {
        HashMap<String,Object> users = new HashMap<>();
        try {
            int page = 1;
            List<Object> query;
            do {
                query = XN_Query.contentQuery().tag("users")
                        .filter("type", "eic", "users")
                        .filter("my.status", "=", "Active")
                        .notDelete().begin((page-1)*100).end(page*100).execute();
                for(Object item: query){
                    if(Utils.isEmpty(((Content)item).get("profileid"))) {
                        continue;
                    }
                    users.put(((Content)item).get("profileid").toString(),item);
                }
                page++;
            }while (query.size() == 100);
        }catch (Exception ignored) {}
        return getSuperiorLeaders(profileid, users, isAll,new ArrayList<>(),new ArrayList<>());
    }
    private List<String> getSuperiorLeaders(String profileid, HashMap<String,Object> users ,Boolean isAll, List<String> directsuperior, List<String> departments) {
        List<String> result = new ArrayList<>();
        if(users.containsKey(profileid)){
            Content item = (Content) users.get(profileid);
            if(!Utils.isEmpty(item.get("directsuperior")) && !directsuperior.contains(item.get("directsuperior").toString())) {
                directsuperior.add(item.get("directsuperior").toString());
                Utils.removeDuplicate(result, item.get("directsuperior"));
                if(isAll) {
                    Utils.removeDuplicate(result,getSuperiorLeaders(item.get("directsuperior").toString(),users,isAll,directsuperior,departments));
                }
            }
            if(!Utils.isEmpty(item.get("departmentid")) && !departments.contains(item.get("departmentid").toString())){
                departments.add(item.get("departmentid").toString());
                HashMap<String, Object> leaders = getLeaderByDepartment(item.get("departmentid"));
                if(!Utils.isEmpty(leaders)){
                    List<String> leaderinfo = new ArrayList<>();
                    Utils.removeDuplicate(leaderinfo,((HashMap)leaders.get(item.get("departmentid"))).get("leadership"));
                    if(!leaderinfo.isEmpty()) {
                        Utils.removeDuplicate(result,leaderinfo);
                        if(isAll) {
                            for (String leaderid : leaderinfo) {
                                Utils.removeDuplicate(result, getSuperiorLeaders(leaderid, users, isAll, directsuperior, departments));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<String> getAllSubordinate(String profileid) {
        return getAllSubordinate(profileid,true);
    }

    @Override
    public List<String> getAllSubordinate(String profileid,Boolean isAll) {
        HashMap<String,Object> users = new HashMap<>();
        try {
            int page = 1;
            List<Object> query;
            do {
                query = XN_Query.contentQuery().tag("users")
                        .filter("type", "eic", "users")
                        .filter("my.status", "=", "Active")
                        .notDelete().begin((page-1)*100).end(page*100).execute();
                for(Object item: query){
                    if(Utils.isEmpty(((Content)item).get("profileid"))) {
                        continue;
                    }
                    users.put(((Content)item).get("profileid").toString(),item);
                }
                page++;
            }while (query.size() == 100);
        }catch (Exception ignored) {}
        return getAllSubordinate(profileid,users,isAll,new ArrayList<>(),new ArrayList<>());
    }
    private List<String> getAllSubordinate(String profileid, HashMap<String,Object> users,Boolean isAll, List<String> directsuperior, List<String> departments) {
        List<String> result = new ArrayList<>();
        if(users.containsKey(profileid)) {
            for(Object item: users.values()){
                Content user = (Content) item;
                if(!Utils.isEmpty(user.get("directsuperior")) && user.get("directsuperior").equals(profileid) && !directsuperior.contains(user.get("profileid").toString())){
                    directsuperior.add(user.get("profileid").toString());
                    Utils.removeDuplicate(result,user.get("profileid"));
                    if(isAll) {
                        Utils.removeDuplicate(result,getAllSubordinate(user.get("profileid").toString(),users,isAll,directsuperior,departments));
                    }
                }
                if(!Utils.isEmpty(user.get("departmentid")) && !departments.contains(user.get("departmentid").toString())){
                    String departmentid = user.get("departmentid").toString();
                    departments.add(departmentid);
                    HashMap<String, Object> leaders = getLeaderByDepartment(departmentid);
                    if(!Utils.isEmpty(leaders.get(departmentid))){
                        HashMap<String,Object> leader = (HashMap<String,Object>) leaders.get(departmentid);
                        if(!Utils.isEmpty(leader.get("leadership")) && ((List<?>)leader.get("leadership")).contains(profileid)){
                            HashMap<String, List<?>> dpu = getUsersByDepartment(departmentid);
                            if(!Utils.isEmpty(dpu.get(departmentid))){
                                for (Object userinfo : dpu.get(departmentid)){
                                    Utils.removeDuplicate(result,((HashMap<?,?>)userinfo).keySet());
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void addQueryFilter(Map<String, Object> Request, BaseEntityUtils viewEntitys, XN_Query query) {
        IPublicService.addSupplierFilter(query);
        if(!Utils.isEmpty(Request.get("departmentid"))){
            query.filter("my.departmentid","=",Request.get("departmentid"));
        }
    }

    @Override
    public Object getActionVerify(String param, String modulename, String record, BaseEntityUtils viewEntitys) {
        if("Active".equals(param) || "Inactive".equals(param)) {
            if(ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) {
                return true;
            }
            return RolesUtils.isEdit(modulename);
        }
        if("Approval".equals(param) || "SubmitOnline".equals(param)){
            try{
                Content info = XN_Content.load(record,viewEntitys.getTabName(),viewEntitys.getDataTypeVal());
                if("-1".equals(info.my.get("deleted")) || !info.author.equals(ProfileUtils.getCurrentProfileId()) || (!Utils.isEmpty(info.my.get("approvalstatus")) && ("1".equals(info.my.get("approvalstatus")) || "2".equals(info.my.get("approvalstatus")) || "4".equals(info.my.get("approvalstatus"))))) {
                    return false;
                }
            }catch (Exception e){
                return false;
            }
            if(ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) {
                return true;
            }
            return RolesUtils.isEdit(modulename);
        }
        if("ChanageAccount".equals(param)) {
            if(ProfileUtils.isAdmin()) {
                return true;
            }
            return false;
        }
        if("ModifyPassword".equals(param)) {
            if(ProfileUtils.isAdmin() ) {
                return true;
            }
            return false;
        }
        return IUsersService.super.getActionVerify(param, modulename, record, viewEntitys);
    }
}
