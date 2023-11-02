package com.qnkj.core.base.modules.supplier.SupplierAuth.service.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.base.modules.supplier.SupplierAuth.entity.SupplierAuth;
import com.qnkj.core.base.modules.supplier.SupplierAuth.service.ISupplierAuthService;
import com.qnkj.core.base.modules.supplier.SupplierUsers.entitys.Supplierusers;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.utils.RolesUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * create by 徐雁
 * create date 2021/4/9
 * @author clubs
 */

@Service
public class SupplierAuthServiceImpl implements ISupplierAuthService {
    @Override
    public HashMap<String, Object> getListViewEntity(HttpServletRequest request, BaseEntityUtils viewEntitys, Class<?> dataface) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        try {
            XN_Query query = XN_Query.contentQuery().tag(viewEntitys.getTabName())
                    .filter("type", "eic", viewEntitys.getTabName())
                    .filter("my.supplierid","=", SupplierUtils.getSupplierid())
                    .notDelete().end(-1);
            List<Object> result = query.execute();
            HashMap<String,Object> data = new HashMap<>();
            if (!result.isEmpty()) {
                result.forEach(item -> {
                    if(!Utils.isEmpty(((Content)item).my.get("authorize"))){
                        data.put(((Content)item).my.get("authorize").toString(),item);
                    }
                });
            }
            List<SupplierAuth> lists = new ArrayList<>();
            SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
            Object authorizess = saasUtils.getUserAuthorizes();
            if(!Utils.isEmpty(authorizess)){
                for(Object item: (List<?>)authorizess){
                    String authorizename = ((HashMap) item).getOrDefault("authorize","").toString();
                    if (!Utils.isEmpty(httpRequest.get("exclude"))) {
                        if (httpRequest.get("exclude") instanceof List) {
                            if(((List<?>)httpRequest.get("exclude")).contains(authorizename)) {
                                continue;
                            }
                        } else if (httpRequest.get("exclude") instanceof String) {
                            if(httpRequest.get("exclude").equals(authorizename)) {
                                continue;
                            }
                        }
                    }
                    if(!Utils.isEmpty(data.get(authorizename))){
                        SupplierAuth authorizes = new SupplierAuth(data.get(authorizename));
                        lists.add(authorizes);
                    } else {
                        SupplierAuth authorizes = new SupplierAuth();
                        authorizes.authorize = authorizename;
                        lists.add(authorizes);
                    }
                }
            }
            HashMap<String, Object> infoMap = new HashMap<>(1);
            infoMap.put("list", lists);
            infoMap.put("total", lists.size());
            return infoMap;
        } catch (Exception ignored) {}
        return null;
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
                        .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                        .filter("my.authorize","in",authorizeids)
                        .execute();
                XN_Content.delete(query,viewEntitys.getTabName());
            }while (!query.isEmpty());
            List<Object> saved = new ArrayList<>();
            for(Object authorize : (List<?>)authorizeids) {
                Content newConn = XN_Content.create(viewEntitys.getTabName(),"", ProfileUtils.getCurrentProfileId());
                newConn.add("authorize",authorize).add("userid",users).add("userlist",usernames);
                newConn.add("supplierid",SupplierUtils.getSupplierid());
                saved.add(newConn);
            }
            if(!saved.isEmpty()){
                XN_Content.batchsave(saved,viewEntitys.getTabName());
            }
        } else {
            throw new Exception("授权失败，无授权对像");
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
                        .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                        .execute();
                XN_Content.delete(query,viewEntitys.getTabName());
            }while (query.size() >= 100);
        } else {
            throw new Exception("取消授权失败，无授权对像");
        }
    }

    @Override
    public boolean isAuthorizes(String profileid, String authorize) {
        if(!Utils.isEmpty(profileid) && !Utils.isEmpty(authorize)){
            try {
                List<Object> query = XN_Query.contentQuery().tag("supplier_authorizes")
                        .filter("type", "eic", "supplier_authorizes")
                        .filter("my.authorize", "=", authorize)
                        .filter("my.userid", "=", profileid)
                        .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                        .notDelete().execute();
                if(!query.isEmpty()){
                    return true;
                }
            }catch (Exception ignored) {}
        }
        return false;
    }

    @Override
    public List<Supplierusers> getUsersByAuthorize(String authorize) {
        if(!Utils.isEmpty(authorize)){
            try {
                Object profileids = null;
                List<Object> query = XN_Query.contentQuery().tag("supplier_authorizes")
                        .filter("type", "eic", "supplier_authorizes")
                        .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                        .filter("my.authorize", "=", authorize)
                        .notDelete().execute();
                if(!query.isEmpty()){
                    if(!Utils.isEmpty(((Content)query.get(0)).my.get("userid"))) {
                        profileids = ((Content) query.get(0)).my.get("userid");
                    }
                }
                query.clear();
                if(!Utils.isEmpty(profileids)){
                    if(profileids instanceof String){
                        profileids = Collections.singletonList(profileids);
                    }
                    if(profileids instanceof List){
                        query = XN_Query.contentQuery().tag("supplier_users")
                                .filter("type","eic","supplier_users")
                                .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                                .notDelete()
                                .filter("my.profileid","in",profileids)
                                .end(-1).execute();
                        if(!query.isEmpty()){
                            List<Supplierusers> userList = new ArrayList<>();
                            for(Object item: query){
                                userList.add(new Supplierusers(item));
                            }
                            return userList;
                        }
                    }
                }
            }catch (Exception ignored) {}
        }
        return null;
    }

    @Override
    public Map<String, Object> getProfileByAuthorizes(Object authorize) {
        Map<String,Object> result = new HashMap<>();
        if(authorize instanceof String){
            authorize = Arrays.asList(((String) authorize).split(","));
        }
        if(authorize instanceof List){
            try {
                List<Object> query = XN_Query.contentQuery().tag("supplier_authorizes")
                        .filter("type", "eic", "supplier_authorizes")
                        .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                        .filter("my.authorize", "in", authorize)
                        .notDelete().end(-1).execute();
                if(!query.isEmpty()){
                    for(Object item: query){
                        if(!Utils.isEmpty(((Content)item).get("userid"))){
                            List<String> ids = null;
                            if(((Content)item).get("userid") instanceof String){
                                ids = Collections.singletonList(((Content) item).get("userid").toString());
                            }else if(((Content)item).get("userid") instanceof List){
                                ids = (List<String>) ((Content)item).get("userid");
                            }
                            if(!Utils.isEmpty(ids)) {
                                result.put(((Content) item).get("authorize").toString(), ProfileUtils.getProfileGivenNames(ids));
                            }
                        }
                    }
                }
                query.clear();
            }catch (Exception ignored) {}
        }
        return result;
    }

    @Override
    public HashMap<String, Object> getAuths() {
        HashMap<String,Object> result = new HashMap<>(1);
        SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
        List<Object> authorizess = saasUtils.getUserAuthorizes();
        if(!Utils.isEmpty(authorizess)){
            for(Object item: authorizess){
                result.put(((HashMap<?,?>)item).get("authorize").toString(),((HashMap<?,?>)item).get("authlabel"));
            }
        }
        return result;
    }

    @Override
    public HashMap<String, String> getNameByAuthorizes(Object authorizes) {
        HashMap<String, String> result = new HashMap<>();
        if(Utils.isNotEmpty(authorizes)) {
            if(authorizes instanceof String){
                authorizes = Collections.singletonList(authorizes);
            }
            if(authorizes instanceof List) {
                SaaSUtils saasUtils = new SaaSUtils(BaseSaasConfig.getDomain());
                List<Object> authorizess = saasUtils.getUserAuthorizes();
                if (!Utils.isEmpty(authorizess)) {
                    for(Object item: (List<?>)authorizes){
                        for(Object auth: authorizess){
                            if(((Map<?,?>)auth).get("authorize").equals(item)) {
                                result.put(item.toString(),((Map<?,?>)auth).get("authlabel").toString());
                        }
                        }
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
            List<Object> query = XN_Query.contentQuery().tag("supplier_authorizes")
                    .filter("type", "eic", "supplier_authorizes")
                    .filter("my.supplierid","=",SupplierUtils.getSupplierid())
                    .notDelete().end(-1).execute();
            if(!query.isEmpty()) {
                for(Object item: query){
                    if(!Utils.isEmpty(((Content)item).get("userid"))){
                        if(((Content)item).get("userid") instanceof String && ((Content)item).get("userid").equals(profileid)){
                            result.add(((Content) item).get("authorize").toString());
                        }else if(((Content)item).get("userid") instanceof List && ((List<?>) ((Content)item).get("userid")).contains(profileid)){
                            result.add(((Content) item).get("authorize").toString());
                        }
                    }
                }
            }
        }catch (Exception ignored) {}
        return result;
    }

    @Override
    public Object getActionVerify(String param, String modulename, String record, BaseEntityUtils viewEntitys) {
        if("Authorize".equals(param) || "CancelAuthorize".equals(param)) {
            if(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()) {
                return true;
            }
            return RolesUtils.isEdit(modulename);
        }
        return ISupplierAuthService.super.getActionVerify(param, modulename, record, viewEntitys);
    }
}
