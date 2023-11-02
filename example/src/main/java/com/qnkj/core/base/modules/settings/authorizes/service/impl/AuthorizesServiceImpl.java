package com.qnkj.core.base.modules.settings.authorizes.service.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.authorizes.entity.Authorizes;
import com.qnkj.core.base.modules.settings.authorizes.service.IAuthorizesService;
import com.qnkj.core.base.modules.settings.users.entity.Users;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.utils.RolesUtils;
import com.qnkj.core.webconfigs.exception.WebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author clubs
 */
@Slf4j
@Service
public class AuthorizesServiceImpl implements IAuthorizesService {

    private static Map<String, Map<String,Boolean>> cacheAuthorizes = new HashMap<>();
    private static final String USER_ID_KEY = "userid";
    private static final String AUTHORIZE_KEY = "authorize";
    private static final String TABLE_NAME_KEY = "authorizes";

    @Override
    public Map<String, Object> getListViewEntity(HttpServletRequest request, BaseEntityUtils viewEntitys, Class<?> dataFace) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            XN_Query query = XN_Query.contentQuery().notDelete().tag(viewEntitys.getTabName())
                    .filter("type", "eic", viewEntitys.getTabName())
                    .end(-1);
            List<Object> result = query.execute();
            HashMap<String,Object> data = new HashMap<>();
            if (!result.isEmpty()) {
                result.forEach(item -> {
                    if(!Utils.isEmpty(((Content)item).my.get(AUTHORIZE_KEY))){
                        data.put(((Content)item).my.get(AUTHORIZE_KEY).toString(),item);
                    }
                });
            }
            List<Authorizes> lists = new ArrayList<>();
            SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
            Object authorizess = saasUtils.getPlatformAuthorizes();
            if(!Utils.isEmpty(authorizess)){
                for(Object item: (List<?>)authorizess){
                    String authorizename = ((HashMap<?,?>) item).get(AUTHORIZE_KEY).toString();
                    String excludeKey = "exclude";
                    if ((httpRequest.get(excludeKey) instanceof List && ((List<?>)httpRequest.get(excludeKey)).contains(authorizename)) ||
                            (httpRequest.get(excludeKey) instanceof String && httpRequest.get(excludeKey).equals(authorizename))
                    ) {
                        continue;
                    }
                    if(!Utils.isEmpty(data.get(authorizename))){
                        Authorizes authorizes = new Authorizes(data.get(authorizename));
                        lists.add(authorizes);
                    } else {
                        Authorizes authorizes = new Authorizes();
                        authorizes.authorize = authorizename;
                        lists.add(authorizes);
                    }
                }
            }
            Map<String, Object> infoMap = new HashMap<>(1);
            infoMap.put("list", lists);
            infoMap.put("total", lists.size());
            return infoMap;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new HashMap<>(1);
    }

    @Override
    public void save(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        Object authorizeids = httpRequest.getOrDefault("authorizeids",null);
        if(!Utils.isEmpty(authorizeids)){
            Object users = httpRequest.getOrDefault("id","");
            Object usernames = httpRequest.getOrDefault("name","");
            if(authorizeids instanceof String) {
                if(((String) authorizeids).contains(",")){
                    authorizeids = new ArrayList<>(Arrays.asList(((String) authorizeids).split(",")));
                }else {
                    authorizeids = Collections.singletonList(authorizeids);
                }
            }
            if(users instanceof String) {
                if(((String) users).contains(",")){
                    users = new ArrayList<>(Arrays.asList(((String) users).split(",")));
                }else {
                    users = Collections.singletonList(users);
                }
            }
            if(usernames instanceof String) {
                if(((String) usernames).contains(",")){
                    usernames = new ArrayList<>(Arrays.asList(((String) usernames).split(",")));
                }else {
                    usernames = Collections.singletonList(usernames);
                }
            }
            List<Object> query;
            do{
                query = XN_Query.contentQuery().tag(viewEntitys.getTabName())
                        .filter("type","eic",viewEntitys.getTabName())
                        .filter("my.authorize","in",authorizeids)
                        .execute();
                XN_Content.delete(query,viewEntitys.getTabName());
            }while (!query.isEmpty());
            List<Object> saved = new ArrayList<>();
            for(Object authorize : (List<?>)authorizeids) {
                Content newConn = XN_Content.create(viewEntitys.getTabName(),"", ProfileUtils.getCurrentProfileId());
                newConn.add(AUTHORIZE_KEY,authorize).add(USER_ID_KEY,users).add("userlist",usernames);
                saved.add(newConn);
            }
            if(!saved.isEmpty()){
                XN_Content.batchsave(saved,viewEntitys.getTabName());
            }
            cacheAuthorizes.clear();
        } else {
            throw new WebException("授权失败，无授权对像");
        }
    }

    @Override
    public void cancel(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        Object authorizeids = httpRequest.getOrDefault("authorizeids",null);
        if(!Utils.isEmpty(authorizeids)){
            if(authorizeids instanceof String) {
                if(((String) authorizeids).contains(",")){
                    authorizeids = new ArrayList<>(Arrays.asList(((String) authorizeids).split(",")));
                }else {
                    authorizeids = Collections.singletonList(authorizeids);
                }
            }
            List<Object> query;
            do{
                query = XN_Query.contentQuery().tag(viewEntitys.getTabName())
                        .filter("type","eic",viewEntitys.getTabName())
                        .filter("my.authorize","in",authorizeids)
                        .execute();
                XN_Content.delete(query,viewEntitys.getTabName());
            }while (query.size() >= 100);
            cacheAuthorizes.clear();
        } else {
            throw new WebException("取消授权失败，无授权对像");
        }
    }

    @Override
    public boolean isAuthorizes(String profileid, String authorize) {
        if (cacheAuthorizes.containsKey(authorize)) {
            Map<String,Boolean> authorizes = cacheAuthorizes.get(authorize);
            if (authorizes.containsKey(profileid)) {
                return authorizes.get(profileid);
            }
        }
        boolean hasAuthorize = false;
        if(!Utils.isEmpty(profileid) && !Utils.isEmpty(authorize)){
            try {
                List<Object> query = XN_Query.contentQuery().notDelete().tag(TABLE_NAME_KEY)
                        .filter("type", "eic", TABLE_NAME_KEY)
                        .filter("my.authorize", "=", authorize)
                        .filter("my.userid", "=", profileid)
                        .execute();
                if(!query.isEmpty()){
                    hasAuthorize = true;
                }
            }catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        if (cacheAuthorizes.containsKey(authorize)) {
            Map<String,Boolean> authorizes = cacheAuthorizes.get(authorize);
            authorizes.put(profileid,hasAuthorize);
        } else {
            Map<String,Boolean> infoMap = new HashMap<>(1);
            infoMap.put(profileid, hasAuthorize);
            cacheAuthorizes.put(authorize,infoMap);
        }
        return hasAuthorize;
    }

    @Override
    public List<Users> getUsersByAuthorize(String authorize) {
        if(!Utils.isEmpty(authorize)){
            try {
                Object profileids = null;
                List<Object> query = XN_Query.contentQuery().notDelete().tag(TABLE_NAME_KEY)
                        .filter("type", "eic", TABLE_NAME_KEY)
                        .filter("my.authorize", "=", authorize)
                        .end(1).execute();
                if(!query.isEmpty() && !Utils.isEmpty(((Content)query.get(0)).my.get(USER_ID_KEY))){
                    profileids = ((Content) query.get(0)).my.get(USER_ID_KEY);
                }
                query.clear();
                if(profileids instanceof String){
                    profileids = Collections.singletonList(profileids);
                }
                if(profileids instanceof List){
                    query = XN_Query.contentQuery().notDelete().tag("users")
                            .filter("type","eic","users")
                            .filter("my.profileid","in",profileids)
                            .end(-1).execute();
                    if(!query.isEmpty()){
                        List<Users> userList = new ArrayList<>();
                        for(Object item: query){
                            userList.add(new Users(item));
                        }
                        return userList;
                    }
                }
            }catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return new ArrayList<>(1);
    }

    @Override
    public Map<String, Object> getProfileByAuthorizes(Object authorize) {
        Map<String,Object> result = new HashMap<>();
        if(authorize instanceof String){
            authorize = Arrays.asList(((String) authorize).split(","));
        }
        if(authorize instanceof List){
            try {
                List<Object> query = XN_Query.contentQuery().notDelete().tag(TABLE_NAME_KEY)
                        .filter("type", "eic", TABLE_NAME_KEY)
                        .filter("my.authorize", "in", authorize)
                        .end(-1).execute();
                for(Object item: query){
                    if(!Utils.isEmpty(((Content)item).get(USER_ID_KEY))){
                        List<String> ids = null;
                        if(((Content)item).get(USER_ID_KEY) instanceof String){
                            ids = Collections.singletonList(((Content) item).get(USER_ID_KEY).toString());
                        }else if(((Content)item).get(USER_ID_KEY) instanceof List){
                            ids = (List<String>) ((Content)item).get(USER_ID_KEY);
                        }
                        if(!Utils.isEmpty(ids)) {
                            result.put(((Content) item).get(AUTHORIZE_KEY).toString(), ProfileUtils.getProfileGivenNames(ids));
                        }
                    }
                }
            }catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getAuths() {
        HashMap<String,Object> result = new HashMap<>();
        SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
        List<Object> authorizess = saasUtils.getPlatformAuthorizes();
        if(!Utils.isEmpty(authorizess)){
            for(Object item: authorizess){
                result.put(((HashMap<?,?>)item).get(AUTHORIZE_KEY).toString(),((HashMap<?,?>)item).get("authlabel"));
            }
        }
        return result;
    }

    @Override
    public HashMap<String, String> getNameByAuthorizes(Object authorizes) {
        HashMap<String, String> result = new HashMap<>();
            if(authorizes instanceof String){
                authorizes = Collections.singletonList(authorizes);
            }
            if(authorizes instanceof List) {
                SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
                List<Object> authorizess = saasUtils.getPlatformAuthorizes();
                for(Object item: (List<?>)authorizes){
                    for(Object auth: authorizess){
                        if(((Map<?,?>)auth).get(AUTHORIZE_KEY).equals(item)) {
                            result.put(item.toString(),((Map<?,?>)auth).get("authlabel").toString());
                        }
                    }
                }
            }
        return result;
    }

    @Override
    public List<String> getAuthorizesByProfile(String profileid) {
        List<String> result = new ArrayList<>(1);
        try{
            List<Object> query = XN_Query.contentQuery().notDelete().tag(TABLE_NAME_KEY)
                    .filter("type", "eic", TABLE_NAME_KEY)
                    .end(-1).execute();
            if(!query.isEmpty()) {
                for(Object item: query){
                    if(!Utils.isEmpty(((Content)item).get(USER_ID_KEY)) && ((((Content)item).get(USER_ID_KEY) instanceof String && ((Content)item).get(USER_ID_KEY).equals(profileid)) ||
                            (((Content)item).get(USER_ID_KEY) instanceof List && ((List<?>) ((Content)item).get(USER_ID_KEY)).contains(profileid))
                    )){
                        result.add(((Content) item).get(AUTHORIZE_KEY).toString());
                    }
                }
            }
        }catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    @Override
    public Object getActionVerify(String param, String modulename, String dataId, BaseEntityUtils viewEntitys) {
        if("Authorize".equals(param) || "CancelAuthorize".equals(param)) {
            if(ProfileUtils.isAdmin() || ProfileUtils.isAssistant()) {
                return true;
            }
            return RolesUtils.isEdit(modulename);
        }
        return IAuthorizesService.super.getActionVerify(param, modulename, dataId, viewEntitys);
    }
}
