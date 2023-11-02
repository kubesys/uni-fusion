package com.qnkj.core.base.modules.supplier.SupplierDepartments.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.google.common.collect.ImmutableSet;
import com.qnkj.common.utils.CallbackUtils;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.departments.entity.Departments;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.base.modules.supplier.SupplierDepartments.entitys.Supplierdepartments;
import com.qnkj.core.base.modules.supplier.SupplierDepartments.services.ISupplierdepartmentsService;
import com.qnkj.core.base.modules.supplier.SupplierUsers.services.ISupplierusersService;
import com.qnkj.core.base.modules.supplier.SupplierUsers.services.impl.SupplierusersServiceImpl;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.utils.UserUtils;
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
public class SupplierdepartmentsServiceImpl implements ISupplierdepartmentsService {
    private List<String> findExcludeRoles(HashMap<String, Object> RolesList, Object exclude, boolean isRoot) {
        List<String> excludeRoles = new ArrayList<>();
        if (exclude != null) {
            List<String> excludes = new ArrayList<>();
            if (exclude instanceof String && !"".equals(exclude)) {
                excludes.add(exclude.toString());
            } else if (exclude instanceof List && !((List) exclude).isEmpty()) {
                excludes.addAll((List) exclude);
            }
            for (String excludeid : excludes) {
                for (String roleid : RolesList.keySet()) {
                    String tmpID = roleid;
                    if (!isRoot) {
                        Supplierdepartments roles = (Supplierdepartments) RolesList.get(roleid);
                        tmpID = roles.parentid;
                    }
                    if (excludeid.equals(tmpID)) {
                        excludeRoles.add(roleid);
                        excludeRoles.addAll(this.findExcludeRoles(RolesList, roleid, false));
                        if (isRoot) {
                            break;
                        }
                    }
                }
            }
            excludes.clear();
        }
        return excludeRoles;
    }

    @Override
    public Object getTree(Map<String, Object> Request, BaseEntityUtils viewEntitys) {
        return getTree(Request,viewEntitys,false);
    }

    @Override
    public Object getTree(Map<String, Object> Request,BaseEntityUtils viewEntitys, Boolean isSelect) {
        List<Object> result = new ArrayList<>();
        HashMap<String, Object> RolesList = new HashMap<>();
        List<String> SortKey = new ArrayList<>();
        try{
            List<Object> query = XN_Query.contentQuery().tag("supplier_departments")
                .filter("type","eic","supplier_departments")
                .notDelete()
                .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                .order("my.sequence","A_N")
                .end(-1).execute();
            if(query.size() <= 0) {
                if(Utils.isEmpty(Request.get("departmentids"))){
                    Object item = XN_Content.create(viewEntitys.getTabName(),"",ProfileUtils.getCurrentProfileId(), viewEntitys.getDataTypeVal())
                            .add("parentid","")
                            .add("name","部门管理")
                            .add("supplierid",SupplierUtils.getSupplierid())
                            .add("sequence","0")
                            .add("deleted","0")
                            .save(viewEntitys.getTabName());
                    Supplierdepartments supplierdepartments = new Supplierdepartments(item);
                    RolesList.put(supplierdepartments.id, supplierdepartments);
                    SortKey.add(supplierdepartments.id);
                }
            } else {
                excludeHide(query,"",SortKey,RolesList);
            }
            if (!SortKey.isEmpty()) {
                Map<String,List<String>> userDatas = new HashMap<>();
                List<String> excludes = this.findExcludeRoles(RolesList, Request.get("departmentids"), true);
                if(!isSelect) {
                    final ISupplierusersService usersService = new SupplierusersServiceImpl();
                    HashMap<String, Object> userInfo = usersService.getUserNamesByDepartment(SortKey);
                    HashMap<String, String> usernames = (HashMap<String,String>)userInfo.getOrDefault("usernames",new HashMap<>());
                    userDatas = (HashMap<String,List<String>>)userInfo.getOrDefault("userdatas",new HashMap<>());
                    for (String roleid : SortKey) {
                        if (excludes.contains(roleid)) {
                            continue;
                        }
                        Supplierdepartments roles = (Supplierdepartments) RolesList.get(roleid);
                        String parentid = roles.parentid.isEmpty() || !roles.parentid.contains(",") ? roles.parentid : roles.parentid.substring(0,roles.parentid.indexOf(","));
                        Supplierdepartments parent = (!"".equals(parentid)) ? (Supplierdepartments) RolesList.get(parentid) : new Supplierdepartments();
                        List<String> leaderships = new ArrayList<>();
                        for (int i = 0; i < roles.leadership.size(); i++) {
                            if (usernames.containsKey(roles.leadership.get(i))) {
                                leaderships.add(usernames.get(roles.leadership.get(i)));
                            }
                        }
                        Map<String, Object> infoMap = new HashMap<>(1);
                        infoMap.put("parentid", parent.id);
                        infoMap.put("parentname", parent.name);
                        infoMap.put("id", roles.id);
                        infoMap.put("departmentid", roles.id);
                        infoMap.put("departmentname", roles.name);
                        infoMap.put("name", roles.name);
                        infoMap.put("leadership", String.join(",", roles.leadership));
                        infoMap.put("leadershipname", String.join(",", leaderships));
                        if (usernames.containsKey(roles.mainleadership)) {
                            infoMap.put("mainleadership", roles.mainleadership);
                            infoMap.put("mainleadershipname", usernames.get(roles.mainleadership));
                        } else {
                            infoMap.put("mainleadership", "");
                            infoMap.put("mainleadershipname", "");
                        }

                        infoMap.put("sequence", roles.sequence);
                        infoMap.put("ishide", false);
                        infoMap.put("pId", parent.id);
                        infoMap.put("tip", roles.name);
                        if (userDatas.containsKey(roles.id)) {
                            infoMap.put("userData", userDatas.get(roles.id));
                        } else {
                            infoMap.put("userData", new ArrayList<>());
                        }
                        result.add(infoMap);
                    }
                } else {
                    final ISupplierusersService usersService = new SupplierusersServiceImpl();
                    HashMap<String, List<?>> userData = usersService.getUsersByDepartment(SortKey);
                    for (String departmentid : SortKey) {
                        if (excludes.contains(departmentid)) {
                            continue;
                        }
                        Supplierdepartments roles = (Supplierdepartments) RolesList.get(departmentid);
                        String parentid = roles.parentid;
                        Supplierdepartments parent = (parentid != null && !"".equals(parentid)) ? (Supplierdepartments) RolesList.get(parentid) : new Supplierdepartments();
                        Map<String, Object> infoMap = new HashMap<>(1);
                        infoMap.put("parentid", parent.id);
                        infoMap.put("parentname", parent.name);
                        infoMap.put("departmentid", roles.id);
                        infoMap.put("departmentname", roles.name);
                        infoMap.put("id",roles.id);
                        infoMap.put("name",roles.name);
                        infoMap.put("leadership", String.join(",", roles.leadership));
                        infoMap.put("mainleadership", roles.mainleadership);
                        infoMap.put("sequence", roles.sequence);
                        infoMap.put("ishide", false);
                        infoMap.put("pId", parent.id);
                        infoMap.put("tip", roles.name);
                        if(!Utils.isEmpty(userData.get(departmentid))){
                            infoMap.put("userData",userData.get(departmentid));
                        } else {
                            infoMap.put("userData",new ArrayList<>());
                        }
                        result.add(infoMap);
                    }
                }
                excludes.clear();
                userDatas.clear();
            }
            RolesList.clear();
            SortKey.clear();
        }catch (Exception ignored) {}

        return result;
    }
    private void excludeHide(List<Object> query,String parentid,List<String> SortKey,HashMap<String, Object>RolesList) {
        if(Utils.isEmpty(query)) {
            return;
        }
        for(Object item: query){
            Supplierdepartments roles = new Supplierdepartments(item);
            if(roles.parentid.equals(parentid)){
                RolesList.put(roles.id, roles);
                SortKey.add(roles.id);
                excludeHide(query, roles.id, SortKey,RolesList);
            }
        }
    }

    @Override
    public void save(Map<String, Object> Request, BaseEntityUtils viewEntitys) throws Exception {
        if (!Utils.isEmpty(Request.get("record"))) {
            String record = Request.get("record").toString();
            if (!Utils.isEmpty(Request.get("name"))) {
                List<Object> list = XN_Query.contentQuery().tag(viewEntitys.getTabName())
                        .filter("type", "eic", viewEntitys.getTabName())
                        .filter("my.name", "=", Request.get("name"))
                        .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                        .notDelete()
                        .filter("id", "!=", record)
                        .end(-1).execute();
                if (!list.isEmpty()) {
                    throw new WebException("部门名称已存在，请更换后再试。。。");
                }
            }
            Content content = XN_Content.load(record,viewEntitys.getTabName(),viewEntitys.getDataTypeVal());
            Supplierdepartments supplierdepartments = new Supplierdepartments(content);
            List<String> oldleadership = supplierdepartments.leadership;
            if (Utils.isEmpty(Request.get("sequence"))) {
                Request.put("sequence","0");
            }
            supplierdepartments.fromRequest(Request);
            supplierdepartments.createnew = 0;
            supplierdepartments.deleted = 0;
            supplierdepartments.supplierid = SupplierUtils.getSupplierid();
            supplierdepartments.toContent(content);
            content.save(viewEntitys.getTabName());
            BaseEntityUtils.deleteOutsideLink(record);

            boolean isUpdateUser = false;
            List<Object> callback = new ArrayList<>();
            if (!Request.get("leadership").equals(oldleadership)) {
                List<String> ids = null;
                if (Request.get("leadership") instanceof String) {
                    if (!Request.get("leadership").toString().isEmpty()) {
                        isUpdateUser = true;
                        if (Request.get("leadership").toString().indexOf(",") > 0) {
                            ids = new ArrayList<>(Arrays.asList(Request.get("leadership").toString().split(",")));
                        } else {
                            ids = new ArrayList<>(ImmutableSet.of(Request.get("leadership").toString()));
                        }
                    }
                } else if (Request.get("leadership") instanceof List) {
                    if (!((List<?>) Request.get("leadership")).isEmpty()) {
                        isUpdateUser = true;
                        ids = new ArrayList<>((List<String>) Request.get("leadership"));
                    }
                }

                if (ids != null && !ids.isEmpty()) {
                    List<Object> list = XN_Query.contentQuery().tag(viewEntitys.getTabName())
                            .filter("type", "eic", viewEntitys.getTabName())
                            .filter("my.leadership", "in", ids)
                            .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                            .filter("id", "!=", record)
                            .end(-1).execute();
                    if (!list.isEmpty()) {
                        List<Object> Saves = new ArrayList<>();
                        List<String> finalIds = ids;
                        list.forEach(item -> {
                            Object leadership = ((Content) item).my.get("leadership");
                            if (leadership instanceof String) {
                                ((Content) item).my.put("leadership", "");
                                Saves.add(item);
                            } else if (leadership instanceof List) {
                                for (String id : finalIds) {
                                    if (((List) leadership).indexOf(id) >= 0) {
                                        ((List) leadership).remove(id);
                                    }
                                }
                                ((Content) item).my.put("leadership", leadership);
                                Saves.add(item);
                            }
                        });
                        if (!Saves.isEmpty()) {
                            XN_Content.batchsave(Saves, viewEntitys.getTabName());
                        }
                        Saves.clear();
                    }

                    if (isUpdateUser) {
                        List<Object> query = XN_Query.contentQuery().tag("supplier_users")
                                .filter("type","eic","supplier_users")
                                .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                                .notDelete()
                                .filter("my.profileid","in",ids)
                                .end(-1).execute();
                        List<Object> Saves = new ArrayList<>();
                        query.forEach(item -> {
                            Map<String,Object> infoMap = new HashMap<>(1);
                            infoMap.put("profileid",((Content) item).my.getOrDefault("profileid",""));
                            infoMap.put("source",((Content) item).my.getOrDefault("departmentid",""));
                            infoMap.put("target",record);
                            callback.add(infoMap);
                            ((Content) item).my.put("departmentid", record);
                            Saves.add(item);
                        });
                        if (!Saves.isEmpty()) {
                            XN_Content.batchsave(Saves, "supplier_users");
                        }
                        Saves.clear();
                    }
                    ids.clear();
                }
            }
            if(!callback.isEmpty()){
                try {
                    CallbackUtils.invoke("updateDepartments", callback);
                }catch (Exception e){
                    log.error("==========================================================");
                    log.error("回调错误 ： Class：SupplierdepartmentsServiceImpl");
                    log.error("回调错误 ： Module：Save");
                    log.error("回调错误 ： invoke：UpdateDepartments");
                    log.error("回调错误 ： param：{}", callback);
                    log.error("回调错误 ： Error：{}", e.getMessage());
                    log.error("==========================================================");
                }
            }
        }
    }

    @Override
    public void delete(Map<String, Object> Request, BaseEntityUtils viewEntitys) throws Exception {
        String record = Request.getOrDefault("id","").toString();
        if(!Utils.isEmpty(record)){
            Content content = XN_Content.load(record,viewEntitys.getTabName(),viewEntitys.getDataTypeVal());
            Supplierdepartments supplierdepartments = new Supplierdepartments(content);
            supplierdepartments.deleted = DateTimeUtils.gettimeStamp();
            supplierdepartments.toContent(content);
            content.save(viewEntitys.getTabName());
        } else {
            throw new Exception("保存失败，参数错误");
        }
    }

    @Override
    public HashMap<String, Object> list() {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<Object> list = XN_Query.contentQuery().tag("supplier_departments")
                    .filter("type", "eic", "supplier_departments")
                    .notDelete()
                    .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                    .end(-1).execute();
            for(Object item: list){
                result.put(((Content)item).id,((Content)item).get("name"));
            }
        } catch (Exception e) {
            log.error("SupplierdepartmentsServiceImpl list Error:{}",e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getAllDepartmentLeader() {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<Object> list = XN_Query.contentQuery().tag("supplier_departments")
                    .filter("type", "eic", "supplier_departments")
                    .notDelete().end(-1).execute();
            for(Object item: list){
                Departments departments = new Departments(item);
                if(!departments.leadership.isEmpty()){
                    HashMap<String,String> users = UserUtils.getNameByProfiles(departments.leadership);
                    result.put(departments.id,users);
                }
            }

        } catch (Exception e) {
            log.error("getAllDepartmentLeader Error:{}",e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getNameByDepartmentIds(Object departmentids) {
        HashMap<String,Object> result = new HashMap<>();
        if(Utils.isNotEmpty(departmentids)){
            if(departmentids instanceof String){
                departmentids = Collections.singletonList(departmentids);
            }
            if(departmentids instanceof List) {
                try {
                    List<Object> query = XN_Content.loadMany((List<String>) departmentids, "supplier_departments");
                    for (Object item : query) {
                        result.put(((Content) item).id, ((Content) item).get("name"));
                    }
                } catch (Exception e) {
                    log.error("getNameByDepartmentIds Error:{}",e.getMessage());
                }
            }
        }
        return result;
    }
}
