package com.qnkj.core.base.modules.settings.departments.service.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.google.common.collect.ImmutableSet;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.CallbackUtils;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.departments.entity.Departments;
import com.qnkj.core.base.modules.settings.departments.service.IDepartmentsService;
import com.qnkj.core.base.modules.settings.users.service.IUsersService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.utils.UserUtils;
import com.qnkj.core.webconfigs.exception.WebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DepartmentsServiceImpl implements IDepartmentsService {
    private final IUsersService usersService;

    public DepartmentsServiceImpl(IUsersService usersService) {
        this.usersService = usersService;
    }
    private List<String> findExcludeRoles(HashMap<String, Object> rolesList, Object exclude, boolean isRoot) {
        List<String> excludeRoles = new ArrayList<>();
        if (exclude != null) {
            List<String> excludes = new ArrayList<>();
            if (exclude instanceof String && !"".equals(exclude)) {
                excludes.add(exclude.toString());
            } else if (exclude instanceof List && !((List<?>) exclude).isEmpty()) {
                excludes.addAll((List) exclude);
            }
            for (String excludeid : excludes) {
                for(Map.Entry<String,Object> entry: rolesList.entrySet()){
                    String tmpId = entry.getKey();
                    if (!isRoot) {
                        Departments roles = (Departments) entry.getValue();
                        tmpId = roles.parentid;
                    }
                    if (excludeid.equals(tmpId)) {
                        excludeRoles.add(entry.getKey());
                        excludeRoles.addAll(this.findExcludeRoles(rolesList, entry.getKey(), false));
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
    public Object getRoleTree(Object departmentids) {
        return getRoleTree(departmentids, false);
    }
    @Override
    public Object getRoleTree(Object departmentids,boolean isEdit) {
        List<Object> result = new ArrayList<>();
        HashMap<String, Object> rolesList = new HashMap<>(1);
        List<String> sortKey = new ArrayList<>(1);
        try {
            XN_Query resultQuery = XN_Query.contentQuery().tag("departments")
                    .filter("type", "eic", "departments")
                    .order("my.sequence", "A_N")
                    .end(-1);
            if(!isEdit){
                resultQuery.filter("my.ishide", "=", "0");
            }
            List<Object> query = resultQuery.execute();
            if (query.isEmpty()) {
                if (departmentids == null) {
                    SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
                    String departmentRoot = saasUtils.getCompany();
                    Content department = XN_Content.create("departments", "", ProfileUtils.getCurrentProfileId());
                    department.add("ishide", "0");
                    department.add("parentid", "");
                    department.add("departmentname", departmentRoot);
                    department.add("sequence", "1");
                    department = department.save("departments");
                    Departments roles = new Departments(department);
                    rolesList.put(roles.id, roles);
                }
            } else {
                excludeHide(query,"",sortKey,rolesList);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (!sortKey.isEmpty()) {
            Map<String,List<String>> userDatas = new HashMap<>();
            Map<String,String> usernames = new HashMap<>();
            try {
                List<Object> users = XN_Query.contentQuery().tag("users")
                        .filter("type", "eic", "users")
                        .notDelete()
                        .filter("my.status ", "=","Active")
                        .order("my.sequence", "A_N")
                        .end(-1).execute();
                for (Object o : users) {
                    Content user = (Content) o;
                    String profileid = user.my.get("profileid").toString();
                    String departmentid = user.my.get("departmentid").toString();
                    String username = user.my.get("username").toString();
                    usernames.put(profileid, username);
                    if (userDatas.containsKey(departmentid)) {
                        List<String> lists = userDatas.get(departmentid);
                        lists.add(username);
                        userDatas.put(departmentid, lists);
                    } else {
                        List<String> lists = new ArrayList<>();
                        lists.add(username);
                        userDatas.put(departmentid, lists);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            List<String> excludes = this.findExcludeRoles(rolesList, departmentids, true);
            for (String roleid : sortKey) {
                if (excludes.contains(roleid)) {
                    continue;
                }
                Departments roles = (Departments) rolesList.get(roleid);
                String parentid = roles.parentid;
                Departments parent = (parentid != null && !"".equals(parentid)) ? (Departments) rolesList.get(parentid) : new Departments();
                List<String> leaderships = new ArrayList<>();
                for(int i =0;i<roles.leadership.size();i++) {
                    if (usernames.containsKey(roles.leadership.get(i))) {
                        leaderships.add(usernames.get(roles.leadership.get(i)));
                    }
                }
                Map<String, Object> infoMap = new HashMap<>(1);
                infoMap.put("parentid", parent.id);
                infoMap.put("parentname", parent.departmentname);
                infoMap.put("id", roles.id);
                infoMap.put("departmentid", roles.id);
                infoMap.put("departmentname", roles.departmentname);
                infoMap.put("name", roles.departmentname);
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
                infoMap.put("ishide", roles.ishide);
                infoMap.put("pId", parent.id);
                infoMap.put("tip", roles.departmentname);
                if (userDatas.containsKey(roles.id)) {
                    infoMap.put("userData",userDatas.get(roles.id));
                } else {
                    infoMap.put("userData",new ArrayList<>());
                }
                result.add(infoMap);
            }
        }
        sortKey.clear();
        rolesList.clear();
        return Utils.objectToJson(result);
    }
    private void excludeHide(List<Object> query,String parentid,List<String> sortKey,HashMap<String, Object>rolesList) {
        if(Utils.isEmpty(query)) {
            return;
        }
        for(Object item: query){
            Departments roles = new Departments(item);
            if(roles.parentid.equals(parentid)){
                rolesList.put(roles.id, roles);
                sortKey.add(roles.id);
                excludeHide(query, roles.id, sortKey,rolesList);
            }
        }
    }
    @Override
    public Object getRoleTreeByUsers(Object departmentids) {
        List<Object> result = new ArrayList<>();
        HashMap<String, Object> rolesList = new HashMap<>(1);
        List<String> sortKey = new ArrayList<>(1);
        try {
            XN_Query resultQuery = XN_Query.contentQuery().tag("departments")
                    .filter("type", "eic", "departments")
                    .order("my.sequence", "A_N")
                    .end(-1);
            List<Object> query = resultQuery.execute();
            if (query.isEmpty()) {
                if (departmentids == null) {
                    SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
                    String departmentRoot = saasUtils.getCompany();
                    Content department = XN_Content.create("departments", "", ProfileUtils.getCurrentProfileId());
                    department.add("ishide", "0");
                    department.add("parentid", "");
                    department.add("departmentname", departmentRoot);
                    department.add("sequence", "1");
                    department = department.save("departments");
                    Departments roles = new Departments(department);
                    rolesList.put(roles.id, roles);
                }
            } else {
                excludeHide(query,"",sortKey,rolesList);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (!sortKey.isEmpty()) {
            List<String> excludes = this.findExcludeRoles(rolesList, departmentids, true);
            HashMap<String, List<?>> userData = usersService.getUsersByDepartment(sortKey);
            for (String departmentid : sortKey) {
                if (excludes.contains(departmentid)) {
                    continue;
                }
                Departments roles = (Departments) rolesList.get(departmentid);
                String parentid = roles.parentid;
                Departments parent = (parentid != null && !"".equals(parentid)) ? (Departments) rolesList.get(parentid) : new Departments();
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("parentid", parent.id);
                infoMap.put("parentname", parent.departmentname);
                infoMap.put("departmentid", roles.id);
                infoMap.put("departmentname", roles.departmentname);
                infoMap.put("id",roles.id);
                infoMap.put("name",roles.departmentname);
                infoMap.put("leadership", String.join(",", roles.leadership));
                infoMap.put("mainleadership", roles.mainleadership);
                infoMap.put("sequence", roles.sequence);
                infoMap.put("ishide", roles.ishide);
                infoMap.put("pId", parent.id);
                infoMap.put("tip", roles.departmentname);
                if(!Utils.isEmpty(userData.get(departmentid))){
                    infoMap.put("userData",userData.get(departmentid));
                } else {
                    infoMap.put("userData",new ArrayList<>());
                }
                result.add(infoMap);
            }
            excludes.clear();
            userData.clear();
        }
        sortKey.clear();
        rolesList.clear();
        return Utils.objectToJson(result);
    }

    @Override
    public Object getTreeNodes(String departmentid) {
        try {
            if (departmentid != null && !departmentid.isEmpty()) {
                Content rolesConn = XN_Content.load(departmentid, "departments");
                return this.getTreeNodes(new ArrayList<>(ImmutableSet.of(rolesConn)), departmentid, false);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    private Object getTreeNodes(List<Object> roles, boolean isEdit) {
        return this.getTreeNodes(roles, ((Content) roles.get(0)).id, isEdit);
    }

    private Object getTreeNodes(List<Object> departments, String currentid, boolean isEdit) {
        List<Object> result = new ArrayList<>();
        if (!departments.isEmpty()) {
            List<String> parentroles = new ArrayList<>();
            HashMap<String, Content> parents = new HashMap<>();
            departments.forEach(item -> {
                String parentrole = ((Content) item).my.get("parentid").toString();
                if (!parentrole.isEmpty()) {
                    parentroles.add(parentrole);
                }
            });
            if (!parentroles.isEmpty()) {
                try {
                    List<Object> list = XN_Content.loadMany(parentroles, "departments");
                    list.forEach(item -> parents.put(((Content) item).id, (Content) item));
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
            parentroles.clear();

            departments.forEach(item -> {
                String parentrole = ((Content) item).my.get("parentid").toString();
                Departments parent;
                if (!Utils.isEmpty(parents.get(parentrole))) {
                    parent = new Departments(parents.get(parentrole));
                } else {
                    parent = new Departments();
                }
                Departments finalParent = parent;
                Departments subRoles = new Departments(item);
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("parentid", finalParent.id);
                infoMap.put("parentname", finalParent.departmentname);
                infoMap.put("departmentid", subRoles.id);
                infoMap.put("departmentname", subRoles.departmentname);
                infoMap.put("leadership", String.join(",", subRoles.leadership));
                infoMap.put("mainleadership", subRoles.mainleadership);
                infoMap.put("sequence", subRoles.sequence);
                infoMap.put("ishide", subRoles.ishide);
                infoMap.put("pId", finalParent.id);
                infoMap.put("tip", subRoles.departmentname);
                if (subRoles.id.equals(currentid)) {
                    if (isEdit) {
                        infoMap.put("operate", "edit");
                    } else {
                        infoMap.put("operate", "add");
                    }
                }
                result.add(infoMap);
            });
        }
        return result;
    }

    @Override
    public Object save(Map<String, Object> httpRequest) throws Exception {
        if (!Utils.isEmpty(httpRequest.get("record"))) {
            String recordId = httpRequest.get("record").toString();
            if (!Utils.isEmpty(httpRequest.get("departmentname"))) {
                List<Object> list = XN_Query.contentQuery().tag("departments")
                        .filter("type", "eic", "departments")
                        .filter("my.departmentname", "=", httpRequest.get("departmentname"))
                        .filter("id", "!=", recordId)
                        .end(-1).execute();
                if (!list.isEmpty()) {
                    throw new WebException("部门名称已存在，请更换后再试。。。");
                }
            }
            if ("-1".equals(recordId)) {
                Content roles = XN_Content.create("departments", "", ProfileUtils.getCurrentProfileId());
                roles.add("departmentname", httpRequest.get("departmentname"));
                roles.add("leadership", httpRequest.get("leadership"));
                roles.add("mainleadership", httpRequest.get("mainleadership"));
                roles.add("sequence", httpRequest.get("sequence"));
                roles.add("ishide", httpRequest.get("ishide"));
                roles.add("parentid", httpRequest.get("parentid"));
                roles = roles.save("departments");
                List<Object> updateRoles = new ArrayList<>();
                List<Object> callback = new ArrayList<>();
                if (!Utils.isEmpty(httpRequest.get("leadership"))) {
                    List<String> ids = null;
                    if (httpRequest.get("leadership") instanceof String) {
                        if (!httpRequest.get("leadership").toString().isEmpty()) {
                            if (httpRequest.get("leadership").toString().contains(",")) {
                                ids = new ArrayList<>(Arrays.asList(httpRequest.get("leadership").toString().split(",")));
                            } else {
                                ids = new ArrayList<>(ImmutableSet.of(httpRequest.get("leadership").toString()));
                            }
                        }
                    } else if (httpRequest.get("leadership") instanceof List && !((List<?>) httpRequest.get("leadership")).isEmpty()) {
                        ids = new ArrayList<>((List<String>) httpRequest.get("leadership"));
                    }

                    if (ids != null && !ids.isEmpty()) {
                        List<Object> list = XN_Query.contentQuery().tag("departments")
                                .filter("type", "eic", "departments")
                                .filter("my.leadership", "in", ids)
                                .filter("id", "!=", roles.id)
                                .end(-1).execute();
                        if (!list.isEmpty()) {
                            List<Object> saves = new ArrayList<>();
                            List<String> finalIds = ids;
                            list.forEach(item -> {
                                Object leadership = ((Content) item).my.get("leadership");
                                if (leadership instanceof String) {
                                    ((Content) item).my.put("leadership", "");
                                    saves.add(item);
                                } else if (leadership instanceof List) {
                                    for (String id : finalIds) {
                                        ((List<?>) leadership).remove(id);
                                    }
                                    ((Content) item).my.put("leadership", leadership);
                                    saves.add(item);
                                }
                            });
                            if (!saves.isEmpty()) {
                                updateRoles = XN_Content.batchsave(saves, "departments");
                            }
                            saves.clear();
                        }

                        List<Object> query = XN_Query.contentQuery().tag("users")
                                .filter("type","eic","users")
                                .notDelete()
                                .filter("my.profileid","in",ids)
                                .end(-1).execute();
                        List<Object> saves = new ArrayList<>();
                        Content finalRoles = roles;
                        query.forEach(item -> {
                            Map<String,Object> infoMap = new HashMap<>(1);
                            infoMap.put("profileid",((Content) item).my.getOrDefault("profileid",""));
                            infoMap.put("source",((Content) item).my.getOrDefault("departmentid",""));
                            infoMap.put("target",finalRoles.id);
                            callback.add(infoMap);
                            ((Content) item).my.put("departmentid", finalRoles.id);
                            saves.add(item);
                        });
                        if (!saves.isEmpty()) {
                            XN_Content.batchsave(saves, "users");
                        }
                        saves.clear();
                        ids.clear();
                    }
                }
                updateRoles.add(roles);
                Object result = getTreeNodes(updateRoles, roles.id, false);
                updateRoles.clear();
                if(!callback.isEmpty()){
                    try {
                        CallbackUtils.invoke("updateDepartments", callback);
                    }catch (Exception e){
                        log.error("==========================================================");
                        log.error("回调错误 ： Class：DepartmentsServiceImpl");
                        log.error("回调错误 ： Module：Save");
                        log.error("回调错误 ： invoke：UpdateDepartments");
                        log.error("回调错误 ： param：{}", callback);
                        log.error("回调错误 ： Error：{}", e.getMessage());
                        log.error("==========================================================");
                    }
                }
                return result;
            } else {
                Content department = XN_Content.load(recordId, "departments");
                boolean isUpdateUser = false;
                List<Object> updateRoles = new ArrayList<>();
                List<Object> callback = new ArrayList<>();
                if (!httpRequest.get("leadership").equals(department.my.get("leadership"))) {
                    List<String> ids = null;
                    if (httpRequest.get("leadership") instanceof String) {
                        if (!httpRequest.get("leadership").toString().isEmpty()) {
                            isUpdateUser = true;
                            if (httpRequest.get("leadership").toString().contains(",")) {
                                ids = new ArrayList<>(Arrays.asList(httpRequest.get("leadership").toString().split(",")));
                            } else {
                                ids = new ArrayList<>(ImmutableSet.of(httpRequest.get("leadership").toString()));
                            }
                        }
                    } else if (httpRequest.get("leadership") instanceof List && !((List<?>) httpRequest.get("leadership")).isEmpty()) {
                        isUpdateUser = true;
                        ids = new ArrayList<>((List<String>) httpRequest.get("leadership"));
                    }

                    if (ids != null && !ids.isEmpty()) {
                        List<Object> list = XN_Query.contentQuery().tag("departments")
                                .filter("type", "eic", "departments")
                                .filter("my.leadership", "in", ids)
                                .filter("id", "!=", recordId)
                                .end(-1).execute();
                        if (!list.isEmpty()) {
                            List<Object> saves = new ArrayList<>();
                            List<String> finalIds = ids;
                            list.forEach(item -> {
                                Object leadership = ((Content) item).my.get("leadership");
                                if (leadership instanceof String) {
                                    ((Content) item).my.put("leadership", "");
                                    saves.add(item);
                                } else if (leadership instanceof List) {
                                    for (String id : finalIds) {
                                        ((List<?>) leadership).remove(id);
                                    }
                                    ((Content) item).my.put("leadership", leadership);
                                    saves.add(item);
                                }
                            });
                            if (!saves.isEmpty()) {
                                updateRoles = XN_Content.batchsave(saves, "departments");
                            }
                            saves.clear();
                        }

                        if (isUpdateUser) {
                            List<Object> query = XN_Query.contentQuery().tag("users")
                                    .filter("type","eic","users")
                                    .notDelete()
                                    .filter("my.profileid","in",ids)
                                    .end(-1).execute();
                            List<Object> saves = new ArrayList<>();
                            query.forEach(item -> {
                                Map<String,Object> infoMap = new HashMap<>(1);
                                infoMap.put("profileid",((Content) item).my.getOrDefault("profileid",""));
                                infoMap.put("source",((Content) item).my.getOrDefault("departmentid",""));
                                infoMap.put("target",recordId);
                                callback.add(infoMap);
                                ((Content) item).my.put("departmentid", recordId);
                                saves.add(item);
                            });
                            if (!saves.isEmpty()) {
                                XN_Content.batchsave(saves, "users");
                            }
                            saves.clear();
                        }
                        ids.clear();
                    }
                }
                department.add("departmentname", httpRequest.get("departmentname"));
                department.add("sequence", httpRequest.get("sequence"));
                department.add("ishide", httpRequest.get("ishide"));
                department.add("leadership", httpRequest.get("leadership"));
                department.add("mainleadership", httpRequest.get("mainleadership"));
                department = department.save("departments");
                BaseEntityUtils.deleteOutsideLink(recordId);
                updateRoles.add(department);
                Object result = this.getTreeNodes(updateRoles, recordId, true);
                updateRoles.clear();
                if(!callback.isEmpty()){
                    try {
                        CallbackUtils.invoke("updateDepartments", callback);
                    }catch (Exception e){
                        log.error("==========================================================");
                        log.error("回调错误 ： Class：DepartmentsServiceImpl");
                        log.error("回调错误 ： Module：Save");
                        log.error("回调错误 ： invoke：UpdateDepartments");
                        log.error("回调错误 ： param：{}", callback);
                        log.error("回调错误 ： Error：{}", e.getMessage());
                        log.error("==========================================================");
                    }
                }
                return result;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void delete(Map<String, Object> httpRequest) throws Exception {
        if (!Utils.isEmpty(httpRequest.get("departmentid")) && !Utils.isEmpty(httpRequest.get("parentid"))) {
            String departmentid = httpRequest.get("departmentid").toString();
            String parentid = httpRequest.get("parentid").toString();
            List<Object> transfer = new ArrayList<>(ImmutableSet.of(departmentid));
            List<Object> resultQuery = XN_Query.contentQuery().tag("departments")
                    .filter("type", "eic", "departments")
                    .end(-1).execute();
            resultQuery.forEach(item -> {
                String parent = ((Content) item).my.get("parentid").toString();
                if (parent.equals(departmentid)) {
                    transfer.add(((Content) item).id);
                }
            });
            if (!transfer.isEmpty()) {
                resultQuery = XN_Query.contentQuery().tag("users")
                        .filter("type", "eic", "users")
                        .filter("my.departmentid", "in", transfer)
                        .notDelete().end(-1).execute();
                List<Object> saves = new ArrayList<>();
                resultQuery.forEach(item -> {
                    ((Content) item).my.put("departmentid", parentid);
                    saves.add(item);
                });
                if (!saves.isEmpty()) {
                    XN_Content.batchsave(saves, "users");
                }
                saves.clear();
                XN_Content.delete(transfer, "departments");
            }
            transfer.clear();
        } else {
            throw new WebException("删除部门或转移部门不能为空");
        }
    }

    @Override
    public void dropMove(Map<String, Object> httpRequest) throws Exception {
        if(!Utils.isEmpty(httpRequest.get("from")) && !Utils.isEmpty(httpRequest.get("to"))){
            Object from = httpRequest.get("from");
            Object to = httpRequest.get("to");
            if(from instanceof String){
                from = Collections.singletonList(from);
            }
            if(from instanceof List){
                List<Object> list = XN_Content.loadMany((List<String>) from,"departments");
                List<Object> saved = new ArrayList<>();
                list.forEach(item -> {
                    ((Content)item).my.put("parentid",to);
                    saved.add(item);
                });
                if(!saved.isEmpty()){
                    XN_Content.batchsave(saved,"departments");
                    saved.clear();
                }
            }
        }
    }

    @Override
    public HashMap<String, Object> list() {
        HashMap<String,Object> result = new HashMap<>();
        try {
            List<Object> list = XN_Query.contentQuery().tag("departments")
                    .filter("type", "eic", "departments")
                    .notDelete().end(-1).execute();
            for(Object item: list){
                result.put(((Content)item).id,((Content)item).get("departmentname"));
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getAllDepartmentLeader() {
        HashMap<String, Object> result = new HashMap<>();
        try {
            List<Object> list = XN_Query.contentQuery().tag("departments")
                    .filter("type", "eic", "departments")
                    .notDelete().end(-1).execute();
            for(Object item: list){
                Departments departments = new Departments(item);
                if(!departments.leadership.isEmpty()){
                    HashMap<String,String> users = UserUtils.getNameByProfiles(departments.leadership);
                    result.put(departments.id,users);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
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
                    List<Object> query = XN_Content.loadMany((List<String>) departmentids, "departments");
                    for (Object item : query) {
                        result.put(((Content) item).id, ((Content) item).get("departmentname"));
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        return result;
    }
}
