package com.qnkj.core.base.services.impl;

import com.github.restapi.XN_Content;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.common.services.*;
import com.qnkj.common.utils.CallbackUtils;
import com.qnkj.common.utils.ClassUtils;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.services.IBaseEntityService;
import com.qnkj.core.base.services.IBaseService;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.webconfigs.exception.WebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Slf4j
public class BaseEntityServiceImpl implements IBaseEntityService {

    private final ITabServices tabServices;
    private final IBlockServices blockServices;
    private final ILayoutServices layoutServices;
    private final ICustomViewServices customViewServices;
    private final ITabFieldServices tabFieldServices;
    private final ISearchColumnServices searchColumnServices;
    private final IEntityLinkServices entityLinkServices;
    private final IOutsideLinkServices outsideLinkServices;
    private final IActionServices actionServices;
    private final IPopupDialogServices popupDialogServices;
    private final IModuleMenuServices moduleMenuServices;
    private final IDataPermissionServices dataPermissionServices;

    public BaseEntityServiceImpl(IModuleMenuServices moduleMenuServices,
                                 ITabServices tabServices,
                                 ICustomViewServices customViewServices,
                                 ITabFieldServices tabFieldServices,
                                 ISearchColumnServices searchColumnServices,
                                 IOutsideLinkServices outsideLinkServices,
                                 IEntityLinkServices entityLinkServices,
                                 IActionServices actionServices,
                                 IPopupDialogServices popupDialogServices,
                                 IBlockServices blockServices,
                                 ILayoutServices layoutServices,
                                 IDataPermissionServices dataPermissionServices) {
        this.tabServices = tabServices;
        this.blockServices = blockServices;
        this.layoutServices = layoutServices;
        this.customViewServices = customViewServices;
        this.tabFieldServices = tabFieldServices;
        this.searchColumnServices = searchColumnServices;
        this.entityLinkServices = entityLinkServices;
        this.outsideLinkServices = outsideLinkServices;
        this.actionServices = actionServices;
        this.popupDialogServices = popupDialogServices;
        this.moduleMenuServices = moduleMenuServices;
        this.dataPermissionServices = dataPermissionServices;
    }

    @Override
    public List<Object> getEntityUtils(String modulename) {
        try {
            ModuleMenu moduleMenu = moduleMenuServices.get(modulename);
            if(Utils.isEmpty(moduleMenu)) {
                return Collections.emptyList();
            }
            Tab moduleinfo = tabServices.get(modulename);
            if (Utils.isEmpty(moduleinfo)) {
                return Collections.emptyList();
            }
            BaseDataConfig dataConfig = new BaseDataConfig();
            BaseActionsConfig actionsConfig = new BaseActionsConfig();
            dataConfig.tabs = moduleinfo;
            dataConfig.moduleMenu = moduleMenu;
            actionsConfig.tabs = moduleinfo;
            List<Block> blocks = blockServices.list(modulename);
            if (!Utils.isEmpty(blocks)) {
                dataConfig.blocks = blocks;
            }
            List<Layout> layouts = layoutServices.list(modulename);
            if (!Utils.isEmpty(layouts)) {
                dataConfig.layouts = layouts;
            }
            List<CustomView> customviews = customViewServices.list(modulename, ProfileUtils.getCurrentProfileId());
            if (!Utils.isEmpty(customviews)) {
                dataConfig.customViews = customviews;
            }
            List<TabField> tabfields = tabFieldServices.list(modulename);
            if (!Utils.isEmpty(tabfields)) {
                dataConfig.fields = tabfields;
            }
            List<SearchColumn> searchcolumns = searchColumnServices.list(modulename);
            if (!Utils.isEmpty(searchcolumns)) {
                dataConfig.searchColumn = searchcolumns;
            }
            EntityLink entitylink = entityLinkServices.get(modulename);
            if (!Utils.isEmpty(entitylink)) {
                dataConfig.entityNames = entitylink;
            }
            List<OutsideLink> outsidelinks = outsideLinkServices.list(modulename);
            if (!Utils.isEmpty(outsidelinks)) {
                dataConfig.outsideLinks = outsidelinks;
            }
            PopupDialog popupdialog = popupDialogServices.get(modulename);
            if (!Utils.isEmpty(popupdialog)) {
                dataConfig.popupDialog = popupdialog;
            }
            DataPermission dataPermission = dataPermissionServices.get(modulename);
            if(Utils.isNotEmpty(dataPermission) && Utils.isNotEmpty(dataPermission.getExpressions())){
                dataConfig.dataPermission = dataPermission;
            }
            List<Action> actions = actionServices.list(modulename, -1);
            if (!Utils.isEmpty(actions)) {
                actionsConfig.actions = actions;
            }
            return Arrays.asList(dataConfig, actionsConfig);
        } catch (Exception e) {
            log.error("getEntityUtils." + modulename + " error ：" + e.toString());
        }
        return null;
    }

    private void addQueryFilter(String modulename,Map<String, Object> httpRequest, BaseEntityUtils viewEntitys,XN_Query query) {
        try{
            CallbackUtils.invokeByModule(modulename,"addQueryFilter",IBaseService.class,httpRequest,viewEntitys,query);
        }catch (Exception e) {
            log.error("======================================");
            log.error("自定义查询条件失败");
            log.error("模块名称：" + modulename);
            log.error("Error：" + e.getMessage());
            log.error("======================================");
        }
    }

    @Override
    public HashMap<String,Object> getExportExcelData(HttpServletRequest request) throws Exception {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        if(Utils.isEmpty(httpRequest.get("module"))) {
            throw new WebException("模块参数丢失！");
        }
        Tab tab = tabServices.get(httpRequest.get("module").toString());
        if(Utils.isEmpty(tab) || Utils.isEmpty(tab.tabname)) {
            throw new WebException("模块信息丢失！");
        }

        BaseEntityUtils viewEntitys = new BaseEntityUtils(tab.modulename);
        if (Utils.isEmpty(viewEntitys)) {
            throw new WebException("模块信息丢失！");
        }
        List<String> statisticFields = viewEntitys.getStatisticFields();
        List<Object> result = new ArrayList<>();
        List<Object> statistic = new ArrayList<>();
        if(Utils.isEmpty(httpRequest.get("ids"))) {
            Map<String,Object> queryinfo = viewEntitys.getOrderBy(request, viewEntitys.getModuleName());
            int page = 0;
            String orderby = queryinfo.get("orderby").toString();
            String ordertype = queryinfo.get("ordertype").toString();
            XN_Query query = XN_Query.create(viewEntitys.getDataType()).tag(viewEntitys.getTabName())
                    .filter("type", "eic", viewEntitys.getTabName())
                    .alwaysReturnTotalCount().notDelete()
                    .order(orderby, ordertype);
            viewEntitys.getDataSearch(query, request);
            addQueryFilter(tab.modulename,httpRequest,viewEntitys,query);
            List<Object> pageresult;
            do{
                pageresult = query.begin(page * 100).end((page+1)*100).execute();
                result.addAll(pageresult);
                page++;
            }while (pageresult.size() == 100);
            pageresult.clear();
            if(!statisticFields.isEmpty()){
                query = XN_Query.create(viewEntitys.getDataType() + "_Count").tag(viewEntitys.getTabName())
                        .filter("type", "eic", viewEntitys.getTabName())
                        .notDelete().rollup(statisticFields);
                viewEntitys.getDataSearch(query, request);
                addQueryFilter(tab.modulename,httpRequest,viewEntitys,query);
                statistic = query.execute();
            }
        } else {
            Object ids = httpRequest.get("ids");
            if(ids instanceof String){
                ids = Collections.singletonList(ids);
            }
            if(ids instanceof List) {
                result = XN_Content.loadMany((List<String>) ids, viewEntitys.getTabName(), viewEntitys.getDataTypeVal());
                if(!statisticFields.isEmpty()){
                    XN_Query query = XN_Query.create(viewEntitys.getDataType() + "_Count").tag(viewEntitys.getTabName())
                            .filter("type", "eic", viewEntitys.getTabName())
                            .filter("id","in",ids)
                            .notDelete().rollup(statisticFields);
                    statistic = query.execute();
                }
            }
        }
        List<String> viewFields = viewEntitys.getCustomViewFields(httpRequest.getOrDefault("viewid","").toString());
        HashMap<String,List<?>> viewFieldnames = viewEntitys.getFieldsName(viewFields);
        List<Object> records = new ArrayList<>(1);
        if(viewFieldnames.containsKey("viewFields") && !viewFieldnames.get("viewFields").isEmpty()) {
            for (Object item : result) {
                String id = "";
                try {
                    id = item.getClass().getField("id").get(item).toString();
                }catch (Exception e){
                    log.error(e.getMessage());
                }
                List<String> recordIds = new ArrayList<>();
                try {
                    for (Object fieldname : viewFieldnames.get("viewFields")) {
                        String itemvalue = Objects.requireNonNull(ClassUtils.exeMethod(item, "get", fieldname)).toString();
                        String value = viewEntitys.formatByUiType(id,fieldname.toString(), itemvalue, false);
                        recordIds.add(value);
                    }
                    records.add(recordIds);
                } catch (Exception e) {
                    recordIds.add("");
                }
            }
        }

        if(!statistic.isEmpty()){
            List<String> recordHj = new ArrayList<>();
            recordHj.add("合计");
            for(String fieldname: viewFields){
                if(statisticFields.contains(fieldname)){
                    for(Object item: statistic){
                        if(((Content)item).my.containsKey(fieldname)){
                            String value = viewEntitys.formatByUiType(((Content) item).id,fieldname, ((Content) item).my.getOrDefault(fieldname, "0"),false);
                            recordHj.add(value);
                        }
                    }
                } else {
                    recordHj.add("");
                }
            }
            records.add(recordHj);
        }
        HashMap<String,Object> infoMap = new HashMap<>(1);
        infoMap.put("result", records);
        infoMap.put("fieldnames", viewFieldnames.getOrDefault("viewNames", new ArrayList<>()));
        infoMap.put("title",viewEntitys.getModuleLabel());
        infoMap.put("name",viewEntitys.getModuleName());
        return infoMap;
    }

    @Override
    public void superDelete(HttpServletRequest request) throws Exception {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        if(!Utils.isEmpty(httpRequest.get("ids")) && !Utils.isEmpty(httpRequest.get("module"))){
            Tab tab = tabServices.get(httpRequest.get("module").toString());
            if(Utils.isEmpty(tab) || Utils.isEmpty(tab.tabname)) {
                throw new Exception("模块信息丢失！");
            }
            Object ids = httpRequest.get("ids");
            if(ids instanceof String){
                ids = Arrays.asList(ids);
            }
            if(ids instanceof List){
                List<Object> list = XN_Content.loadMany((List<String>) ids, tab.tabname);
                List<Object> saved = new ArrayList<>();
                for (Object item : list) {
                    ((Content)item).my.put("deleted", -DateTimeUtils.gettimeStamp());
                    saved.add(item);
                }
                if(!saved.isEmpty()){
                    XN_Content.batchsave(saved,tab.tabname);
                    saved.clear();
                }
            }
        } else {
            throw new Exception("数据和模块名不为空！");
        }
    }
}
