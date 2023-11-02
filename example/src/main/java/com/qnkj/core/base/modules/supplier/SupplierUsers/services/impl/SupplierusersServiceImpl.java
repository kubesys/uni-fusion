package com.qnkj.core.base.modules.supplier.SupplierUsers.services.impl;

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
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.base.modules.settings.users.entity.Users;
import com.qnkj.core.base.modules.supplier.SupplierUsers.entitys.Supplierusers;
import com.qnkj.core.base.modules.supplier.SupplierUsers.services.ISupplierusersService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.utils.RolesUtils;
import com.qnkj.core.webconfigs.exception.WebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * create by Auto Generator
 * create date 2021-03-31
 */

@Slf4j
@Service
public class SupplierusersServiceImpl implements ISupplierusersService {
    private static Map<String,Boolean> caches = new HashMap<>();

    @Override
    public HashMap<String, Object> list() {
        HashMap<String,Object> result = new HashMap<>();
        try {
            List<Object> list = XN_Query.contentQuery().tag("supplier_users")
                    .filter("type","eic","supplier_users")
                    .notDelete()
                    .filter("my.status","=","Active")
                    .end(-1).execute();
            for(Object conn :list) {
                CacheBaseEntitys.addProfileName(((Content)conn).get("profileid").toString(),((Content)conn).get("username").toString());
                result.put(((Content)conn).get("profileid").toString(),((Content)conn).get("username").toString());
            }
        } catch (Exception ignored) {}

        return result;
    }

    @Override
    public void save(Map<String, Object> httpRequest) throws Exception {
        caches.clear();
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
            List<Object> list = XN_Query.contentQuery().tag("supplier_users")
                    .filter("type","eic","supplier_users")
                    .notDelete()
                    .filter("id","!=",record)
                    .filter("my.account","=",account)
                    .end(1).execute();
            if(!list.isEmpty()){
                throw new Exception("账户名称已经存在，请更换后再试。。。");
            }
            list = XN_Query.contentQuery().tag("supplier_users")
                    .filter("type","eic","supplier_users")
                    .notDelete()
                    .filter("id","!=",record)
                    .filter("my.mobile","=",mobile)
                    .end(1).execute();
            if(!list.isEmpty()){
                throw new Exception("手机号码已经存在，请更换后再试。。。");
            }

            List<Object> callback = new ArrayList<>();
            Content userConn = XN_Content.load(record,"supplier_users");
            Supplierusers users = new Supplierusers(userConn);
            String source = users.departmentid;
            users.fromRequest(httpRequest);
            users.deleted = 0;
            users.createnew = 0;
            users.supplierid = SupplierUtils.getSupplierid();
            if(Utils.isEmpty(users.profileid)) {
                String password = httpRequest.getOrDefault("password","").toString();
                if(Utils.isEmpty(password)){
                    throw new Exception("账号密码不能为空");
                }
                password = password.replace(" ","");
                ValidationUtil.checkPassword(password);
                list = XN_Query.create("Profile").tag("profile")
                        .filter("username", "=", account)
                        .filter("type","in", Arrays.asList("admin","pt","supplier"))
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
                    profile.type = "supplier";
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
                profile.type = "supplier";
                profile.mobile = users.mobile;
                profile.status = "Active".equals(users.status);
                profile.givenname = users.username;
                profile.save("profile");
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("profileid",users.profileid);
                infoMap.put("source",source);
                infoMap.put("target",users.departmentid);
                callback.add(infoMap);
            }
            users.toContent(userConn);
            userConn.save("supplier_users");
            ProfileUtils.logout(users.profileid);
            if(!callback.isEmpty()){
                try {
                    CallbackUtils.invoke("updateDepartments", callback);
                }catch (Exception e){
                    log.error("==========================================================");
                    log.error("回调错误 ： Class：SupplierusersServiceImpl");
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
                    List<Object> query = XN_Content.loadMany((List<String>) record,"supplier_users");
                    if(!query.isEmpty()){
                        List<Object> saved = new ArrayList<>();
                        List<String> profieids = new ArrayList<>();
                        query.forEach(item -> {
                            ((Content)item).my.put("status","Active");
                            profieids.add(((Content)item).my.get("profileid").toString());
                            saved.add(item);
                        });
                        if (!profieids.isEmpty()) {
                            query = XN_Profile.loadMany(profieids, "profile");
                            for(Object item : query){
                                ((Profile) item).status = true;
                                ((Profile) item).save("profile");
                            }
                            profieids.clear();
                        }
                        if(!saved.isEmpty()){
                            XN_Content.batchsave(saved,"supplier_users");
                            saved.clear();
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
                try{
                    List<?> query = XN_Content.loadMany((List<String>) record,"supplier_users");
                    if(!query.isEmpty()){
                        List<Object> saved = new ArrayList<>();
                        List<String> profieids = new ArrayList<>();
                        for(Object item: query){
                            if (!("boss".equals(((Content) item).my.get("usertype")))) {
                                ((Content)item).my.put("status","Inactive");
                                profieids.add(((Content)item).my.get("profileid").toString());
                                saved.add(item);
                            } else {
                                throw new WebException("【" + ((Content) item).my.get("username") + "】管理员不能被停用！");
                            }
                        }
                        query = XN_Profile.loadMany(profieids, "profile");
                        for(Object item : query){
                            ((Profile) item).status = false;
                            ((Profile) item).save("profile");
                            ProfileUtils.logout(((Profile) item).id);
                        }
                        profieids.clear();
                        if(!saved.isEmpty()){
                            XN_Content.batchsave(saved,"supplier_users");
                            saved.clear();
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
    public HashMap<String, Object> getUserNamesByDepartment(Object departmentids) {
        HashMap<String, String> usernames = new HashMap<>();
        Map<String,List<String>> userDatas = new HashMap<>();
        try {
            List<Object> users = XN_Query.contentQuery().tag("supplier_users")
                    .filter("type", "eic", "supplier_users")
                    .filter("my.status ", "=", "Active")
                    .filter("my.supplierid", "=", SupplierUtils.getSupplierid())
                    .order("my.sequence", "A_N")
                    .notDelete().end(-1).execute();
            for (Object item : users) {
                Content user = (Content) item;
                String profileid = user.my.getOrDefault("profileid", "").toString();
                String departmentid = user.my.getOrDefault("departmentid", "").toString();
                String username = user.my.getOrDefault("username", "").toString();
                if (Utils.isEmpty(profileid) || Utils.isEmpty(departmentid) || Utils.isEmpty(username)) {
                    continue;
                }
                usernames.put(profileid, username);
                List<String> lists;
                if (userDatas.containsKey(departmentid)) {
                    lists = userDatas.get(departmentid);
                } else {
                    lists = new ArrayList<>();
                }
                lists.add(username);
                userDatas.put(departmentid, lists);
            }
        }catch (Exception ignored) {}
        HashMap<String,Object> infoMap = new HashMap<>(1);
        infoMap.put("usernames",usernames);
        infoMap.put("userdatas",userDatas);
        return infoMap;
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
                HashMap<String, List<?>> result = new HashMap<>(1);
                do {
                    query = XN_Query.contentQuery().tag("supplier_users")
                            .filter("type", "eic", "supplier_users")
                            .filter("my.departmentid", "in", records)
                            .filter("my.supplierid", "=", SupplierUtils.getSupplierid())
                            .notDelete().order("my.sequence", "A_N")
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
                }while (query.size() == 100);
                return result;
            }catch (Exception ignored) {}
        }
        return null;
    }

    @Override
    public String getBossRoleId(){
        try{
            List<Object> users = XN_Query.contentQuery().tag("supplier_users")
                    .filter("type", "eic", "supplier_users")
                    .filter("my.status ", "=", "Active")
                    .filter("my.usertype ", "=", "boss")
                    .filter("my.approvalstatus ", "in", Arrays.asList("2","4"))
                    .filter("my.supplierid", "=", SupplierUtils.getSupplierid())
                    .notDelete().end(1).execute();
            if(!users.isEmpty()){
                Supplierusers supplierusers = new Supplierusers(users.get(0));
                return supplierusers.roleid;
            }
        }catch (Exception ignored){}
        return "";
    }

    @Override
    public Boolean isBossUser(String profileid) {
        try{
            if (caches.containsKey(profileid)) {
                return caches.get(profileid);
            }
            List<Object> users = XN_Query.contentQuery().tag("supplier_users")
                    .filter("type", "eic", "supplier_users")
                    .filter("my.usertype ", "=", "boss")
                    .filter("my.approvalstatus ", "in", Arrays.asList("2","4"))
                    .filter("my.profileid","=",profileid)
                    .filter("my.supplierid", "=", SupplierUtils.getSupplierid())
                    .notDelete().end(1).execute();
            if(!users.isEmpty()){
                caches.put(profileid,true);
                return true;
            }
            caches.put(profileid,false);
        }catch (Exception ignored){}
        return false;
    }

    @Override
    public String getRoleByProfile(String record) {
        try {
            List<Object> list = XN_Query.contentQuery().tag("supplier_users")
                    .filter("type", "eic", "supplier_users")
                    .filter("my.profileid", "=", record)
                    .notDelete().end(1).execute();
            if(!list.isEmpty()){
                return ((Content)list.get(0)).my.getOrDefault("roleid","").toString();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

    @Override
    public HashMap<String,String> getProfileIdsByName(String name) {
        if(!Utils.isEmpty(name)) {
            try {
                List<Object> list = XN_Query.contentQuery().tag("supplier_users")
                        .filter("type", "eic", "supplier_users")
                        .filter("my.username", "like", name)
                        .begin(0).end(20)
                        .notDelete().execute();
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
    public String getNameByProfileid(String profileid) {
        if(CacheBaseEntitys.hasProfileName(profileid)) {
            return CacheBaseEntitys.getProfileName(profileid);
        }
        if(!Utils.isEmpty(profileid)) {
            try {
                List<Object> list = XN_Query.contentQuery().tag("supplier_users")
                        .filter("type", "eic", "supplier_users")
                        .filter("my.profileid", "=", profileid)
                        .begin(0).end(20)
                        .notDelete().execute();
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
                if(Boolean.TRUE.equals(CacheBaseEntitys.hasProfileName(id))) {
                    result.put(id,CacheBaseEntitys.getProfileName(id));
                } else {
                    noFinds.add(id);
                }
            }
            if(!noFinds.isEmpty()) {
                try {
                    List<Object> list = XN_Query.contentQuery().tag("supplier_users")
                            .filter("type", "eic", "supplier_users")
                            .filter("my.profileid", "in", noFinds)
                            .notDelete().end(-1).execute();
                    if (!list.isEmpty()) {
                        for (Object item : list) {
                            Supplierusers users = new Supplierusers(item);
                            result.put(users.profileid, users.username);
                            CacheBaseEntitys.addProfileName(users.profileid,users.username);
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        return result;
    }

    @Override
    public String getDepartmentId(String profileid) {
        try {
            List<Object> list = XN_Query.contentQuery().tag("supplier_users")
                    .filter("type", "eic", "supplier_users")
                    .filter("my.profileid", "=", profileid)
                    .notDelete().end(1).execute();
            if(!list.isEmpty()){
                return ((Content)list.get(0)).my.getOrDefault("departmentid","").toString();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String getDepartment(String profileid) {
        String departmentid = getDepartmentId(profileid);
        if(!Utils.isEmpty(departmentid)){
            return this.getDepartmentName(departmentid);
        }
        return null;
    }

    @Override
    public String getDepartmentName(String departmentid) {
        try{
            if(!Utils.isEmpty(departmentid)) {
                Content conn = XN_Content.load(departmentid, "supplier_departments");
                return conn.my.get("name").toString();
            }
        }catch (Exception ignored) {}
        return null;
    }

    @Override
    public Map<String, Object> getProfileByRoles(Object roles) {
        Map<String, Object> result = new HashMap<>(1);
        if(roles instanceof String){
            roles = Arrays.asList(((String) roles).split(","));
        }
        if(roles instanceof List){
            try {
                List<Object> list = XN_Query.contentQuery().tag("supplier_users")
                        .filter("type", "eic", "supplier_users")
                        .filter("my.roleid", "in", roles)
                        .notDelete().end(-1).execute();
                if(!list.isEmpty()){
                    for(Object item: list){
                        Supplierusers users = new Supplierusers(item);
                        if(Utils.isEmpty(users.profileid) || Utils.isEmpty(users.username)) {
                            continue;
                        }
                        if(result.containsKey(users.roleid)){
                            if(!((Map<?,?>)result.get(users.roleid)).containsKey(users.profileid)){
                                ((Map<String,Object>)result.get(users.roleid)).put(users.profileid,users.username);
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
            List<Object> list = XN_Query.contentQuery().tag("supplier_users")
                    .filter("type", "eic", "supplier_users")
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
        if(departmentids instanceof String){
            departmentids = Collections.singletonList(departmentids);
        }
        if(departmentids instanceof List){
            try{
                List<Object> query = XN_Content.loadMany((List<String>) departmentids,"supplier_departments");
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
        }
        return null;
    }

    @Override
    public Object getActionVerify(String param, String modulename, String record, BaseEntityUtils viewEntitys) {
        if("Active".equals(param) || "Inactive".equals(param)) {
            if(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()) {
                return true;
            }
            return RolesUtils.isEdit(modulename);
        }
        if("Approval".equals(param) || "SubmitOnline".equals(param)){
            try{
                Content info = XN_Content.load(record,viewEntitys.getTabName(),viewEntitys.getDataTypeVal());
                if("-1".equals(info.my.get("deleted")) || !info.author.equals(ProfileUtils.getCurrentProfileId()) || "1".equals(info.my.get("approvalstatus")) || "2".equals(info.my.get("approvalstatus")) || "4".equals(info.my.get("approvalstatus"))) {
                    return false;
                }
            }catch (Exception e){
                return false;
            }
            if(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()) {
                return true;
            }
            return RolesUtils.isEdit(modulename);
        }
        if("ChanageAccount".equals(param)) {
            if(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()) {
                return true;
            }
            return false;
        }
        if("ModifyPassword".equals(param)) {
            if(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()) {
                return true;
            }
            return false;
        }
        return ISupplierusersService.super.getActionVerify(param, modulename, record, viewEntitys);
    }



    @Override
    public void chanageAccount(Map<String, Object> httpRequest) throws Exception {
        try {
            String record = httpRequest.getOrDefault("record", "").toString();
            String account = httpRequest.getOrDefault("account", "").toString();
            List<Object> list = XN_Query.contentQuery().tag("supplier_users")
                    .filter("type", "eic", "supplier_users")
                    .filter("id", "!=", record)
                    .filter("my.account", "=", account)
                    .notDelete().end(1).execute();
            if (!list.isEmpty()) {
                throw new WebException("账户名称已经存在，请更换后再试。。。");
            }
            Content userConn = XN_Content.load(record, "supplier_users");
            Supplierusers user = new Supplierusers(userConn);
            if (user.supplierid.equals(SupplierUtils.getSupplierid())) {
                user.account = account;
                user.deleted = 0;
                user.createnew = 0;
                list = XN_Query.create("Profile").tag("profile")
                        .filter("username", "=", account)
                        .filter("id", "!=", user.profileid)
                        .filter("type", "in", Arrays.asList("admin", "pt", "supplier"))
                        .end(1).execute();
                if (!list.isEmpty()) {
                    throw new WebException("账户名称已经存在，请更换后再试。。。");
                }
                Profile profile = XN_Profile.load(user.profileid, "profile");
                profile.fullname = user.account;
                profile.save("profile");
                user.toContent(userConn);
                userConn.save("supplier_users");
                ProfileUtils.logout(user.profileid);
            } else {
                throw new WebException("用户所属企业异常");
            }
        }catch (WebException e) {
            throw e;
        }catch (Exception e) {
            throw new WebException("修改失败");
        }
    }
    @Override
    public void modifyPassword(Map<String, Object> httpRequest) throws Exception {
        try {
            String record = httpRequest.getOrDefault("record", "").toString();
            String password = httpRequest.getOrDefault("password", "").toString();
            ValidationUtil.checkPassword(password);
            Content userConn = XN_Content.load(record, "supplier_users");
            Users users = new Users(userConn);
            Profile profile = XN_Profile.load(users.profileid, "profile");
            profile.password = password;
            profile.save("profile");
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
