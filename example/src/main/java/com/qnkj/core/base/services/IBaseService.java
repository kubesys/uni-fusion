package com.qnkj.core.base.services;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Filter;
import com.github.restapi.XN_ModentityNum;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.google.common.collect.ImmutableSet;
import com.qnkj.common.entitys.EntityLink;
import com.qnkj.common.entitys.Tab;
import com.qnkj.common.entitys.TabField;
import com.qnkj.common.utils.*;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.entitys.WorkflowStatus;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.utils.*;
import com.qnkj.core.webconfigs.exception.WebException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public interface IBaseService  extends IPublicService {
    Logger logger = LoggerFactory.getLogger(IBaseService.class);

    /**
     * 获取数据集
     * @param request 客户端请求参数集
     * @param viewEntitys 数据模块实体
     * @param entity 数据实本的类名
     * @return 返回Entity实体对像集及总记录数和统计信息
     */
    default Map<String, Object> getListViewEntity(HttpServletRequest request, BaseEntityUtils viewEntitys, Class<?> entity) {
        return getListViewEntity(request,viewEntitys,entity,null);
    }
    default Map<String, Object> getListViewEntity(HttpServletRequest request, BaseEntityUtils viewEntitys, Class<?> entity,String filter) {
        Map<String, Object> customListEntity = viewEntitys.getCustomListEntity(request);
        if (Utils.isNotEmpty(customListEntity)) {
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            HttpSession session = request.getSession();
            session.setAttribute(viewEntitys.getModuleName() + "_DataSearch",httpRequest);
            return customListEntity;
        }
        Map<String, Object> queryinfo = viewEntitys.getOrderBy(request, viewEntitys.getModuleName());
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        int page = (int) queryinfo.getOrDefault("page",1);
        int limit = (int) queryinfo.getOrDefault("limit",10);
        String orderby = queryinfo.getOrDefault("orderby","published").toString();
        String ordertype = queryinfo.getOrDefault("ordertype","D_N").toString();
        String modulename = httpRequest.getOrDefault("remodule","").toString();
        String record = httpRequest.getOrDefault("record","").toString();
        boolean ispopup = !Utils.isEmpty(httpRequest.get("ispopup")) && "true".equals(httpRequest.get("ispopup"));
        String linkage = httpRequest.getOrDefault("linkage","").toString();
        httpRequest.remove("ispopup");
        httpRequest.remove("remodule");
        httpRequest.remove("record");
        httpRequest.remove("linkage");

        try {
            XN_Query query = XN_Query.create(viewEntitys.getDataType())
                    .notDelete().tag(viewEntitys.getTabName())
                    .filter("type", "eic", viewEntitys.getTabName())
                    .alwaysReturnTotalCount()
                    .begin((page - 1) * limit)
                    .end(page * limit);
            if(Utils.isNotEmpty(orderby) && Utils.isNotEmpty(ordertype)){
                query.order(orderby, ordertype);
            }

            List<String> exclusiveFilters = new ArrayList<>();
            if (Utils.isNotEmpty(filter)) {
                //企业模块数据过滤
                IPublicService.addSupplierFilter(query);
                query.filter(filter);
            } else {
                if(ispopup) {
                    if (Boolean.TRUE.equals(viewEntitys.getModuletab().isapproval)) {
                        query.filter("my.approvalstatus ", "in", Arrays.asList("2","4"));
                    }
                    if (Boolean.TRUE.equals(viewEntitys.getModuletab().isenabledisable)) {
                        query.filter("my.status", "=", "Active");
                    }
                }
                //专属查看权限过滤
                if(Boolean.TRUE.equals(viewEntitys.getModuletab().isexclusive)) {
                    if((viewEntitys.getModuletab().datarole != 1 && (!(ProfileUtils.isAdmin() || ProfileUtils.isAssistant()))) ||
                            (viewEntitys.getModuletab().datarole == 1 && (!(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant())))){
                        String profileid = ProfileUtils.getCurrentProfileId();
                        List<String> listviewpermission = UserUtils.getAllSubordinate(profileid, false);
                        Utils.removeDuplicate(listviewpermission, profileid);
                        if (!listviewpermission.isEmpty()) {
                            exclusiveFilters.add(XN_Filter.filter("author", "in", listviewpermission));
                            exclusiveFilters.add(XN_Filter.filter("author", "=", ""));
                            exclusiveFilters.add(XN_Filter.filter("author", "=", null));
                        }
                    }
                    //配置了流程后查看过滤
                    if(Boolean.TRUE.equals(viewEntitys.getWorkflowStatus().isDefineflow())) {
                        if(Utils.isEmpty(exclusiveFilters)){
                            exclusiveFilters.add(XN_Filter.filter("author","=",ProfileUtils.getCurrentProfileId()));
                        }
                        exclusiveFilters.add(XN_Filter.filter("my.activiti_dealwiths", "=", ProfileUtils.getCurrentProfileId()));
                    }
                    if(Utils.isNotEmpty(exclusiveFilters)) {
                        query.filter(XN_Filter.any(exclusiveFilters));
                    }
                }
                //企业模块数据过滤
                IPublicService.addSupplierFilter(query);

                //报表类型过滤
                if(!Utils.isEmpty(httpRequest.get("reportParam"))){
                    Map<String,Object> reportParam= (Map<String, Object>) httpRequest.get("reportParam");
                    for(String mKey:reportParam.keySet()){
                        if("startDate".equals(mKey)||"endDate".equals(mKey)){
                            continue;
                        }
                        query.filter("my."+mKey,"=",reportParam.get(mKey));
                    }
                    if(reportParam.containsKey("startDate")&&reportParam.containsKey("endDate")){
                        query.filter("published",">=",reportParam.get("startDate").toString().concat(" 00:00:00"));
                        query.filter("published","<=",reportParam.get("endDate").toString().concat(" 23:59:59"));
                    }
                } else {
                    if(!ispopup) {
                        viewEntitys.getDataSearch(query, request);
                    }
                }
                if(ispopup && !modulename.isEmpty()) {
                    addPopupQueryFilter(httpRequest,viewEntitys,query,modulename,record,linkage);
                }else {
                    addQueryFilter(httpRequest, viewEntitys, query);
                }
                if (!Utils.isEmpty(httpRequest.get("exclude"))) {
                    if (httpRequest.get("exclude") instanceof List) {
                        query.filter("id", "!in", httpRequest.get("exclude"));
                    } else if (httpRequest.get("exclude") instanceof String) {
                        query.filter("id", "!=", httpRequest.get("exclude"));
                    }
                }
                //弹窗查询
                if (ispopup && !Utils.isEmpty(httpRequest.get("popupkeyfield"))) {
                    if (!Utils.isEmpty(httpRequest.get("popupkeyword"))) {
                        String field = httpRequest.get("popupkeyfield").toString();
                        String fieldvalue = httpRequest.get("popupkeyword").toString();
                        if("author".equals(field) || "finishapprover".equals(field)) {
                            query.filter(viewEntitys.getAuthorfilter(field, "like", fieldvalue));
                        }else if(Arrays.asList("updated","published","submitdatetime","approvaldatetime").contains(field)) {
                            if(DateTimeUtils.isDate(fieldvalue)){
                                String date = DateTimeUtils.getDatetime(DateTimeUtils.toDateTime(fieldvalue),DateTimeUtils.DATE_TIME_FORMAT);
                                query.filter(IPublicService.checkFilter(field), ">=", date);
                            } else {
                                query.filter(IPublicService.checkFilter(field), "=", "null");
                            }
                        } else {
                            query.filter(IPublicService.checkFilter(field), "like", fieldvalue);
                        }
                    }
                }
            }

            //添加数据权限过滤
            if(Utils.isNotEmpty(viewEntitys.getDataPermission())) {
                List<String> auths = AuthorizeUtils.getAuthorizesByProfileId(ProfileUtils.getCurrentProfileId());
                if(!auths.isEmpty()) {
                    for(String auth: auths){
                        if(viewEntitys.getDataPermission().containsKey(auth)){
                            String filterStr = ExpressionUtils.execute(viewEntitys.getDataPermission().get(auth));
                            if(Utils.isNotEmpty(filterStr)){
                                query.filter(filterStr);
                            }
                        }
                    }
                }
            }

            if(Boolean.FALSE.equals(query.isOrder())){
                query.order("published", "D_N");
            }
            List<Object> result = query.execute();
            long count = query.getTotalCount();
            if(result.isEmpty() && page > 1){
                query.begin(0).end(limit);
                result = query.execute();
                count = query.getTotalCount();
            }
            List<Object> lists = new ArrayList<>();
            if (!result.isEmpty()) {
                result.forEach(item -> {
                    try {
                        Object data = ClassUtils.create(entity,item);
                        if(Utils.isNotEmpty(data)){
                            lists.add(data);
                        }
                    }catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                });
            }

            List<String> statisticFields = viewEntitys.getStatisticFields();
            List<Object> statistic = new ArrayList<>();
            if(!statisticFields.isEmpty()){
                XN_Query statisticQuery = XN_Query.create(viewEntitys.getDataType() + "_Count")
                        .notDelete().tag(viewEntitys.getTabName())
                        .filter("type", "eic", viewEntitys.getTabName())
                        .begin((page - 1) * limit)
                        .end(page * limit)
                        .rollup(statisticFields);
                if (Utils.isNotEmpty(filter)) {
                    //企业模块数据过滤
                    IPublicService.addSupplierFilter(query);
                    statisticQuery.filter(filter);
                } else {
                    //查看权限过滤
                    if(Boolean.TRUE.equals(viewEntitys.getModuletab().isexclusive)) {
                        if(Utils.isNotEmpty(exclusiveFilters)) {
                            statisticQuery.filter(XN_Filter.any(exclusiveFilters));
                        }
                    }
                    //企业模块数据过滤
                    IPublicService.addSupplierFilter(query);
                    //报表类型过滤
                    if(!Utils.isEmpty(httpRequest.get("reportParam"))){
                        Map<String,Object> reportParam= (Map<String, Object>) httpRequest.get("reportParam");
                        for(Map.Entry<String,Object> entry: reportParam.entrySet()){
                            if("startDate".equals(entry.getKey())||"endDate".equals(entry.getKey())){
                                continue;
                            }
                            statisticQuery.filter("my."+entry.getKey(),"=",reportParam.get(entry.getKey()));
                        }
                        if(reportParam.containsKey("startDate")&&reportParam.containsKey("endDate")){
                            statisticQuery.filter("published",">=",reportParam.get("startDate").toString().concat(" 00:00:00"));
                            statisticQuery.filter("published","<=",reportParam.get("endDate").toString().concat(" 23:59:59"));
                        }
                    } else {
                        viewEntitys.getDataSearch(statisticQuery, request);
                    }

                    if(ispopup && !modulename.isEmpty()) {
                        addPopupQueryFilter(httpRequest,viewEntitys,statisticQuery,modulename,record,linkage);
                    }else {
                        addQueryFilter(httpRequest, viewEntitys, statisticQuery);
                    }
                    if (!Utils.isEmpty(httpRequest.get("exclude"))) {
                        if (httpRequest.get("exclude") instanceof List) {
                            statisticQuery.filter("id", "!in", httpRequest.get("exclude"));
                        } else if (httpRequest.get("exclude") instanceof String) {
                            statisticQuery.filter("id", "!=", httpRequest.get("exclude"));
                        }
                    }
                    if (ispopup && !Utils.isEmpty(httpRequest.get("popupkeyfield")) && !Utils.isEmpty(httpRequest.get("popupkeyword"))) {
                        String field = httpRequest.get("popupkeyfield").toString();
                        String fieldvalue = httpRequest.get("popupkeyword").toString();
                        if("author".equals(field) || "finishapprover".equals(field)) {
                            statisticQuery.filter(viewEntitys.getAuthorfilter(field,"like",fieldvalue));
                        }else if(Arrays.asList("updated","published","submitdatetime","approvaldatetime").contains(field)) {
                            if(DateTimeUtils.isDate(fieldvalue)){
                                String date = DateTimeUtils.getDatetime(DateTimeUtils.toDateTime(fieldvalue),DateTimeUtils.DATE_TIME_FORMAT);
                                query.filter(IPublicService.checkFilter(field), ">=", date);
                            } else {
                                query.filter(IPublicService.checkFilter(field), "=", "null");
                            }
                        } else {
                            statisticQuery.filter(IPublicService.checkFilter(field), "like", fieldvalue);
                        }
                    }
                }
                statistic = statisticQuery.execute();
            }

            List<Object> finalStatistic = statistic;
            long finalCount = count;
            Map<String,Object> infoMap = new HashMap<>(1);
            infoMap.put("list", lists);
            infoMap.put("total", finalCount);
            infoMap.put("statistic",finalStatistic);
            return infoMap;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new HashMap<>(1);
    }

    /**
     * 标准删除记录
     */
    default void delete(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        if (!Utils.isEmpty(httpRequest.get("ids"))) {
            Object ids = httpRequest.get("ids");
            if (ids instanceof String) {
                 if (ids.toString().contains(",")) {
                     ids = Arrays.asList(ids.toString().split(","));
                 } else {
                     ids = new ArrayList<>(ImmutableSet.of((String) ids));
                 }
            } else if (ids instanceof Integer) {
                 ids = new ArrayList<>(ImmutableSet.of((String) ids.toString()));
            } else if (ids instanceof List) {
                ids = new ArrayList<>((List<String>) ids);
            }

            if (ids instanceof List && !((List<?>) ids).isEmpty()) {
                this.deleteBefore((List<String>) ids);
                List<Object> list = XN_Content.loadMany((List<String>) ids, viewEntitys.getTabName(),viewEntitys.getDataTypeVal());
                List<String> approvals = new ArrayList<>();
                for (Object item : list) {
                    try {
                        String fieldname = viewEntitys.getCustomViews().get(0).columnlist.get(0);
                        List<?> viewNames = viewEntitys.getFieldsName(Collections.singletonList(fieldname)).getOrDefault("viewNames", new ArrayList<String>());
                        String fieldlabel = "";
                        if(Utils.isNotEmpty(viewNames)) {
                            fieldlabel = (String) viewNames.get(0);
                        }
                        String fieldvalue = ((Content) item).id;
                        if (!Utils.isEmpty(fieldname)) {
                            fieldvalue = ((Content) item).get(fieldname, "记录").toString();
                        }

                        if (((viewEntitys.getModuletab().datarole != 1 && !(ProfileUtils.isAdmin() || ProfileUtils.isAssistant())) ||
                                (viewEntitys.getModuletab().datarole == 1 && !(ProfileUtils.isBoss() || ProfileUtils.isSupplierAssistant()))) &&
                                !((Content) item).get("author").equals(ProfileUtils.getCurrentProfileId())) {
                            approvals.add("[<span style='color: blue;'>" + fieldlabel + "：" + fieldvalue + "</span>] 创建者不是您，不允许删除！<br>");
                        } else if (Utils.isNotEmpty(((Content) item).my.get("approvalstatus")) && Integer.parseInt(((Content) item).my.get("approvalstatus").toString(),10) > 0) {
                            if ("1".equals(((Content) item).my.get("approvalstatus")) || "2".equals(((Content) item).my.get("approvalstatus")) || "4".equals(((Content) item).my.get("approvalstatus"))) {
                                approvals.add("[<span style='color: blue;'>" + fieldlabel + "：" + fieldvalue + "</span>] " + ("1".equals(((Content) item).my.get("approvalstatus")) ? "等待审批" : "2".equals(((Content) item).my.get("approvalstatus")) ? "已经通过审批" : "已提交") + "，不允许删除！<br>");
                            }
                        } else {
                            WorkflowStatus workflowStatus = WorkflowUtils.getWorkflowStatus(viewEntitys.getModuleName(),((Content) item).id);
                            if(workflowStatus.isStartflow()) {
                                approvals.add("[<span style='color: blue;'>" + fieldlabel + "：" + fieldvalue + "</span>] " + (workflowStatus.isFinished() ? "已经办理完成" : !workflowStatus.isFirstNode() ? "等待办理" : "已进入流程") + "，不允许删除！<br>");
                            }
                        }
                    }catch (Exception ignored) {}
                }
                if (!approvals.isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    approvals.forEach(sb::append);
                    approvals.clear();
                    throw new WebException(sb.toString());
                }
                List<Object> saves = new ArrayList<>(1);
                list.forEach(item -> {
                    ((Content) item).set("deleted", DateTimeUtils.gettimeStamp());
                    saves.add(item);
                    WorkflowUtils.deletedWorkflowInstance(viewEntitys.getModuleName(), ((Content) item).id);
                    WorkflowUtils.synchronizationWorkFlows(viewEntitys.getModuleName(),item);
                });
                if (!saves.isEmpty()) {
                    XN_Content.batchsave(saves, viewEntitys.getTabName()+",report,report_"+viewEntitys.getTabName());
                    saves.clear();
                }
                this.deleteAfter((List<String>) ids);
            }
        }
    }
    /**
     * 删除单条记录
     */
    default void deleteOneRecord(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        String record = httpRequest.getOrDefault("id","").toString();
        if(!Utils.isEmpty(record)){
            this.deleteBefore(Arrays.asList(record));
            Content content = XN_Content.load(record,viewEntitys.getTabName(),viewEntitys.getDataTypeVal());
            content.my.put("deleted",DateTimeUtils.gettimeStamp());
            content.save(viewEntitys.getTabName()+",report,report_"+viewEntitys.getTabName());
            this.deleteAfter(Arrays.asList(record));
        } else {
            throw new WebException("保存失败，参数错误");
        }
    }
    /**
     * 获取记录实体
     * @param record 为数据库唯一ID，为空时为自动创建一条新记录，不为空时加载记录
     * @param viewEntitys 数据模块实体
     * @param entity 数据实本的类名
     * @return 返回Entity实体对像
     */
    default Object getObjectByRecord(Object record, BaseEntityUtils viewEntitys, Class<?> entity) {
        Object data = null;
        try{
            Object customResult = null;
            try{
                if (Utils.isNotEmpty(record)) {
                    customResult = this.getCustomEditEntity(record.toString());
                } else {
                    customResult = this.getCustomEditEntity(null);
                }
            }catch (Exception ignored) {}
            if (Utils.isNotEmpty(customResult)) {
                return customResult;
            }
            Content result;
            if(Utils.isNotEmpty(record)){
                result = XN_Content.load(record.toString(), viewEntitys.getTabName(), viewEntitys.getDataTypeVal());
            } else {
                result = XN_Content.create(viewEntitys.getTabName(), "", ProfileUtils.getCurrentProfileId(), viewEntitys.getDataTypeVal());
                result = result.set("createnew", "1").set("deleted", "-1").save(viewEntitys.getTabName());
            }
            data = ClassUtils.create(entity,result);
            if(Utils.isNotEmpty(data)){
                ClassUtils.exeMethod(data,"setEditState",Utils.isNotEmpty(record));
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
        return data;
    }

    /**
     * 外部数据引用函数
     */
    default Object getOutsideValue(String linkvalue, String relmodule) {
        List<String> result = new ArrayList<>();
        if (!Utils.isEmpty(linkvalue) && !Utils.isEmpty(relmodule)) {
            try {
                List<Object> tabs = XN_Query.contentQuery().tag("tabs")
                        .filter("type","eic","tabs")
                        .filter("my.modulename","eic",relmodule)
                        .notDelete().end(1).execute();
                if(!tabs.isEmpty()){
                    Tab tab = new Tab(tabs.get(0));
                    List<Object> entitynames = XN_Query.contentQuery().tag("entitynames")
                            .filter("type","eic","entitynames")
                            .filter("my.modulename","eic",relmodule)
                            .notDelete().end(1).execute();
                    if(!entitynames.isEmpty()){
                        EntityLink entityLink = new EntityLink(entitynames.get(0));
                        if (!Utils.isEmpty(entityLink.entityfield) && !Utils.isEmpty(entityLink.fieldname)) {
                            String linkfield = entityLink.entityfield;
                            String linkname = entityLink.fieldname;
                            if (linkname != null && !linkname.isEmpty()) {
                                if (linkfield != null && !linkfield.isEmpty()) {
                                    List<String> queryvalue;
                                    if(linkvalue.contains(",")){
                                        queryvalue = Arrays.asList(linkvalue.split(","));
                                    } else {
                                        queryvalue = Collections.singletonList(linkvalue);
                                    }
                                    if(!queryvalue.isEmpty()){
                                        List<Object> contents;
                                        if ("id".equals(linkfield) || "xn_id".equals(linkfield)) {
                                            contents = XN_Content.loadMany(queryvalue, tab.tabname, BaseEntityUtils.getDataTypeVal(tab.datatype));
                                        } else {
                                            contents = XN_Query.create(tab.datatype).tag(tab.tabname).notDelete()
                                                    .filter("type", "eic", tab.tabname)
                                                    .filter(IPublicService.checkFilter(linkfield), "in", queryvalue)
                                                    .end(1).execute();
                                        }
                                        if(!contents.isEmpty()){
                                            for(Object item: contents){
                                                Content content = (Content) item;
                                                if("0".equals(content.get("deleted"))){
                                                    result.add(content.get(linkname).toString());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return String.join(",",result);
    }

    /**
     * 获取模块指定记录的实体
     */
    default Object load(String record) throws Exception {
        List<Object> entitys = this.loadMany(Arrays.asList(record));
        if (entitys.size() == 0) {
            throw new WebException("no record");
        }
        return entitys.get(0);
    }
    default List loadMany(List<String> ids) throws Exception {
        try {
            String className = this.getClass().getPackage().getName();
            Matcher m = MODULE_PATTERN.matcher(className);
            if (m.find()) {
                String packageName = m.group(1);
                String moduleName = m.group(2);
                BaseEntityUtils viewEntitys = new BaseEntityUtils(moduleName);

//                Class<?> entity = Class.forName(packageName + "." + moduleName + ".entitys." + moduleName);
                ClassLoader loader = SpringServiceUtil.getApplicationContext().getClassLoader();
                Class<?> entity = loader.loadClass(packageName + "." + moduleName + ".entitys." + moduleName);
                List<Content> lists = XN_Content.loadMany(ids, viewEntitys.getTabName(), BaseEntityUtils.getDataTypeVal(viewEntitys.getDataType()));
                List<Object> entitys = new ArrayList<>();
                for(Content item : lists) {
                    try {
                        entitys.add(ClassUtils.create(entity,item));
                    } catch (Exception ignored){}
                }
                return entitys;
            }
            throw new WebException("Unable to detect module configuration information");
        }catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取模块指定记录的实体
     */
    default Object load(String record,String tag,int datatype,Class<?> entity) throws Exception {
        if(!Utils.isEmpty(record)) {
            Content result = XN_Content.load(record, tag, datatype);
            return ClassUtils.create(entity,result);
        }
        throw new WebException("no record");
    }
    /**
     * 保存
     */
    default void batchSave(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys, Class<?> entity) throws Exception {
        String record = httpRequest.getOrDefault("record","").toString();
        List<String> records = Arrays.asList(record.split(","));
        Map<String, Object> requests = (Map<String, Object>)((HashMap)httpRequest).clone();
        for (Map.Entry<String, Object> entry : httpRequest.entrySet()) {
            if (Utils.isEmpty(entry.getValue())) {
                requests.remove(entry.getKey());
            }
        }
        for(String item : records) {
            requests.put("record",item);
            save(requests, viewEntitys, entity);
        }
    }
    /**
     * 保存
     */
    default void save(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys, Class<?> entity) throws Exception {
        this.saveBefore(httpRequest,viewEntitys,entity);
        String record = httpRequest.getOrDefault("record","").toString();
        if(Utils.isNotEmpty(record)){
            Map<String,String> uniqueFieldMaps = viewEntitys.getModuleField().values().stream().filter( v -> v.unique).collect(Collectors.toMap(v1 -> v1.fieldname, v1 -> v1.fieldlabel, (k1, k2) -> k1));
            for(String uniqueField : uniqueFieldMaps.keySet()) {
                if (httpRequest.containsKey(uniqueField) && Utils.isNotEmpty(httpRequest.get(uniqueField))) {
                    String fieldValue = httpRequest.get(uniqueField).toString();
                    XN_Query query = XN_Query.create("Content").tag(viewEntitys.getTabName())
                    .filter("type", "eic", viewEntitys.getTabName())
                    .filter("my.deleted", "=", '0')
                    .filter("my."+uniqueField, "=", fieldValue)
                    .filter("id", "!=", record)
                    .end(1);
                    if (viewEntitys.getModuleField().containsKey("supplierid")) {
                        if(Boolean.TRUE.equals(ProfileUtils.isSupplier())) {
                            query.filter("my.supplierid", "=", SupplierUtils.getSupplierid());
                        }
                    }
                    List<Object> objs = query.execute();
                    if (objs.size() > 0) {
                        throw new WebException("保存失败，"+ uniqueFieldMaps.get(uniqueField) + "不允许重复!");
                    }
                }
            }
            Content content = XN_Content.load(record,viewEntitys.getTabName(),viewEntitys.getDataTypeVal());
            Object tmpEntity = ClassUtils.create(entity,content);
            if(Utils.isNotEmpty(tmpEntity)) {
                ClassUtils.exeMethod(tmpEntity, "fromRequest", httpRequest);
                ClassUtils.setValue(tmpEntity, "deleted", 0);
                ClassUtils.setValue(tmpEntity, "createnew", 0);
                String moduleNumber = viewEntitys.getModuleName().toLowerCase() + "_no";
                if (Utils.isNotEmpty(ClassUtils.getField(tmpEntity, moduleNumber))) {
                    if ("false".equals(httpRequest.getOrDefault("isEditState", "true")) && Utils.isEmpty(ClassUtils.getValue(tmpEntity, moduleNumber))) {
                        ClassUtils.setValue(tmpEntity, moduleNumber, XN_ModentityNum.get(viewEntitys.getModuleName()));
                    }
                }
                if (Utils.isEmpty(ClassUtils.getValue(tmpEntity, "supplierid")) || "0".equals(ClassUtils.getValue(tmpEntity, "supplierid"))) {
                    ClassUtils.setValue(tmpEntity, "supplierid", SupplierUtils.getSupplierid());
                }
                if (Boolean.TRUE.equals(viewEntitys.getWorkflowStatus().isApproval())) {
                    if (Utils.isEmpty(ClassUtils.getValue(tmpEntity, "approvalstatus")) || Integer.parseInt(Objects.requireNonNull(ClassUtils.getValue(tmpEntity, "approvalstatus")).toString()) == -1) {
                        ClassUtils.setValue(tmpEntity, "approvalstatus", 0);
                    }
                    if (Utils.isEmpty(ClassUtils.getValue(tmpEntity,"activiti_circulations")) || Integer.parseInt(Objects.requireNonNull(ClassUtils.getValue(tmpEntity, "activiti_circulations")).toString()) == -1) {
                        ClassUtils.setValue(tmpEntity, "activiti_circulations", 0);
                    }
                }
                if(Boolean.TRUE.equals(viewEntitys.getWorkflowStatus().isWorkflow())) {
                    if (Utils.isEmpty(ClassUtils.getValue(tmpEntity, "activiti_executionstatus")) || Integer.parseInt(Objects.requireNonNull(ClassUtils.getValue(tmpEntity, "activiti_executionstatus")).toString()) == -1) {
                        ClassUtils.setValue(tmpEntity, "activiti_executionstatus", 0);
                    }
                }
                modifyLog(tmpEntity, entity, content, viewEntitys);
                ClassUtils.exeMethod(tmpEntity, "toContent", content);
                this.saveBefore(content);
                content.save(viewEntitys.getTabName()+",report,report_"+viewEntitys.getTabName());
                this.saveAfter(content);
                BaseEntityUtils.deleteOutsideLink(record);
                this.saveAfter(httpRequest,viewEntitys,entity);
                if(Boolean.TRUE.equals(viewEntitys.getWorkflowStatus().isWorkflow())) {
                    WorkflowUtils.synchronizationWorkFlows(viewEntitys.getModuleName(), content);
                }
            } else {
                throw new WebException("保存失败，参数错误");
            }
        } else {
            throw new WebException("保存失败，参数错误");
        }
    }

    static void modifyLog(Object obj,Class<?> entity,Content content,BaseEntityUtils viewEntitys) {
        if (content.my.containsKey("approvalstatus") && Arrays.asList("2","4").contains(content.my.get("approvalstatus").toString())) {
            Field[] allFields = entity.getFields();
            HashMap<String, HashMap<String, String>> modifys = new HashMap<>();
            for (Field field : allFields) {
                String fieldname = field.getName();
                if (field.getModifiers() == Modifier.PUBLIC) {
                    try {
                            String newValue = "";
                            String oldValue = "";
                            if (!Arrays.asList("id","author","published","updated","title","isEditState","application").contains(fieldname)) {
                                if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
                                    NumberFormat nf = NumberFormat.getInstance();
                                    nf.setGroupingUsed(false);
                                    newValue = nf.format(field.get(obj));
                                } else {
                                    newValue = field.get(obj).toString();
                                }
                                if (content.my.containsKey(fieldname)) {
                                    oldValue = content.my.get(fieldname).toString();
                                }
                            }
                            if (!newValue.equals(oldValue)) {
                                HashMap<String, String> modify = new HashMap<>();
                                modify.put("newvalue",newValue);
                                modify.put("oldvalue",oldValue);
                                modifys.put(fieldname,modify);
                            }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            }
            if (!modifys.isEmpty()) {
                try {
                    StringBuilder body = new StringBuilder();
                    HashMap<String, TabField> tabFields =  viewEntitys.getModuleField();
                    modifys.forEach((key, value) -> {
                        if (tabFields.containsKey(key)) {
                            String fieldlabel = tabFields.get(key).fieldlabel;
                            body.append(fieldlabel).append(": ").append(value.get("oldvalue")).append(" => ").append(value.get("newvalue")).append("\n");
                        }
                    });
                    XN_Content.create("modifylog","",ProfileUtils.getCurrentProfileId(),7)
                        .add("deleted",0)
                        .add("supplierid",SupplierUtils.getSupplierid())
                        .add("body",body.toString())
                        .add("belongmodule",viewEntitys.getModuleName())
                        .add("record",content.id)
                        .add("table",viewEntitys.getTabName())
                        .add("deleted","0")
                        .save("modifylog,report,report_modifylog");
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }
    /**
     * 获取分类树
     */
    default Object getTree(BaseEntityUtils viewEntitys,Class<?> entity) {
        List<Object> result = new ArrayList<>();
        try{
            List<String> fields = viewEntitys.getModuleField().entrySet().stream().map(v-> v.getValue().fieldname).collect(Collectors.toList());
            XN_Query query = XN_Query.create(viewEntitys.getDataType()).tag(viewEntitys.getTabName())
                    .filter("type","eic",viewEntitys.getTabName())
                    .order("my.sequence","A_N")
                    .notDelete().end(-1);
            if (fields.contains("supplierid")) {
                query.filter("my.supplierid", "=", SupplierUtils.getSupplierid());
            }
            List<Object> objs = query.execute();
            if(!objs.isEmpty()) {
                for(Object item: objs){
                    Object obj = entity.getDeclaredConstructor(Object.class).newInstance(item);
                    Map<String,Object> infoMap = new HashMap<>(1);
                    infoMap.put("pId", entity.getField("parentid").get(obj).toString());
                    infoMap.put("id", entity.getField("id").get(obj).toString());
                    infoMap.put("title", entity.getField("name").get(obj).toString());
                    infoMap.put("name", entity.getField("name").get(obj).toString());
                    result.add(infoMap);
                }
            } else {
                Content newContent = XN_Content.create(viewEntitys.getTabName(),"",ProfileUtils.getCurrentProfileId(), viewEntitys.getDataTypeVal())
                        .add("parentid","")
                        .add("name","分类")
                        .add("sequence","0")
                        .add("deleted","0");
                if (fields.contains("supplierid")) {
                    newContent.add("supplierid", SupplierUtils.getSupplierid());
                }
                Object item = newContent.save(viewEntitys.getTabName());
                Object obj = entity.getDeclaredConstructor(Object.class).newInstance(item);
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("pId", entity.getField("parentid").get(obj).toString());
                infoMap.put("id", entity.getField("id").get(obj).toString());
                infoMap.put("title", entity.getField("name").get(obj).toString());
                infoMap.put("name", entity.getField("name").get(obj).toString());
                result.add(infoMap);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
        }

        return result;
    }
    /**
     * 保存分类树
     */
    default void saveTree(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        List<String> fields = viewEntitys.getModuleField().entrySet().stream().map(v-> v.getValue().fieldname).collect(Collectors.toList());
        if(Boolean.parseBoolean(httpRequest.getOrDefault("isroot",false).toString())){
            Object sequence = httpRequest.getOrDefault("sequence","0");
            if (Utils.isEmpty(sequence)) {
                sequence = "0";
            }
            Content newContent = XN_Content.create(viewEntitys.getTabName(),"",ProfileUtils.getCurrentProfileId(), viewEntitys.getDataTypeVal())
                    .add("parentid","")
                    .add("name",httpRequest.getOrDefault("name",""))
                    .add("sequence",sequence)
                    .add("deleted","0");
            if (fields.contains("supplierid")) {
                newContent.add("supplierid", SupplierUtils.getSupplierid());
            }
            if (httpRequest.containsKey("image")) {
                newContent.my.put("image",httpRequest.getOrDefault("image",""));
            }
            newContent.save(viewEntitys.getTabName()+",report,report_"+viewEntitys.getTabName());
        }else {
            String record = httpRequest.getOrDefault("record","").toString();
            if(!Utils.isEmpty(record)){
                Object sequence = httpRequest.getOrDefault("sequence","0");
                if (Utils.isEmpty(sequence)) {
                    sequence = "0";
                }
                Content content = XN_Content.load(record,viewEntitys.getTabName(),viewEntitys.getDataTypeVal());
                content.my.put("deleted",0);
                content.my.put("createnew",0);
                content.my.put("sequence",sequence);
                if (httpRequest.containsKey("parentid")) {
                    content.my.put("parentid",httpRequest.get("parentid").toString());
                }
                if (httpRequest.containsKey("name")) {
                    content.my.put("name",httpRequest.get("name").toString());
                }
                if (fields.contains("supplierid")) {
                    content.my.put("supplierid", SupplierUtils.getSupplierid());
                }
                if (httpRequest.containsKey("image")) {
                    content.my.put("image",httpRequest.getOrDefault("image",""));
                }
                content.save(viewEntitys.getTabName()+",report,report_"+viewEntitys.getTabName());
                BaseEntityUtils.deleteOutsideLink(record);
            } else {
                throw new WebException("保存失败，参数错误");
            }
        }
    }
    /**
     * 停用操作
     */
    default void inactive(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        Object record = httpRequest.getOrDefault("ids",null);
        if(!Utils.isEmpty(record)){
            if(record instanceof  String){
                record = new ArrayList<>(ImmutableSet.of(record.toString()));
            }
            if(record instanceof List){
                try{
                    List<Object> query = XN_Content.loadMany((List<String>) record,viewEntitys.getTabName(),viewEntitys.getDataTypeVal());
                    if(!query.isEmpty()){
                        List<Object> saveds = new ArrayList<>();
                        query.forEach(item -> {
                            ((Content)item).my.put("status","Inactive");
                            saveds.add(item);
                        });
                        if(!saveds.isEmpty()){
                            XN_Content.batchsave(saveds,viewEntitys.getTabName()+",report,report_"+viewEntitys.getTabName());
                            saveds.clear();
                        }
                    }
                }catch (Exception e){
                    throw new WebException("参数错误！启用失败");
                }
            } else {
                throw new WebException("参数错误！启用失败");
            }
        } else {
            throw new WebException("参数错误！启用失败");
        }
    }
    /**
     * 启用操作
     */
    default  void active(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        Object record = httpRequest.getOrDefault("ids",null);
        if(!Utils.isEmpty(record)){
            if(record instanceof  String){
                record = new ArrayList<>(ImmutableSet.of(record.toString()));
            }
            if(record instanceof List){
                try{
                    List<Object> query = XN_Content.loadMany((List<String>) record,viewEntitys.getTabName(),viewEntitys.getDataTypeVal());
                    if(!query.isEmpty()){
                        List<Object> saveds = new ArrayList<>();
                        query.forEach(item -> {
                            ((Content)item).my.put("status","Active");
                            saveds.add(item);
                        });
                        if(!saveds.isEmpty()){
                            XN_Content.batchsave(saveds,viewEntitys.getTabName()+",report,report_"+viewEntitys.getTabName());
                            saveds.clear();
                        }
                    }
                }catch (Exception e){
                    throw new WebException("启用失败");
                }
            } else {
                throw new WebException("参数错误！启用失败");
            }
        } else {
            throw new WebException("参数错误！启用失败");
        }
    }
    /**
     * 提交操作
     */
    default void submitOnline(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        if(!Utils.isEmpty(httpRequest.get("record"))){
            try{
                Content info = XN_Content.load(httpRequest.get("record").toString(),viewEntitys.getTabName(),viewEntitys.getDataTypeVal());
                info.add("approvalstatus","4");
                info.add("submitdatetime",DateTimeUtils.getDatetime());
                info.add("approvaldatetime",DateTimeUtils.getDatetime());
                info.add("finishapprover", ProfileUtils.getCurrentProfileId());
                info.save(viewEntitys.getTabName()+",report,report_"+viewEntitys.getTabName());
            }catch (Exception e){
                throw new WebException("提交失败");
            }
        } else {
            throw new WebException("提交失败！参数错误");
        }
    }
    /**
     * 批量提交操作
     */
    default void batchSubmitOnline(Map<String, Object> httpRequest, BaseEntityUtils viewEntitys) throws Exception {
        Object record = httpRequest.getOrDefault("ids",null);
        if(!Utils.isEmpty(record)){
            if(record instanceof  String){
                record = new ArrayList<>(ImmutableSet.of(record.toString()));
            }
            if(record instanceof List){
                try{
                    List<Object> query = XN_Content.loadMany((List<String>) record,viewEntitys.getTabName(),viewEntitys.getDataTypeVal());
                    if(!query.isEmpty()){
                        List<Object> saveds = new ArrayList<>();
                        query.forEach(item -> {
                            ((Content)item).add("approvalstatus","4");
                            ((Content)item).add("submitdatetime",DateTimeUtils.getDatetime());
                            ((Content)item).add("approvaldatetime",DateTimeUtils.getDatetime());
                            ((Content)item).add("finishapprover",ProfileUtils.getCurrentProfileId());
                            saveds.add(item);
                        });
                        if(!saveds.isEmpty()){
                            XN_Content.batchsave(saveds,viewEntitys.getTabName()+",report,report_"+viewEntitys.getTabName());
                            saveds.clear();
                        }
                    }
                }catch (Exception e){
                    throw new WebException("批量提交失败！");
                }
            } else {
                throw new WebException("批量失败！");
            }
        } else {
            throw new WebException("批量失败！");
        }
    }
    /**
     * 前置验证函数
     */
    default Object getActionVerify(String param, String modulename, String record, BaseEntityUtils viewEntitys) {
        if("Approval".equals(param) || "SubmitOnline".equals(param)){
            try{
                if (Utils.isNotEmpty(record)) {
                    Content info = XN_Content.load(record, viewEntitys.getTabName(), viewEntitys.getDataTypeVal());
                    if ("-1".equals(info.my.get("deleted")) || !info.author.equals(ProfileUtils.getCurrentProfileId()) || "1".equals(info.my.get("approvalstatus")) || "2".equals(info.my.get("approvalstatus")) || "4".equals(info.my.get("approvalstatus"))) {
                        return false;
                    }
                }
            }catch (Exception e){
                return false;
            }
            if(ProfileUtils.isBoss() || ProfileUtils.isAssistant()) {
                return true;
            }
            return RolesUtils.isEdit(modulename);
        }
        if("BatchSubmitOnline".equals(param)) {
            if(ProfileUtils.isBoss() || ProfileUtils.isAssistant()) {
                return true;
            }
            return RolesUtils.isEdit(modulename);
        }

        if("neworder".equals(param)){

        }
        return this.actionVerify(param,modulename,record,viewEntitys);
    }

    /**
     * 工作流事件
     * @param record    流程记录
     * @param eventType 流程事件
     *                  PROCESSSTARTED：流程启动
     *                  PROCESSCOMPLETED：流程结束
     *                  EVACUATE：发起人撤回
     *                  ROLLBACK：办理人员退回
     *                  REPUBLISH：流程重新发布
     *                  DELETED：流程删除
     *                  TASKCOMPLETED：办理人员办理完成
     */
    default void workFlowEvent(String record, String eventType) {}

    /**
     * 审批流事件
     * @param record    流程记录
     * @param eventType 流程事件
     *                  PROCESSSTARTED：流程启动
     *                  PROCESSCOMPLETED：流程结束
     *                  EVACUATE：发起人撤回
     *                  ROLLBACK：办理人员退回
     *                  REPUBLISH：流程重新发布
     *                  DELETED：流程删除
     *                  TASKCOMPLETED：办理人员办理完成
     */
    default void approvalFlowEvent(String record, String eventType) {}

    /**
     * 工作流待办事件
     * @param record 记录ID
     * @param dealtwith 待办人ID
     */
    default void workFlowDealtWith(String record, String dealtwith) {}

    /**
     * 审批流待办事件
     * @param record 记录ID
     * @param dealtwith 待办人ID
     */
    default void approvalFlowDealtWith(String record, String dealtwith) {}
}
