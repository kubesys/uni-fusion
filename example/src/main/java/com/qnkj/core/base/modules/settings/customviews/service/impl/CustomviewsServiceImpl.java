package com.qnkj.core.base.modules.settings.customviews.service.impl;

import com.qnkj.common.entitys.CustomFieldSetting;
import com.qnkj.common.entitys.CustomView;
import com.qnkj.common.entitys.TabField;
import com.qnkj.common.services.ICustomViewServices;
import com.qnkj.common.services.ITabFieldServices;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.CacheBaseEntitys;
import com.qnkj.core.base.modules.settings.customviews.service.ICustomviewsService;
import com.qnkj.core.utils.AuthorizeUtils;
import com.qnkj.core.utils.ProfileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomviewsServiceImpl implements ICustomviewsService {
    private final ICustomViewServices customViewServices;
    private final ITabFieldServices fieldServices;

    public CustomviewsServiceImpl(ICustomViewServices customViewServices, ITabFieldServices fieldServices) {
        this.customViewServices = customViewServices;
        this.fieldServices = fieldServices;
    }

    @Override
    public List<Object> getModuleViews(HttpServletRequest request) {
        List<Object> views = new ArrayList<>();
        try {
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            if(!Utils.isEmpty(httpRequest.get("modulename"))) {
                String modulename = httpRequest.get("modulename").toString();
                String defaultid = "";
                List<CustomView> dbviews = customViewServices.list(modulename,ProfileUtils.getCurrentProfileId());
                HttpSession session = request.getSession();
                HashMap<String, Object> sessionRequest = (HashMap)session.getAttribute(modulename+"_DataSearch");
                if(!Utils.isEmpty(sessionRequest)){
                    if(!Utils.isEmpty(sessionRequest.get("viewid"))){
                        defaultid = sessionRequest.get("viewid").toString();
                    }
                }

                if(!Utils.isEmpty(defaultid)) {
                    boolean isfind = false;
                    for (CustomView item : dbviews) {
                        if (item.id.equals(defaultid)) {
                            isfind = true;
                            break;
                        }
                    }
                    if (!isfind) {
                        defaultid = "";
                    }
                }
                if(Utils.isEmpty(defaultid)) {
                    for (CustomView item : dbviews) {
                        if(!Utils.isEmpty(item.privateuser) && item.privateuser.equals(ProfileUtils.getCurrentProfileId()) && item.isdefault){
                            defaultid = item.id;
                            break;
                        }
                    }
                    if(Utils.isEmpty(defaultid)) {
                        for (CustomView item : dbviews) {
                            if (!Utils.isEmpty(item.authorize) && AuthorizeUtils.isAuthorizes(ProfileUtils.getCurrentProfileId(), item.authorize)) {
                                defaultid = item.id;
                                break;
                            }
                        }
                    }
                    if(Utils.isEmpty(defaultid)) {
                        for (CustomView item : dbviews) {
                            if (item.isdefault) {
                                defaultid = item.id;
                                break;
                            }
                        }
                    }
                }
                for(CustomView item: dbviews){
                    if (!Utils.isEmpty(item.authorize) && !AuthorizeUtils.isAuthorizes(ProfileUtils.getCurrentProfileId(), item.authorize)) {
                        continue;
                    }
                    Map<String,Object> infoMap = new HashMap<>(1);
                    infoMap.put("viewname",item.viewname);
                    infoMap.put("id",item.id);
                    infoMap.put("select",item.id.equals(defaultid));
                    if(Utils.isEmpty(item.privateuser)){
                        infoMap.put("ispublic",true);
                    }else{
                        infoMap.put("ispublic",false);
                    }
                    views.add(infoMap);
                }
            }
        }catch (Exception e) {
            log.error("getModuleViews Error: {}",e.getMessage());
        }
        return views;
    }

    @Override
    public HashMap<String,Object> getListViewHeader(HttpServletRequest request) {
        HashMap<String,Object> headerInfo = new HashMap<>();
        try {
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            if(!Utils.isEmpty(httpRequest.get("modulename"))) {
                String modulename = httpRequest.get("modulename").toString();
                BaseEntityUtils viewEntitys = new BaseEntityUtils(modulename);
                List<List<Map<String,Object>>> viewHeader = viewEntitys.getListViewHeader(httpRequest);
                HttpSession session = request.getSession();
                HashMap<String, Object> sessionRequest = (HashMap<String,Object>)session.getAttribute(modulename+"_DataSearch");
                int page = 1;
                int limit = 20;
                if(!Utils.isEmpty(sessionRequest)){
                    if(!Utils.isEmpty(sessionRequest.get("page"))){
                        page = Double.valueOf(sessionRequest.getOrDefault("page","1").toString()).intValue();
                    }
                    if(!Utils.isEmpty(sessionRequest.get("limit"))){
                        limit = Double.valueOf(sessionRequest.getOrDefault("limit","20").toString()).intValue();
                    }
                }
                String sorttype = viewEntitys.getDefaultOrder(httpRequest.getOrDefault("viewid", "").toString());
                String sortby = viewEntitys.getDefaultOrderBy(httpRequest.getOrDefault("viewid", "").toString());
                sortby = sortby.replace("my.","");
                if(Utils.isEmpty(sessionRequest)) {
                    sessionRequest = new HashMap<>();
                }
                sessionRequest.put("sortType",sorttype);
                sessionRequest.put("sortBy",sortby);
                session.setAttribute(modulename+"_DataSearch",sessionRequest);
                headerInfo.put("page",page);
                headerInfo.put("limit",limit);
                headerInfo.put("cols",BaseEntityUtils.headerToJson(viewHeader));
                headerInfo.put("sortfield",sortby);
                headerInfo.put("sorttype",sorttype);
            }
        }catch (Exception e) {
            log.error("getListViewHeader Error: {}",e.getMessage());
        }
        return headerInfo;
    }

    @Override
    public CustomView getCustomView(String viewid) {
        if(!Utils.isEmpty(viewid)){
            return customViewServices.load(viewid);
        } else {
            return null;
        }
    }

    @Override
    public List<TabField> getListFields(Map<String, Object> httpRequest) {
        List<TabField> fields = new ArrayList<>();
        if(!Utils.isEmpty(httpRequest.get("modulename"))){
            String modulename = httpRequest.get("modulename").toString();
            BaseEntityUtils viewEntitys = new BaseEntityUtils(modulename);
            List<CustomFieldSetting> customFields = viewEntitys.getCustomFields();
            Map<String,CustomFieldSetting> customFieldMaps = customFields.stream().collect(Collectors.toMap(v1 -> v1.fieldname, v1 -> v1, (k1, k2) -> k1));
            List<TabField> dbfields = fieldServices.list(modulename);
            List<String> fieldNames = new ArrayList<>();
            if(!Utils.isEmpty(dbfields) && !dbfields.isEmpty()) {
                for (TabField field : dbfields) {
                    fieldNames.add(field.fieldname);
                    if (field.uitype != 3 && field.uitype != 18  && field.uitype != 22 && field.uitype != 24 && field.uitype != 30 && field.uitype != 31 && field.uitype != 33) {
                        if (customFieldMaps.containsKey(field.fieldname)) {
                            CustomFieldSetting setting = customFieldMaps.get(field.fieldname);
                            field.fieldlabel = setting.fieldlabel;
                            field.grouplabel = setting.grouplabel;
                            if (!setting.ishidden) {
                                fields.add(field);
                            }
                        } else {
                            fields.add(field);
                        }

                    }
                }
            }
            if(!"backupmanage".equals(modulename)) {
                if (!fieldNames.contains("approvalstatus")) {
                    fields.add(new TabField().fieldname("approvalstatus").fieldlabel("状态"));
                }
                if (!fieldNames.contains("author")) {
                    fields.add(new TabField().fieldname("author").fieldlabel("创建人"));
                }
                if (!fieldNames.contains("updated")) {
                    fields.add(new TabField().fieldname("updated").fieldlabel("更新时间"));
                }
                if (!fieldNames.contains("published")) {
                    fields.add(new TabField().fieldname("published").fieldlabel("创建时间"));
                }
            }
        }
        return fields;
    }

    @Override
    public String saveCustomView(HttpServletRequest request) throws Exception {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        CustomView customView = new CustomView();
        Object columnlist = httpRequest.getOrDefault("columnlist",new ArrayList<String>());
        if(columnlist instanceof String) {
            columnlist = Collections.singletonList(columnlist);
        }
        customView.id = httpRequest.getOrDefault("viewid","").toString();
        customView.isdefault("true".equals(httpRequest.getOrDefault("isdefault","false")))
                .viewname(httpRequest.getOrDefault("viewname","").toString())
                .authorize(httpRequest.getOrDefault("authorize","").toString())
                .privateuser(ProfileUtils.getCurrentProfileId())
                .order(httpRequest.getOrDefault("order","").toString())
                .orderby(httpRequest.getOrDefault("orderby","").toString())
                .columnlist((List)columnlist)
                .modulename(httpRequest.getOrDefault("modulename","").toString());
        customView.createnew = 0;
        customView.deleted = 0;
        customViewServices.update(customView);
        CacheBaseEntitys.clear(customView.modulename);
        return customView.id;
    }

    @Override
    public String delCustomviews(HttpServletRequest request) throws Exception {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        if(Utils.isEmpty(httpRequest.get("viewid"))) {
            throw new Exception("视图不能为空");
        }
        if(Utils.isEmpty(httpRequest.get("modulename"))) {
            throw new Exception("模块名不能为空");
        }
        String viewid = httpRequest.get("viewid").toString();
        String modulename = httpRequest.get("modulename").toString();
        CustomView customView = customViewServices.load(viewid);
        customView.deleted = DateTimeUtils.gettimeStamp();
        customViewServices.update(customView);
        CacheBaseEntitys.clear(customView.modulename);
        List<CustomView> dbviews = customViewServices.list(modulename,ProfileUtils.getCurrentProfileId());
        for(CustomView item: dbviews){
            if(!Utils.isEmpty(item.privateuser) && item.privateuser.equals(ProfileUtils.getCurrentProfileId()) && item.isdefault){
                return item.id;
            }
        }
        for(CustomView item: dbviews){
            if(item.isdefault){
                return item.id;
            }
        }
        return "";
    }
}
