package com.qnkj.core.base;

import cn.hutool.core.util.NumberUtil;
import com.github.restapi.*;
import com.github.restapi.models.Content;
import com.github.restapi.models.Profile;
import com.google.common.collect.ImmutableSet;
import com.qnkj.common.configs.BaseActionsConfig;
import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.common.utils.*;
import com.qnkj.core.base.entitys.WorkflowStatus;
import com.qnkj.core.base.services.IBaseService;
import com.qnkj.core.base.services.IPublicService;
import com.qnkj.core.utils.*;
import com.qnkj.core.webconfigs.WebViews;
import com.qnkj.core.webconfigs.configure.FreemarkerConfig;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public class BaseEntityUtils {
    //region 内置变量
    private BaseDataConfig dataEntity;
    private BaseActionsConfig actionsEntity;
    private HashMap<String, TabField> moduleFields;
    private HashMap<String, OutsideLink> outsideLinks;
    private static HashMap<String, Object> outsideLinkList;
    private List<String> statisticFields;
    private String entityPackageName;
    private WorkflowStatus workflowStatus;

    //endregion

    //region 构造方法
    public static BaseEntityUtils init(Object module) {
        if (Utils.isEmpty(module)) {
            return null;
        }
        String[] packages = module.getClass().getPackage().getName().split("\\.");
        if (packages.length <= 0) {
            return null;
        }
        String modulename = packages[packages.length - 2];
        if(module.getClass().getPackage().getName().endsWith(".services.impl")){
            modulename = packages[packages.length - 3];
        }
        if (Utils.isEmpty(modulename)) {
            return null;
        }
        BaseEntityUtils viewEntitys = CacheBaseEntitys.getEntitys(modulename);
        if(Utils.isEmpty(viewEntitys)){
            viewEntitys = new BaseEntityUtils(modulename);
            viewEntitys.entityPackageName = String.join(".",Arrays.asList(packages).subList(0,packages.length - 1));
            if(!Utils.isEmpty(viewEntitys)) {
                CacheBaseEntitys.setEntitys(viewEntitys);
            }
        } else {
            if(Utils.isEmpty(viewEntitys.getWorkflowStatus())){
                viewEntitys.setWorkflowStatus(WorkflowUtils.getWorkflowStatus(modulename));
            }
        }
        if (Utils.isEmpty(viewEntitys.dataEntity)) {
            return null;
        }
        return viewEntitys;
    }

    public static BaseEntityUtils init(String modulename) {
        if (Utils.isEmpty(modulename)) {
            return null;
        }
        BaseEntityUtils viewEntitys = CacheBaseEntitys.getEntitys(modulename);
        if(Utils.isEmpty(viewEntitys)){
            viewEntitys = new BaseEntityUtils(modulename);
            if(!Utils.isEmpty(viewEntitys)) {
                CacheBaseEntitys.setEntitys(viewEntitys);
            }
        } else {
            if(Utils.isEmpty(viewEntitys.getWorkflowStatus())){
                viewEntitys.setWorkflowStatus(WorkflowUtils.getWorkflowStatus(modulename));
            }
        }
        if (Utils.isEmpty(viewEntitys.dataEntity)) {
            return null;
        }
        return viewEntitys;
    }

    public BaseEntityUtils(Object module) {
        this.dataEntity = null;
        this.actionsEntity = null;
        if (Utils.isEmpty(module)) {
            return;
        }
        String[] packages = module.getClass().getPackage().getName().split("\\.");
        if (packages.length <= 0) {
            return;
        }
        String modulename = packages[packages.length - 2];
        if(module.getClass().getPackage().getName().endsWith(".services.impl")){
            modulename = packages[packages.length - 3];
        }
        if (Utils.isEmpty(modulename)) {
            return;
        }
        entityPackageName = String.join(".",Arrays.asList(packages).subList(0,packages.length - 1));
        List<Object> entity = EntityUtils.getEntityUtils(modulename);
        if (Utils.isEmpty(entity)) {
            this.dataEntity = null;
            this.actionsEntity = null;
        } else {
            for (Object item : entity) {
                if (item instanceof BaseDataConfig) {
                    if (!Utils.isEmpty(item)) {
                        this.dataEntity = (BaseDataConfig) item;
                    } else {
                        this.dataEntity = null;
                    }
                } else if (item instanceof BaseActionsConfig) {
                    if (!Utils.isEmpty(item)) {
                        this.actionsEntity = (BaseActionsConfig) item;
                    } else {
                        this.actionsEntity = null;
                    }
                }
            }
        }
        if(Utils.isEmpty(outsideLinkList)) {
            outsideLinkList = new HashMap<>();
        }
        this.statisticFields = new ArrayList<>();
        this.getModuleFields();
        this.getOutsideLinks();
    }

    public BaseEntityUtils(String modulename) {
        this.dataEntity = null;
        this.actionsEntity = null;
        if (Utils.isEmpty(modulename)) {
            return;
        }
        List<Object> entity = EntityUtils.getEntityUtils(modulename);
        if (Utils.isEmpty(entity)) {
            this.dataEntity = null;
            this.actionsEntity = null;
        } else {
            for (Object item : entity) {
                if (item instanceof BaseDataConfig) {
                    if (!Utils.isEmpty(item)) {
                        this.dataEntity = (BaseDataConfig) item;
                    } else {
                        this.dataEntity = null;
                    }
                } else if (item instanceof BaseActionsConfig) {
                    if (!Utils.isEmpty(item)) {
                        this.actionsEntity = (BaseActionsConfig) item;
                    } else {
                        this.actionsEntity = null;
                    }
                }
            }
        }
        if(Utils.isEmpty(outsideLinkList)) {
            outsideLinkList = new HashMap<>();
        }
        if(Utils.isEmpty(workflowStatus)){
            workflowStatus = WorkflowUtils.getWorkflowStatus(modulename);
        }

        this.statisticFields = new ArrayList<>();
        this.getModuleFields();
        this.getOutsideLinks();
    }

    public BaseEntityUtils(BaseDataConfig bdEntity) {
        this.dataEntity = bdEntity;
        this.actionsEntity = null;
        if(Utils.isEmpty(outsideLinkList)) {
            outsideLinkList = new HashMap<>();
        }
        if(Utils.isEmpty(workflowStatus)){
            workflowStatus = WorkflowUtils.getWorkflowStatus(bdEntity.tabs.modulename);
        }
        this.statisticFields = new ArrayList<>();
        this.getModuleFields();
        this.getOutsideLinks();
    }

    public BaseEntityUtils(BaseDataConfig bdEntity, BaseActionsConfig baEntity) {
        this.dataEntity = bdEntity;
        this.actionsEntity = baEntity;
        if(Utils.isEmpty(outsideLinkList)) {
            outsideLinkList = new HashMap<>();
        }
        if(Utils.isEmpty(workflowStatus)){
            workflowStatus = WorkflowUtils.getWorkflowStatus(bdEntity.tabs.modulename);
        }
        this.statisticFields = new ArrayList<>();
        this.getModuleFields();
        this.getOutsideLinks();
    }

    @ApiOperation("根据字段定义格式化字段字典")
    private void getModuleFields() {
        this.moduleFields = new HashMap<>();
        if (!Utils.isEmpty(this.dataEntity) && !Utils.isEmpty(this.dataEntity.fields)) {
            this.dataEntity.fields.forEach(item -> {
                this.moduleFields.put(item.fieldname, item);
                if (item.isstatistic) {
                    this.statisticFields.add(item.fieldname);
                }
            });
        }
    }

    public HashMap<String,TabField> getModuleField(){
        return this.moduleFields;
    }

    public List<TabField> getModuleFieldSetting(){
        return this.dataEntity.fields;
    }

    private void getOutsideLinks() {
        this.outsideLinks = new HashMap<>();
        if (!Utils.isEmpty(this.dataEntity) && !Utils.isEmpty(this.dataEntity.outsideLinks)) {
            this.dataEntity.outsideLinks.forEach(item -> this.outsideLinks.put(item.fieldname, item));
        }
    }
    //endregion

    //region 对外访问的公共接口
    public static int getDataTypeVal(String datatype) {
        switch (datatype.toLowerCase()) {
            case "content":
                return 0;
            case "bigcontent":
                return 1;
            case "mq":
                return 2;
            case "maincontent":
                return 4;
            case "schedule":
                return 5;
            case "message":
                return 6;
            case "yearcontent":
                return 7;
            case "yearmonthcontent":
                return 9;
            default:
                return -1;
        }
    }

    public int getDataTypeVal() {
        return getDataTypeVal(this.dataEntity.tabs.datatype);
    }

    public String getDataType() {
        if (!Utils.isEmpty(this.dataEntity)) {
            return this.dataEntity.tabs.datatype;
        } else {
            return "";
        }
    }

    public String getTabName() {
        if (!Utils.isEmpty(this.dataEntity)) {
            return this.dataEntity.tabs.tabname;
        } else {
            return "";
        }
    }

    public String getModuleName() {
        if (!Utils.isEmpty(this.dataEntity)) {
            return this.dataEntity.tabs.modulename;
        } else {
            return "";
        }
    }

    public String getModuleLabel() {
        if (!Utils.isEmpty(this.dataEntity)) {
            return this.dataEntity.tabs.modulelabel;
        } else {
            return "";
        }
    }

    public List<CustomView> getCustomViews() {
        if (!Utils.isEmpty(this.dataEntity)) {
            return this.dataEntity.customViews;
        } else {
            return new ArrayList<>();
        }
    }

    public String getDefaultOrderBy(String viewid) {
        if(Utils.isEmpty(this.dataEntity.customViews)) {
            return "";
        }
        if(!Utils.isEmpty(viewid)) {
            for (CustomView item : this.dataEntity.customViews) {
                if (item.id.equals(viewid)) {
                    return item.orderby;
                }
            }
        }
        for (CustomView item : this.dataEntity.customViews) {
            if(!Utils.isEmpty(item.privateuser) && item.privateuser.equals(ProfileUtils.getCurrentProfileId()) && item.isdefault){
                return item.orderby;
            }
        }
        for (CustomView item : this.dataEntity.customViews) {
            if(!Utils.isEmpty(item.authorize) && AuthorizeUtils.isAuthorizes(ProfileUtils.getCurrentProfileId(), item.authorize)){
                return item.orderby;
            }
        }
        for (CustomView item : this.dataEntity.customViews) {
            if (item.isdefault) {
                return item.orderby;
            }
        }
        return "";
    }

    public String getDefaultOrder(String viewid) {
        if(Utils.isEmpty(this.dataEntity.customViews)) {
            return "";
        }
        if(!Utils.isEmpty(viewid)) {
            for (CustomView item : this.dataEntity.customViews) {
                if (item.id.equals(viewid)) {
                    return item.order;
                }
            }
        }
        for (CustomView item : this.dataEntity.customViews) {
            if(!Utils.isEmpty(item.privateuser) && item.privateuser.equals(ProfileUtils.getCurrentProfileId()) && item.isdefault){
                return item.order;
            }
        }
        for (CustomView item : this.dataEntity.customViews) {
            if(!Utils.isEmpty(item.authorize) && AuthorizeUtils.isAuthorizes(ProfileUtils.getCurrentProfileId(), item.authorize)){
                return item.order;
            }
        }
        for (CustomView item : this.dataEntity.customViews) {
            if (item.isdefault) {
                return item.order;
            }
        }
        return "";
    }

    public List<String> getCustomViewFields(String viewid) {
        List<String> viewFields = new ArrayList<>();
        if(Utils.isEmpty(this.dataEntity.customViews)) {
            return viewFields;
        }
        if(!Utils.isEmpty(viewid) && !"undefined".equals(viewid)) {
            for (CustomView item : this.dataEntity.customViews) {
                if (item.id.equals(viewid)) {
                    viewFields.addAll(item.columnlist);
                    return viewFields;
                }
            }
        }
        for (CustomView item : this.dataEntity.customViews) {
            if(!Utils.isEmpty(item.privateuser) && item.privateuser.equals(ProfileUtils.getCurrentProfileId()) && item.isdefault){
                viewFields.addAll(item.columnlist);
                return viewFields;
            }
        }
        for (CustomView item : this.dataEntity.customViews) {
            if(!Utils.isEmpty(item.authorize) && AuthorizeUtils.isAuthorizes(ProfileUtils.getCurrentProfileId(), item.authorize)){
                viewFields.addAll(item.columnlist);
                return viewFields;
            }
        }
        for (CustomView item : this.dataEntity.customViews) {
            if (item.isdefault) {
                viewFields.addAll(item.columnlist);
                return viewFields;
            }
        }
        if(!this.dataEntity.customViews.isEmpty()){
            viewFields.addAll(this.dataEntity.customViews.get(0).columnlist);
        }
        return viewFields;
    }

    public HashMap<String,List<?>> getFieldsName(List<String> fields) {
        List<String> resultname = new ArrayList<>();
        List<String> resultlabel = new ArrayList<>();
        for (String item : fields) {
            switch (item) {
                case "title":
                    resultlabel.add("标题");
                    resultname.add(item);
                    break;
                case "author":
                    resultlabel.add("创建人");
                    resultname.add(item);
                    break;
                case "published":
                    resultlabel.add("创建时间");
                    resultname.add(item);
                    break;
                case "updated":
                    resultlabel.add("更新时间");
                    resultname.add(item);
                    break;
                case "approvalstatus":
                    resultlabel.add("状态");
                    resultname.add(item);
                    break;
                case "submitdatetime":
                    resultlabel.add("提交审批时间");
                    resultname.add(item);
                    break;
                case "approvaldatetime":
                    resultlabel.add("审批时间");
                    resultname.add(item);
                    break;
                case "finishapprover":
                    resultlabel.add("审批人");
                    resultname.add(item);
                    break;
                default:
                    if (this.moduleFields.get(item) != null) {
                        String label = this.moduleFields.get(item).fieldlabel;
                        resultlabel.add(label);
                        resultname.add(item);
                    }
                    break;
            }
        }
        HashMap<String,List<?>> info = new HashMap<>(1);
        info.put("viewFields",resultname);
        info.put("viewNames",resultlabel);
        return info;
    }

    public HashMap<String, OutsideLink> getFieldLinkModules() {
        if (!Utils.isEmpty(this.dataEntity.outsideLinks)) {
            HashMap<String, OutsideLink> result = new HashMap<>();
            for (OutsideLink item : this.dataEntity.outsideLinks) {
                result.put(item.fieldname, item);
            }
            return result;
        } else {
            return new HashMap<>();
        }
    }
    public List<OutsideLink> getRelMeLinkModules() {
        return OutsideLinkUtils.getRelMeLinkModules(this.getModuleName());
    }

    public List<String> getStatisticFields() {
        if (!Utils.isEmpty(this.dataEntity)) {
            return this.statisticFields;
        } else {
            return new ArrayList<>();
        }
    }

    public Tab getModuletab() {
        if (!Utils.isEmpty(this.dataEntity)) {
            return this.dataEntity.tabs;
        } else {
            return new Tab();
        }
    }

    public BaseActionsConfig getActionsEntity() {
        if (!Utils.isEmpty(this.actionsEntity)) {
            return this.actionsEntity;
        } else {
            return null;
        }
    }

    public List<SearchColumn> getSearchColumns() {
        if (!Utils.isEmpty(this.actionsEntity)) {
            return this.dataEntity.searchColumn;
        } else {
            return null;
        }
    }

    //获取模块的模块组
    public String getEntityName() {
        if(Utils.isNotEmpty(this.dataEntity) && Utils.isNotEmpty(this.dataEntity.entityNames)) {
            return this.dataEntity.entityNames.fieldname;
        }
        return "";
    }

    //获取模块的模块组
    public String getModuleGroup() {
        if(Utils.isEmpty(this.dataEntity)) {
            return "";
        }
        return this.dataEntity.moduleMenu.parent;
    }
    //获取模块的菜单组
    public String getModuleProgram() {
        if(Utils.isEmpty(this.dataEntity)) {
            return "";
        }
        return this.dataEntity.moduleMenu.program;
    }
    //获取模块所有数值型字段名
    public List<String> getModuleFieldsByInt() {
        List<String> result = new ArrayList<>();
        if(!Utils.isEmpty(this.dataEntity) && !Utils.isEmpty(this.dataEntity.fields) && !this.dataEntity.fields.isEmpty()){
            for(TabField field : this.dataEntity.fields){
                if(Arrays.asList(5,14,16,20,27,28).contains(field.uitype)){
                    result.add(field.fieldname);
                }else if(field.typeofdata.startsWith("NN~") || field.typeofdata.startsWith("IN~") || field.typeofdata.startsWith("MONEY~")) {
                    result.add(field.fieldname);
                }
            }
        }
        return result;
    }

    //获取流程状态信息
    public WorkflowStatus getWorkflowStatus() {
        return this.workflowStatus;
    }
    public void setWorkflowStatus(WorkflowStatus status) {
        this.workflowStatus = status;
    }

    public String getPackageName() {
        return this.entityPackageName;
    }

    //获取数据权限设置
    public Map<String, List<Expression>> getDataPermission() {
        if(Utils.isEmpty(this.dataEntity.dataPermission) || Utils.isEmpty(this.dataEntity.dataPermission.getExpressions())) {
            return null;
        }
        return this.dataEntity.dataPermission.getExpressions();
    }
    //endregion

    //region 列表封装
    @ApiOperation("获取日期查询的Html")
    private String getCalendarHtml(String fieldlabel, String tagStr, String fieldname, int width, String startdate, String enddate, String thistype, boolean isQuickBtn) {
        String linkHtml = "";
        if (this.dataEntity.tabs.defaultsection != null && !this.dataEntity.tabs.defaultsection.isEmpty()) {
            linkHtml += "<a href='javascript:;' style='margin-left:10px' lay-value='all' id='" + tagStr + "_all' lay-filter='" + tagStr + "_period' " + ("all".equals(thistype) ? "isdefault class='layui-bg-blue-2 period-radius'" : "") + " title='全部'>全部</a>";
        }
        linkHtml += "<a href='javascript:;' lay-value='' id='" + tagStr + "_thisyear' lay-filter='" + tagStr + "_period' " + ("thisyear".equals(thistype) ? "isdefault class='layui-bg-blue-2 period-radius'" : "") + " title='本年'>本年</a>" +
                "<a href='javascript:;' lay-value='thisquater' id='" + tagStr + "_thisquater' lay-filter='" + tagStr + "_period' " + ("thisquater".equals(thistype) ? "isdefault class='layui-bg-blue-2 period-radius'" : "") + " title='本季'>本季</a>" +
                "<a href='javascript:;' lay-value='thismonth' id='" + tagStr + "_thismonth' lay-filter='" + tagStr + "_period' " + ("thismonth".equals(thistype) ? "isdefault class='layui-bg-blue-2 period-radius'" : "") + " title='本月'>本月</a>" +
                "<a href='javascript:;' lay-value='recently' id='" + tagStr + "_recently' lay-filter='" + tagStr + "_period' " + ("recently".equals(thistype) ? "isdefault class='layui-bg-blue-2 period-radius'" : "") + " title='最近'>最近</a>";
        String searchpaneHtml = "<div class='layui-input-date-div'><div class='layui-input-inline' style='width:78px;'>" +
                "<input style='padding-left:10px;font-size: 12px;' type='text' id='" + tagStr + "_startdate' value='" + startdate + "' name='" + fieldname + "_startdate' lay-filter='searchpanel' class='layui-input " + tagStr + "_input' placeholder='开始日期'>" +
                "</div><div class='layui-input-inline layui-input-date-line'>&nbsp;-&nbsp;</div><div class='layui-input-inline' style='width:74px;'>" +
                "<input style='font-size: 12px;' type='text' id='" + tagStr + "_enddate' name='" + fieldname + "_enddate' lay-filter='searchpanel' value='" + enddate + "' class='layui-input " + tagStr + "_input' placeholder='结束日期'>" +
                (isQuickBtn ?
                        "<input type='hidden' id='" + tagStr + "_thistype' name='" + fieldname + "_thistype' lay-filter='searchpanel' value='" + thistype + "' readonly >"
                        : "") +
                "</div></div>" +
                "<script>" +
                "layui.use(['laydate', 'element', 'util'], function () {" +
                "var $ = layui.jquery," +
                "   util = layui.util," +
                "   laydate = layui.laydate;" +
                "$('." + tagStr + "_input').each(function(){" +
                "   laydate.render({elem:this,theme: 'molv'});" +
                "});" +
                (isQuickBtn ?
                        "$('a[lay-filter=" + tagStr + "_period]').on('click', function(){" +
                                "   $('a[lay-filter=" + tagStr + "_period]').toggleClass('layui-bg-blue-2 period-radius',false);" +
                                tagStr + "_period_onclick(this);" +
                                "   $(this).addClass('layui-bg-blue-2 period-radius');" +
                                "});" +
                                "" +
                                "function " + tagStr + "_period_onclick(obj) {" +
                                "   var start = '', end = '';" +
                                "   if($(obj).attr('id') == '" + tagStr + "_all'){" +
                                "       start = ''; end = '';" +
                                "       $('#" + tagStr + "_thistype').val('all');" +
                                "   }else if($(obj).attr('id') == '" + tagStr + "_thisyear'){" +
                                "       start = '" + DateTimeUtils.getDateAttr(Calendar.YEAR, new Date()) + "-01-01';" +
                                "       end = '" + DateTimeUtils.getDateStringByFormat(new Date(), "yyyy-MM-dd") + "';" +
                                "       $('#" + tagStr + "_thistype').val('thisyear');" +
                                "   }else if($(obj).attr('id') == '" + tagStr + "_thisquater'){" +
                                "       start = '" + DateTimeUtils.getDateStringByFormat(DateTimeUtils.getFirstDateOfMonth(DateTimeUtils.getSeasonDate(new Date())[0]), "yyyy-MM-dd") + "';" +
                                "       end = '" + DateTimeUtils.getDateStringByFormat(DateTimeUtils.getLastDateOfMonth(DateTimeUtils.getSeasonDate(new Date())[2]), "yyyy-MM-dd") + "';" +
                                "       $('#" + tagStr + "_thistype').val('thisquater');" +
                                "   }else if($(obj).attr('id') == '" + tagStr + "_recently'){" +
                                "       start = '" + DateTimeUtils.getDateStringByFormat(DateTimeUtils.getFormatDate(Calendar.MONTH, -1, new Date()), "yyyy-MM-dd") + "';" +
                                "       end = '" + DateTimeUtils.getDateStringByFormat(new Date(), "yyyy-MM-dd") + "';" +
                                "       $('#" + tagStr + "_thistype').val('recently');" +
                                "   } else {" +
                                "       start = '" + DateTimeUtils.getDateStringByFormat(DateTimeUtils.getFirstDateOfMonth(new Date()), "yyyy-MM-dd") + "';" +
                                "       end = '" + DateTimeUtils.getDateStringByFormat(DateTimeUtils.getLastDateOfMonth(new Date()), "yyyy-MM-dd") + "';" +
                                "       $('#" + tagStr + "_thistype').val('thismonth');" +
                                "   }" +
                                "   $('#" + tagStr + "_startdate').val(start);" +
                                "   $('#" + tagStr + "_enddate').val(end);" +
                                "};"
                        : "") +
                "});" +
                "</script>";
        if (fieldlabel == null || fieldlabel.isEmpty()) {
            return "<style>#" + this.dataEntity.tabs.modulename + "_listpage .period-radius{border-radius:5px;padding: 0 4px;}</style><div class='filter-wrap-cell-con filter-wrap-cell-con-date'>" + (isQuickBtn ? linkHtml : "") + searchpaneHtml+"</div>";
        } else {
            return "<style>#" + this.dataEntity.tabs.modulename + "_listpage .period-radius{border-radius:5px;padding: 0 4px;}</style><div class='filter-wrap-cell-con filter-wrap-cell-con-date'>" +
                    "<label class='fwcc-div-label'>" + fieldlabel + "：</label>" + searchpaneHtml + (isQuickBtn ? linkHtml : "")+"</div>";
        }
    }

    @ApiOperation("获取输入框查询的Html")
    private String getInputhtml(String fieldlabel,String tagStr, String fieldname, String value, String tip, String aux ) {
        if(Utils.isEmpty(tip)) {
            tip = "请输入";
        }
        return  "<div class='filter-wrap-cell-con'>" +
                "<label class='fwcc-div-label'>" + fieldlabel +"：</label>" +
                "<div class='fwcc-div-input fwcc-div-input-table'>" +
                "    <input type='text' name='" + tagStr + fieldname + "' lay-filter='searchpanel' value='" + value + "' placeholder='" + tip + "' class='fwcc-input1 fwcc-input1-tc'>" + (!aux.isEmpty()?"<span class='addition-tc'>"+aux+"</span>":"") +
                "</div></div>";
    }

    @ApiOperation("获取输入框查询的Html")
    private String getTextHtml(String fieldlabel, String fieldname, String value, String tagStr, List<Object> picklists, int uitype) {
        String searchpaneHtml = "<div class='filter-wrap-cell-con'><label class='fwcc-div-label'>"+ fieldlabel +"：</label>" +
                "       <input type='hidden' class='layui-input' id='" + tagStr + "_listpage_search' name='" + fieldname + "' lay-filter='searchpanel' value='" + value + "'><div class='fwcc-div-input2'>";
        searchpaneHtml += "<a href='javascript:;' class='fwcc-div-input2-a "+(value.isEmpty() ? " fwcc-a-action":"")+"' lay-value='' lay-filter='" + tagStr + "_period' " + (value.isEmpty() ? " isdefault " : "") + " title='全部'>全部</a>";
        for (Object pick : picklists) {
            Object pickvalue = ((List<?>) pick).get(0);
            Object defaultvalue = value;
            if (Arrays.asList(5, 14, 16, 20).contains(uitype)) {
                pickvalue = ((List<?>) pick).get(2);
                if(Utils.isEmpty(value)){
                    defaultvalue = -1;
                }else {
                    defaultvalue = Double.valueOf(value).intValue();
                }
            }
            searchpaneHtml += "<a href='javascript:;' class='fwcc-div-input2-a " + (defaultvalue.equals(pickvalue) ? "fwcc-a-action" : "") + "' lay-value='" + pickvalue + "' ' lay-filter='" + tagStr + "_period' " + (defaultvalue.equals(pickvalue) ? " isdefault " : "") + " title='" + ((List<?>) pick).get(1) + "'>" + ((List<?>) pick).get(1) + "</a>";
        }
        searchpaneHtml += " </div></div>" +
                "<script>" +
                "   layui.use(['jquery'],function () {" +
                "       var $ = layui.jquery;" +
                "       $('a[lay-filter=" + tagStr + "_period]').unbind('click');" +
                "       $('a[lay-filter=" + tagStr + "_period]').on('click', function(){" +
                "           $('a[lay-filter=" + tagStr + "_period]').toggleClass('fwcc-a-action',false);" +
                "           var value = $(this).attr('lay-value');" +
                "           $('#" + tagStr + "_listpage_search').val(value);" +
                "           $(this).addClass('fwcc-a-action');" +
                "       })" +
                "   });" +
                "</script>";

        return searchpaneHtml;
    }

    private String getSelectHtml(String fieldlabel, String fieldname, String value, String tip, String aux, List<Object> picklists, int uitype){
        return getSelectHtml(fieldlabel,fieldname,value,tip,aux,picklists,uitype,false,true);
    }

    private String getSelectHtml(String fieldlabel, String fieldname, String value, String tip, String aux, List<Object> picklists, int uitype, boolean isSelect, boolean isEmpty){
        if(Utils.isEmpty(tip)) {
            tip = "请选择";
        }
        String searchpaneHtml = "<div class='filter-wrap-cell-con'>" +
                "    <label class='fwcc-div-label'>" + fieldlabel + "：</label>" +
                "    <div class='layui-input-inline-select-table'><div class='layui-input-inline layui-input-inline-select layui-input-inline-tc'>" +
                "       <select  name='" + fieldname + "' lay-filter='searchpanel' placeholder='" + tip + "' "+ (isSelect?"lay-search":"") +">";
        if(isEmpty){
            searchpaneHtml += "<option value=' '>全部</option>";
        }
        for (Object pick : picklists) {
            Object pickvalue = ((List<?>) pick).get(0);
            Object defaultvalue = value;
            if (Arrays.asList(5, 14, 16, 20).contains(uitype)) {
                pickvalue = ((List<?>) pick).get(2);
                if(Utils.isEmpty(value)){
                    defaultvalue = -1;
                }else {
                    defaultvalue = Double.valueOf(value).intValue();
                }
            }
            searchpaneHtml += "<option value='" + pickvalue + "' " + (defaultvalue.equals(pickvalue) ? "selected" : "") + ">" + ((List<?>) pick).get(1) + "</option>";
        }
        searchpaneHtml += "     </select>" +
                (!aux.isEmpty()?"    </div><span class='addition-tc'>"+aux+"</span></div>":"") +
                "    </div></div>" +
                "  </div>" +
                "<script>" +
                "   layui.use(['form'],function () {" +
                "       var form = layui.form;" +
                "           form.render('select')" +
                "   });" +
                "</script>";
        return searchpaneHtml;
    }

    private String getTreeSelectHtml(String modulename,String fieldlabel, String fieldname, String tip,List<TreeSelectOption> treeOptions){
        if(Utils.isEmpty(tip)) {
            tip = "请选择";
        }
        String selected_id = "";
        String selected_label = "";
        for (TreeSelectOption treeOption : treeOptions) {
            if (treeOption.selected) {
                selected_id = treeOption.id;
                selected_label = treeOption.label;
            }
        }
        String searchpaneHtml = "<div class='filter-wrap-cell-con'>\n";
        searchpaneHtml += "    <label class='fwcc-div-label'>" + fieldlabel + "：</label>\n";
        searchpaneHtml += "    <div class='layui-input-inline layui-input-inline-select'>\n";
        searchpaneHtml += "         <div class='layui-unselect layui-form-select " + modulename + "_" + fieldname + "_select_div' >\n";
        searchpaneHtml += "             <div class='layui-select-title layui-form-department'>\n";
        searchpaneHtml += "                 <input type='hidden' id='" + modulename + "_" + fieldname + "_input' lay-filter='searchpanel' name='" + fieldname + "' value='" + selected_id + "'>\n";
        searchpaneHtml += "                 <input type='text' id='" + modulename + "_" + fieldname + "' lay-filter='searchpanel' class='layui-input layui-popup-input " + modulename + "_" + fieldname + "' value='" + selected_label + "' placeholder='" + tip + "' readonly lay-vertype='tips'>\n";
        searchpaneHtml += "                 <i class='layui-edge'></i>\n";
        searchpaneHtml += "                 <i id='" + modulename + "_" + fieldname + "_clear' class='lcdp-icon lcdp-icon-guanbi layui-clear-icon' title='重置输入框' ></i>\n";
        searchpaneHtml += "             </div>\n";
        searchpaneHtml += "              <dl style='padding: 0;max-height: 240px;'><ul id='" + modulename + "_" + fieldname + "_tree' class='ztree'></ul></dl>\n";
        searchpaneHtml += "         </div>\n";
        searchpaneHtml += "     </div>\n";
        searchpaneHtml += "</div>\n";
        searchpaneHtml += "<script type='text/javascript'>\n";
        searchpaneHtml += "     layui.use(['jquery','dropdown'], function () {\n";
        searchpaneHtml += "           var dropdown = layui.dropdown,\n";
        searchpaneHtml += "           $ = layui.jquery;\n";
        log.info("treeOptions : {}",treeOptions);
        searchpaneHtml += "         var znodes = [\n";
        int index = 0;
        for (TreeSelectOption treeOption : treeOptions) {
            searchpaneHtml += "             { id: " + treeOption.id + ", pId: " + (Utils.isEmpty(treeOption.parentid) ? "0" : treeOption.parentid) + ", name: \"" + treeOption.label + "\", open: true }";
            if (index != treeOptions.size()-1) {
                searchpaneHtml += ",\n";
            }
            index ++;
         }
        searchpaneHtml += "];\n";
        searchpaneHtml += "         var setting = {\n";
        searchpaneHtml += "             data: {  simpleData: { enable: true }},\n";
        searchpaneHtml += "             callback: {  onClick: " + modulename + "_" + fieldname + "_tree_onClick}\n";
        searchpaneHtml += "         };\n";
        searchpaneHtml += "         $(document).ready(function () { zTree = $.fn.zTree.init($('#" + modulename + "_" + fieldname + "_tree'), setting, znodes); });\n";
        searchpaneHtml += "         function " + modulename + "_" + fieldname + "_tree_onClick(event, treeId, treeNode) {\n";
        searchpaneHtml += "             $('#" + modulename + "_" + fieldname + "_input').val(treeNode.id)\n";
        searchpaneHtml += "             $('#" + modulename + "_" + fieldname + "').val(treeNode.name)\n";
        searchpaneHtml += "             $('div." + modulename + "_" + fieldname + "_select_div').removeClass('layui-form-selected');\n";
        searchpaneHtml += "             $('#" + modulename + "_" + fieldname + "').removeClass('layui-form-danger');\n";
        searchpaneHtml += "             layui.event('input','searchpanel',treeNode.id);\n";
        searchpaneHtml += "         }";
        searchpaneHtml += "         $('." + modulename + "_" + fieldname + "').unbind();\n";
        searchpaneHtml += "         $('." + modulename + "_" + fieldname + "').click('" + modulename + "_" + fieldname + "_select_div',function (event) {\n";
        searchpaneHtml += "             layui.stope(event);\n";
        searchpaneHtml += "             if($('.'+event.data).hasClass('layui-form-selected')) {\n";
        searchpaneHtml += "                 $('.'+event.data).removeClass('layui-form-selected');\n";
        searchpaneHtml += "             } else {\n";
        searchpaneHtml += "                 $('.'+event.data).addClass('layui-form-selected');\n";
        searchpaneHtml += "                 var selected_id = $('#" + modulename + "_" + fieldname + "_input').val();\n";
        searchpaneHtml += "                 var zTree = $.fn.zTree.getZTreeObj(\"" + modulename + "_" + fieldname + "_tree\");\n";
        searchpaneHtml += "                 var node = zTree.getNodeByParam(\"id\", selected_id);\n";
        searchpaneHtml += "                 if(node != null) {\n";
        searchpaneHtml += "                     zTree.selectNode(node, true);\n";
        searchpaneHtml += "                 }\n";
        searchpaneHtml += "             }\n";
        searchpaneHtml += "             var dlEle=$('.'+event.data).children('dl')[0];\n";
        searchpaneHtml += "             var dlHeight = $(dlEle).height();\n";
        searchpaneHtml += "             if(dlHeight>10000) {\n";
        searchpaneHtml += "                 $(dlEle).height(500);\n";
        searchpaneHtml += "             }\n";
        searchpaneHtml += "         })\n";
        searchpaneHtml += "         $('." + modulename + "_" + fieldname + "_select_div').unbind();\n";
        searchpaneHtml += "         $('." + modulename + "_" + fieldname + "_select_div').click('" + modulename + "_" + fieldname + "_tree',function (event) {\n";
        searchpaneHtml += "             layui.stope(event)\n";
        searchpaneHtml += "         })\n";
        searchpaneHtml += "         $('#" + modulename + "_" + fieldname + "_clear').click('" + modulename + "_" + fieldname + "_tree',function (event) {\n";
        searchpaneHtml += "             $('#" + modulename + "_" + fieldname + "_input').val('')\n";
        searchpaneHtml += "             $('#" + modulename + "_" + fieldname + "').val('')\n";
        searchpaneHtml += "             layui.event('input','searchpanel','');\n";
        searchpaneHtml += "         })\n";
        searchpaneHtml += "     })";
        searchpaneHtml += "</script>";
        return searchpaneHtml;
    }

    @ApiOperation("根据定义生成列表页面的查询面版")
    private Map<String, Object> dataSearch(HttpServletRequest request, List<String> hideFields) {
        ArrayList<Object> search = new ArrayList<>();
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        Object methodresult = null;
        try{
            methodresult = CallbackUtils.invoke("addDataSearch", entityPackageName, IBaseService.class, httpRequest);
        }catch (Exception ignored) {}
        if (this.dataEntity.searchColumn.size() <= 0 && Utils.isEmpty(methodresult)) {
            return new HashMap<>();
        }
        HttpSession session = request.getSession();
        HashMap<String, Object> sessionRequest = (HashMap<String, Object>)session.getAttribute(this.dataEntity.tabs.modulename+"_DataSearch");
        if(!Utils.isEmpty(sessionRequest)){
            for (String key : sessionRequest.keySet()){
                if(!httpRequest.containsKey(key) || Utils.isEmpty(httpRequest.get(key))){
                    httpRequest.put(key,sessionRequest.get(key));
                }
            }
        }
        int column = this.dataEntity.tabs.searchcolumn;
        for (SearchColumn item : this.dataEntity.searchColumn) {
            boolean newline = item.newline;
            int colspan = item.colspan;
            int width = item.width;
            Object searchvalue = item.searchvalue;
            if (hideFields != null && hideFields.contains(item.fieldname)) {
                continue;
            }
            if (item.searchtype.contains("hidden")) {
                continue;
            }
            if (!this.dataEntity.tabs.searchcolumnflow) {
                if (colspan >= column) {
                    newline = true;
                    colspan = 1;
                }
            }
            boolean finalNewline = newline;
            int finalColspan = colspan;
            if ("input".equals(item.searchtype) || "multi_input".equals(item.searchtype) || "search_input".equals(item.searchtype) || "vague_input".equals(item.searchtype)) {
                String value = httpRequest.getOrDefault(item.fieldname, "").toString();
                if (item.searchtype.contains("multi_input") && !Utils.isEmpty(httpRequest.get("multi_" + item.fieldname))) {
                    value = httpRequest.getOrDefault("multi_" + item.fieldname, "").toString();
                }
                if (!httpRequest.containsKey(item.fieldname) && !Utils.isEmpty(searchvalue)) {
                    value = searchvalue.toString();
                }

                String searchhtml = this.getInputhtml(item.fieldlabel, (item.searchtype.contains("multi_input") ? "multi_" : ""), item.fieldname, value, item.tip, item.aux);
                Map<String,Object> info = new HashMap<>(1);
                info.put("fieldname", item.fieldname);
                info.put("order", item.order);
                info.put("search", searchhtml);
                info.put("newline", finalNewline);
                info.put("colspan", finalColspan);
                search.add(info);
            } else if (item.searchtype.contains("calendar")) {
                String startdate = "";
                String enddate = "";
                String thistype = "";
                if (item.fieldname.contains("published") || item.fieldname.contains("updated")) {
                    if (Utils.isEmpty(httpRequest.get(item.fieldname + "_thistype"))) {
                        startdate = "";
                        enddate = "";
                        if (!Utils.isEmpty(this.dataEntity.tabs.defaultsection)) {
                            thistype = this.dataEntity.tabs.defaultsection;
                        } else {
                            thistype = "thisyear";
                        }
                    }
                }
                if (!Utils.isEmpty(httpRequest.get(item.fieldname + "_startdate")) && !Utils.isEmpty(httpRequest.get(item.fieldname + "_enddate"))) {
                    startdate = httpRequest.get(item.fieldname + "_startdate").toString();
                    enddate = httpRequest.get(item.fieldname + "_enddate").toString();
                } else if (!Utils.isEmpty(httpRequest.get(item.fieldname + "_startdate"))) {
                    startdate = httpRequest.get(item.fieldname + "_startdate").toString();
                } else if (!Utils.isEmpty(httpRequest.get(item.fieldname + "_enddate"))) {
                    enddate = httpRequest.get(item.fieldname + "_enddate").toString();
                }
                if (!Utils.isEmpty(httpRequest.get(item.fieldname + "_thistype"))) {
                    thistype = httpRequest.get(item.fieldname + "_thistype").toString();
                }
                if (!"all".equals(thistype) && !thistype.isEmpty()) {
                    if (!Utils.isEmpty(this.dataEntity.tabs.defaultsection) && Utils.isEmpty(startdate) && Utils.isEmpty(enddate)) {
                        switch (this.dataEntity.tabs.defaultsection.toLowerCase()) {
                            case "thisyear":
                                startdate = DateTimeUtils.getDateAttr(Calendar.YEAR, new Date()) + "-01-01";
                                enddate = DateTimeUtils.getDateAttr(Calendar.YEAR, new Date()) + "-12-31";
                                thistype = "thisyear";
                                break;
                            case "thisquater":
                                startdate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getFirstDateOfMonth(DateTimeUtils.getSeasonDate(new Date())[0]), "yyyy-MM-dd");
                                enddate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getLastDateOfMonth(DateTimeUtils.getSeasonDate(new Date())[2]), "yyyy-MM-dd");
                                thistype = "thisquater";
                                break;
                            case "thismonth":
                                startdate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getFirstDateOfMonth(new Date()), "yyyy-MM-dd");
                                enddate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getLastDateOfMonth(new Date()), "yyyy-MM-dd");
                                thistype = "thismonth";
                                break;
                            default:
                                startdate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getFormatDate(Calendar.MONTH, -1, new Date()), "yyyy-MM-dd");
                                enddate = DateTimeUtils.getDateStringByFormat(new Date(), "yyyy-MM-dd");
                                thistype = "recently";
                                break;
                        }
                    } else if (Utils.isEmpty(startdate) && Utils.isEmpty(enddate)) {
                        startdate = DateTimeUtils.getDateAttr(Calendar.YEAR, new Date()) + "-01-01";
                        enddate = DateTimeUtils.getDateAttr(Calendar.YEAR, new Date()) + "-12-31";
                        thistype = "thisyear";
                    }
                }
                if (!Utils.isEmpty(startdate)) {
                    startdate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getDateByFormatString(startdate, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd");
                }
                if (!Utils.isEmpty(enddate)) {
                    enddate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getDateByFormatString(enddate, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd");
                }
                String searchPaneHtml = this.getCalendarHtml(item.fieldlabel, this.dataEntity.tabs.modulename + "_" + item.fieldname, item.fieldname, width, startdate, enddate, thistype, item.quickbtn);
                Map<String,Object> info = new HashMap<>(1);
                info.put("fieldname", item.fieldname);
                info.put("order", item.order);
                info.put("search", searchPaneHtml);
                info.put("newline", finalNewline);
                info.put("colspan", finalColspan);
                search.add(info);
            } else if (item.searchtype.contains("text")) {
                if (!Utils.isEmpty(this.moduleFields.get(item.fieldname))) {
                    int uitype = this.moduleFields.get(item.fieldname).uitype;
                    if (Arrays.asList(13, 14, 15, 16, 19, 20).contains(uitype)) {
                        String pickList = !Utils.isEmpty(this.moduleFields.get(item.fieldname).picklist) ? this.moduleFields.get(item.fieldname).picklist : item.fieldname;
                        Object picklists = PickListUtils.getPickList(pickList);
                        if (!Utils.isEmpty(picklists)) {
                            String value = httpRequest.getOrDefault(item.fieldname, "").toString();
                            if (!httpRequest.containsKey(item.fieldname) && !Utils.isEmpty(searchvalue)) {
                                value = searchvalue.toString();
                            }
                            String tagStr = this.dataEntity.tabs.modulename + "_" + item.fieldname;
                            String searchhtml = this.getTextHtml(item.fieldlabel, item.fieldname, value, tagStr, (List<Object>) picklists, uitype);
                            Map<String,Object> info = new HashMap<>(1);
                            info.put("fieldname", item.fieldname);
                            info.put("order", item.order);
                            info.put("search", searchhtml);
                            info.put("newline", finalNewline);
                            info.put("colspan", finalColspan);
                            search.add(info);
                        }
                    } else if (uitype == 5) {
                        String value = httpRequest.getOrDefault(item.fieldname, "").toString();
                        if (!httpRequest.containsKey(item.fieldname) && !Utils.isEmpty(searchvalue)) {
                            value = searchvalue.toString();
                        }
                        List<Object> picklists = Arrays.asList(
                                Arrays.asList("是", "是", 1, "", 1, ""),
                                Arrays.asList("否", "否", 0, "", 0, "")
                        );
                        String tagStr = this.dataEntity.tabs.modulename + "_" + item.fieldname;
                        String searchhtml = this.getTextHtml(item.fieldlabel, item.fieldname, value, tagStr, picklists, uitype);
                        Map<String,Object> info = new HashMap<>(1);
                        info.put("fieldname", item.fieldname);
                        info.put("order", item.order);
                        info.put("search", searchhtml);
                        info.put("newline", finalNewline);
                        info.put("colspan", finalColspan);
                        search.add(info);
                    }
                }else if("approvalstatus".equals(item.fieldname)) {
                    int uitype = 14;
                    String pickList = "approvalstatus";
                    Object picklists = PickListUtils.getPickList(pickList);
                    if (!Utils.isEmpty(picklists)) {
                        String value = httpRequest.getOrDefault(item.fieldname, "").toString();
                        if (!httpRequest.containsKey(item.fieldname) && !Utils.isEmpty(searchvalue)) {
                            value = searchvalue.toString();
                        }
                        String tagStr = this.dataEntity.tabs.modulename + "_" + item.fieldname;
                        String searchhtml = this.getTextHtml(item.fieldlabel, item.fieldname, value, tagStr, (List<Object>) picklists, uitype);
                        Map<String,Object> info = new HashMap<>(1);
                        info.put("fieldname", item.fieldname);
                        info.put("order", item.order);
                        info.put("search", searchhtml);
                        info.put("newline", finalNewline);
                        info.put("colspan", finalColspan);
                        search.add(info);
                    }
                }
            } else if (item.searchtype.contains("select")) {
                if (!Utils.isEmpty(this.moduleFields.get(item.fieldname))) {
                    int uitype = this.moduleFields.get(item.fieldname).uitype;
                    if (Arrays.asList(13, 14, 15, 16, 19, 20).contains(uitype)) {
                        String pickList = !Utils.isEmpty(this.moduleFields.get(item.fieldname).picklist) ? this.moduleFields.get(item.fieldname).picklist : item.fieldname;
                        Object picklists = PickListUtils.getPickList(pickList);
                        if (!Utils.isEmpty(picklists)) {
                            String value = httpRequest.getOrDefault(item.fieldname, "").toString();
                            if (!httpRequest.containsKey(item.fieldname) && !Utils.isEmpty(searchvalue)) {
                                value = searchvalue.toString();
                            }
                            String searchhtml = this.getSelectHtml(item.fieldlabel, item.fieldname, value, item.tip, item.aux, (List<Object>) picklists, uitype);
                            Map<String,Object> info = new HashMap<>(1);
                            info.put("fieldname", item.fieldname);
                            info.put("order", item.order);
                            info.put("search", searchhtml);
                            info.put("newline", finalNewline);
                            info.put("colspan", finalColspan);
                            search.add(info);
                        }
                    } else if (uitype == 5) {
                        String value = httpRequest.getOrDefault(item.fieldname, "").toString();
                        if (!httpRequest.containsKey(item.fieldname) && !Utils.isEmpty(searchvalue)) {
                            value = searchvalue.toString();
                        }
                        List<Object> picklists = Arrays.asList(
                                Arrays.asList("是", "是", 1, "", 1, ""),
                                Arrays.asList("否", "否", 0, "", 0, "")
                        );
                        String searchhtml = this.getSelectHtml(item.fieldlabel, item.fieldname, value, item.tip, item.aux, picklists, uitype);
                        Map<String,Object> info = new HashMap<>(1);
                        info.put("fieldname", item.fieldname);
                        info.put("order", item.order);
                        info.put("search", searchhtml);
                        info.put("newline", finalNewline);
                        info.put("colspan", finalColspan);
                        search.add(info);
                    }
                }else if("approvalstatus".equals(item.fieldname)) {
                    int uitype = 14;
                    String pickList = "approvalstatus";
                    Object picklists = PickListUtils.getPickList(pickList);
                    if (!Utils.isEmpty(picklists)) {
                        String value = httpRequest.getOrDefault(item.fieldname, "").toString();
                        if (!httpRequest.containsKey(item.fieldname) && !Utils.isEmpty(searchvalue)) {
                            value = searchvalue.toString();
                        }
                        String searchhtml = this.getSelectHtml(item.fieldlabel, item.fieldname, value, item.tip, item.aux, (List) picklists, uitype);
                        Map<String,Object> info = new HashMap<>(1);
                        info.put("fieldname", item.fieldname);
                        info.put("order", item.order);
                        info.put("search", searchhtml);
                        info.put("newline", finalNewline);
                        info.put("colspan", finalColspan);
                        search.add(info);
                    }
                }
            }
        }
        try{
            if(!Utils.isEmpty(methodresult)){
                if(methodresult instanceof CustomDataSearch){
                    methodresult = Collections.singletonList(methodresult);
                }
                if(methodresult instanceof List) {
                    for (CustomDataSearch dataSearch : (List<CustomDataSearch>) methodresult) {
                        if(Utils.isEmpty(dataSearch.fieldname())) {
                            continue;
                        }
                        boolean newline = dataSearch.newline();
                        int colspan = dataSearch.colspan();
                        if (!this.dataEntity.tabs.searchcolumnflow) {
                            if (colspan >= column) {
                                newline = true;
                                colspan = 1;
                            }
                        }
                        switch (dataSearch.searchtype()) {
                            case "text": {
                                if(!Utils.isEmpty(dataSearch.options()) && !dataSearch.options().isEmpty()){
                                    List<Object> picklists = new ArrayList<>();
                                    String value = "";
                                    for(SelectOption option : dataSearch.options()){
                                        if(option.selected){
                                            value = option.value;
                                        }
                                        picklists.add(Arrays.asList(option.value,option.label));
                                    }
                                    String tagStr = this.dataEntity.tabs.modulename + "_" + dataSearch.fieldname();
                                    String searchhtml = this.getTextHtml(dataSearch.fieldlabel(), dataSearch.fieldname(), value, tagStr, picklists, 18);
                                    boolean isfind = false;
                                    for(Object searchitem: search){
                                        if(((HashMap<?,?>)searchitem).get("fieldname").equals(dataSearch.fieldname())){
                                            Map<String,Object> info = new HashMap<>(1);
                                            info.put("fieldname",dataSearch.fieldname());
                                            info.put("order", dataSearch.order());
                                            info.put("search", searchhtml);
                                            info.put("newline", newline);
                                            info.put("colspan", colspan);
                                            Collections.replaceAll(search, searchitem, info);
                                            isfind = true;
                                            break;
                                        }
                                    }
                                    if(!isfind) {
                                        Map<String,Object> info = new HashMap<>(1);
                                        info.put("fieldname", dataSearch.fieldname());
                                        info.put("order", dataSearch.order());
                                        info.put("search", searchhtml);
                                        info.put("newline", newline);
                                        info.put("colspan", colspan);
                                        search.add(info);
                                    }
                                }else {
                                    String pickList = !Utils.isEmpty(dataSearch.picklist()) ? dataSearch.picklist() : dataSearch.fieldname();
                                    if (Utils.isEmpty(pickList)) {
                                        continue;
                                    }
                                    Object picklists = PickListUtils.getPickList(pickList);
                                    if (!Utils.isEmpty(picklists)) {
                                        int uitype = dataSearch.pickintval() ? 20 : 19;
                                        String tagStr = this.dataEntity.tabs.modulename + "_" + dataSearch.fieldname();
                                        String searchhtml = this.getTextHtml(dataSearch.fieldlabel(), dataSearch.fieldname(), dataSearch.searchvalue(), tagStr, (List<Object>) picklists, uitype);
                                        boolean isfind = false;
                                        for(Object searchitem: search){
                                            if(((HashMap<?,?>)searchitem).get("fieldname").equals(dataSearch.fieldname())){
                                                Map<String,Object> info = new HashMap<>(1);
                                                info.put("fieldname",dataSearch.fieldname());
                                                info.put("order", dataSearch.order());
                                                info.put("search", searchhtml);
                                                info.put("newline", newline);
                                                info.put("colspan", colspan);
                                                Collections.replaceAll(search, searchitem, info);
                                                isfind = true;
                                                break;
                                            }
                                        }
                                        if(!isfind) {
                                            Map<String,Object> info = new HashMap<>(1);
                                            info.put("fieldname", dataSearch.fieldname());
                                            info.put("order", dataSearch.order());
                                            info.put("search", searchhtml);
                                            info.put("newline", newline);
                                            info.put("colspan", colspan);
                                            search.add(info);
                                        }
                                    }
                                }
                            }
                            break;
                            case "select": {
                                if(!Utils.isEmpty(dataSearch.options()) && !dataSearch.options().isEmpty()){
                                    List<Object> picklists = new ArrayList<>();
                                    String value = "";
                                    for(SelectOption option : dataSearch.options()){
                                        if(option.selected){
                                            value = option.value;
                                        }
                                        picklists.add(Arrays.asList(option.value,option.label));
                                    }
                                    String searchhtml = this.getSelectHtml(dataSearch.fieldlabel(), dataSearch.fieldname(), value, dataSearch.tip(), dataSearch.aux(), picklists, 18,dataSearch.hasSearch(),dataSearch.searchvalue().isEmpty());
                                    boolean isfind = false;
                                    for(Object searchitem: search){
                                        if(((HashMap<?,?>)searchitem).get("fieldname").equals(dataSearch.fieldname())){
                                            Map<String,Object> info = new HashMap<>(1);
                                            info.put("fieldname",dataSearch.fieldname());
                                            info.put("order", dataSearch.order());
                                            info.put("search", searchhtml);
                                            info.put("newline", newline);
                                            info.put("colspan", colspan);
                                            Collections.replaceAll(search, searchitem, info);
                                            isfind = true;
                                            break;
                                        }
                                    }
                                    if(!isfind) {
                                        Map<String,Object> info = new HashMap<>(1);
                                        info.put("fieldname", dataSearch.fieldname());
                                        info.put("order", dataSearch.order());
                                        info.put("search", searchhtml);
                                        info.put("newline", newline);
                                        info.put("colspan", colspan);
                                        search.add(info);
                                    }
                                }else {
                                    String pickList = !Utils.isEmpty(dataSearch.picklist()) ? dataSearch.picklist() : dataSearch.fieldname();
                                    if (Utils.isEmpty(pickList)) {
                                        continue;
                                    }
                                    Object picklists = PickListUtils.getPickList(pickList);
                                    if (!Utils.isEmpty(picklists)) {
                                        int uitype = dataSearch.pickintval() ? 20 : 19;
                                        String searchhtml = this.getSelectHtml(dataSearch.fieldlabel(), dataSearch.fieldname(), dataSearch.searchvalue(), dataSearch.tip(),dataSearch.aux(), (List<Object>) picklists, uitype);
                                        boolean isfind = false;
                                        for(Object searchitem: search){
                                            if(((HashMap<?,?>)searchitem).get("fieldname").equals(dataSearch.fieldname())){
                                                Map<String,Object> info = new HashMap<>(1);
                                                info.put("fieldname",dataSearch.fieldname());
                                                info.put("order", dataSearch.order());
                                                info.put("search", searchhtml);
                                                info.put("newline", newline);
                                                info.put("colspan", colspan);
                                                Collections.replaceAll(search, searchitem, info);
                                                isfind = true;
                                                break;
                                            }
                                        }
                                        if(!isfind) {
                                            Map<String,Object> info = new HashMap<>(1);
                                            info.put("fieldname", dataSearch.fieldname());
                                            info.put("order", dataSearch.order());
                                            info.put("search", searchhtml);
                                            info.put("newline", newline);
                                            info.put("colspan", colspan);
                                            search.add(info);
                                        }
                                    }
                                }
                            }
                            break;
                            case "tree_select": {
                                if(!Utils.isEmpty(dataSearch.treeOptions()) && !dataSearch.treeOptions().isEmpty()) {
                                    String searchhtml = this.getTreeSelectHtml(this.dataEntity.tabs.modulename,dataSearch.fieldlabel(), dataSearch.fieldname(),dataSearch.tip(),dataSearch.treeOptions());
                                    Map<String,Object> info = new HashMap<>(1);
                                    info.put("fieldname", dataSearch.fieldname());
                                    info.put("order", dataSearch.order());
                                    info.put("search", searchhtml);
                                    info.put("newline", newline);
                                    info.put("colspan", colspan);
                                    search.add(info);
                                }
                            }
                            break;
                            case "calendar":{
                                String tagStr = this.dataEntity.tabs.modulename + "_" + dataSearch.fieldname();
                                String searchPaneHtml = this.getCalendarHtml(dataSearch.fieldlabel(), tagStr, dataSearch.fieldname(), dataSearch.width(), dataSearch.start(), dataSearch.end(), "", dataSearch.quickbtn());
                                boolean isfind = false;
                                for(Object searchitem: search){
                                    if(((HashMap<?,?>)searchitem).get("fieldname").equals(dataSearch.fieldname())){
                                        Map<String,Object> info = new HashMap<>(1);
                                        info.put("fieldname",dataSearch.fieldname());
                                        info.put("order", dataSearch.order());
                                        info.put("search", searchPaneHtml);
                                        info.put("newline", newline);
                                        info.put("colspan", colspan);
                                        Collections.replaceAll(search, searchitem, info);
                                        isfind = true;
                                        break;
                                    }
                                }
                                if(!isfind) {
                                    Map<String,Object> info = new HashMap<>(1);
                                    info.put("fieldname", dataSearch.fieldname());
                                    info.put("order", dataSearch.order());
                                    info.put("search", searchPaneHtml);
                                    info.put("newline", newline);
                                    info.put("colspan", colspan);
                                    search.add(info);
                                }
                            }
                            break;
                            default:{
                                String searchhtml = this.getInputhtml(dataSearch.fieldlabel(),"",dataSearch.fieldname(), dataSearch.searchvalue(),dataSearch.tip(),dataSearch.aux());
                                boolean isfind = false;
                                for(Object searchitem: search){
                                    if(((HashMap<?,?>)searchitem).get("fieldname").equals(dataSearch.fieldname())){
                                        Map<String,Object> info = new HashMap<>(1);
                                        info.put("fieldname",dataSearch.fieldname());
                                        info.put("order", dataSearch.order());
                                        info.put("search", searchhtml);
                                        info.put("newline", newline);
                                        info.put("colspan", colspan);
                                        Collections.replaceAll(search, searchitem, info);
                                        isfind = true;
                                        break;
                                    }
                                }
                                if(!isfind) {
                                    Map<String,Object> info = new HashMap<>(1);
                                    info.put("fieldname", dataSearch.fieldname());
                                    info.put("order", dataSearch.order());
                                    info.put("search", searchhtml);
                                    info.put("newline", newline);
                                    info.put("colspan", colspan);
                                    search.add(info);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }catch (Exception ignored) { }
        Collections.sort(search, new Comparator<Object>() {
            @Override
            public int compare(Object v1, Object v2) {
                Integer order1 = Integer.parseInt(((Map<String,Object>)v1).get("order").toString());
                Integer order2 = Integer.parseInt(((Map<String,Object>)v2).get("order").toString());
                return order1.compareTo(order2);
            }
        });
        Map<String,Object> info = new HashMap<>(1);
        info.put("column", column);
        info.put("search", search);
        return info;
    }

    public String getAuthorfilter(String filter, String oper,Object author) {
        if(Utils.isEmpty(author)) {
            return XN_Filter.filter(IPublicService.checkFilter(filter),"=",null);
        }
        if(Utils.isEmpty(oper)) {
            oper = "=";
        }
        try {
            List<Object> list = XN_Query.create("Profile").tag("profile")
                    .filter("givenname", oper, author)
                    .filter("type", "in", Arrays.asList("admin", "pt", "supplier"))
                    .end(20).execute();
            if (!list.isEmpty()) {
                List<String> ids = new ArrayList<>();
                for(Object item: list){
                    ids.add(((Profile)item).id);
                }
                if(!ids.isEmpty()){
                    return XN_Filter.filter(IPublicService.checkFilter(filter),"in",ids);
                }
            }
        }catch (Exception ignored) { }
        return XN_Filter.filter(IPublicService.checkFilter(filter),"=",null);
    }

    @ApiOperation("获取列表查询")
    public void getDataSearch(XN_Query query, HttpServletRequest request) {
        if (this.dataEntity.searchColumn.size() <= 0) {
            return;
        }
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        HttpSession session = request.getSession();
        HashMap<String, Object> sessionRequest = (HashMap<String,Object>)session.getAttribute(this.dataEntity.tabs.modulename+"_DataSearch");
        if(!Utils.isEmpty(sessionRequest)){
            for (String key : sessionRequest.keySet()){
                if(!httpRequest.containsKey(key)){
                    httpRequest.put(key,sessionRequest.get(key));
                }
            }
        }
        for (SearchColumn item : this.dataEntity.searchColumn) {
            String fieldname = item.fieldname;
            String type = item.searchtype;
            Object searchvalue = item.searchvalue;
            if ("hidden".equals(type)) {
                continue;
            }
            if ("multi_input".equals(type) || "input".equals(type) || "search_input".equals(type) || "vague_input".equals(type) || "text".equals(type)) {
                if (type.contains("multi_input")) {
                    if (!Utils.isEmpty(httpRequest.get("multi_" + fieldname))) {
                        String input = httpRequest.get("multi_" + fieldname).toString().trim();
                        if (input.isEmpty()) {
                            continue;
                        }
                        if (fieldname.contains(",")) {
                            String[] fields = StringUtils.split(fieldname, ",");
                            Set<String> numSet = new HashSet<>(Arrays.asList(fields));
                            List<String> subQuery = new ArrayList<>(1);
                            for (String field : numSet) {
                                if("author".equals(field) || "finishapprover".equals(field)){
                                    String filterStr = getAuthorfilter(field,"like",input);
                                    if(!Utils.isEmpty(filterStr)) {
                                        subQuery.add(filterStr);
                                    }
                                }else {
                                    subQuery.add(XN_Filter.filter(IPublicService.checkFilter(field), "like", input));
                                }
                            }
                            if (!subQuery.isEmpty()) {
                                query.filter(XN_Filter.any(subQuery));
                            }
                        }
                    }else if(!httpRequest.containsKey(fieldname) && !Utils.isEmpty(searchvalue)) {
                        if (fieldname.contains(",")) {
                            String[] fields = StringUtils.split(fieldname, ",");
                            Set<String> numSet = new HashSet<String>(Arrays.asList(fields));
                            List<String> subQuery = new ArrayList<String>();
                            for (String field : numSet) {
                                if("author".equals(field) || "finishapprover".equals(field)){
                                    String filterStr = getAuthorfilter(field,"like",searchvalue);
                                    if(!Utils.isEmpty(filterStr)) {
                                        subQuery.add(filterStr);
                                    }
                                }else {
                                    subQuery.add(XN_Filter.filter(IPublicService.checkFilter(field), "like", searchvalue));
                                }
                            }
                            if (!subQuery.isEmpty()) {
                                query.filter(XN_Filter.any(subQuery));
                            }
                        }
                    }
                } else if (type.contains("vague_input")) {
                    if (!Utils.isEmpty(httpRequest.get(fieldname))) {
                        String input = httpRequest.get(fieldname).toString().trim();
                        if (input.isEmpty()) {
                            continue;
                        }
                        if("author".equals(fieldname) || "finishapprover".equals(fieldname)){
                            query.filter(getAuthorfilter(fieldname,"like",input));
                        }else {
                            query.filter(IPublicService.checkFilter(fieldname), "like", input);
                        }
                    }else if(!httpRequest.containsKey(fieldname) && !Utils.isEmpty(searchvalue)) {
                        if("author".equals(fieldname) || "finishapprover".equals(fieldname)){
                            query.filter(getAuthorfilter(fieldname,"like",searchvalue));
                        }else {
                            query.filter(IPublicService.checkFilter(fieldname), "like", searchvalue);
                        }
                    }
                } else if (!Utils.isEmpty(httpRequest.get(fieldname))) {
                    String input = httpRequest.get(fieldname).toString().trim();
                    if (input.isEmpty()) {
                        continue;
                    }
                    if("author".equals(fieldname) || "finishapprover".equals(fieldname)){
                        query.filter(getAuthorfilter(fieldname,"=",input));
                    }else {
                        query.filter(IPublicService.checkFilter(fieldname), "=", input);
                    }
                } else if(!httpRequest.containsKey(fieldname) && !Utils.isEmpty(searchvalue)) {
                    if("author".equals(fieldname) || "finishapprover".equals(fieldname)){
                        query.filter(getAuthorfilter(fieldname,"=",searchvalue));
                    }else {
                        query.filter(IPublicService.checkFilter(fieldname), "=", searchvalue);
                    }
                }
            } else if (type.contains("calendar")) {
                String startdate = "";
                String enddate = "";
                int uitype = getUitype(fieldname);
                if (!Utils.isEmpty(httpRequest.get(fieldname + "_startdate"))) {
                    switch (uitype){
                        case 6:
                            startdate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getDateByFormatString(httpRequest.get(fieldname + "_startdate").toString(), DateTimeUtils.DATE_FORMAT), DateTimeUtils.DATE_FORMAT);
                            break;
                        case 7:
                            startdate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getDateByFormatString(httpRequest.get(fieldname + "_startdate").toString(), DateTimeUtils.DATE_FORMAT), DateTimeUtils.DATE_FORMAT) + " 00:00";
                            break;
                        case 8:
                            startdate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getDateByFormatString(httpRequest.get(fieldname + "_startdate").toString(), DateTimeUtils.TIME_FORMAT), DateTimeUtils.TIME_FORMAT);
                            break;
                        default:
                            startdate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getDateByFormatString(httpRequest.get(fieldname + "_startdate").toString(), DateTimeUtils.DATE_FORMAT), DateTimeUtils.DATE_FORMAT) + " 00:00:00";
                            break;
                    }
                }
                if (!Utils.isEmpty(httpRequest.get(fieldname + "_enddate"))) {
                    switch (uitype){
                        case 6:
                            enddate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getDateByFormatString(httpRequest.get(fieldname + "_enddate").toString(), DateTimeUtils.DATE_FORMAT), DateTimeUtils.DATE_FORMAT);
                            break;
                        case 7:
                            enddate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getDateByFormatString(httpRequest.get(fieldname + "_enddate").toString(), DateTimeUtils.DATE_FORMAT), DateTimeUtils.DATE_FORMAT) + " 23:59";
                            break;
                        case 8:
                            enddate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getDateByFormatString(httpRequest.get(fieldname + "_enddate").toString(), DateTimeUtils.TIME_FORMAT), DateTimeUtils.TIME_FORMAT);
                            break;
                        default:
                            enddate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getDateByFormatString(httpRequest.get(fieldname + "_enddate").toString(), DateTimeUtils.DATE_FORMAT), DateTimeUtils.DATE_FORMAT) + " 23:59:59";
                            break;
                    }
                }
                if (fieldname.contains("published") || fieldname.contains("updated")) {
                    if (Utils.isEmpty(startdate) && Utils.isEmpty(enddate) && Utils.isEmpty(httpRequest.get(fieldname + "_thistype"))) {
                        httpRequest.put(fieldname + "_thistype",this.dataEntity.tabs.defaultsection.toLowerCase());
                        switch (this.dataEntity.tabs.defaultsection.toLowerCase()) {
                            case "thisyear":
                                startdate = DateTimeUtils.getDateAttr(Calendar.YEAR, new Date()) + "-01-01 00:00:00";
                                enddate = DateTimeUtils.getDateStringByFormat(new Date(), "yyyy-MM-dd 23:59:59");
                                break;
                            case "thisquater":
                                startdate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getFirstDateOfMonth(DateTimeUtils.getSeasonDate(new Date())[0]), "yyyy-MM-dd 00:00:00");
                                enddate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getLastDateOfMonth(DateTimeUtils.getSeasonDate(new Date())[2]), "yyyy-MM-dd 23:59:59");
                                break;
                            case "thismonth":
                                startdate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getFirstDateOfMonth(new Date()), "yyyy-MM-dd 00:00:00");
                                enddate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getLastDateOfMonth(new Date()), "yyyy-MM-dd 23:59:59");
                                break;
                            default:
                                startdate = DateTimeUtils.getDateStringByFormat(DateTimeUtils.getFormatDate(Calendar.MONTH, -1, new Date()), "yyyy-MM-dd 00:00:00");
                                enddate = DateTimeUtils.getDateStringByFormat(new Date(), "yyyy-MM-dd 23:59:59");
                                break;
                        }
                    }
                    if (!Utils.isEmpty(httpRequest.get(fieldname + "_thistype")) && "all".equals(httpRequest.get(fieldname + "_thistype"))) {
                        httpRequest.remove(fieldname + "_startdate");
                        httpRequest.remove(fieldname + "_enddate");
                    } else if (!Utils.isEmpty(httpRequest.get(fieldname + "_thistype")) && !startdate.isEmpty() && !enddate.isEmpty()) {
                        httpRequest.put(fieldname + "_startdate",startdate);
                        httpRequest.put(fieldname + "_enddate",enddate);
                    }
                }
                if (Utils.isNotEmpty(startdate) && Utils.isNotEmpty(enddate)) {
                    query.filter(IPublicService.checkFilter(fieldname), ">=", startdate);
                    query.filter(IPublicService.checkFilter(fieldname), "<=", enddate);
                } else if (!startdate.isEmpty()) {
                    query.filter(IPublicService.checkFilter(fieldname), ">=", startdate);
                } else if (!enddate.isEmpty()) {
                    query.filter(IPublicService.checkFilter(fieldname), "<=", enddate);
                }
            } else if (item.searchtype.contains("select")) {
                if (!Utils.isEmpty(httpRequest.get(fieldname))) {
                    String input = httpRequest.get(fieldname).toString().trim();
                    if (input.isEmpty()) {
                        continue;
                    }
                    query.filter(IPublicService.checkFilter(fieldname), "=", input);
                }else if(!httpRequest.containsKey(fieldname) && !Utils.isEmpty(searchvalue)){
                    query.filter(IPublicService.checkFilter(fieldname), "=", searchvalue);
                }
            }
        }
        session.setAttribute(this.dataEntity.tabs.modulename+"_DataSearch",httpRequest);
    }

    private int getUitype(String fieldname) {
        int result = 0;
        for(TabField field: this.dataEntity.fields){
            if(field.fieldname.equals(fieldname)) {
                result = field.uitype;
                break;
            }
        }
        return result;
    }

    @ApiOperation("获取页面回传分页及排序信息")
    public Map<String, Object> getOrderBy(HttpServletRequest request, String module) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        HttpSession session = request.getSession();
        HashMap<String, Object> sessionRequest = (HashMap)session.getAttribute(this.dataEntity.tabs.modulename+"_DataSearch");
        if(!Utils.isEmpty(sessionRequest)){
            sessionRequest.remove("limit");
            boolean isSort = true;
            for (String key : sessionRequest.keySet()){
                if("sortType".equals(key) && Utils.isEmpty(httpRequest.get(key))){
                    isSort = false;
                }
                if(!httpRequest.containsKey(key) || Utils.isEmpty(httpRequest.get(key))){
                    httpRequest.put(key, sessionRequest.get(key));
                }else if("sortBy".equals(key) && !isSort){
                    httpRequest.put(key, "");
                }
            }
        }
        int page = 1;
        int limit = ProfileUtils.getPageLimit();
        String orderBy = "published";
        String orderType = "DESC";
        String orderby = this.getDefaultOrderBy(httpRequest.getOrDefault("viewid", "").toString());
        String order = this.getDefaultOrder(httpRequest.getOrDefault("viewid", "").toString());
        if (!orderby.isEmpty()) {
            orderBy = orderby;
        }
        if (!order.isEmpty()) {
            orderType = order;
        }

        if(!Utils.isEmpty(httpRequest.get("page"))){
            page = Double.valueOf(httpRequest.getOrDefault("page","1").toString()).intValue();
        }
        if(httpRequest.containsKey("limit") && httpRequest.get("limit").toString().compareTo("") != 0){
            limit = Double.valueOf(httpRequest.getOrDefault("limit","20").toString()).intValue();
            ProfileUtils.updatePageLimit(limit);
        }

        if (!Utils.isEmpty(httpRequest.get("sortType")) && !Utils.isEmpty(httpRequest.get("sortBy")) && this.getCustomViewFields(httpRequest.getOrDefault("viewid","").toString()).contains(httpRequest.get("sortBy").toString())) {
            orderType = httpRequest.get("sortType").toString();
            if (!Utils.isEmpty(this.moduleFields.get(httpRequest.get("sortBy").toString())) && this.moduleFields.get(httpRequest.get("sortBy").toString()).isnumsort) {
                orderType += "_number";
            }
            if (!Utils.isEmpty(httpRequest.get("sortBy"))) {
                orderBy = IPublicService.checkFilter(httpRequest.get("sortBy").toString().toLowerCase());
            }
        } else {
            if(!orderby.isEmpty()) {
                orderBy = IPublicService.checkFilter(orderby);
            }
            httpRequest.put("sortBy",orderBy.replace("my.",""));
            httpRequest.put("sortType",orderType);
        }
        session.setAttribute(this.dataEntity.tabs.modulename+"_DataSearch",httpRequest);

        int finalPage = page;
        int finalLimit = limit;
        String finalOrderBy = orderBy;
        String finalOrderType = orderType;
        Map<String,Object> info = new HashMap<>(1);
        info.put("page", finalPage);
        info.put("limit", finalLimit);
        info.put("orderby", finalOrderBy);
        info.put("ordertype", finalOrderType);
        return info;
    }

    @ApiOperation("根据配置信息，生成列表页面按钮")
    private List<Object> getListViewActions() {
        List<Action> buttons = new ArrayList<>();
        if (this.actionsEntity.tabs.iscreate && (!this.workflowStatus.isDefineflow() || this.workflowStatus.isSuspend() || (this.workflowStatus.isDefineflow() && !this.workflowStatus.isSuspend() && this.workflowStatus.isWorkflow() && this.workflowStatus.isStartUser()))) {
            buttons.add(new Action().actionkey("EditView").actionlabel("新建").toggle("navtab").icon("lcdp-icon-xinjian"));
        }
        List<String> actionKeys = new ArrayList<>();
        if (this.actionsEntity.actions != null && !this.actionsEntity.actions.isEmpty()) {
            buttons.forEach(item -> {
                actionKeys.add(item.actionkey);
            });

            for (Action item : this.actionsEntity.actions) {
                if (!Utils.isEmpty(item.actiontype) && "listview".equals(item.actiontype)) {
                    if (Utils.isEmpty(item.actionkey)) {
                        continue;
                    }
                    Object funResult = null;
                    if (item.securitycheck) {
                        //进行权限验证
                        if (Utils.isNotEmpty(item.authorizes)) {
                            funResult = false;
                            List<String> authorizes = Arrays.asList(item.authorizes.split(","));
                            String profileid = ProfileUtils.getCurrentProfileId();
                            for (String authorize : authorizes) {
                                if (AuthorizeUtils.isAuthorizes(profileid, authorize)) {
                                    funResult = true;
                                    break;
                                }
                            }
                            if (!Boolean.parseBoolean(funResult.toString()) && !Utils.isEmpty(item.funclass)) {
                                try {
                                    funResult = getActionVerify(item.funclass, item.funpara, this.actionsEntity.tabs.modulename, "");
                                } catch (Exception e) {
                                    funResult = false;
                                }
                            }
                        } else {
                            if (!Utils.isEmpty(item.funclass)) {
                                //执行自定义处理函数
                                try {
                                    funResult = getActionVerify(item.funclass, item.funpara, this.actionsEntity.tabs.modulename, "");
                                } catch (Exception e) {
                                    funResult = false;
                                }
                            } else {
                                funResult = false;
                            }
                        }
                    }
                    if (actionKeys.contains(item.actionkey)) {
                        for (Action button : buttons) {
                            if (button.actionkey.equals(item.actionkey)) {
                                if (funResult instanceof Boolean) {
                                    if (!((Boolean) funResult)) {
                                        buttons.remove(button);
                                        actionKeys.remove(item.actionkey);
                                        break;
                                    }
                                } else {
                                    if (!Utils.isEmpty(funResult) && funResult instanceof String) {
                                        item.element(funResult.toString());
                                    }
                                }
                                if (!item.actionlabel.isEmpty()) {
                                    button.actionlabel(item.actionlabel);
                                }
                                if (!item.icon.isEmpty()) {
                                    button.icon(item.icon);
                                }
                                if (!item.event.isEmpty()) {
                                    button.event(item.event);
                                }
                                if (!item.toggle.isEmpty()) {
                                    button.toggle(item.toggle);
                                }
                                if (!item.group.isEmpty()) {
                                    button.group(item.group);
                                }
                                if (!item.digwidth.isEmpty()) {
                                    button.digwidth(item.digwidth);
                                }
                                if (!item.digheight.isEmpty()) {
                                    button.digheight(item.digheight);
                                }
                                if (!item.confirm.isEmpty()) {
                                    button.confirm(item.confirm);
                                }
                                if (!item.url.isEmpty()) {
                                    button.url(item.url);
                                }
                                if (!item.actionclass.isEmpty()) {
                                    button.actionclass(item.actionclass);
                                }
                                if (!item.actionstyle.isEmpty()) {
                                    button.actionstyle(item.actionstyle);
                                }
                                if (!item.multiselect) {
                                    button.multiselect(false);
                                }
                                break;
                            }
                        }
                    } else {
                        if (funResult instanceof Boolean) {
                            if (!((Boolean) funResult)) {
                                actionKeys.remove(item.actionkey);
                                continue;
                            }
                        } else {
                            if (!Utils.isEmpty(funResult) && funResult instanceof String) {
                                item.element(funResult.toString());
                            }
                        }
                        buttons.add(item);
                        actionKeys.add(item.actionkey);
                    }
                }
            }
        }
        if(!this.workflowStatus.isDefineflow() && !this.workflowStatus.isSuspend()) {
            if (this.actionsEntity.tabs.isimport && !actionKeys.contains("Import")&&ImportdataUtils.hasExcelset(this.actionsEntity.tabs.modulename)) {
                buttons.add(new Action().actionkey("importdata").actionlabel("Excel导入").toggle("dialog").url("importdataSet/importAction/ajax"));
            }
        }
        if (this.actionsEntity.tabs.isdelete && !actionKeys.contains("Delete")) {
            buttons.add(new Action().actionkey("Delete").actionlabel("批量删除").icon("lcdp-icon-shanchu").toggle("ajax").actionclass("layui-btn-danger").group("ids").confirm("确定要删除选择的记录吗？"));
        }
        if (RolesUtils.isSupperDelete() && !actionKeys.contains("SuperDelete")) {
            buttons.add(new Action().actionkey("SuperDelete").actionlabel("超级删除").icon("lcdp-icon-shanchu").toggle("ajax").actionclass("layui-btn-danger").group("ids")
                    .confirm("确实要删除这些记录吗？超级删除功能将无视数据关联，可能造成关联数据错误，请慎用！").modulename(this.actionsEntity.tabs.modulename).url("/superdeleteAction/ajax"));
        }

        List<Object> result = new ArrayList<>();
        for (Action action : buttons) {
            if("EditView".equals(action.actionkey)){
                if(!RolesUtils.isEdit(this.dataEntity.tabs.modulename)){
                    continue;
                }
            }
            if("Delete".equals(action.actionkey)){
                if(!RolesUtils.isDelete(this.dataEntity.tabs.modulename)){
                    continue;
                }
            }
            if("BatchSubmitOnline".equals(action.actionkey)) {
                if(this.workflowStatus.isDefineflow() && !this.workflowStatus.isSuspend()) {
                    continue;
                }
            }
            result.add(action.toMap());
        }
        return result;
    }

    @ApiOperation("根据配置信息，生成列表操作按钮")
    private List<Object> getListViewOperationActions(){
        List<Action> buttons = new ArrayList<>();
        if (this.actionsEntity.actions != null && !this.actionsEntity.actions.isEmpty()) {
            List<String> actionKeys = new ArrayList<>();
            for (Action item : this.actionsEntity.actions) {
                if (!Utils.isEmpty(item.actiontype) && "operation".equals(item.actiontype)) {
                    if (Utils.isEmpty(item.actionkey)) {
                        continue;
                    }
                    Object funResult = null;
                    if (item.securitycheck) {
                        //进行权限验证
                        if (Utils.isNotEmpty(item.authorizes)) {
                            funResult = false;
                            List<String> authorizes = Arrays.asList(item.authorizes.split(","));
                            String profileid = ProfileUtils.getCurrentProfileId();
                            for(String authorize : authorizes) {
                                if (AuthorizeUtils.isAuthorizes(profileid,authorize)) {
                                    funResult = true;
                                    break;
                                }
                            }
                            if (!Boolean.parseBoolean(funResult.toString()) && !Utils.isEmpty(item.funclass)) {
                                try {
                                    funResult = getActionVerify(item.funclass, item.funpara, this.actionsEntity.tabs.modulename, "");
                                } catch (Exception e) {
                                    funResult = false;
                                }
                            }
                        } else {
                            if (!Utils.isEmpty(item.funclass)) {
                                //执行自定义处理函数
                                try {
                                    funResult = getActionVerify(item.funclass, item.funpara, this.actionsEntity.tabs.modulename, null);
                                } catch (Exception e) {
                                    funResult = false;
                                }
                            } else {
                                funResult = false;
                            }
                        }
                    }
                    if (actionKeys.contains(item.actionkey)) {
                        for (Action button : buttons) {
                            if (button.actionkey.equals(item.actionkey)) {
                                if (funResult instanceof Boolean) {
                                    if (!((Boolean) funResult)) {
                                        buttons.remove(button);
                                        actionKeys.remove(item.actionkey);
                                        break;
                                    }
                                }
                                if (!item.icon.isEmpty()) {
                                    button.icon(item.icon);
                                }
                                if (!item.url.isEmpty()) {
                                    button.url(item.url);
                                }
                                break;
                            }
                        }
                    } else {
                        if (funResult instanceof Boolean) {
                            if (!((Boolean) funResult)) {
                                actionKeys.remove(item.actionkey);
                                continue;
                            }
                        }
                        buttons.add(item);
                        actionKeys.add(item.actionkey);
                    }
                }
            }
        }
        List<Object> result = new ArrayList<>();
        for (Action action : buttons) {
            result.add(action.toMap());
        }
        return result;
    }
    private List<Object> getCustomListViewHeader(List<TabField> fields,Boolean allowSort) {
        List<Object> viewHeader = new ArrayList<>();
        for (TabField item : fields) {
            String label = item.fieldlabel;
            String align = item.align;
            String width = item.listwidth;
            Boolean isSort = item.issort;
            Map<String,Object> info = new HashMap<>(1);
            info.put("field", item.fieldname);
            info.put("event", item.fieldname);
            info.put("title", label);
            info.put("align", align);
            if (!width.isEmpty()) {
                info.put("width", Integer.parseInt(width));
            }
            if (allowSort && isSort) {
                info.put("sort", true);
            }
            viewHeader.add(info);
        }
        return viewHeader;
    }

    public List<CustomFieldSetting> getCustomFields() {
        List<CustomFieldSetting> customFieldSetting = new ArrayList<>();
        try{
            Object result = CallbackUtils.invoke("getCustomFields", entityPackageName, IBaseService.class);
            if (!Utils.isEmpty(result)) {
                customFieldSetting = (List<CustomFieldSetting>)result;
            }
        }catch (Exception ignored) { }
        return customFieldSetting;
    }

    @ApiOperation("根据定义生成列表页面的表头")
    public List<List<Map<String,Object>>> getListViewHeader(Map<String, Object> httpRequest) {
        List<String> viewFields = this.getCustomViewFields(httpRequest.getOrDefault("viewid", "").toString());
        List<String> statistic = this.statisticFields;

        List<CustomFieldSetting> customFieldSetting = getCustomFields();
        Map<String,CustomFieldSetting> fieldSetting = customFieldSetting.stream().collect(Collectors.toMap(v1 -> v1.fieldname, v1 -> v1, (k1, k2) -> k1));

        Map<String,String> groupMaps = viewFields.stream()
                .map(v -> this.moduleFields.get(v))
                .filter( v -> Utils.isNotEmpty(v) && Utils.isNotEmpty(v.grouplabel))
                .collect(Collectors.toMap(v1 -> v1.fieldname, v1 -> v1.grouplabel, (k1, k2) -> k1));
        Map<String,String> customGroupMaps = customFieldSetting.stream().filter( v -> Utils.isNotEmpty(v.grouplabel)).collect(Collectors.toMap(v1 -> v1.fieldname, v1 -> v1.grouplabel, (k1, k2) -> k1));
        groupMaps.putAll(customGroupMaps);
        Boolean hasGroupHeader = groupMaps.size() > 0;
        Map<String,Object> info = new HashMap<>(1);
        info.put("type", "checkbox");
        info.put("fixed", "left");
        if (!statistic.isEmpty()) {
            info.put("totalRowText", "合计");
        }
        if (hasGroupHeader) {
            info.put("rowspan", 2);
        }
        List<Map<String,Object>> viewHeaders = new ArrayList<>(ImmutableSet.of(info));
        List<Map<String,Object>> secondHeaders = new ArrayList<>();

        String groupLabel = "";
        for (String item : viewFields) {
            TabField field = this.moduleFields.get(item);
            if (field != null) {
                int displaytype = field.displaytype;
                if (displaytype == 0 || displaytype == 2) {
                    String label = field.fieldlabel;
                    String align = field.align;
                    String width = field.listwidth;
                    Boolean isSort = field.issort;
                    Map<String,Object> fieldInfo = new HashMap<>(1);
                    fieldInfo.put("field", item);
                    if (fieldSetting.containsKey(item)) {
                        fieldInfo.put("title", fieldSetting.get(item).fieldlabel);
                    } else {
                        fieldInfo.put("title", label);
                    }
                    fieldInfo.put("align", align);
                    if (!width.isEmpty()) {
                        if(!width.contains("px") && !width.contains("%")){
                            fieldInfo.put("minWidth", width);
                        }else {
                            fieldInfo.put("width", width);
                        }
                    }
                    if (isSort) {
                        fieldInfo.put("sort", true);
                    }
                    String modulename = this.dataEntity.tabs.modulename;
                    if (field.event) {
                        fieldInfo.put("event", modulename + "_" + field.fieldname);
                    }
                    Boolean isValid = false;
                    if (fieldSetting.containsKey(item)) {
                        if (!fieldSetting.get(item).ishidden) {
                            isValid = true;
                        }
                    } else {
                        isValid = true;
                    }
                    if (isValid) {
                        if (hasGroupHeader) {
                            if (Utils.isNotEmpty(field.grouplabel)) {
                                if (groupLabel.equals(field.grouplabel)) {
                                    secondHeaders.add(fieldInfo);
                                    Map<String,Object> groupInfo = viewHeaders.get(viewHeaders.size() - 1);
                                    Integer colspan = (Integer)groupInfo.get("colspan");
                                    groupInfo.put("colspan",colspan + 1);
                                    viewHeaders.remove(viewHeaders.size() - 1);
                                    viewHeaders.add(groupInfo);
                                } else {
                                    secondHeaders.add(fieldInfo);
                                    Map<String,Object> groupInfo = new HashMap<>(1);
                                    groupInfo.put("title",field.grouplabel);
                                    groupInfo.put("align","center");
                                    groupInfo.put("colspan", 1);
                                    viewHeaders.add(groupInfo);
                                    groupLabel = field.grouplabel;
                                }
                            }else if (fieldSetting.containsKey(item) && Utils.isNotEmpty(fieldSetting.get(item).grouplabel)) {
                                CustomFieldSetting cfs = fieldSetting.get(item);
                                if (groupLabel.equals(cfs.grouplabel)) {
                                    secondHeaders.add(fieldInfo);
                                    Map<String,Object> groupInfo = viewHeaders.get(viewHeaders.size() - 1);
                                    Integer colspan = (Integer)groupInfo.get("colspan");
                                    groupInfo.put("colspan",colspan + 1);
                                    viewHeaders.remove(viewHeaders.size() - 1);
                                    viewHeaders.add(groupInfo);
                                } else {
                                    secondHeaders.add(fieldInfo);
                                    Map<String,Object> groupInfo = new HashMap<>(1);
                                    groupInfo.put("title",cfs.grouplabel);
                                    groupInfo.put("align","center");
                                    groupInfo.put("colspan", 1);
                                    viewHeaders.add(groupInfo);
                                    groupLabel = cfs.grouplabel;
                                }
                            }
                            else {
                                fieldInfo.put("rowspan", 2);
                                groupLabel = "";
                                viewHeaders.add(fieldInfo);
                            }
                        } else {
                            viewHeaders.add(fieldInfo);
                        }
                    }
                }
            } else if (Arrays.asList("title", "author", "published", "updated", "approvalstatus", "submitdatetime", "finishapprover", "approvaldatetime").contains(item)) {
                String label = "";
                String width = "120";
                switch (item) {
                    case "title":
                        label = "标题";
                        break;
                    case "author":
                        label = "创建人";
                        break;
                    case "published":
                        label = "创建时间";
                        width = "165";
                        break;
                    case "updated":
                        label = "更新时间";
                        width = "165";
                        break;
                    case "approvalstatus":
                        label = "状态";
                        break;
                    case "submitdatetime":
                        label = "提交审批时间";
                        width = "165";
                        break;
                    case "approvaldatetime":
                        label = "审批时间";
                        width = "165";
                        break;
                    case "finishapprover":
                        label = "审批人";
                        break;
                    default:
                        break;
                }
                Map<String,Object> fieldInfo = new HashMap<>(1);
                fieldInfo.put("field", item);
                fieldInfo.put("title", label);
                fieldInfo.put("align", "center");
                fieldInfo.put("sort", "true");
                fieldInfo.put("minWidth", width);
                if (hasGroupHeader) {
                    fieldInfo.put("rowspan", 2);
                }
                viewHeaders.add(fieldInfo);
            }
        }

        int op = this.getListViewOperationActions().size();
        if (viewHeaders.size() > 1 && (this.dataEntity.tabs.hasoperator || op > 0)) {
            int width = 30;
            if (this.dataEntity.tabs.hasoperator) {
                width = 60;
            }
            if(op > 0){
                width = 30 + 20 * (op+1) + 17 * op;
            }
            String modulename = this.dataEntity.tabs.modulename;
            Map<String,Object> opeInfo = new HashMap<>(1);
            opeInfo.put("fixed", "right");
            opeInfo.put("title", "操作");
            opeInfo.put("toolbar", "#" + modulename + "_listview_record_panel");
            opeInfo.put("align", "center");
            opeInfo.put("minWidth", "60");
            opeInfo.put("width", width);
            if (hasGroupHeader) {
                opeInfo.put("rowspan", 2);
            }
            viewHeaders.add(opeInfo);
        }
        if (hasGroupHeader && secondHeaders.size() > 0) {
            return Arrays.asList(viewHeaders, secondHeaders);
        }else {
            return Arrays.asList(viewHeaders);
        }
    }

    @ApiOperation("根据字段定义的UIType格式化数据")
    public String formatByUiType(String record, String fieldName, Object fieldValue, Boolean isListview) {
        if ("author".equals(fieldName) || "finishapprover".equals(fieldName)) {
            try {
                if (!Utils.isEmpty(fieldValue)) {
                    return this.getOutsideProfileValue(fieldValue, "--");
                }
            } catch (Exception ignored) {
            }
            return "--";
        } else {
            if (!Utils.isEmpty(this.moduleFields.get(fieldName))) {
                TabField fieldInfo = this.moduleFields.get(fieldName);
                return formatByUiType(record, fieldName, fieldValue, isListview,fieldInfo);
            }else if("approvalstatus".equals(fieldName)) {
                if (!Utils.isEmpty(fieldValue)) {
                    WorkflowStatus workflowStatus = this.getWorkflowStatus();
                    if(Utils.isNotEmpty(workflowStatus) && !workflowStatus.isDefineflow()){
                        if ("0".equals(fieldValue.toString())) {
                            return this.getPicklistLabelByIntValue("approvalstatus", fieldValue, "--",isListview).replace("未审核","未提交");
                        }
                    }
                    return this.getPicklistLabelByIntValue("approvalstatus", fieldValue, "--",isListview);
                } else {
                    return "--";
                }
            }
        }
        if (fieldValue instanceof List) {
            return String.join(",", (List) fieldValue);
        } else {
            return fieldValue.toString();
        }
    }

    @ApiOperation("根据字段定义的UIType格式化数据")
    @SuppressWarnings("unchecked")
    public String formatByUiType(String record, String fieldName, Object fieldValue, Boolean isListview,TabField fieldInfo) {
        int uitype = fieldInfo.uitype;
        if(fieldValue instanceof List){
            fieldValue = String.join(",",(List<String>)fieldValue);
        }
        switch (uitype) {
            case 1:
                if(fieldValue instanceof Double) {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    fieldValue = nf.format(fieldValue);
                }
                break;
            case 5:
                if(!Utils.isEmpty(fieldValue) && ((fieldValue instanceof Integer && fieldValue.equals(1)) || (fieldValue instanceof Boolean && (Boolean) fieldValue) ||
                        fieldValue instanceof String && ("1".equals(fieldValue) || "true".equalsIgnoreCase((String) fieldValue)))) {
                    return "是";
                } else {
                    return "否";
                }
            case 6:
                if(!Utils.isEmpty(fieldValue)) {
                    return DateTimeUtils.getDateStringByFormat(DateTimeUtils.toDateTime(fieldValue.toString()),DateTimeUtils.DATE_FORMAT);
                } else {
                    return "--";
                }
            case 7:
                if(!Utils.isEmpty(fieldValue)) {
                    return DateTimeUtils.getDateStringByFormat(DateTimeUtils.toDateTime(fieldValue.toString()),DateTimeUtils.DATE_SHORT_TIME_FORMAT);
                } else {
                    return "--";
                }
            case 8:
                if(!Utils.isEmpty(fieldValue)) {
                    return DateTimeUtils.getDateStringByFormat(DateTimeUtils.toDateTime(fieldValue.toString()),DateTimeUtils.SHORT_TIME_FORMAT);
                } else {
                    return "--";
                }
            case 10:
                if(!Utils.isEmpty(fieldValue)){
                    if (NumberUtil.isInteger(fieldValue.toString()) ) {
                        return this.getOutsideDepartmentValue(fieldValue, "");
                    } else if (NumberUtil.isInteger(fieldValue.toString().split(",")[0]) ) {
                        return this.getOutsideDepartmentValue(fieldValue, "");
                    } else {
                        return fieldValue.toString();
                    }
                }else {
                    return "--";
                }
            case 11:
            case 12:
                    if (Utils.isNotEmpty(fieldValue)) {
                        if (NumberUtil.isInteger(fieldValue.toString()) ) {
                            return this.getOutsideLinkValue(fieldName, fieldValue, "--");
                        } else if (NumberUtil.isInteger(fieldValue.toString().split(",")[0]) ) {
                            return this.getOutsideLinkValue(fieldName, fieldValue, "--");
                        } else {
                            return fieldValue.toString();
                        }
                    } else {
                        return "--";
                    }
            case 13:
            case 14:
            case 15:
            case 16:
            case 19:
            case 20:
                if (!Utils.isEmpty(fieldValue)) {
                    if (Utils.isEmpty(fieldInfo.picklist)) {
                        if (PickListUtils.getPickList().contains(fieldName)) {
                            if (Arrays.asList(14, 16, 20).contains(uitype)) {
                                return this.getPicklistLabelByIntValue(fieldName, fieldValue, "--",isListview);
                            } else {
                                return this.getPicklistLabelByValue(fieldName, fieldValue, "--",isListview);
                            }
                        } else {
                           return fieldValue.toString();
                        }
                    } else {
                        if (Arrays.asList(14, 16, 20).contains(uitype)) {
                            return this.getPicklistLabelByIntValue(fieldInfo.picklist, fieldValue, "--",isListview);
                        } else {
                            return this.getPicklistLabelByValue(fieldInfo.picklist, fieldValue, "--",isListview);
                        }
                    }
                } else {
                    return "--";
                }
            case 24:
                if(!Utils.isEmpty(fieldValue)){
                    return Utils.delHTMLTag(fieldValue.toString());
                } else {
                    return "--";
                }
            case 25:
                if (!Utils.isEmpty(fieldValue)) {
                    return this.getOutsideProfileValue(fieldValue, "--");
                } else {
                    return "--";
                }
            case 27:
                int ratio = fieldInfo.ratio;
                if (ratio <= 0) {
                    ratio = 1;
                }
                if(Utils.isEmpty(fieldValue)) {
                    fieldValue = "0";
                }
                BigDecimal bc = new BigDecimal(fieldValue.toString());
                String value = bc.divide(BigDecimal.valueOf(ratio), 2, RoundingMode.HALF_DOWN).toString();
                NumberFormat nf = new DecimalFormat("￥#,##0.00");
                return nf.format(Double.parseDouble(value));
            case 17:
                if(isListview) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<img src='/file").append(fieldValue).append("?width=100' class='showBig' style='max-width: 100px;'>");
                    sb.append("<script type = \"text/javascript\">layui.use([], function () {");
                    sb.append("var $ = layui.jquery;");
                    sb.append("var $image = $('.showBig');");
                    sb.append("$image.viewer({inline: false,fullscreen: false,navbar: false,viewed: function() {}});");
                    sb.append("});");
                    sb.append("</script>");
                    return sb.toString();
                } else {
                    return fieldValue.toString();
                }
            case 28:
                if(isListview) {
                    String html = "<div class='rate-div "+record+"_"+fieldName+"'></div><script type = \"text/javascript\">layui.use(['rate'], function () {" +
                            "var rate = layui.rate;" +
                            "rate.render({\n" +
                            "            elem: '."+record+"_"+fieldName+"',\n" +
                            "            length: "+fieldInfo.scorelevel+",\n" +
                            "            value: '"+fieldValue+"',\n" +
                            "            half: false,\n" +
                            "            text: true,\n" +
                            "            readonly: true\n" +
                            "        });" +
                            "})</script>";
                    return html;
                } else {
                    return fieldValue.toString();
                }
            default:
                break;
        }
//        if (fieldValue instanceof List) {
//            return String.join(",", (List) fieldValue);
//        } else {
            return fieldValue.toString();
//        }
    }

    @ApiOperation("判断指定的资源文件是否存在")
    private boolean isExistResource(String sourcepath) {
        freemarker.template.Configuration config = FreemarkerConfig.getFreemarkerConfig();
        if (Utils.isNotEmpty(config)) {
            try {
                if (Utils.isNotEmpty(config.getTemplateLoader().findTemplateSource(sourcepath))) {
                    return true;
                }
            }catch (Exception ignored) { }
        }
        return false;
    }

    @ApiOperation("表格行编辑")
    public Object tableEditView(List<Object> entitys,List<TabField> fields, Map<String,Object> picklists,Map<String,Object> configs, Model model,String record) {
        return tableEditView(entitys, fields, picklists, new ArrayList<>(), configs, model, record,"TableEditView");
    }

    public Object tableEditView(List<Object> entitys,List<TabField> fields, Map<String,Object> picklists,List<OutsideLink> outsideLinks, Map<String,Object> configs, Model model,String record) {
        return tableEditView(entitys, fields, picklists, outsideLinks,configs, model, record, "TableEditView");
    }

    private List<Object> getTableEditViewHeader(List<TabField> fields,Boolean hasIndex,Boolean hasAdd,Boolean hasClone,Boolean hasDelete,String name,Map<String, Object> picklists,List<OutsideLink> outsideLinks) {
        Map<String, OutsideLink> outsideLinkMaps = outsideLinks.stream().collect(Collectors.toMap(v1 -> v1.fieldname, v1 -> v1, (k1, k2) -> k1));

        List<Object> viewHeader = new ArrayList<>();
        if (hasIndex) {
            Map<String,Object> info = new HashMap<>(1);
            info.put("field", "index");
            info.put("title", "序号");
            info.put("align", "center");
            info.put("width", "60");
            info.put("fixed", "left");
            viewHeader.add(info);
        }
        for (TabField item : fields) {
            String label = item.fieldlabel;
            String align = item.align;
            String width = item.listwidth;
            Map<String,Object> info = new HashMap<>(1);
            info.put("field", item.fieldname);
            info.put("event", item.fieldname);
            info.put("title", label);
            info.put("align", align);
            if (!width.isEmpty()) {
                info.put("width", Integer.parseInt(width));
            }
            if (Arrays.asList(13,14,15,16,19,20).contains(item.uitype)) {
                if (item.typeofdata.endsWith("~M")) {
                    info.put("required", "true");
                } else {
                    info.put("required", "false");
                }
                String key = name + "-" + item.fieldname;
                StringBuilder sb = new StringBuilder();
                sb.append("<button type=\"button\" class=\"layui-btn layui-btn-primary layui-wrap layui-edit dropdown-").append(key).append("\">").append("\n");
                if (picklists.containsKey(item.fieldname)) {
                   List<Map<String,String>> lists = (List<Map<String,String>>)picklists.get(item.fieldname);
                   if (lists.size() > 0 ) {
                       sb.append("   <span>");
                       sb.append("{{# if(d.").append(item.fieldname).append(" == ''){ }}");
                       sb.append("&nbsp;");
                       for(int i=0;i<lists.size();i++) {
                           Map<String,String> pickitem = lists.get(i);
                           sb.append("{{#} else if(d.").append(item.fieldname).append(" == '").append(pickitem.get("id")).append("'){ }}");
                           sb.append(pickitem.get("title"));
                       }
                       sb.append("{{# } }}");
                       sb.append("</span>").append("\n");
                   } else {
                       sb.append("   <span>&nbsp;</span>").append("\n");
                   }
                } else  {
                    sb.append("   <span>&nbsp;</span>").append("\n");
                }
                sb.append("   <i class=\"layui-icon layui-icon-down layui-font-12\"></i>").append("\n");
                sb.append("</button>").append("\n");
                info.put("tpl", sb.toString());
                info.put("type", "select");
                info.put("key", key);
                info.put("templet", "tpl-" + key );
            } else if (item.uitype == 11) {
                if (item.typeofdata.endsWith("~M")) {
                    info.put("required", "true");
                } else {
                    info.put("required", "false");
                }
                String key = name + "-" + item.fieldname;
                OutsideLink outsideLink = outsideLinkMaps.get(item.fieldname);
                StringBuilder sb = new StringBuilder();
                sb.append("<div class=\"layui-wrap layui-edit\">").append("\n");
                sb.append("<input lay-name=\"").append(item.fieldname).append("\" class=\"layui-input layui-popup-input\" readonly placeholder=\"请选择\" lay-url=\"").append(outsideLink.url).append("\" lay-remodule=\"").append(this.getModuletab().modulename).append("\" ").append("value=\"");
                if (picklists.containsKey(item.fieldname)) {
                    List<Map<String,String>> lists = (List<Map<String,String>>)picklists.get(item.fieldname);
                    if (lists.size() > 0 ) {
                        sb.append("{{# if(d.").append(item.fieldname).append(" == ''){ }}");
                        for(int i=0;i<lists.size();i++) {
                            Map<String,String> pickitem = lists.get(i);
                            sb.append("{{#} else if(d.").append(item.fieldname).append(" == '").append(pickitem.get("id")).append("'){ }}");
                            sb.append(pickitem.get("title"));
                        }
                        sb.append("{{# } }}");
                    }
                }
                sb.append("\">").append("\n");
                sb.append("<i class=\"lcdp-icon lcdp-icon-mysousuo layui-popup-icon\" title=\"选择\"></i>").append("\n");
                sb.append("<i class=\"lcdp-icon lcdp-icon-guanbi layui-clear-icon\" title=\"重置输入框\"></i>").append("\n");
//                <input type="text" lay-filter="fenlei" lay_title="" class="layui-input layui-popup-input mytest_edit_fenlei_name " autocomplete="off" readonly="" placeholder="请选择" value="" lay-url="fenlei/popupview" lay-remodule="mytest" lay-record="86894">
                sb.append("</div>").append("\n");
                info.put("tpl", sb.toString());
                info.put("type", "select");
                info.put("key", key);
                info.put("templet", "tpl-" + key );
            } else if (Arrays.asList(5,6).contains(item.uitype)) {
                if (item.typeofdata.endsWith("~M")) {
                    info.put("required", "true");
                } else {
                    info.put("required", "false");
                }
                String key = name + "-" + item.fieldname;
                StringBuilder sb = new StringBuilder();
                sb.append("<input class=\"layui-input layui-edit laydate-").append(key).append(" placeholder=\"选择日期\" value=\"{{= d.").append( item.fieldname).append(" || '' }}\">");
                info.put("tpl", sb.toString());
                info.put("type", "date");
                info.put("key", key);
                info.put("templet", "tpl-" + key );
            } else {
                if (item.typeofdata.endsWith("~M")) {
                    info.put("required", "true");
                    info.put("edit", "text");
                } else {
                    info.put("required", "false");
                    info.put("edit", "text");
                }
            }
            viewHeader.add(info);
        }
        if (hasAdd || hasDelete || hasClone) {
            String deleteButton = "<a lay-event=\"delete\" style=\"color:#FF5722\"><i class=\"layui-icon layui-icon-subtraction\"></i></a>";
            String addButton = "<a lay-event=\"add\"><i class=\"layui-icon layui-icon-addition\"></i></a>";
            String cloneButton = "<a lay-event=\"clone\"><i class=\"lcdp-icon lcdp-icon-copy\"></i></a>";
            StringBuilder sb = new StringBuilder();
            sb.append("function(d) {");
            sb.append("if (Number(d.id) > 0) {");
            String spacespan = "<span style=\"background-color: #E9E9E9;padding-left: 1px;margin: 0 8px;\"></span>";
            if (hasDelete) {
                if (hasClone) {
                    sb.append(" return '" + cloneButton + spacespan + deleteButton + "'");
                } else {
                    sb.append(" return '" + deleteButton + "'");
                }
             }
            sb.append("} else {");
            if (hasAdd) {
                if (hasDelete) {
                    if (hasClone) {
                        sb.append(" return '" + addButton + spacespan + cloneButton + spacespan + deleteButton + "'");
                    } else {
                        sb.append(" return '" + addButton + spacespan + deleteButton + "'");
                    }
                } else {
                    if (hasClone) {
                        sb.append(" return '" + addButton + spacespan + cloneButton + "'");
                    } else {
                        sb.append(" return '" + addButton + "'");
                    }
                }
            } else {
                if (hasDelete) {
                    if (hasClone) {
                        sb.append(" return '" + cloneButton + spacespan + deleteButton + "'");
                    } else {
                        sb.append(" return '" + deleteButton + "'");
                    }
                }
            }
            sb.append("} }");
            Map<String,Object> info = new HashMap<>(1);
            info.put("field", "oper");
            info.put("title", "操作");
            info.put("align", "center");
            if (hasClone) {
                info.put("width", "112");
            } else {
                info.put("width", "90");
            }
            info.put("fixed", "right");
            info.put("templet", sb.toString());
            viewHeader.add(info);
        }
        return viewHeader;
    }

    public List<Object> getPicklistByModule() {
        List<Object> picklists = new ArrayList<>();
        try {
            if (Utils.isEmpty(this.getEntityName())) {
                return picklists;
            }
            XN_Query query = XN_Query.create(this.getDataType())
                    .notDelete().tag(this.getTabName())
                    .filter("type", "eic", this.getTabName())
                    .notDelete()
                    .begin(0)
                    .end(-1);
            if (Boolean.TRUE.equals(this.getModuletab().isapproval)) {
                query.filter("my.approvalstatus ", "in", Arrays.asList("2", "4"));
            }
            if (Boolean.TRUE.equals(this.getModuletab().isenabledisable)) {
                query.filter("my.status", "=", "Active");
            }
            //企业模块数据过滤
            IPublicService.addSupplierFilter(query);
            //添加数据权限过滤
            if (Utils.isNotEmpty(this.getDataPermission())) {
                List<String> auths = AuthorizeUtils.getAuthorizesByProfileId(ProfileUtils.getCurrentProfileId());
                if (!auths.isEmpty()) {
                    for (String auth : auths) {
                        if (this.getDataPermission().containsKey(auth)) {
                            String filterStr = ExpressionUtils.execute(this.getDataPermission().get(auth));
                            if (Utils.isNotEmpty(filterStr)) {
                                query.filter(filterStr);
                            }
                        }
                    }
                }
            }

            if (Boolean.FALSE.equals(query.isOrder())) {
                query.order("published", "D_N");
            }
            List<Content> result = query.execute();
            String entityName = this.getEntityName();
            for(Content obj: result) {
                if (obj.my.containsKey(entityName)) {
                    picklists.add(new HashMap<String, String>() {{
                        put("id", obj.id);
                        put("title", obj.my.get(entityName).toString());
                    }});
                }
            }
        } catch (Exception e) { }
        return picklists;
    }

    private Map<String, Object> getTableEditViewPicklist(List<TabField> fields, Map<String,Object> picklists,List<OutsideLink> outsideLinks) {
        Map<String, Object> picklistMaps = new HashMap<>();
        Map<String, OutsideLink> outsideLinkMaps = outsideLinks.stream().collect(Collectors.toMap(v1 -> v1.fieldname, v1 -> v1, (k1, k2) -> k1));
        for (TabField item : fields) {
            if (Arrays.asList(13,14,15,16,19,20).contains(item.uitype)) {
                List<List<String>> lists;
                if (!Utils.isEmpty(item.picklist)) {
                    lists = (List<List<String>>)PickListUtils.getPickList(item.picklist);
                } else {
                    lists = (List<List<String>>)PickListUtils.getPickList(item.fieldname);
                }
                if (!Utils.isEmpty(lists)) {
                    List<Map<String,String>> fieldLists = new ArrayList<>();
                    fieldLists.addAll(lists.stream().map(v -> { return new HashMap<String,String>(){{
                        put("id",v.get(0).toString());
                        put("title",v.get(1).toString());
                    }};}).collect(Collectors.toList()));
                    fieldLists.add(new HashMap<String, String>() {{
                        put("id","");
                        put("title","&nbsp;");
                    }});
                    picklistMaps.put(item.fieldname,fieldLists);
                }
            } else if (item.uitype == 11 && outsideLinkMaps.containsKey(item.fieldname)) {
                OutsideLink outsideLink = outsideLinkMaps.get(item.fieldname);
                BaseEntityUtils viewEntitys = new BaseEntityUtils(outsideLink.relmodule);
                picklistMaps.put(item.fieldname,viewEntitys.getPicklistByModule());
            }
        }
        picklistMaps.putAll(picklists);
        log.info("picklistMaps : {}",picklistMaps);
        return picklistMaps;
    }

    private Object tableEditView(List<Object> entitys,List<TabField> fields, Map<String,Object> picklists,List<OutsideLink> outsideLinks,Map<String,Object> configs, Model model,String record,String viewPath) {
        boolean hasIndex = false;
        boolean hasAdd = false;
        boolean hasClone = false;
        boolean hasDelete = false;
        boolean isDialog = false;
        boolean hasEmptyLine = false;
        String name = "detail";
        String saveLabel = "保存";
        String size = "";
        String height = "";
        String minwidth = "";
        if (configs.containsKey("hasIndex") && Boolean.parseBoolean(configs.get("hasIndex").toString())) {
            hasIndex = true;
        }
        if (configs.containsKey("hasAdd") && Boolean.parseBoolean(configs.get("hasAdd").toString())) {
            hasAdd = true;
        }
        if (configs.containsKey("hasClone") && Boolean.parseBoolean(configs.get("hasClone").toString())) {
            hasClone = true;
        }
        if (configs.containsKey("hasDelete") && Boolean.parseBoolean(configs.get("hasDelete").toString())) {
            hasDelete = true;
        }
        if (configs.containsKey("isDialog") && Boolean.parseBoolean(configs.get("isDialog").toString())) {
            isDialog = true;
        }
        if (configs.containsKey("hasEmptyLine") && Boolean.parseBoolean(configs.get("hasEmptyLine").toString())) {
            hasEmptyLine = true;
        }
        if (configs.containsKey("name")) {
            name = configs.get("name").toString();
        }
        if (configs.containsKey("saveLabel")) {
            saveLabel = configs.get("saveLabel").toString();
        }
        if (configs.containsKey("size")) {
            size = configs.get("size").toString();
        }
        if (configs.containsKey("height")) {
            height = configs.get("height").toString();
        }
        if (configs.containsKey("minwidth")) {
            minwidth = configs.get("minwidth").toString();
        }
        Map<String, String> fieldMaps = fields.stream().collect(Collectors.toMap(v -> v.fieldname, v -> v.defaultvalue, (k1, k2) -> k1));

        if (hasAdd && hasEmptyLine) {
            Map<String,Object> info = new HashMap<>(1);
            info.put("id", -DateTimeUtils.getCurrentTimeStamp());
            info.putAll(fieldMaps);
            entitys.add(info);
        }
        Map<String, Object> entityMaps = entitys.stream().collect(Collectors.toMap(v -> ((Map)v).get("id").toString(), v -> v));
        Map<String, Object> fieldPicklists = getTableEditViewPicklist(fields,picklists,outsideLinks);
        List<Object> result = this.getTableEditViewHeader(fields,hasIndex,hasAdd,hasClone,hasDelete,name,fieldPicklists,outsideLinks);
        model.addAttribute("RECORD", record);
        model.addAttribute("NAME", name);
        model.addAttribute("SIZE", size);
        model.addAttribute("HEIGHT", height);
        model.addAttribute("MINWIDTH", minwidth);
        model.addAttribute("SAVELABEL", saveLabel);
        model.addAttribute("ISDIALOG", isDialog);
        model.addAttribute("HASEMPTYLINE", hasEmptyLine);
        model.addAttribute("HASADD", hasAdd);
        model.addAttribute("LISTHEADER", result);
        List<String> requiredFields = fields.stream().filter( v -> v.typeofdata.endsWith("~M")).map( v -> v.fieldname).collect(Collectors.toList());
        model.addAttribute("FIELDS", Utils.objectToJson(fieldMaps));
        model.addAttribute("REQUIREDFIELDS", Utils.objectToJson(requiredFields));
        model.addAttribute("PICKLISTS", Utils.objectToJson(fieldPicklists));
        model.addAttribute("ENTITYMAPS", Utils.objectToJson(entityMaps));
        return WebViews.view(viewPath);
    }


    @ApiOperation("自定义列表")
    public Object listCustomView(List<TabField> fields, Map<String,Object> configs,HttpServletRequest request, Model model) {
        return listCustomView(null, fields, configs, request, model, "CustomListView");
    }

    @ApiOperation("自定义列表")
    public Object listCustomView(List<Object> entitys,List<TabField> fields, Map<String,Object> configs,HttpServletRequest request, Model model) {
        return listCustomView(entitys,fields, configs,request, model, "CustomListView");
    }

    @ApiOperation("自定义列表")
    public Object listCustomView(List<Object> entitys,List<TabField> fields, Map<String,Object> configs,HttpServletRequest request, Model model,String viewPath) {
        try {
            boolean hasPage = false;
            boolean isDialog = false;
            String size = "";
            int height = 0;
            if (configs.containsKey("hasPage") && Boolean.parseBoolean(configs.get("hasPage").toString())) {
                hasPage = true;
            }
            if (configs.containsKey("isDialog") && Boolean.parseBoolean(configs.get("isDialog").toString())) {
                isDialog = true;
            }
            if (configs.containsKey("size")) {
                size = configs.get("size").toString();
            }
            if (configs.containsKey("height")) {
                height = Integer.parseInt(configs.get("height").toString());
            }
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            if (entitys != null) {
                List<Object> result = this.getCustomListViewHeader(fields,false);
                model.addAttribute("LISTHEADER", Utils.objectToJson(result));
                model.addAttribute("ENTITYMAPS", Utils.objectToJson(entitys));
                hasPage = false;
            } else {
                int page = 1;
                int limit = ProfileUtils.getPageLimit();
                if(!Utils.isEmpty(httpRequest.get("page"))){
                    page = Double.valueOf(httpRequest.getOrDefault("page","1").toString()).intValue();
                }
                if(httpRequest.containsKey("limit") && httpRequest.get("limit").toString().compareTo("") != 0){
                    limit = Double.valueOf(httpRequest.getOrDefault("limit","20").toString()).intValue();
                }

                if(!Utils.isEmpty(httpRequest.get("sortType"))){
                    model.addAttribute("SORTTYPE", httpRequest.get("sortType"));
                    if(!Utils.isEmpty(httpRequest.get("sortBy"))) {
                        List<String> viewFields = this.getCustomViewFields(httpRequest.getOrDefault("viewid", "").toString());
                        if(viewFields.contains(httpRequest.get("sortBy"))) {
                            String sortby = httpRequest.get("sortBy").toString();
                            sortby = sortby.replace("my.","");
                            model.addAttribute("SORTFIELD", sortby);
                        } else {
                            model.addAttribute("SORTTYPE", this.getDefaultOrder(httpRequest.getOrDefault("viewid", "").toString()));
                            String sortby = this.getDefaultOrderBy(httpRequest.getOrDefault("viewid", "").toString());
                            sortby = sortby.replace("my.","");
                            model.addAttribute("SORTFIELD",sortby);
                        }
                    }
                }
                List<Object> result = this.getCustomListViewHeader(fields,true);
                model.addAttribute("LISTHEADER", Utils.objectToJson(result));
                model.addAttribute("PAGE", page);
                model.addAttribute("LIMIT", limit);
            }
            model.addAttribute("HEIGHT", height);
            model.addAttribute("SIZE",size);
            model.addAttribute("HASPAGE",hasPage);
            model.addAttribute("ISDIALOG",isDialog);
            return WebViews.view(viewPath);
        } catch (Exception e) {
            return WebViews.view("error/403");
        }
    }


    public static String headerToJson(List<List<Map<String,Object>>> headers) {
        if (headers.size() > 0 && headers.get(0).size() > 0 ) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for(int i = 0 ; i < headers.size(); i++) {
                List<Map<String,Object>> header = headers.get(i);
                sb.append("[");
                for(int j = 0 ; j < header.size(); j++) {
                    sb.append("{");
                    Map<String,Object> item = header.get(j);
                    List<String> keys = new ArrayList<String>(item.keySet());
                    String lastKey = keys.get(keys.size() - 1);
                    for(String key : keys) {
                        Object obj = item.get(key);
                        sb.append(key);
                        sb.append(":");
                        if(obj instanceof Integer || obj instanceof Long || obj instanceof Boolean) {
                            sb.append(obj.toString());
                        } else if(obj instanceof Double || obj instanceof Float) {
                            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");//格式化设置
                            sb.append(decimalFormat.format(obj));
                        }else if(obj instanceof String) {
                            if ("templet".equals(key)) {
                                if (obj.toString().startsWith("function")) {
                                    sb.append(obj);
                                } else {
                                    sb.append("\'");
                                    sb.append(obj);
                                    sb.append("\'");
                                }
                            } else {
                                sb.append("\'");
                                sb.append(obj);
                                sb.append("\'");
                            }
                        }
                        if (!key.equals(lastKey)) {
                            sb.append(",");
                        }
                    }
                    sb.append("}");
                    if (j != header.size() - 1) {
                        sb.append(",");
                    }
                }
                sb.append("]");
                if (i != headers.size() - 1) {
                    sb.append(",\n");
                }
            }
            sb.append("]");
            return sb.toString();
        }
        return "[[]]";
    }

    @ApiOperation("根据定义生成列表页面")
    public Object listView(HttpServletRequest request, Model model) {
        return listView(request, model, "ListView");
    }

    public Object listView(HttpServletRequest request, Model model, String viewPath) {
        try {
            HttpSession session = request.getSession();
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            HashMap<String, Object> sessionRequest = (HashMap)session.getAttribute(this.dataEntity.tabs.modulename+"_DataSearch");
            if(!Utils.isEmpty(sessionRequest)){
                sessionRequest.remove("limit");
                for (String key : sessionRequest.keySet()){
                    if(!httpRequest.containsKey(key) || Utils.isEmpty(httpRequest.get(key))){
                        httpRequest.put(key,sessionRequest.get(key));
                    }
                }
            }
            int page = 1;
            int limit = ProfileUtils.getPageLimit();
            if(!Utils.isEmpty(httpRequest.get("page"))){
                page = Double.valueOf(httpRequest.getOrDefault("page","1").toString()).intValue();
            }
            if(httpRequest.containsKey("limit") && httpRequest.get("limit").toString().compareTo("") != 0){
                limit = Double.valueOf(httpRequest.getOrDefault("limit","20").toString()).intValue();
            }

            if(!Utils.isEmpty(httpRequest.get("sortType"))){
                model.addAttribute("SORTTYPE", httpRequest.get("sortType"));
                if(!Utils.isEmpty(httpRequest.get("sortBy"))) {
                    List<String> viewFields = this.getCustomViewFields(httpRequest.getOrDefault("viewid", "").toString());
                    if(viewFields.contains(httpRequest.get("sortBy").toString())) {
                        String sortby = httpRequest.get("sortBy").toString();
                        sortby = sortby.replace("my.","");
                        model.addAttribute("SORTFIELD", sortby);
                    } else {
                        model.addAttribute("SORTTYPE", this.getDefaultOrder(httpRequest.getOrDefault("viewid", "").toString()));
                        String sortby = this.getDefaultOrderBy(httpRequest.getOrDefault("viewid", "").toString());
                        sortby = sortby.replace("my.","");
                        model.addAttribute("SORTFIELD",sortby);
                    }
                }
            }

            this.workflowStatus = WorkflowUtils.getWorkflowStatus(this.getModuleName());
            List<List<Map<String,Object>>> result = this.getListViewHeader(httpRequest);
            Boolean hasGroupHeader = result.size() == 2;
            model.addAttribute("HASGROUPHEADER", hasGroupHeader);
            model.addAttribute("MODULE", this.getModuleName());
            model.addAttribute("SEARCHPANEL", this.dataSearch(request, null));
            model.addAttribute("LISTHEADER", headerToJson(result));
            if (!Utils.isEmpty(this.actionsEntity.actions) || this.actionsEntity.tabs.isexport || this.actionsEntity.tabs.iscreate || this.actionsEntity.tabs.isdelete || RolesUtils.isSupperDelete()) {
                model.addAttribute("LISTACTIONS", this.getListViewActions());
                model.addAttribute("LISTOPERATION",this.getListViewOperationActions());
            }
            if (!Utils.isEmpty(this.statisticFields) && !this.statisticFields.isEmpty()) {
                model.addAttribute("ISSTATISTIC", true);
            }
            if( this.actionsEntity.tabs.isexport){
                model.addAttribute("ISEXPORT", true);
            }
            if (!Utils.isEmpty(getCustomViews())){
                model.addAttribute("HASCUSTOMVIEW", true);
            }
            model.addAttribute("HASOPERATOR",this.dataEntity.tabs.hasoperator);
            model.addAttribute("AUTOLINEHEIGHT",this.dataEntity.tabs.autolineheight);
            model.addAttribute("PAGELIMIT",this.dataEntity.tabs.pagelimit);
            model.addAttribute("HASCUSTOMVIEW",this.dataEntity.tabs.customview);
            model.addAttribute("SEARCHCOLUMNFLOW", this.dataEntity.tabs.searchcolumnflow);
            model.addAttribute("PAGE", page);
            model.addAttribute("LIMIT", limit);
            model.addAttribute("LISTVIEWHASSCRIPT",false);
            model.addAttribute("LISTVIEWTABLEHASSCRIPT",false);
            model.addAttribute("LISTVIEWBARHASSCRIPT",false);
            model.addAttribute("LISTVIEWDONEHASSCRIPT",false);
            model.addAttribute("LISTVIEWHASHTML",false);
            model.addAttribute("LISTVIEWBOTTOMHASHTML",false);
            model.addAttribute("LEFTLISTVIEWWIDTH",2);
            try{
                Integer LeftListViewWidth  = Integer.parseInt(CallbackUtils.invoke("getLeftListViewWidth", entityPackageName, IBaseService.class).toString());
                if (LeftListViewWidth > 11 || LeftListViewWidth < 1) {
                    LeftListViewWidth = 2;
                }
                model.addAttribute("LEFTLISTVIEWWIDTH",LeftListViewWidth);
            }catch (Exception ignored) { }

            if (isExistResource("views/"+this.getModuleName()+"/listview.js")) {
                model.addAttribute("LISTVIEWHASSCRIPT",true);
            }
            if (isExistResource("views/"+this.getModuleName()+"/listviewTable.js")) {
                model.addAttribute("LISTVIEWTABLEHASSCRIPT",true);
            }
            if (isExistResource("views/"+this.getModuleName()+"/listviewBar.js")) {
                model.addAttribute("LISTVIEWBARHASSCRIPT",true);
            }
            if (isExistResource("views/"+this.getModuleName()+"/listviewDone.js")) {
                model.addAttribute("LISTVIEWDONEHASSCRIPT",true);
            }
            if (isExistResource("views/"+this.getModuleName()+"/listview.html")) {
                model.addAttribute("LISTVIEWHASHTML",true);
            }
            if (isExistResource("views/"+this.getModuleName()+"/listviewBottom.html")) {
                model.addAttribute("LISTVIEWBOTTOMHASHTML",true);
            }

            try{
                CallbackUtils.invoke("initModuleData", entityPackageName, IBaseService.class);
            }catch (Exception ignored) { }
            return WebViews.view(viewPath);
        } catch (Exception e) {
            return WebViews.view("error/403");
        }
    }
    @ApiOperation("根据定义生成列表页面数据结构")
    public Object listEntity(HttpServletRequest request, Map<String, Object> result, List<String> viewFields) {
        if (Utils.isEmpty(result) || !(result.get("list") instanceof List)) {
            Map<String,Object> info = new HashMap<>(1);
            info.put("code", 0);
            info.put("msg", "");
            info.put("count", 0);
            info.put("data", new ArrayList<>());
            info.put("styles", new HashMap<>());
            return info;
        } else {
            try{
                CallbackUtils.invoke("customListViewEntity", entityPackageName, IBaseService.class, result.get("list"));
            }catch (Exception ignored) {}
            HttpSession session = request.getSession();
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            HashMap<String, Object> sessionRequest = (HashMap)session.getAttribute(this.dataEntity.tabs.modulename+"_DataSearch");
            if(!Utils.isEmpty(sessionRequest)){
                for (String key : sessionRequest.keySet()){
                    if(!httpRequest.containsKey(key) || Utils.isEmpty(httpRequest.get(key))){
                        httpRequest.put(key,sessionRequest.get(key));
                    }
                }
            }

            if(Utils.isEmpty(viewFields)){
                viewFields = this.getCustomViewFields(httpRequest.getOrDefault("viewid", "").toString());
            }
            if (!viewFields.contains("id")) {
                viewFields.add(0, "id");
            }
            try{
                List<Map<String, Object>> headers = new ArrayList<>(1);
                if (headers.size() > 0) {
                    for (Map<String, Object> header : headers) {
                        if (header.containsKey("field")) {
                            String field = header.get("field").toString();
                            if (!viewFields.contains(field)) {
                                viewFields.add(field);
                            }
                        }
                    }
                }
            }catch (Exception ignored) {}

            List<Object> records = new ArrayList<>();
            for (Object item : (List<?>) result.get("list")) {
                String id = "";
                try {
                    id = Objects.requireNonNull(ClassUtils.getValue(item, "id")).toString();
                }catch (Exception ignored){ }
                Map<String, Object> record = new HashMap<>();
                for (String fieldname : viewFields) {
                    try {
                        Field field = item.getClass().getField(fieldname);
                        String value = formatByUiType(id,fieldname, field.get(item),true);
                        record.put(fieldname, value);
                        if (!Utils.isEmpty(this.moduleFields.get(fieldname))) {
                            TabField fieldInfo = this.moduleFields.get(fieldname);
                            if (Arrays.asList(10,11,12,13,14,15,16,19,20,25).contains(fieldInfo.uitype)) {
                                record.put(fieldname+"_value", field.get(item));
                            }
                        }
                    } catch (Exception e) {
                        record.put(fieldname, "");
                    }
                }
                records.add(record);
            }

            try{
                CallbackUtils.invoke("customListViewRecord", entityPackageName, IBaseService.class, records);
            }catch (Exception ignored) {}

            HashMap<String, String> statistic = new HashMap<>();
            if (!Utils.isEmpty(result.get("statistic")) && !((List<?>) result.get("statistic")).isEmpty()) {
                for (Object item : (List<?>) result.get("statistic")) {
                    if (item instanceof Content) {
                        for (String fieldname : this.statisticFields) {
                            TabField fieldInfo = this.moduleFields.get(fieldname);
                            int uitype = fieldInfo.uitype;
                            String fieldValue = ((Content) item).my.getOrDefault(fieldname, "0").toString();
                            if(uitype == 27) {
                                int ratio = fieldInfo.ratio;
                                if (ratio <= 0) {
                                    ratio = 1;
                                }
                                if(Utils.isEmpty(fieldValue)) {
                                    fieldValue = "0";
                                }
                                BigDecimal bc = new BigDecimal(fieldValue);
                                fieldValue = bc.divide(BigDecimal.valueOf(ratio), 2, RoundingMode.HALF_DOWN).toString();
                                NumberFormat nf = new DecimalFormat("￥#,##0.00");
                                statistic.put(fieldname, nf.format(Double.parseDouble(fieldValue)));
                            } else {
                                statistic.put(fieldname, fieldValue);
                            }
                        }
                    }
                }
            }else {
                if(!Utils.isEmpty(result.get("totalRow")) && !((HashMap<?,?>) result.get("totalRow")).isEmpty()){
                    statistic = (HashMap<String,String>) result.get("totalRow");
                }
            }
            Map<String,Object> customListEntityStyles = new HashMap<>();
            try{
                Object resultStyles = CallbackUtils.invoke("getCustomListEntityStyles", entityPackageName, IBaseService.class, result.get("list"));
                if (!Utils.isEmpty(result)) {
                    customListEntityStyles = (Map<String,Object>)resultStyles;
                }
            }catch (Exception ignored) {}

            Map<String,Object> info = new HashMap<>(1);
            info.put("code", 0);
            info.put("count", result.get("total"));
            info.put("data", records);
            info.put("styles", customListEntityStyles);
            if (!statistic.isEmpty()) {
                info.put("totalRow", statistic);
            }
            return info;
        }
    }


    @ApiOperation("根据定义生成列表页面数据结构")
    public Object listCustomEntity(List<TabField> fields, Map<String, Object> result, Class<?> entity) {
        if (Utils.isEmpty(result) || !(result.get("list") instanceof List)) {
            Map<String,Object> info = new HashMap<>(1);
            info.put("code", 0);
            info.put("msg", "");
            info.put("count", 0);
            info.put("data", new ArrayList<>(1));
            return info;
        } else {
            try {
                Map<String, TabField> fieldMaps = fields.stream().collect(Collectors.toMap(v -> v.fieldname, v -> v, (k1, k2) -> k1));
                List<Object> records = new ArrayList<>();
                Field[] entityFields = entity.getDeclaredFields();

                for (Object item : (List<?>) result.get("list")) {
                    Map<String, Object> record = new HashMap<>();
                    String id = "";
                    try {
                        id = item.getClass().getField("id").get(item).toString();
                        String title = item.getClass().getField("title").get(item).toString();
                        String published = item.getClass().getField("published").get(item).toString();
                        String updated = item.getClass().getField("updated").get(item).toString();
                        String author = item.getClass().getField("author").get(item).toString();
                        record.put("id", id);
                        record.put("title", title);
                        record.put("published", published);
                        record.put("updated", updated);
                        record.put("author", getOutsideProfileValue(author, "--"));
                    } catch (Exception ignored) { }
                    for (Field field : entityFields) {
                        String fieldname = field.getName();
                        if (field.getModifiers() == Modifier.PUBLIC) {
                            if (fieldMaps.containsKey(fieldname)) {
                                TabField fieldInfo = fieldMaps.get(fieldname);
                                try {
                                    String fieldvalue = field.get(item).toString();
                                    String value = formatByUiType(id,fieldname,fieldvalue,true, fieldInfo);
                                    record.put(fieldname, value);
                                } catch (Exception e) {
                                    record.put(fieldname, "");
                                }
                            } else {
                                record.put(fieldname, field.get(item).toString());
                            }
                        } else {
                            record.put(fieldname, field.get(item).toString());
                        }
                    }
                    records.add(record);
                }
                long total = Long.parseLong(result.get("total").toString());
                Map<String,Object> info = new HashMap<>(1);
                info.put("code", 0);
                info.put("msg", "");
                info.put("count", total);
                info.put("data", records);
                return info;
            }catch (Exception e) {
                log.error("ListCustomEntity : {}",e.getMessage());
            }
        }
        Map<String,Object> info = new HashMap<>(1);
        info.put("code", 0);
        info.put("msg", "");
        info.put("count", 0);
        info.put("data", new ArrayList<>());
        return info;
    }
    @ApiOperation("生成自定义列表页面数据结构")
    public Map<String,Object> getCustomListEntity(HttpServletRequest request) {
        Map<String, Object> httpRequest = Utils.getRequestQuery(request);
        Object result = null;
        try{
            result = CallbackUtils.invoke("getCustomListEntity", entityPackageName, IBaseService.class, httpRequest);
        }catch (Exception ignored) {}
        return (Map<String, Object>)result;
    }


    @ApiOperation("根据定义生成列表页面数据结构")
    public Object listEntity(HttpServletRequest request, Map<String, Object> result) {
        return this.listEntity(request,result,null);
    }


    @ApiOperation("根据定义生成列表页面数据结构")
    public Object listAllEntity(HttpServletRequest request, Map<String, Object> result) {
        List<String> viewFields = new ArrayList<>(this.moduleFields.keySet());
        return this.listEntity(request,result,viewFields);
    }

    //endregion

    //region 编辑封装
    @ApiOperation("根据字段定义的UIType生成编辑页面数据结构")
    private void getOutputbyUitype(TabField fieldInfo, Object recordInfo, Boolean isedit, HashMap<String, Object> result) {
        getOutputbyUitype(fieldInfo,this.outsideLinks,recordInfo,isedit,result);
    }
    private void getOutputbyUitype(TabField fieldInfo, Map<String,OutsideLink> outsideLinks, Object recordInfo, Boolean isedit, HashMap<String, Object> result) {
        Object value = "";
        List<Object> listValues = new ArrayList<>();
        try {
            if (!Utils.isEmpty(recordInfo)) {
                Field field = recordInfo.getClass().getField(fieldInfo.fieldname);
                if (!Utils.isEmpty(field.get(recordInfo))) {
                    if (field.getType().equals(List.class)) {
                        value = String.join(",", (List) field.get(recordInfo));
                        if (fieldInfo.uitype == 1) {
                            listValues = (List)field.get(recordInfo);
                        }
                    } else {
                        value = field.get(recordInfo);
                    }
                }
            }
        } catch (Exception ignored) { }
        result.put("uitype", fieldInfo.uitype);
        String label = fieldInfo.fieldlabel;
        if (Utils.isNotEmpty(fieldInfo.grouplabel)) {
            label = fieldInfo.grouplabel + label;
        }
        result.put("label", label);
        result.put("name", fieldInfo.fieldname);
        Map<String,Object> valueMap = new HashMap<>(1);
        valueMap.put("1", value.toString());
        valueMap.put("2", "");
        valueMap.put("3", "");
        valueMap.put("4", "");
        if (listValues.size() > 0) {
            valueMap.put("5", listValues);
        }
        result.put("value", valueMap);
        switch (fieldInfo.uitype) {
            case 1:
                if(value instanceof Double) {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    String doubleValue = nf.format(value);
                    valueMap.put("1", doubleValue);
                    valueMap.put("2", "");
                    valueMap.put("3", "");
                    valueMap.put("4", "");
                    result.put("value", valueMap);
                }
                break;
            case 4:
                if (!isedit) {
                    try {
                        valueMap.put("1", XN_ModentityNum.get(fieldInfo.modulename));
                        valueMap.put("2", "");
                        valueMap.put("3", "");
                        valueMap.put("4", "");
                        result.put("value", valueMap);
                    } catch (Exception ignored) {
                    }
                }
                break;
            case 6:
                if(!Utils.isEmpty(value)) {
                    valueMap.put("1", DateTimeUtils.getDateStringByFormat(DateTimeUtils.toDateTime(value.toString()),DateTimeUtils.DATE_FORMAT));
                    valueMap.put("2", "");
                    valueMap.put("3", "");
                    valueMap.put("4", "");
                    result.put("value", valueMap);
                }
                break;
            case 7:
                if(!Utils.isEmpty(value)) {
                    valueMap.put("1", DateTimeUtils.getDateStringByFormat(DateTimeUtils.toDateTime(value.toString()),DateTimeUtils.DATE_SHORT_TIME_FORMAT));
                    valueMap.put("2", "");
                    valueMap.put("3", "");
                    valueMap.put("4", "");
                    result.put("value", valueMap);
                }
                break;
            case 8:
                if(!Utils.isEmpty(value)) {
                    valueMap.put("1", DateTimeUtils.getDateStringByFormat(DateTimeUtils.toDateTime(value.toString()),DateTimeUtils.TIME_FORMAT));
                    valueMap.put("2", "");
                    valueMap.put("3", "");
                    valueMap.put("4", "");
                    result.put("value", valueMap);
                }
                break;
            case 10:
                if (!Utils.isEmpty(outsideLinks.get(fieldInfo.fieldname))) {
                    String linkValue = value.toString();
                    if(!isedit && Utils.isEmpty(linkValue)){
                        linkValue = fieldInfo.defaultvalue;
                    }
                    OutsideLink linkModule = outsideLinks.get(fieldInfo.fieldname);
                    String seevalue = this.getOutsideDepartmentValue(linkValue, "");
                    String placeholder = "";
                    String url = "";
                    if (!Utils.isEmpty(linkModule.url)) {
                        url = linkModule.url;
                    }
                    if (!Utils.isEmpty(linkModule.placeholder)) {
                        placeholder = linkModule.placeholder;
                    }
                    valueMap.put("1", linkValue);
                    valueMap.put("2", seevalue);
                    valueMap.put("3", url);
                    valueMap.put("4", placeholder);
                    result.put("value", valueMap);
                }
                break;
            case 11:
            case 12:
                if (!Utils.isEmpty(outsideLinks.get(fieldInfo.fieldname))) {
                    try {
                        String linkValue = value.toString();
                        if(!isedit && Utils.isEmpty(linkValue)){
                            linkValue = fieldInfo.defaultvalue;
                        }
                        OutsideLink linkModule = outsideLinks.get(fieldInfo.fieldname);
                        String seevalue = linkValue;
                        if (NumberUtil.isInteger(linkValue)) {
                            seevalue = this.getOutsideLinkValue(outsideLinks,fieldInfo.fieldname, linkValue, "");
                        } else if (NumberUtil.isInteger(linkValue.split(",")[0]) ) {
                            seevalue = this.getOutsideLinkValue(outsideLinks,fieldInfo.fieldname, linkValue, "");
                        }
                        String url = "";
                        String placeholder = "";
                        if (!Utils.isEmpty(linkModule.url)) {
                            url = linkModule.url;
                        }
                        if (!Utils.isEmpty(linkModule.placeholder)) {
                            placeholder = linkModule.placeholder;
                        }
                        valueMap.put("1", linkValue);
                        valueMap.put("2", seevalue);
                        valueMap.put("3", url);
                        valueMap.put("4", placeholder);
                        result.put("value", valueMap);
                    } catch (Exception ignored) {
                    }
                }
                break;
            case 13:
            case 14:
            case 15:
            case 16:
            case 19:
            case 20:
                String pickList = !Utils.isEmpty(fieldInfo.picklist) ? fieldInfo.picklist : fieldInfo.fieldname;
                Object picklists = PickListUtils.getPickList(pickList);
                Object pickvalue = "-1".equals(value.toString()) && !fieldInfo.defaultvalue.isEmpty() ? fieldInfo.defaultvalue : value.toString();
                if (!Utils.isEmpty(picklists)) {
                    valueMap.put("1", pickvalue);
                    valueMap.put("2", getPicklistLabelByValue(pickList, pickvalue, "",false));
                    valueMap.put("3", picklists);
                    valueMap.put("4", fieldInfo.fieldlabel);
                    result.put("value", valueMap);
                } else {
                    valueMap.put("1", pickvalue);
                    valueMap.put("2", "");
                    valueMap.put("3", "");
                    valueMap.put("4", fieldInfo.fieldlabel);
                    result.put("value", valueMap);
                }
                break;
            case 25:
                if (!Utils.isEmpty(outsideLinks.get(fieldInfo.fieldname))) {
                    try {
                        String linkValue = value.toString();
                        if(!isedit && Utils.isEmpty(linkValue)){
                            linkValue = fieldInfo.defaultvalue;
                        }
                        OutsideLink linkModule = outsideLinks.get(fieldInfo.fieldname);
                        String seevalue = this.getOutsideProfileValue(linkValue, "");
                        String url = "";
                        String placeholder = "";
                        if (!Utils.isEmpty(linkModule.url)) {
                            url = linkModule.url;
                        }
                        if (!Utils.isEmpty(linkModule.placeholder)) {
                            placeholder = linkModule.placeholder;
                        }
                        valueMap.put("1", linkValue);
                        valueMap.put("2", seevalue);
                        valueMap.put("3", url);
                        valueMap.put("4", placeholder);
                        result.put("value", valueMap);
                    } catch (Exception ignored) {
                    }
                }
                break;
            case 26:
                try {
                    String relation = fieldInfo.relation;
                    String city = "", district = "";

                    if (!relation.isEmpty()) {
                        String[] relations = relation.split(",");
                        int index = 0;
                        for (String relfield : relations) {
                            Field field = recordInfo.getClass().getField(relfield);
                            if (index == 0) {
                                city = field.get(recordInfo).toString();
                            } else if (index == 1) {
                                district = field.get(recordInfo).toString();
                            }
                            index++;
                        }
                    }

                    valueMap.put("1", value.toString());
                    valueMap.put("2", city);
                    valueMap.put("3", district);
                    valueMap.put("4", fieldInfo.fieldname+","+fieldInfo.relation);
                    result.put("value", valueMap);
                } catch (Exception ignored) {
                }
                break;
            case 27:
                if(!Utils.isEmpty(value)) {
                    int ratio = fieldInfo.ratio;
                    if (ratio <= 0) {
                        ratio = 1;
                    }
                    BigDecimal bc = new BigDecimal(value.toString());
                    value = bc.divide(BigDecimal.valueOf(ratio), 2, RoundingMode.HALF_DOWN).toString();
                    valueMap.put("1", value.toString());
                    valueMap.put("2", "");
                    valueMap.put("3", "");
                    valueMap.put("4", "");
                    result.put("value", valueMap);
                }
                break;
            case 28:
                valueMap.put("1", value.toString());
                valueMap.put("2", fieldInfo.scorelevel.toString());
                valueMap.put("3", "");
                valueMap.put("4", "");
                result.put("value", valueMap);
                break;
            case 29:
                if (!Utils.isEmpty(outsideLinks.get(fieldInfo.fieldname))) {
                    try {
                        OutsideLink linkModule = outsideLinks.get(fieldInfo.fieldname);
                        String url = "";
                        String placeholder = "";
                        if (!Utils.isEmpty(linkModule.url)) {
                            url = linkModule.url;
                        }
                        if (!Utils.isEmpty(linkModule.placeholder)) {
                            placeholder = linkModule.placeholder;
                        }
                        valueMap.put("1", value.toString());
                        valueMap.put("2", value.toString());
                        valueMap.put("3", url);
                        valueMap.put("4", placeholder);
                        result.put("value", valueMap);
                    } catch (Exception ignored) {
                    }
                }
                break;
            default:
                break;
        }
        result.put("typeofdata", Arrays.asList(fieldInfo.typeofdata.split("~")));
    }

    @ApiOperation("根据字段定义匹配块中所显示字段")
    private List<Object> getBlockField(Integer blockId, int columns, Object recordInfo) {
        HashMap<String, Object> blockFields = new HashMap<>();
        boolean isEditState = false;
        try {
            isEditState = Boolean.parseBoolean(Objects.requireNonNull(ClassUtils.exeMethod(recordInfo, "getEditState")).toString());
        } catch (Exception ignored) {}

        for (TabField item : this.dataEntity.fields) {
            if (item.block.equals(blockId)) {
                if (Utils.isEmpty(item.fieldname)) {
                    continue;
                }
                int displaytype = item.displaytype;
                if ((isEditState && displaytype == 1) || displaytype == 2) {
                    continue;
                }
                if (item.uitype == 26 && item.relation.isEmpty()) {
                    continue;
                }

                HashMap<String, Object> result = new HashMap<>();
                this.getOutputbyUitype(item, recordInfo, isEditState, result);
                result.put("deputy", item.deputy_column);
                result.put("merge", item.merge_column);
                result.put("showlabel", item.show_title);
                result.put("readonly", item.readonly);
                result.put("aux", item.aux);
                result.put("editwidth", item.editwidth);
                result.put("editheight", item.editheight);
                result.put("maxlength", item.maxlength);
                result.put("multiselect", item.multiselect);
                result.put("defaultvalue", item.defaultvalue);
                result.put("remoteverify", item.remoteverify);
                result.put("relation", item.relation);
                result.put("clearbtn", item.clearbtn);
                result.put("digwidth", item.digwidth);
                result.put("digheight", item.digheight);
                result.put("regionlevel", item.regionlevel);
                if(item.cutwidth > 0 && item.cutheight <= 0){
                    item.cutheight = item.cutwidth;
                }
                if(item.cutheight > 0 && item.cutwidth <= 0){
                    item.cutwidth = item.cutheight;
                }
                result.put("cutwidth", item.cutwidth);
                result.put("cutheight", item.cutheight);

                if (blockFields.containsKey(String.valueOf(blockId))) {
                    ((List) blockFields.get(String.valueOf(blockId))).add(result);
                } else {
                    blockFields.put(String.valueOf(blockId), new ArrayList<Object>(ImmutableSet.of(result)));
                }
            }
        }

        List<Object> editViewData = new ArrayList<>();
        int fieldpos = 0;
        if(!Utils.isEmpty(blockFields.get(String.valueOf(blockId)))) {
            for (Object fieldinfo : (List<?>) blockFields.get(String.valueOf(blockId))) {
                boolean deputy = (Boolean) ((HashMap<?,?>) fieldinfo).get("deputy");
                if (deputy && !editViewData.isEmpty()) {
                    int deputyPos = fieldpos - 1;
                    HashMap<String, Object> prevfield = (HashMap<String, Object>) editViewData.get(deputyPos);
                    if (prevfield.containsKey("deputys")) {
                        ((List<Object>) prevfield.get("deputys")).add(fieldinfo);
                    } else {
                        prevfield.put("deputys", new ArrayList<>(ImmutableSet.of(fieldinfo)));
                    }
                    editViewData.set(deputyPos, prevfield);
                } else {
                    editViewData.add(fieldpos, fieldinfo);
                    fieldpos++;
                }
            }
        }
        fieldpos = 0;
        int fieldrow = 0;
        List<Object> layoutData = new ArrayList<>();
        for (Object fieldinfo : editViewData) {
            int merge = (int) ((HashMap) fieldinfo).get("merge");
            if (merge == 1) {
                if (fieldpos == 0) {
                    layoutData.add(fieldrow, new ArrayList<>(ImmutableSet.of(fieldinfo)));
                    fieldrow++;
                } else {
                    fieldrow++;
                    layoutData.add(fieldrow, new ArrayList<>(ImmutableSet.of(fieldinfo)));
                    fieldrow++;
                }
                fieldpos = 0;
            } else if (merge == 2) {
                if (fieldpos == 0) {
                    layoutData.add(fieldrow, new ArrayList<>(ImmutableSet.of(fieldinfo)));
                } else {
                    fieldrow++;
                    layoutData.add(fieldrow, new ArrayList<>(ImmutableSet.of(fieldinfo)));
                }
                fieldpos = 1;
            } else {
                if (columns == 1) {
                    layoutData.add(fieldrow, new ArrayList<>(ImmutableSet.of(fieldinfo)));
                    fieldpos++;
                    fieldrow++;
                } else if (fieldpos == 0) {
                    layoutData.add(fieldrow, new ArrayList<>(ImmutableSet.of(fieldinfo)));
                    fieldpos++;
                } else if (fieldpos >= columns - 1) {
                    List<Object> tmp = (List<Object>) layoutData.get(fieldrow);
                    tmp.add(fieldinfo);
                    layoutData.set(fieldrow, tmp);
                    fieldrow++;
                    fieldpos = 0;
                } else {
                    List<Object> tmp = (List<Object>) layoutData.get(fieldrow);
                    tmp.add(fieldinfo);
                    layoutData.set(fieldrow, tmp);
                    fieldpos++;
                }
            }
        }
        return layoutData;
    }

    private List<Layout> getLayout(List<TabField> fields) {
        List<Layout> layouts = new ArrayList<>();
        if(!Utils.isEmpty(fields)){
            int index = 0;
            for(TabField field: fields){
                if(field.block < 0) {
                    continue;
                }
                if(field.displaytype == 2) {
                    continue;
                }
                int row = index / 2;
                Layout layout;
                if(field.merge_column == 1){
                    layout = new Layout();
                    layout.columns = Collections.singletonList("12");
                    layout.fields.add(field.fieldname);
                    layout.sequence = row;
                    layouts.add(layout);
                    if(index % 2 == 1) {
                        index++;
                    }
                    index+=2;
                    continue;
                }
                if(field.merge_column == 2) {
                    if(index % 2 == 1) {
                        index++;
                        row = index / 2;
                    }
                }
                if(layouts.size() <= row || Utils.isEmpty(layouts.get(row))){
                    layout = new Layout();
                    layout.columns = Arrays.asList("6","6");
                    layout.fields = Arrays.asList("","");
                    layout.sequence = row;
                    layouts.add(layout);
                } else {
                    layout = layouts.get(row);
                }
                layout.fields.set(index % 2,field.fieldname);
                index++;
            }
        }
        return layouts;
    }

    @ApiOperation("根据字段定义匹配布局信息")
    private List<Object> getLayoutField(Integer blockId,Object recordInfo) {
        return getLayoutField(blockId,this.dataEntity.layouts,this.dataEntity.fields,this.outsideLinks,recordInfo);
    }
    private List<Object> getLayoutField(Integer blockId,List<Layout> layouts, List<TabField> fields, Map<String,OutsideLink> outsideLinks,Object recordInfo) {
        boolean isEditState = false;
        try {
            isEditState = Boolean.parseBoolean(Objects.requireNonNull(ClassUtils.exeMethod(recordInfo, "getEditState")).toString());
        } catch (Exception ignored) { }
        List<Object> layoutData = new ArrayList<>();
        if(Utils.isEmpty(layouts)) {
            layouts = this.getLayout(fields);
        }
        for(Layout layout: layouts) {
            if(layout.block.equals(blockId)) {
                List<Object> row = new ArrayList<>();
                for (int i = 0; i < layout.columns.size(); i++) {
                    if(Utils.isEmpty(layout.columns.get(i))) {
                        continue;
                    }
                    if (layout.fields.size()-1 >= i && !Utils.isEmpty(layout.fields.get(i))) {
                        String fieldname = layout.fields.get(i);
                        HashMap<String, Object> result = new HashMap<>();
                        for (TabField item : fields) {
                            if(item.fieldname.equals(fieldname)){
                                int displaytype = item.displaytype;
                                if ((isEditState && displaytype == 1) || displaytype == 2) {
                                    break;
                                }
                                if (item.uitype == 26 && !"province".equals(item.regionlevel) && item.relation.isEmpty()) {
                                    break;
                                }

                                this.getOutputbyUitype(item, outsideLinks, recordInfo, isEditState, result);
                                result.put("deputy", item.deputy_column);
                                result.put("merge", item.merge_column);
                                result.put("showlabel", item.show_title);
                                result.put("html", item.html);
                                result.put("readonly", item.readonly);
                                result.put("aux", item.aux);
                                result.put("editwidth", item.editwidth);
                                result.put("editheight", item.editheight);
                                result.put("maxlength", item.maxlength);
                                result.put("multiselect", item.multiselect);
                                result.put("defaultvalue", item.defaultvalue);
                                result.put("remoteverify", item.remoteverify);
                                result.put("relation", item.relation);
                                result.put("clearbtn", item.clearbtn);
                                result.put("digwidth", item.digwidth);
                                result.put("digheight", item.digheight);
                                result.put("regionlevel", item.regionlevel);
                                result.put("onlyselect",item.onlyselect);
                                result.put("isarray",item.isarray);
                                if(item.cutwidth > 0 && item.cutheight <= 0){
                                    item.cutheight = item.cutwidth;
                                }
                                if(item.cutheight > 0 && item.cutwidth <= 0){
                                    item.cutwidth = item.cutheight;
                                }
                                result.put("cutwidth", item.cutwidth);
                                result.put("cutheight", item.cutheight);
                                if(Utils.isNotEmpty(item.getFlowvalue())){
                                    result.put("flowvalue",item.getFlowvalue());
                                }
                                if(this.workflowStatus.isDefineflow() && this.workflowStatus.isApproval() && this.workflowStatus.isStartflow() && !this.workflowStatus.isFirstNode()){
                                    result.put("flowreadonly",true);
                                }
                                break;
                            }
                        }
                        Map<String,Object> info = new HashMap<>(1);
                        info.put("count",layout.columns.get(i));
                        if(!result.keySet().isEmpty()) {
                            info.put("field",result);
                        }
                        row.add(info);
                    } else {
                        Map<String,Object> info = new HashMap<>(1);
                        info.put("count",layout.columns.get(i));
                        row.add(info);
                    }
                }
                layoutData.add(row);
            }
        }
        return layoutData;
    }

    private List<Object> getLayoutField(Integer blockId,Object recordInfo,FlowTaskSettings flowTaskSettings) {
        boolean isEditState = false;
        try {
            isEditState = Boolean.parseBoolean(Objects.requireNonNull(ClassUtils.exeMethod(recordInfo, "getEditState")).toString());
        } catch (Exception ignored) { }
        List<Object> layoutData = new ArrayList<>();
        for(Layout layout: flowTaskSettings.getLayouts()) {
            if(layout.block.equals(blockId)) {
                List<Object> row = new ArrayList<>();
                for (int i = 0; i < layout.columns.size(); i++) {
                    if(Utils.isEmpty(layout.columns.get(i))) {
                        continue;
                    }
                    if (layout.fields.size()-1 >= i && !Utils.isEmpty(layout.fields.get(i))) {
                        String fieldname = layout.fields.get(i);
                        HashMap<String, Object> result = new HashMap<>();
                        for (TabField item : flowTaskSettings.getFields()) {
                            if(item.fieldname.equals(fieldname)){
                                int displaytype = item.displaytype;
                                if ((isEditState && displaytype == 1) || displaytype == 2) {
                                    break;
                                }
                                if (item.uitype == 26 && !"province".equals(item.regionlevel) && item.relation.isEmpty()) {
                                    break;
                                }

                                this.getOutputbyUitype(item, flowTaskSettings.getOutsideLinks(), recordInfo, isEditState, result);
                                result.put("deputy", item.deputy_column);
                                result.put("merge", item.merge_column);
                                result.put("showlabel", item.show_title);
                                result.put("html", item.html);
                                result.put("readonly", item.readonly);
                                result.put("aux", item.aux);
                                result.put("editwidth", item.editwidth);
                                result.put("editheight", item.editheight);
                                result.put("maxlength", item.maxlength);
                                result.put("multiselect", item.multiselect);
                                result.put("defaultvalue", item.defaultvalue);
                                result.put("remoteverify", item.remoteverify);
                                result.put("relation", item.relation);
                                result.put("clearbtn", item.clearbtn);
                                result.put("digwidth", item.digwidth);
                                result.put("digheight", item.digheight);
                                result.put("regionlevel", item.regionlevel);
                                result.put("onlyselect",item.onlyselect);
                                if(item.cutwidth > 0 && item.cutheight <= 0){
                                    item.cutheight = item.cutwidth;
                                }
                                if(item.cutheight > 0 && item.cutwidth <= 0){
                                    item.cutwidth = item.cutheight;
                                }
                                result.put("cutwidth", item.cutwidth);
                                result.put("cutheight", item.cutheight);
                                if(Utils.isNotEmpty(item.getFlowvalue())){
                                    result.put("flowvalue",item.getFlowvalue());
                                }
                                if(Utils.isNotEmpty(item.getFlowReadonly())) {
                                    result.put("flowreadonly", item.getFlowReadonly());
                                } else {
                                    result.put("flowreadonly", true);
                                }
                                break;
                            }
                        }
                        Map<String,Object> info = new HashMap<>(1);
                        info.put("count",layout.columns.get(i));
                        if(!result.keySet().isEmpty()) {
                            info.put("field",result);
                        }
                        row.add(info);
                    } else {
                        Map<String,Object> info = new HashMap<>(1);
                        info.put("count",layout.columns.get(i));
                        row.add(info);
                    }
                }
                layoutData.add(row);
            }
        }
        return layoutData;
    }

    @ApiOperation("根据字段定义生成编辑页面数据结构")
    private List<Object> getEditViewEntity(Object recordInfo) {
        List<Object> blockInfos = new ArrayList<>();
        FlowTaskSettings flowTaskSettings = null;
        if(this.workflowStatus.isDefineflow() && this.workflowStatus.isWorkflow() && !this.workflowStatus.isSuspend()){
            flowTaskSettings = getFlowTaskSettings(recordInfo);
        }
        for (Block item : this.dataEntity.blocks) {
            if (item.blockid <= 0) {
                continue;
            }
            List<Object> layout;
            if(Utils.isNotEmpty(flowTaskSettings)){
                layout = this.getLayoutField(item.blockid, recordInfo, flowTaskSettings);
            } else {
                layout = this.getLayoutField(item.blockid, recordInfo);
            }
            if (blockFieldAllHide(layout)) {
                continue;
            }
            Map<String,Object> info = new HashMap<>(1);
            info.put("label", item.blocklabel);
            info.put("blockid", item.blockid);
            info.put("columns", item.columns);
            info.put("showtitle", item.showtitle);
            info.put("layouts", layout);
            blockInfos.add(info);
        }
        return blockInfos;
    }

    @ApiOperation("根据配置信息，生成编辑页面按钮")
    private List<Action> getEditViewButtonActions(Object recordInfo, BaseActionsConfig actionsEntity) {
        List<Action> buttons = new ArrayList<>();
        String record = ((BaseRecordConfig) recordInfo).id;
        List<String> actionKeys = new ArrayList<>();
        if (actionsEntity.actions != null && !actionsEntity.actions.isEmpty()) {
            for (Action item : actionsEntity.actions) {
                if (!Utils.isEmpty(item.actiontype) && "editview".equals(item.actiontype)) {
                    if (Utils.isEmpty(item.actionkey)) {
                        continue;
                    }
                    Object funResult = null;
                    if (item.securitycheck) {
                        //进行权限验证
                        if (Utils.isNotEmpty(item.authorizes)) {
                            funResult = false;
                            List<String> authorizes = Arrays.asList(item.authorizes.split(","));
                            String profileid = ProfileUtils.getCurrentProfileId();
                            for(String authorize : authorizes) {
                                if (AuthorizeUtils.isAuthorizes(profileid,authorize)) {
                                    funResult = true;
                                    break;
                                }
                            }
                            if (!Boolean.parseBoolean(funResult.toString()) && !Utils.isEmpty(item.funclass)) {
                                try {
                                    funResult = getActionVerify(item.funclass, item.funpara, this.actionsEntity.tabs.modulename, "");
                                } catch (Exception e) {
                                    funResult = false;
                                }
                            }
                        } else {
                            if (!Utils.isEmpty(item.funclass)) {
                                //执行自定义处理函数
                                try {
                                    funResult = getActionVerify(item.funclass, item.funpara, actionsEntity.tabs.modulename, record);
                                } catch (Exception e) {
                                    funResult = false;
                                }
                            } else {
                                funResult = false;
                            }
                        }
                    }
                    if (actionKeys.contains(item.actionkey)) {
                        for (Action button : buttons) {
                            if (button.actionkey.equals(item.actionkey)) {
                                if (funResult instanceof Boolean) {
                                    if (!((Boolean) funResult)) {
                                        buttons.remove(button);
                                        actionKeys.remove(item.actionkey);
                                        break;
                                    }
                                } else {
                                    if (!Utils.isEmpty(funResult) && funResult instanceof String) {
                                        button.element(funResult.toString());
                                    }
                                }
                                if (!item.actionlabel.isEmpty()) {
                                    button.actionlabel(item.actionlabel);
                                }
                                if (!item.icon.isEmpty()) {
                                    button.icon(item.icon);
                                }
                                if (!item.event.isEmpty()) {
                                    button.event(item.event);
                                }
                                if (!item.toggle.isEmpty()) {
                                    button.toggle(item.toggle);
                                }
                                if (!item.digwidth.isEmpty()) {
                                    button.digwidth(item.digwidth);
                                }
                                if (!item.digheight.isEmpty()) {
                                    button.digheight(item.digheight);
                                }
                                if (!item.confirm.isEmpty()) {
                                    button.confirm(item.confirm);
                                }
                                if (!item.actionclass.isEmpty()) {
                                    button.actionclass(item.actionclass);
                                }
                                if (!item.actionstyle.isEmpty()) {
                                    button.actionstyle(item.actionstyle);
                                }
                                break;
                            }
                        }
                    } else {
                        if (funResult instanceof Boolean) {
                            if (!((Boolean) funResult)) {
                                actionKeys.remove(item.actionkey);
                                continue;
                            }
                        } else {
                            if (!Utils.isEmpty(funResult) && funResult instanceof String) {
                                item.element(funResult.toString());
                            }
                        }
                        buttons.add(item);
                        actionKeys.add(item.actionkey);
                    }
                }
            }
        }
        if(this.workflowStatus.isDefineflow() && !this.workflowStatus.isSuspend()){
            boolean isauthor = ((BaseRecordConfig) recordInfo).author.equals(ProfileUtils.getCurrentProfileId());
            boolean isedit = ((BaseRecordConfig) recordInfo).getEditState();
            if (!this.workflowStatus.isFinished() && isedit) {
                if (isauthor) {
                    if (this.workflowStatus.isApproval()) {
                        if ((!this.workflowStatus.isStartflow() || this.workflowStatus.isFirstNode()) && !this.workflowStatus.isWaitupper()) {
                            buttons.add(new Action().actionkey("Activiti_SubmitApproval").actionlabel("提交审批").actiontype("editview").modulename(actionsEntity.tabs.modulename).submit(true).actionclass("layui-btn-warm").url("approvalcenter/submitapproval/ajax").confirm("提交审批后，您将没有权限再进行修改，是否确定提交?"));
                        }
                    } else {
                        if ((this.workflowStatus.isDealwith() || !this.workflowStatus.isStartflow()) && !this.workflowStatus.isWaitupper()) {
                            buttons.add(new Action().actionkey("Activiti_AssignFlow").actionlabel("交办流程").actiontype("editview").toggle("dialog").digwidth("800px").modulename(actionsEntity.tabs.modulename).submit(true).actionclass("layui-btn-warm").url("flowlaunch/release/ajax").submiturl("flowlaunch/release/submit"));
                        }
                    }
                    if (this.workflowStatus.isStartflow() && !this.workflowStatus.isFirstNode()) {
                        if(this.workflowStatus.isApproval()){
                            buttons.add(new Action().actionkey("Activiti_ViewApproval").actionlabel("查看审批").actiontype("editview").toggle("dialogtab").modulename(actionsEntity.tabs.modulename).url("approvalcenter/viewapproval/ajax"));
                            buttons.add(new Action().actionkey("Activiti_EvacuateFlow").actionlabel("撤回审批").actiontype("editview").toggle("dialog").digwidth("500px").modulename(actionsEntity.tabs.modulename).submit(true).actionclass("layui-btn-danger").url("approvalcenter/evacuate/ajax").submiturl("approvalcenter/evacuate/submit"));
                        }else {
                            buttons.add(new Action().actionkey("Activiti_ViewWorkFlow").actionlabel("查看流程").actiontype("editview").toggle("dialogtab").modulename(actionsEntity.tabs.modulename).url("flowlaunch/viewworkflow/ajax"));
                            buttons.add(new Action().actionkey("Activiti_EvacuateFlow").actionlabel("撤回流程").actiontype("editview").toggle("dialog").digwidth("500px").modulename(actionsEntity.tabs.modulename).submit(true).actionclass("layui-btn-danger").url("flowlaunch/evacuate/ajax").submiturl("flowlaunch/evacuate/submit"));
                        }
                    }
                } else {
                    if(this.workflowStatus.isStartflow()) {
                        if (this.workflowStatus.isApproval()) {
                            buttons.add(new Action().actionkey("Activiti_ViewApproval").actionlabel("查看审批").actiontype("editview").toggle("dialogtab").modulename(actionsEntity.tabs.modulename).url("approvalcenter/viewapproval/ajax"));
                        }
                        if (this.workflowStatus.isWorkflow()) {
                            buttons.add(new Action().actionkey("Activiti_ViewWorkFlow").actionlabel("查看流程").actiontype("editview").toggle("dialogtab").modulename(actionsEntity.tabs.modulename).url("flowlaunch/viewworkflow/ajax"));
                        }
                    }
                    if (this.workflowStatus.isDealwith() && !this.workflowStatus.isWaitupper() && this.getWorkflowStatus().isWorkflow()) {
                        buttons.add(new Action().actionkey("Activiti_AssignFlow").actionlabel("交办流程").actiontype("editview").toggle("dialog").digwidth("800px").modulename(actionsEntity.tabs.modulename).submit(true).actionclass("layui-btn-warm").url("flowlaunch/release/ajax").submiturl("flowlaunch/release/submit"));
                        if (this.workflowStatus.isRollBack()) {
                            buttons.add(new Action().actionkey("Activiti_RollbackFlow").actionlabel("退回流程").actiontype("editview").toggle("dialog").digwidth("800px").modulename(actionsEntity.tabs.modulename).submit(true).actionclass("layui-btn-danger").url("flowlaunch/rollback/ajax").submiturl("flowlaunch/rollback/submit"));
                        }
                    }
                }
            }else if(this.workflowStatus.isStartflow() && isedit) {
                if(this.workflowStatus.isApproval()) {
                    buttons.add(new Action().actionkey("Activiti_ViewApproval").actionlabel("查看审批").actiontype("editview").toggle("dialogtab").modulename(actionsEntity.tabs.modulename).url("approvalcenter/viewapproval/ajax"));
                }
                if(this.workflowStatus.isWorkflow()){
                    buttons.add(new Action().actionkey("Activiti_ViewWorkFlow").actionlabel("查看流程").actiontype("editview").toggle("dialogtab").modulename(actionsEntity.tabs.modulename).url("flowlaunch/viewworkflow/ajax"));
                }
            }
        }
        if(((BaseRecordConfig) recordInfo).getEditState() && PrintUtils.hasPrint(actionsEntity.tabs.modulename)) {
            buttons.add(new Action().actionkey("print").actionlabel("打印").actiontype("editview").toggle("dialog").modulename(actionsEntity.tabs.modulename).digwidth("100%").digheight("100%").url("/"+actionsEntity.tabs.modulename+"/print"));
        }
        List<Action> result = new ArrayList<>();
        for (Action action : buttons) {
            if("SubmitOnline".equals(action.actionkey)) {
                if(this.workflowStatus.isDefineflow() && !this.workflowStatus.isSuspend()){
                    continue;
                }
            }
            result.add(action);
        }
        return result;
    }

    @ApiOperation("根据配置信息，生成编辑页面Tab区块")
    private List<Action> getEditViewPanelActions(Object recordInfo, String location, BaseActionsConfig actionsEntity) {
        List<Action> buttons = new ArrayList<>();
        String record = ((BaseRecordConfig) recordInfo).id;
        if (actionsEntity.actions != null && !actionsEntity.actions.isEmpty()) {
            List<String> actionKeys = new ArrayList<>();
            for (Action item : actionsEntity.actions) {
                if (!Utils.isEmpty(item.actiontype) && "panel".equals(item.actiontype)) {
                    if (Utils.isEmpty(item.actionkey)) {
                        continue;
                    }
                    if (Utils.isEmpty(item.location) || !item.location.equals(location)) {
                        continue;
                    }
                    Object funResult = null;
                    if (item.securitycheck) {
                        //进行权限验证
                        if (Utils.isNotEmpty(item.authorizes)) {
                            funResult = false;
                            String[] authorizes = item.authorizes.split(",");
                            String profileid = ProfileUtils.getCurrentProfileId();
                            for(String authorize : authorizes) {
                                if (AuthorizeUtils.isAuthorizes(profileid,authorize)) {
                                    funResult = true;
                                    break;
                                }
                            }
                            if (!Boolean.parseBoolean(funResult.toString()) && !Utils.isEmpty(item.funclass)) {
                                try {
                                    funResult = getActionVerify(item.funclass, item.funpara, this.actionsEntity.tabs.modulename, "");
                                } catch (Exception e) {
                                    funResult = false;
                                }
                            }
                        } else {
                            if (!Utils.isEmpty(item.funclass)) {
                                //执行自定义处理函数
                                try {
                                    funResult = getActionVerify(item.funclass, item.funpara, actionsEntity.tabs.modulename, record);
                                } catch (Exception e) {
                                    funResult = false;
                                }
                            } else {
                                funResult = false;
                            }
                        }
                    }
                    if (actionKeys.contains(item.actionkey)) {
                        for (Action button : buttons) {
                            if (button.actionkey.equals(item.actionkey)) {
                                if (funResult instanceof Boolean) {
                                    if (!((Boolean) funResult)) {
                                        buttons.remove(button);
                                        actionKeys.remove(item.actionkey);
                                        break;
                                    }
                                }
                                if (!item.actionlabel.isEmpty()) {
                                    button.actionlabel(item.actionlabel);
                                }
                                if (!item.url.isEmpty()) {
                                    button.url(item.url);
                                }
                                break;
                            }
                        }
                    } else {
                        if (funResult instanceof Boolean) {
                            if (!((Boolean) funResult)) {
                                actionKeys.remove(item.actionkey);
                                continue;
                            }
                        }
                        buttons.add(item);
                        actionKeys.add(item.actionkey);
                    }
                }
            }
        }
        if("base".equals(location)) {
            if(this.workflowStatus.isDefineflow() && this.workflowStatus.isStartflow() && !this.workflowStatus.isFinished() && this.workflowStatus.isApproval() && this.workflowStatus.isDealwith() && !this.workflowStatus.isFirstNode() && !this.workflowStatus.isSuspend()){
                buttons.add(new Action().actionkey("Activiti_ConfirmApproval").actionlabel("审批信息").actiontype("panel").toggle("ajax").modulename(actionsEntity.tabs.modulename).location("base").url("/approvalcenter/confirmapproval"));
            }
        }

        return buttons;
    }

    @ApiOperation("根据字段值获取字典中对应的标签")
    private String getPicklistLabelByValue(String fieldName, Object fieldValue, String defaultValue,Boolean isListview) {
        Object picklists = PickListUtils.getPickList(fieldName);
        if(fieldValue.toString().contains(",")){
            fieldValue = Arrays.asList(fieldValue.toString().split(","));
        }
        if (!Utils.isEmpty(picklists)) {
            List<String> result = new ArrayList<>();
            for (Object picklist : (List) picklists) {
                if(fieldValue instanceof List){
                    for(Object val: (List)fieldValue){
                        if (((List) picklist).get(0).equals(val.toString())) {
                            if (!Utils.isEmpty(((List) picklist).get(3)) && isListview) {
                                result.add("<span class='" + ((List) picklist).get(3).toString() + "'>" + ((List) picklist).get(1).toString() + "</span>");
                            } else {
                                result.add(((List) picklist).get(1).toString());
                            }
                        }
                    }
                }else {
                    if (((List) picklist).get(0).equals(fieldValue)) {
                        if (!Utils.isEmpty(((List) picklist).get(3)) && isListview) {
                            return "<span class='" + ((List) picklist).get(3).toString() + "'>" + ((List) picklist).get(1).toString() + "</span>";
                        } else {
                            return ((List) picklist).get(1).toString();
                        }
                    }
                }
            }
            if(!result.isEmpty()) {
                return String.join(",",result);
            } else if(Utils.isNotEmpty(fieldValue)) {
                if (fieldValue instanceof List) {
                    List<String> lists = ( List<String>)fieldValue;
                    return String.join(",",lists);
                } else {
                    return fieldValue.toString();
                }
            }
        }
        return defaultValue;
    }

    @ApiOperation("根据字段值获取字典中对应的标签")
    private String getPicklistLabelByIntValue(String fieldName, Object fieldValue, String defaultValue,Boolean isListview) {
        Object picklists = PickListUtils.getPickList(fieldName);
        if(fieldValue.toString().contains(",")){
            fieldValue = Arrays.asList(fieldValue.toString().split(","));
        }
        if (!Utils.isEmpty(picklists)) {
            List<String> result = new ArrayList<>();
            for (Object picklist : (List) picklists) {
                if(fieldValue instanceof List){
                    for(Object val: (List)fieldValue){
                        if (((List) picklist).get(2).equals(Double.valueOf(val.toString()).intValue())) {
                            if (!Utils.isEmpty(((List) picklist).get(3)) && isListview) {
                                result.add("<span class='" + ((List) picklist).get(3).toString() + "'>" + ((List) picklist).get(1).toString() + "</span>");
                            } else {
                                result.add(((List) picklist).get(1).toString());
                            }
                        }
                    }
                }else {
                    if (((List)picklist).get(2).equals(Double.valueOf(fieldValue.toString()).intValue())) {
                        if (!Utils.isEmpty(((List) picklist).get(3)) && isListview) {
                            return "<span class='" + ((List) picklist).get(3).toString() + "'>" + ((List) picklist).get(1).toString() + "</span>";
                        } else {
                            return ((List) picklist).get(1).toString();
                        }
                    }

                }
            }
            if(!result.isEmpty()) {
                return String.join(",",result);
            }
        }
        return defaultValue;
    }

    @ApiOperation("根据Action获取Fun的值")
    private Object getActionVerify(String fun, String param, String modulename, String record) {
        if (Utils.isEmpty(fun)) {
            return false;
        }
        try {
            Object result = CallbackUtils.invoke("getActionVerify", Class.forName(fun,false, SpringServiceUtil.getApplicationContext().getClassLoader()), param, modulename, record, this);
            if (result == null) {
                return false;
            } else {
                return result;
            }
        } catch (Exception e) {
            log.error("==========================================================");
            log.error("回调错误 ： ActionVerify");
            log.error("回调错误 ： Class：{}", fun);
            log.error("回调错误 ： Module：getActionVerify");
            log.error("回调错误 ： param：{}", param);
            log.error("回调错误 ： Record：{}", record);
            log.error("回调错误 ： Error：{}", e.getMessage());
            log.error("==========================================================");
            return false;
        }
    }

    public static void deleteOutsideLink(String fieldValue) {
        if(Utils.isEmpty(outsideLinkList) || Utils.isEmpty(fieldValue)) {
            return;
        }
        outsideLinkList.remove(fieldValue);
    }

    @ApiOperation("根据字段值获取关联表的值")
    public String getOutsideLinkValue(String fieldName, Object fieldValue, String defaultValue) {
        return getOutsideLinkValue(this.outsideLinks,fieldName,fieldValue,defaultValue);
    }
    private String getOutsideLinkValue(Map<String,OutsideLink> outsideLinks,String fieldName, Object fieldValue, String defaultValue) {
        if (!Utils.isEmpty(outsideLinkList.get(fieldValue.toString())) && !outsideLinkList.get(fieldValue.toString()).equals(defaultValue)) {
            if ("--".equals(outsideLinkList.get(fieldValue.toString()).toString())) {
                return defaultValue;
            }
            return outsideLinkList.get(fieldValue.toString()).toString();
        } else {
            if (!Utils.isEmpty(fieldValue) && !Utils.isEmpty(outsideLinks.get(fieldName))) {
                OutsideLink linkModule = outsideLinks.get(fieldName);
                if (!Utils.isEmpty(linkModule.serviceclass)) {
                    String serviceclass = linkModule.serviceclass;
                    String relmodule = linkModule.relmodule;
                    try {
                        Object methodresult = CallbackUtils.invoke("getOutsideValue", Class.forName(serviceclass,false, SpringServiceUtil.getApplicationContext().getClassLoader()), fieldValue, relmodule);
                        if (!Utils.isEmpty(methodresult)) {
                            outsideLinkList.put(fieldValue.toString(), methodresult);
                            return methodresult.toString();
                        } else {
                            outsideLinkList.put(fieldValue.toString(), defaultValue);
                        }
                    } catch (Exception e) {
                        outsideLinkList.put(fieldValue.toString(), defaultValue);
                        log.error("==========================================================");
                        log.error("外链数据错误 ： Uitype：11、12");
                        log.error("外链数据错误 ： Class：{}", serviceclass);
                        log.error("外链数据错误 ： Module：getOutsideValue");
                        log.error("外链数据错误 ： Record：{}", fieldValue);
                        log.error("外链数据错误 ： Error：{}", e.getMessage());
                        log.error("==========================================================");
                    }
                } else {
                    outsideLinkList.put(fieldValue.toString(), defaultValue);
                    log.warn("==========================================================");
                    log.warn("外链数据错误 ： Uitype：11、12");
                    log.warn("外链数据错误 ： fieldName：{}", fieldName);
                    log.warn("外链数据错误 ： Module：getOutsideValue");
                    log.warn("外链数据错误 ： Record：{}", fieldValue);
                    log.warn("外链数据错误 ： Error：参数配置不全");
                    log.warn("==========================================================");
                }
            }
        }
        return defaultValue;
    }

    @ApiOperation("根据字段值获取用户表的值")
    public String getOutsideProfileValue(Object fieldValue, String defaultValue) {
        if(fieldValue.toString().contains(",")){
            List<String> tmp = new ArrayList<>(Arrays.asList(fieldValue.toString().split(",")));
            if(tmp.isEmpty()) {
                return defaultValue;
            }
            List<String> names = new ArrayList<>();
            for(String item: tmp) {
                if (!Utils.isEmpty(CacheBaseEntitys.getProfileName(item))) {
                    Utils.removeDuplicate(names,CacheBaseEntitys.getProfileName(item));
                } else {
                    try {
                        if (Utils.isEmpty(item) || item.compareTo("anonymous") == 0) {
                            names.add(defaultValue);
                        }
                        Profile profile = XN_Profile.load(item, "profile");
                        CacheBaseEntitys.addProfileName(item, profile.givenname);
                        Utils.removeDuplicate(names,profile.givenname);
                    } catch (Exception e) {
                        names.add(defaultValue);
                    }
                }
            }
            return String.join(",",names);
        } else {
            if (!Utils.isEmpty(CacheBaseEntitys.getProfileName(fieldValue.toString()))) {
                return CacheBaseEntitys.getProfileName(fieldValue.toString());
            } else {
                try {
                    if (Utils.isEmpty(fieldValue) || fieldValue.toString().compareTo("anonymous") == 0) {
                        return defaultValue;
                    }
                    Profile profile = XN_Profile.load(fieldValue.toString(), "profile");
                    CacheBaseEntitys.addProfileName(fieldValue.toString(), profile.givenname);
                    return profile.givenname;
                } catch (Exception e) {
                    return defaultValue;
                }
            }
        }
    }
    @ApiOperation("根据字段值获取部门表的值")
    public String getOutsideDepartmentValue(Object fieldValue, String defaultValue) {
        if(fieldValue.toString().contains(",")){
            List<String> tmp = new ArrayList<>(Arrays.asList(fieldValue.toString().split(",")));
            if(tmp.isEmpty()) {
                return defaultValue;
            }
            List<String> names = new ArrayList<>();
            for(String item: tmp) {
                if (!Utils.isEmpty(outsideLinkList.get(item)) && !outsideLinkList.get(item).equals(defaultValue)) {
                    Utils.removeDuplicate(names,outsideLinkList.get(item).toString());
                } else {
                    try {
                        if(ProfileUtils.isSupplier()){
                            Content depar = XN_Content.load(item,"supplier_departments");
                            outsideLinkList.put(item,depar.get("name",defaultValue));
                            Utils.removeDuplicate(names,depar.get("name",defaultValue));
                        } else {
                            Content depar = XN_Content.load(item,"departments");
                            outsideLinkList.put(item,depar.get("departmentname",defaultValue));
                            Utils.removeDuplicate(names,depar.get("departmentname",defaultValue));
                        }
                    } catch (Exception e) {
                        names.add(defaultValue);
                    }
                }
            }
            return String.join(",",names);
        } else {
            if (!Utils.isEmpty(outsideLinkList.get(fieldValue.toString())) && !outsideLinkList.get(fieldValue.toString()).equals(defaultValue)) {
                return outsideLinkList.get(fieldValue.toString()).toString();
            } else {
                try {
                    if(ProfileUtils.isSupplier()){
                        Content depar = XN_Content.load(fieldValue.toString(),"supplier_departments");
                        outsideLinkList.put(fieldValue.toString(),depar.get("name",defaultValue));
                        return depar.get("name",defaultValue).toString();
                    } else {
                        Content depar = XN_Content.load(fieldValue.toString(),"departments");
                        outsideLinkList.put(fieldValue.toString(),depar.get("departmentname",defaultValue));
                        return depar.get("departmentname",defaultValue).toString();
                    }
                } catch (Exception e) {
                    return defaultValue;
                }
            }
        }
    }

    private List<Object> getcustomEditViewEntity(List<TabField> fields) {
        List<Object> rows = new ArrayList<>();
        for (TabField item : fields) {
                HashMap<String, Object> result = new HashMap<>();
                int displaytype = item.displaytype;
                if (displaytype == 1 || displaytype == 2) {
                    break;
                }
                if (item.uitype == 26 && !"province".equals(item.regionlevel) && item.relation.isEmpty()) {
                    break;
                }
                this.getOutputbyUitype(item, null, true, result);
                result.put("deputy", item.deputy_column);
                result.put("merge", item.merge_column);
                result.put("showlabel", item.show_title);
                result.put("readonly", item.readonly);
                result.put("aux", item.aux);
                result.put("editwidth", item.editwidth);
                result.put("editheight", item.editheight);
                result.put("maxlength", item.maxlength);
                result.put("multiselect", item.multiselect);
                result.put("defaultvalue", item.defaultvalue);
                result.put("remoteverify", item.remoteverify);
                result.put("relation", item.relation);
                result.put("clearbtn", item.clearbtn);
                result.put("digwidth", item.digwidth);
                result.put("digheight", item.digheight);
                result.put("regionlevel", item.regionlevel);
                result.put("onlyselect",item.onlyselect);
                if(item.cutwidth > 0 && item.cutheight <= 0){
                    item.cutheight = item.cutwidth;
                }
                if(item.cutheight > 0 && item.cutwidth <= 0){
                    item.cutwidth = item.cutheight;
                }
                result.put("cutwidth", item.cutwidth);
                result.put("cutheight", item.cutheight);
                rows.add(result);
        }
        return rows;
    }

    @ApiOperation("根据定义生成编辑页面")
    public Object editView(Map<String, Object> httpRequest, Model model, Object recordInfo) {
        return editView(httpRequest, model, recordInfo, "EditView");
    }
    public Object editView(Map<String, Object> httpRequest, Model model, Object recordInfo, String viewPath) {
        try {
            this.workflowStatus = WorkflowUtils.getWorkflowStatus(this.getModuleName(),((BaseRecordConfig) recordInfo).id);
            List<Object> blocks = this.getEditViewEntity(recordInfo);
            customEditViewEntityCallback(blocks,recordInfo);
            customEditViewSetting(blocks);
            model.addAttribute("MODULE", this.getModuleName());
            model.addAttribute("BLOCKS", blocks);
            if(!model.containsAttribute("TABNAME")) {
                model.addAttribute("TABNAME", ((BaseRecordConfig) recordInfo).getEditState() ? this.getModuleLabel() + "详情" : "新建" + this.getModuleLabel());
            }
            model.addAttribute("ISEDIT", ((BaseRecordConfig) recordInfo).getEditState());
            model.addAttribute("RECORD", ((BaseRecordConfig) recordInfo).id);
            model.addAttribute("AUTHOR", getOutsideProfileValue(((BaseRecordConfig) recordInfo).author,"--"));
            model.addAttribute("PUBLISHED", ((BaseRecordConfig) recordInfo).published);
            model.addAttribute("FLOWREADONLY", true);
            if (this.actionsEntity != null) {
                model.addAttribute("EDITACTIONS", Utils.classToData(this.getEditViewButtonActions(recordInfo, this.actionsEntity)));
                model.addAttribute("PANELACTIONS", Utils.classToData(this.getEditViewPanelActions(recordInfo, "panel", this.actionsEntity)));
                model.addAttribute("BOTTOMACTIONS", Utils.classToData(this.getEditViewPanelActions(recordInfo, "bottom", this.actionsEntity)));
                model.addAttribute("BASEACTIONS", Utils.classToData(this.getEditViewPanelActions(recordInfo, "base", this.actionsEntity)));
            }
            boolean readonly = this.dataEntity.tabs.isreadonly || !RolesUtils.isEdit(this.dataEntity.tabs.modulename);

            if (!readonly) {
                Object isCreatorPrivateUse = CallbackUtils.invoke("isCreatorPrivateUse", entityPackageName, IBaseService.class);
                if (Utils.isNotEmpty(isCreatorPrivateUse) && Boolean.TRUE.equals(isCreatorPrivateUse)) {
                    readonly = !((BaseRecordConfig) recordInfo).author.equals(ProfileUtils.getCurrentProfileId());
                }
            }

            if(this.workflowStatus.isDefineflow()){
                model.addAttribute("ISDEFINEFLOW", true);
            }
            if(this.workflowStatus.isDefineflow() && !this.workflowStatus.isSuspend()) {
                boolean isauthor = ((BaseRecordConfig) recordInfo).author.equals(ProfileUtils.getCurrentProfileId());
                readonly = this.workflowStatus.isFinished() || ((!this.workflowStatus.isFirstNode() || !isauthor) && (this.workflowStatus.isStartflow() || !isauthor) && (!this.workflowStatus.isDealwith() || !this.workflowStatus.isWaitupper()));

                if (readonly) {
                    if (this.workflowStatus.isFinished()) {
                        if (this.workflowStatus.isWorkflow()) {
                            model.addAttribute("FLOWSTATUS", 2);
                        } else if (this.workflowStatus.isApproval()) {
                            model.addAttribute("APPROVALSTATUS", 2);
                        }
                    } else if (this.workflowStatus.isStartflow() && !this.workflowStatus.isFirstNode()) {
                        if (this.workflowStatus.isWorkflow()) {
                            model.addAttribute("FLOWSTATUS", ((BaseRecordConfig) recordInfo).activiti_executionstatus);
                        } else if (this.workflowStatus.isApproval()) {
                            model.addAttribute("APPROVALSTATUS", ((BaseRecordConfig) recordInfo).approvalstatus);
                        }
                        if(this.workflowStatus.isDealwith()) {
                            model.addAttribute("FLOWREADONLY", getFlowReadonly(((BaseRecordConfig) recordInfo).id));
                        }
                    } else {
                        if (this.workflowStatus.isWorkflow()) {
                            model.addAttribute("FLOWSTATUS", 0);
                        } else if (this.workflowStatus.isApproval()) {
                            model.addAttribute("APPROVALSTATUS", 0);
                        }
                    }
                }
                if (this.workflowStatus.isWaitupper()) {
                    readonly = true;
                    if (Utils.isNotEmpty(this.workflowStatus.getWaitUppers())) {
                        List<Map<String, Object>> uppers = this.workflowStatus.getWaitUppers();
                        StringBuilder html = new StringBuilder("<table class='layui-table'><tr>")
                                .append("<th class='layui-table-header layui-table-cell'>环节名称</th>")
                                .append("<th class='layui-table-header layui-table-cell'>处理人员</th>")
                                .append("<th class='layui-table-header layui-table-cell'>停留时间</th></tr>");
                        for (Map<String, Object> upper : uppers) {
                            html.append("<tr><td>").append(upper.getOrDefault("flownode", "")).append("</td><td>").append(upper.getOrDefault("user", "")).append("</td><td>").append(upper.getOrDefault("time", "")).append("</td></tr>");
                        }
                        html.append("</table>").append("必需等待以上办理完成");
                        model.addAttribute("MSGPROMPT", html.toString());
                    } else {
                        model.addAttribute("MSGPROMPT", "必需等待其它人办理完成");
                    }
                }
            }else if(this.workflowStatus.isDefineflow() && this.workflowStatus.isSuspend()) {
                readonly = true;
                if (this.workflowStatus.isWorkflow()) {
                    model.addAttribute("FLOWSTATUS", 5);
                } else if (this.workflowStatus.isApproval()) {
                    if(this.workflowStatus.isStartflow()) {
                        model.addAttribute("APPROVALSTATUS", 5);
                    } else {
                        readonly = false;
                        if(((BaseRecordConfig) recordInfo).approvalstatus.equals(1) || ((BaseRecordConfig) recordInfo).approvalstatus.equals(2) || ((BaseRecordConfig) recordInfo).approvalstatus.equals(4) || ((BaseRecordConfig) recordInfo).approvalstatus.equals(5)){
                            model.addAttribute("APPROVALSTATUS", ((BaseRecordConfig) recordInfo).approvalstatus);
                            readonly = true;
                        }
                    }
                }
            } else {
                if(((BaseRecordConfig) recordInfo).approvalstatus.equals(1) || ((BaseRecordConfig) recordInfo).approvalstatus.equals(2) || ((BaseRecordConfig) recordInfo).approvalstatus.equals(4) || ((BaseRecordConfig) recordInfo).approvalstatus.equals(5)){
                    readonly = true;
                }
            }
            Object editViewIsReadOnly = CallbackUtils.invoke("getEditViewIsReadOnly", entityPackageName, IBaseService.class, recordInfo);
            if (Utils.isNotEmpty(editViewIsReadOnly)) {
                readonly = (Boolean) editViewIsReadOnly;
            }
            model.addAttribute("READONLY", readonly);
            model.addAttribute("EDITACTIONFLOW", this.dataEntity.tabs.editactionflow);
            model.addAttribute("REQUIRELEFT",this.dataEntity.tabs.requireleft);
            model.addAttribute("SAVEISCLOSEEDITVIEW",this.dataEntity.tabs.saveiscloseeditview);
            model.addAttribute("BLOCKCARD",this.dataEntity.tabs.blockcard);
            model.addAttribute("EDITVIEWHASSCRIPT",false);

            Object result = CallbackUtils.invoke("getLabelWidth", entityPackageName, IBaseService.class);
            if (!Utils.isEmpty(result)) {
                model.addAttribute("LABELWIDTH",result);
            } else {
                model.addAttribute("LABELWIDTH",120);
            }

            if (isExistResource("views/"+this.getModuleName()+"/editview.js")) {
                model.addAttribute("EDITVIEWHASSCRIPT",true);
            }
            return WebViews.view(viewPath);
        } catch (Exception e) {
            return WebViews.view("error/403");
        }
    }
    @SuppressWarnings("unchecked")
    private void customEditViewSetting(List<Object> blocks) {
        try{
            Object result = CallbackUtils.invoke("getCustomFields", entityPackageName, IBaseService.class);
            if (!Utils.isEmpty(result)) {
                List<CustomFieldSetting> customFieldSetting = (List<CustomFieldSetting>)result;
                Map<String,CustomFieldSetting> fieldSetting = customFieldSetting.stream().collect(Collectors.toMap(v1 -> v1.fieldname, v1 -> v1, (k1, k2) -> k1));
                if (!Utils.isEmpty(blocks)) {
                    for (Object block : blocks) {
                        if (!Utils.isEmpty(((HashMap<?,?>) block).get("layouts"))) {
                            List<Object> layouts = (List<Object>) ((HashMap<?,?>) block).get("layouts");
                            Integer index = 0;
                            for (Object layout : layouts) {
                                if (!Utils.isEmpty(layout)) {
                                    List<?> myLayouts = (List<?>) layout;
                                    List<?> newLayout =  myLayouts.stream().filter( v -> {
                                         if (!Utils.isEmpty(((HashMap<?,?>) v).get("field"))) {
                                            HashMap<String, Object> field = (HashMap<String,Object>) ((HashMap<?,?>) v).get("field");
                                            String fieldname = field.get("name").toString();
                                            if (fieldSetting.containsKey(fieldname)) {
                                                if (fieldSetting.get(fieldname).ishidden) {
                                                    return false;
                                                }
                                            }
                                        }
                                        return true;
                                    }).map( v -> {
                                        if (!Utils.isEmpty(((HashMap<?,?>) v).get("field"))) {
                                            HashMap<String, Object> field = (HashMap<String,Object>) ((HashMap<?,?>) v).get("field");
                                            String fieldname = field.get("name").toString();
                                            if (fieldSetting.containsKey(fieldname)) {
                                                field.put("label",fieldSetting.get(fieldname).fieldlabel);
                                            }
                                        }
                                        return v;
                                    }).collect(Collectors.toList());
                                    layouts.set(index,newLayout);
                                }
                                index ++;
                            }
                        }
                    }
                }
            }
        }catch (Exception ignored) { }
    }
    @SuppressWarnings("unchecked")
    private void customEditViewEntityCallback(List<Object> blocks,Object recordInfo) {
        try {
            if (!Utils.isEmpty(blocks)) {
                for (Object block : blocks) {
                    if (!Utils.isEmpty(((HashMap<?,?>) block).get("layouts"))) {
                        List<Object> layouts = (List<Object>) ((HashMap<?,?>) block).get("layouts");
                        for (Object layout : layouts) {
                            if (!Utils.isEmpty(layout)) {
                                for (Object laycol : (List<?>) layout) {
                                    if (!Utils.isEmpty(((HashMap<?,?>) laycol).get("field"))) {
                                        HashMap<String, Object> field = (HashMap<String,Object>) ((HashMap<?,?>) laycol).get("field");
                                        String fieldname = field.get("name").toString();
                                        String fieldValue = ((HashMap<?,?>) field.get("value")).get("1").toString();
                                        String record = ((BaseRecordConfig)recordInfo).id;
                                        Object result = CallbackUtils.invoke("customEditViewEntity", this.entityPackageName, IBaseService.class, record,fieldname,fieldValue);
                                        if(!Utils.isEmpty(result)){
                                            CustomEditEntity editEntity = (CustomEditEntity) result;
                                            if(!Utils.isEmpty(editEntity.uitype)) {
                                                field.put("uitype",editEntity.uitype);
                                            }
                                            if(!Utils.isEmpty(editEntity.onevalue)) {
                                                ((HashMap<String,Object>)field.get("value")).put("1",editEntity.onevalue);
                                            }
                                            if(!Utils.isEmpty(editEntity.twovalue)) {
                                                ((HashMap<String,Object>)field.get("value")).put("2",editEntity.twovalue);
                                            }
                                            if(!Utils.isEmpty(editEntity.threevalue)) {
                                                ((HashMap<String,Object>)field.get("value")).put("3",editEntity.threevalue);
                                            }
                                            if(!Utils.isEmpty(editEntity.fourvalue)) {
                                                ((HashMap<String,Object>)field.get("value")).put("4",editEntity.fourvalue);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception ignored) { }
    }

    //endregion

    //region 弹窗封装
    @ApiOperation("根据定义生成弹出窗口")
    public Object popupView(Map<String, Object> httpRequest, Model model) {
        try {
            boolean multiselect = false;
            if (!Utils.isEmpty(httpRequest.get("multiselect")) && "true".equals(httpRequest.get("multiselect"))) {
                model.addAttribute("MULTISELECT", true);
                multiselect = true;
            }
            if (!Utils.isEmpty(httpRequest.get("exclude"))) {
                if (httpRequest.get("exclude") instanceof String) {
                    model.addAttribute("EXCLUDE", Utils.objectToJson(Collections.singletonList(httpRequest.get("exclude"))));
                } else {
                    model.addAttribute("EXCLUDE", Utils.objectToJson(httpRequest.get("exclude")));
                }
            }

            if (!Utils.isEmpty(httpRequest.get("linkage"))) {
                model.addAttribute("LINKAGE", httpRequest.get("linkage").toString());
            }

            if (!Utils.isEmpty(httpRequest.get("select"))) {
                if (httpRequest.get("select") instanceof String) {
                    model.addAttribute("SELECT", Utils.objectToJson(Collections.singletonList(httpRequest.get("select"))));
                } else {
                    model.addAttribute("SELECT", Utils.objectToJson(httpRequest.get("select")));
                }
            }

            if (!Utils.isEmpty(httpRequest.get("digheight"))) {
                String digheight = httpRequest.get("digheight").toString();
                if (digheight.contains("px")) {
                    digheight = digheight.replace("px", "");
                    model.addAttribute("DIGHEIGHT",  Double.valueOf(digheight).intValue() - 110);
                } else {
                    model.addAttribute("DIGHEIGHT", "378");
                }
            }
            String linkfield = this.dataEntity.entityNames.fieldname;
            model.addAttribute("MODULE", this.getModuleName());
            List<Map<String,Object>> result = this.getPopupViewHeader(multiselect);
            List<Object> search = this.popupDataSearch(null);
            model.addAttribute("POPLISTHEADER", headerToJson(Arrays.asList(result)));
            model.addAttribute("POPLISTSEARCH", search);
            model.addAttribute("LINKFIELD", linkfield);
            model.addAttribute("REMODULE",httpRequest.get("remodule"));
            model.addAttribute("RECORD",httpRequest.get("record"));
            int limit = ProfileUtils.getPageLimit();
            if(httpRequest.containsKey("limit") && httpRequest.get("limit").toString().compareTo("") != 0){
                limit = Double.valueOf(httpRequest.getOrDefault("limit","20").toString()).intValue();
            }
            model.addAttribute("LIMIT", limit);
            result.clear();
            return WebViews.view("PopupView");
        } catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

    @ApiOperation("根据定义生成弹出窗口的查询面版")
    private ArrayList<Object> popupDataSearch(List<String> hideFields) {
        ArrayList<Object> search = new ArrayList<>();
        for (String fieldname : this.dataEntity.popupDialog.search) {
            if (hideFields != null && hideFields.contains(fieldname)) {
                continue;
            }
            if (Utils.isEmpty(fieldname)) {
                continue;
            }
            String fieldlabel = "";
            if(Utils.isEmpty(this.moduleFields.get(fieldname))){
                if("author".equals(fieldname)){
                    fieldlabel = "创建人";
                }
                if("updated".equals(fieldname)){
                    fieldlabel = "更新时间";
                }
                if("published".equals(fieldname)){
                    fieldlabel = "创建时间";
                }
                if("approvalstatus".equals(fieldname)){
                    fieldlabel = "状态";
                }
                if("submitdatetime".equals(fieldname)){
                    fieldlabel = "提交审批时间";
                }
                if("approvaldatetime".equals(fieldname)){
                    fieldlabel = "审批时间";
                }
                if("finishapprover".equals(fieldname)){
                    fieldlabel = "审批人";
                }
            }else {
                fieldlabel = this.moduleFields.get(fieldname).fieldlabel;
            }
            Map<String,Object> info = new HashMap<>(1);
            info.put("fieldname", fieldname);
            info.put("fieldlabel", fieldlabel);
            search.add(info);
        }
        return search;
    }

    @ApiOperation("根据定义生成弹出窗口的表头")
    private List<Map<String,Object>> getPopupViewHeader(boolean multiselect) {
        List<Map<String,Object>> viewHeader = new ArrayList<>();
        if (multiselect) {
            Map<String,Object> info = new HashMap<>(1);
            info.put("type", "checkbox");
            info.put("fixed", "left");
            viewHeader.add(info);
        }
        String linkfield = this.dataEntity.entityNames.fieldname;
        List<String> viewFields = new ArrayList<>(this.dataEntity.popupDialog.columns);
        if (!linkfield.isEmpty() && !viewFields.contains(linkfield)) {
            viewFields.add(0, linkfield);
        }
        for (String item : viewFields) {
            if (!Utils.isEmpty(this.moduleFields.get(item))) {
                String label = this.moduleFields.get(item).fieldlabel;
                String fieldname = this.moduleFields.get(item).fieldname;
                Integer uitype = this.moduleFields.get(item).uitype;
                String align = this.moduleFields.get(item).align;
                Boolean isSort = this.moduleFields.get(item).issort;
                BaseEntityUtils that = this;
                Map<String,Object> info = new HashMap<>(1);
                info.put("field", item);
                info.put("title", label);
                info.put("align", align);
                info.put("sort", isSort);
                if (!multiselect) {
                    if (item.equals(linkfield)) {
                        info.put("templet", "<div><a class=\\'layui-table-link\\' onclick=\\'javascript:" + that.dataEntity.tabs.modulename + "_popup_link_click(\"{{ d.id }}\",\"{{ layui.form.encodeHtml(d." + linkfield + ") }}\");\\'>{{ layui.form.encodeHtml(d." + linkfield + ") }}</a></div>");
                    }
                }
                viewHeader.add(info);
            }else if(Arrays.asList("author", "published", "updated", "approvalstatus","submitdatetime","approvaldatetime","finishapprover").contains(item)){
                String label = "";
                if("author".equals(item)){
                    label = "创建人";
                }
                if("updated".equals(item)){
                    label = "更新时间";
                }
                if("published".equals(item)){
                    label = "创建时间";
                }
                if("approvalstatus".equals(item)){
                    label = "状态";
                }
                if("submitdatetime".equals(item)){
                    label = "提交审批时间";
                }
                if("approvaldatetime".equals(item)){
                    label = "审批时间";
                }
                if("finishapprover".equals(item)){
                    label = "审批人";
                }
                if(!Utils.isEmpty(label)) {
                    Map<String,Object> info = new HashMap<>(1);
                    info.put("field", item);
                    info.put("title", label);
                    info.put("align", "left");
                    info.put("sort", true);
                    viewHeader.add(info);
                }
            }
        }
        if(!viewFields.contains("published")){
            Map<String,Object> info = new HashMap<>(1);
            info.put("field", "published");
            info.put("title", "创建日期");
            info.put("align", "left");
            info.put("sort", true);
            info.put("hide",true);
            viewHeader.add(info);
        }
        return viewHeader;
    }

    @ApiOperation("根据定义生成弹出窗口的数据结构")
    public Object popupEntity(HttpServletRequest request, Map<String, Object> result) {
        if (Utils.isEmpty(result) || !(result.get("list") instanceof List)) {
            Map<String,Object> info = new HashMap<>(1);
            info.put("code", 0);
            info.put("msg", "");
            info.put("count", 0);
            info.put("data", new ArrayList<>(1));
            return info;
        } else {
            Map<String, Object> httpRequest = Utils.getRequestQuery(request);
            List<String> select = new ArrayList<>();
            if(!Utils.isEmpty(httpRequest.get("select"))){
                Object parm = httpRequest.get("select");
                if(parm instanceof String){
                    select = Collections.singletonList(parm.toString());
                }else if(parm instanceof List){
                    select.addAll((List<String>)parm);
                }
            }
            boolean multiselect = !Utils.isEmpty(httpRequest.get("multiselect")) && "true".equals(httpRequest.get("multiselect"));

            String linkfield = this.dataEntity.entityNames.fieldname;
            List<String> viewFields = new ArrayList<>(this.dataEntity.popupDialog.columns);
            if (!linkfield.isEmpty() && !viewFields.contains(linkfield)) {
                viewFields.add(0, linkfield);
            }
            if (!viewFields.contains("id")) {
                viewFields.add(0, "id");
            }
            if (!viewFields.contains("published")) {
                viewFields.add("published");
            }

            List<Object> records = new ArrayList<>();
            for (Object item : (List<?>) result.get("list")) {
                String id = "";
                try {
                    id = item.getClass().getField("id").get(item).toString();
                }catch (Exception ignored){ }
                Map<String, Object> record = new HashMap<>();
                for (String fieldname : viewFields) {
                    try {
                        Field field = item.getClass().getField(fieldname);
                        String value = formatByUiType(id,fieldname, field.get(item),true);
                        record.put(fieldname, value);
                        if(multiselect && linkfield.equals(fieldname) && select.contains(value)){
                            record.put("LAY_CHECKED", true);
                        }
                    } catch (Exception e) {
                        record.put(fieldname, "");
                    }
                }
                if(multiselect && select.contains(id)) {
                    record.put("LAY_CHECKED", true);
                }
                records.add(record);
            }
            Map<String,Object> info = new HashMap<>(1);
            info.put("code", 0);
            info.put("count", result.get("total"));
            info.put("data", records);
            return info;
        }
    }

    @ApiOperation("根据定义生成弹出窗口")
    public Object popupTreeView(Map<String, Object> httpRequest, Model model) {
        return popupTreeView(httpRequest,model, "PopupTreeView");
    }
    @ApiOperation("根据定义生成弹出窗口")
    public Object popupTreeView(Map<String, Object> httpRequest, Model model,String viewPath) {
        try {
            if (!Utils.isEmpty(httpRequest.get("multiselect")) && "true".equals(httpRequest.get("multiselect"))) {
                model.addAttribute("MULTISELECT", true);
            }
            if (!Utils.isEmpty(httpRequest.get("exclude"))) {
                if (httpRequest.get("exclude") instanceof String) {
                    model.addAttribute("EXCLUDE", Utils.objectToJson(Collections.singletonList(httpRequest.get("exclude"))));
                } else {
                    model.addAttribute("EXCLUDE", Utils.objectToJson(httpRequest.get("exclude")));
                }
            }
            if (!Utils.isEmpty(httpRequest.get("select"))) {
                if (httpRequest.get("select") instanceof String) {
                    model.addAttribute("SELECT", Utils.objectToJson(Collections.singletonList(httpRequest.get("select"))));
                } else {
                    model.addAttribute("SELECT", Utils.objectToJson(httpRequest.get("select")));
                }
            }
            if (!Utils.isEmpty(httpRequest.get("digheight"))) {
                String digheight = httpRequest.get("digheight").toString();
                if (digheight.contains("px")) {
                    digheight = digheight.replace("px", "");
                    model.addAttribute("DIGHEIGHT", Double.valueOf(digheight).intValue() - 110);
                } else {
                    model.addAttribute("DIGHEIGHT", "390");
                }
            }
            return WebViews.view(viewPath);
        } catch (Exception e) {
            return WebViews.view("error/403");
        }
    }
    @ApiOperation("自定义弹窗编辑页面")
    public Object popupCustomEditView(List<TabField> fields, Map<String,Object> picklists,Model model,String record) {
        return popupCustomEditView(fields, picklists, model, record, true,1);
    }
    @ApiOperation("自定义弹窗编辑页面")
    public Object popupCustomEditView(List<TabField> fields, Map<String,Object> picklists,Model model,String record,Boolean checkModify) {
        return popupCustomEditView(fields, picklists, model, record, checkModify,1);
    }

    @ApiOperation("纯自定义弹窗编辑页面")
    public Object popupCustomEditView(List<TabField> fields, Map<String,Object> picklists,Model model,String record,Boolean checkModify,Integer tableColumn) {
        model.addAttribute("MODULE", this.getModuleName());
        model.addAttribute("RECORD", record);
        List<Object> fieldEntitys = getcustomEditViewEntity(fields);
        if (!Utils.isEmpty(picklists)) {
            for(Object entity: fieldEntitys){
                Map<String, Object> info = (Map<String, Object>)entity;
                String fieldname = info.get("name").toString();
                HashMap<String, Object> value = (HashMap<String, Object>)info.get("value");
                if (picklists.containsKey(fieldname)) {
                    value.put("3",picklists.get(fieldname));
                }
            }
        }
        model.addAttribute("FIELDS", fieldEntitys);
        model.addAttribute("EDITVIEWHASSCRIPT",false);
        model.addAttribute("CHECKMODIFY",checkModify);
        if (tableColumn > 3) {
            tableColumn = 3;
        }
        model.addAttribute("TABLECOLUMN",tableColumn);
        if (isExistResource("views/"+this.getModuleName()+"/customEditview.js")) {
            model.addAttribute("EDITVIEWHASSCRIPT",true);
        }
        return WebViews.view("PopupCustomEditView");
    }

    @ApiOperation("根据定义生成弹窗编辑页面")
    public Object popupEditView(Map<String, Object> httpRequest, Model model, Object recordInfo) {
        return popupEditView(httpRequest, model, recordInfo, "PopupEditView");
    }

    @ApiOperation("根据定义生成批量弹窗编辑页面")
    public Object batchEditView(Map<String, Object> httpRequest, Model model, Object recordInfo,String ids) {
        return batchEditView(httpRequest, model, recordInfo, "PopupEditView",ids);
    }
    private Object batchEditView(Map<String, Object> httpRequest, Model model, Object recordInfo, String viewPath,String ids) {
        try {
            List<Object> blocks = this.getEditViewEntity(recordInfo);
            customEditViewEntityCallback(blocks,recordInfo);
            model.addAttribute("MODULE", this.getModuleName());
            model.addAttribute("ISBATCH", true);
            model.addAttribute("BLOCKS", Collections.singletonList(blocks.get(0)));

            List<String> popupEditViewFields = new ArrayList<>();
            try{
                Object methodresult = CallbackUtils.invoke("getBatchEditViewFields", entityPackageName, IBaseService.class);
                if (!Utils.isEmpty(methodresult)) {
                    popupEditViewFields = (List<String>)methodresult;
                }
            }catch (Exception ignored) { }
            model.addAttribute("PopupEditViewFields", popupEditViewFields);
            if(!model.containsAttribute("TABNAME")) {
                model.addAttribute("TABNAME", ((BaseRecordConfig) recordInfo).getEditState() ? this.getModuleLabel() + "详情" : "新建" + this.getModuleLabel());
            }
            model.addAttribute("ISEDIT", ((BaseRecordConfig) recordInfo).getEditState());
            model.addAttribute("RECORD", ids);

            model.addAttribute("READONLY",false);
            model.addAttribute("REQUIRELEFT",this.dataEntity.tabs.requireleft);
            model.addAttribute("SAVEISCLOSEEDITVIEW",this.dataEntity.tabs.saveiscloseeditview);
            model.addAttribute("BLOCKCARD",this.dataEntity.tabs.blockcard);
            model.addAttribute("EDITVIEWHASSCRIPT",false);

            if (isExistResource("views/"+this.getModuleName()+"/editview.js")) {
                model.addAttribute("EDITVIEWHASSCRIPT",true);
            }
            return WebViews.view(viewPath);
        } catch (Exception e) {
            return WebViews.view("error/403");
        }
    }

    private Object popupEditView(Map<String, Object> httpRequest, Model model, Object recordInfo, String viewPath) {
        try {
            List<Object> blocks = this.getEditViewEntity(recordInfo);
            customEditViewEntityCallback(blocks,recordInfo);
            model.addAttribute("ISBATCH", false);
            model.addAttribute("MODULE", this.getModuleName());
            model.addAttribute("BLOCKS", blocks);

            List<String> popupEditViewFields = new ArrayList<>();
            try{
                Object methodresult = CallbackUtils.invoke("getPopupEditViewFields", entityPackageName, IBaseService.class);
                if (!Utils.isEmpty(methodresult)) {
                    popupEditViewFields = (List<String>)methodresult;
                }
            }catch (Exception ignored) { }
            model.addAttribute("TABLECOLUMN",1);
            try{
                Object result = CallbackUtils.invoke("getPopupEditViewColumnSetting", entityPackageName, IBaseService.class);
                if (!Utils.isEmpty(result)) {
                    Integer tableColumn = (Integer)result;
                    if (tableColumn > 3) {
                        tableColumn = 3;
                    }
                    model.addAttribute("TABLECOLUMN",tableColumn);
                }
            }catch (Exception ignored) { }
            model.addAttribute("PopupEditViewFields", popupEditViewFields);
            if(!model.containsAttribute("TABNAME")) {
                model.addAttribute("TABNAME", ((BaseRecordConfig) recordInfo).getEditState() ? this.getModuleLabel() + "详情" : "新建" + this.getModuleLabel());
            }
            model.addAttribute("ISEDIT", ((BaseRecordConfig) recordInfo).getEditState());
            model.addAttribute("RECORD", ((BaseRecordConfig) recordInfo).id);

            model.addAttribute("READONLY",false);
            model.addAttribute("REQUIRELEFT",this.dataEntity.tabs.requireleft);
            model.addAttribute("SAVEISCLOSEEDITVIEW",this.dataEntity.tabs.saveiscloseeditview);
            model.addAttribute("BLOCKCARD",this.dataEntity.tabs.blockcard);
            model.addAttribute("EDITVIEWHASSCRIPT",false);

            if (isExistResource("views/"+this.getModuleName()+"/editview.js")) {
                model.addAttribute("EDITVIEWHASSCRIPT",true);
            }
            return WebViews.view(viewPath);
        } catch (Exception e) {
            return WebViews.view("error/403");
        }
    }
    //endregion

    //region 树型封装
    @ApiOperation("根据定义生成树型界面")
    public Object treeView(HttpServletRequest request, Model model) {
        try {
            model.addAttribute("MODULE", this.getModuleName());
            return WebViews.view("TreeView");
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }
    @ApiOperation("根据定义生成树型界面")
    public Object treeViewEdit(Map<String, Object> httpRequest, Model model, Object recordInfo) {
        try {
            model.addAttribute("MODULE", this.getModuleName());
            model.addAttribute("BLOCKS", this.getEditViewEntity(recordInfo));
            return WebViews.view("TreeEdit");
        }catch (Exception e) {
            return WebViews.view("error/403");
        }
    }
    //endregion

    //region 格式化保存数据
    @ApiOperation("根据定义格式化保存数据")
    public void formatRequest(Map<String, Object> httpRequest) {
        for (String fieldname : this.moduleFields.keySet()) {
            TabField field = this.moduleFields.get(fieldname);
            if (field.uitype == 27) {
                Integer ratio = field.ratio;
                if(ratio <= 0) {
                    ratio = 1;
                }
                if (httpRequest.containsKey(fieldname)) {
                    String value = httpRequest.get(fieldname).toString();
                    try {
                        BigDecimal bc = new BigDecimal(value);
                        value = bc.multiply(BigDecimal.valueOf(ratio)).toString();
                        httpRequest.put(fieldname, value);
                    } catch (Exception e) {
                        httpRequest.put(fieldname, "");
                    }
                } else {
                    httpRequest.put(fieldname, "");
                }
            } else if (field.uitype == 32 && field.isarray) {
                if (httpRequest.containsKey(fieldname)) {
                    String value = httpRequest.get(fieldname).toString();
                    List<String> lists = Arrays.asList(value.split(","));
                    httpRequest.put(fieldname, lists);
                } else {
                    httpRequest.put(fieldname,new ArrayList<>());
                }
            } else if (field.uitype == 15 || field.uitype == 16) {
                if (!httpRequest.containsKey(fieldname)) {
                    httpRequest.put(fieldname,new ArrayList<>());
                }
            }
        }
    }
    //endregion

    //region流程封装
    @ApiModelProperty("获取流程当前环节信息")
    protected FlowTaskSettings getFlowTaskSettings(Object recordInfo) {
        FlowTaskSettings flowTaskSettings = new FlowTaskSettings();
        String record = "",author = "";
        try {
            Field field = recordInfo.getClass().getField("id");
            record = field.get(recordInfo).toString();
            field = recordInfo.getClass().getField("author");
            author = field.get(recordInfo).toString();
            if(Utils.isEmpty(this.dataEntity.layouts)) {
                this.dataEntity.layouts = this.getLayout(this.dataEntity.fields);
            }
            flowTaskSettings = getFlowTaskSettings(record,this.dataEntity.layouts,this.dataEntity.fields,this.outsideLinks,author,recordInfo);
        } catch (Exception ignored) { }
        return flowTaskSettings;
    }
    @SuppressWarnings("unchecked")
    protected FlowTaskSettings getFlowTaskSettings(String record,List<Layout> layouts, List<TabField> fields,Map<String,OutsideLink> outsideLinks, String author,Object recordInfo) {
        FlowTaskSettings flowTaskSettings = new FlowTaskSettings();
        if(this.workflowStatus.isDefineflow() && !this.workflowStatus.isSuspend()){
            boolean isEdit = !this.workflowStatus.isStartflow() || this.workflowStatus.isDealwith() && !this.workflowStatus.isFinished();
            boolean isStartuser = (!this.workflowStatus.isStartflow() || (this.workflowStatus.isDealwith() && this.workflowStatus.isFirstNode())) && author.equals(ProfileUtils.getCurrentProfileId());
            List<Layout> tmpLayouts = new ArrayList<>();
            int maxColumns = 2;
            for(Layout layout: layouts){
                if(maxColumns < layout.columns.size()) {
                    maxColumns = layout.columns.size();
                }
                tmpLayouts.add(layout.clone());
            }
            tmpLayouts.addAll(0,getFlowlayouts(maxColumns));
            flowTaskSettings.setLayouts(tmpLayouts);

            Map<String,OutsideLink> tmpOutsideLinks = new HashMap<>();
            for(String item: outsideLinks.keySet()){
                tmpOutsideLinks.put(item,outsideLinks.get(item).clone());
            }
            flowTaskSettings.setOutsideLinks(tmpOutsideLinks);
            List<TabField> flowFields = new ArrayList<>();
            Object flowinfo = WorkflowUtils.getWorkflowFormSettings(this.getModuleName(),record);
            if(Utils.isNotEmpty(flowinfo)) {
                outsideLinks.put("activiti_process", new OutsideLink().fieldname("activiti_process").relmodule("workflowmanage").serviceclass(((Map<?, String>) flowinfo).getOrDefault("ServiceClass", "")));
                if (isEdit) {
                    String flowId = Utils.isNotEmpty(((Map<?, ?>) flowinfo).getOrDefault("FlowID", null)) ? ((Map<?, ?>) flowinfo).get("FlowID").toString() : "";
                    if (isStartuser && !Objects.equals(ClassUtils.getValue(recordInfo, "activiti_process"), flowId)) {
                        try {
                            ClassUtils.setValue(recordInfo, "activiti_process", flowId);
                        } catch (Exception ignored) {
                        }
                    }
                    flowFields.add(new TabField().fieldname("activiti_processtitle").fieldlabel("流程标题").typeofdata("NS~M").readonly(!isStartuser).setFlowReadonly(!isStartuser));
                    flowFields.add(new TabField().fieldname("activiti_process").fieldlabel("所属流程").uitype(11).defaultvalue(flowId).readonly(true));
                    flowFields.add(new TabField().fieldname("activiti_urgencydegree").fieldlabel("紧急程度").uitype(14).picklist("urgencydegree").defaultvalue("0").readonly(!isStartuser).setFlowReadonly(!isStartuser));
                    flowFields.add(new TabField().fieldname("activiti_initiatetime").fieldlabel("发起时间").uitype(7).readonly(true).defaultvalue(DateTimeUtils.getDatetime(DateTimeUtils.DATE_SHORT_TIME_FORMAT)));
                    flowFields.add(new TabField().fieldname("activiti_divide0").fieldlabel("").uitype(22).show_title(false));
                    if (((Map<?, ?>) flowinfo).containsKey("FormSettings")) {
                        Map<String, Object> mapPropertys = (Map<String, Object>) ((Map<String, Object>) flowinfo).get("FormSettings");
                        for (TabField field : fields) {
                            TabField flowField = field.clone();
                            if (mapPropertys.containsKey(flowField.fieldname)) {
                                Map<String, Object> settings = (Map<String, Object>) mapPropertys.get(flowField.fieldname);
                                if (!Utils.isEmpty(settings.get("defaultvalue"))) {
                                    String defaultvalue = settings.get("defaultvalue").toString();
                                    String dataValue = "";
                                    switch (defaultvalue) {
                                        //当前用户
                                        case "${CurrentProfile}":
                                            if (flowField.uitype == 25) {
                                                dataValue = ProfileUtils.getCurrentProfileId();
                                            } else {
                                                dataValue = ProfileUtils.getCurrentProfileName();
                                            }
                                            break;
                                        //当前部门
                                        case "${CurrentDepartment}":
                                            if (flowField.uitype == 10) {
                                                dataValue = DepartmentUtils.getCurrentDepartmentId();
                                            } else {
                                                dataValue = DepartmentUtils.getCurrentDepartmentName();
                                            }
                                            break;
                                        //当前日期
                                        case "${CurrentDate}":
                                            if (Arrays.asList(1, 2, 6, 24).contains(field.uitype)) {
                                                dataValue = DateTimeUtils.getDatetime("yyyy-MM-dd");
                                            }
                                            break;
                                        //当前时间
                                        case "${CurrentTime}":
                                            if (Arrays.asList(1, 2, 8, 24).contains(field.uitype)) {
                                                dataValue = DateTimeUtils.getDatetime("HH:mm:ss");
                                            }
                                            break;
                                        //当前日期时间
                                        case "${CurrentDatetime}":
                                            if (Arrays.asList(1, 2, 7, 24).contains(field.uitype)) {
                                                dataValue = DateTimeUtils.getDatetime("yyyy-MM-dd HH:mm");
                                            }
                                            break;
                                        default:
                                            dataValue = settings.get("defaultvalue").toString();
                                            break;
                                    }
                                    flowField.setFlowvalue(dataValue);
                                }
                                if (!Utils.isEmpty(settings.get("ishide")) && (Boolean) settings.get("ishide")) {
                                    flowField.displaytype(2);
                                }
                                if (!Utils.isEmpty(settings.get("isreadonly")) && (Boolean) settings.get("isreadonly")) {
                                    flowField.readonly((Boolean) settings.get("isreadonly"));
                                    flowField.displaytype(0);
                                    String[] typeofdata = flowField.typeofdata.split("~");
                                    typeofdata[1] = "O";
                                    flowField.typeofdata(StringUtils.join(typeofdata, "~"));
                                }
                                if (!Utils.isEmpty(settings.get("isedit")) && (Boolean) settings.get("isedit")) {
                                    flowField.setFlowReadonly(false);
                                    flowField.displaytype(0);
                                    String[] typeofdata = flowField.typeofdata.split("~");
                                    typeofdata[1] = "O";
                                    flowField.typeofdata(StringUtils.join(typeofdata, "~"));
                                }
                                if (!Utils.isEmpty(settings.get("isrequire")) && (Boolean) settings.get("isrequire")) {
                                    flowField.setFlowReadonly(false);
                                    flowField.displaytype(0);
                                    String[] typeofdata = flowField.typeofdata.split("~");
                                    typeofdata[1] = "M";
                                    flowField.typeofdata(StringUtils.join(typeofdata, "~"));
                                }
                            } else {
                                flowField.setFlowReadonly(true);
                            }
                            flowFields.add(flowField);
                        }
                    }
                } else {
                    flowFields.add(new TabField().fieldname("activiti_processtitle").fieldlabel("流程标题").typeofdata("NS~M").readonly(true));
                    flowFields.add(new TabField().fieldname("activiti_process").fieldlabel("所属流程").uitype(11).readonly(true));
                    flowFields.add(new TabField().fieldname("activiti_urgencydegree").fieldlabel("紧急程度").uitype(14).picklist("urgencydegree").defaultvalue("0").readonly(true));
                    flowFields.add(new TabField().fieldname("activiti_initiatetime").fieldlabel("发起时间").uitype(7).readonly(true).defaultvalue(DateTimeUtils.getDatetime(DateTimeUtils.DATE_SHORT_TIME_FORMAT)));
                    flowFields.add(new TabField().fieldname("activiti_divide0").fieldlabel("").uitype(22).show_title(false));
                    for (TabField field : fields) {
                        TabField flowField = field.clone();
                        flowField.setFlowReadonly(true);
                        flowFields.add(flowField);
                    }
                }
            }
            flowTaskSettings.setFields(flowFields);
        } else {
            flowTaskSettings.setLayouts(layouts).setFields(fields).setOutsideLinks(outsideLinks);
        }
        return flowTaskSettings;
    }

    private List<Layout> getFlowlayouts(int maxColumns){
        List<String> defields = Arrays.asList("activiti_processtitle","activiti_process","activiti_urgencydegree","activiti_initiatetime");
        List<Layout> layouts = new ArrayList<>();
        layouts.add(new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("activiti_currentnode","")));
        boolean isnot = false;
        int index = 0;
        do {
            List<String> fields = new ArrayList<>();
            List<String> columns = new ArrayList<>();
            int c = 0;
            for (int i = 0; i < maxColumns; i++) {
                if (defields.size() > index) {
                    fields.add(defields.get(index));
                } else {
                    isnot = true;
                    if(i==0) {
                        break;
                    }
                    fields.add("");
                }
                if (i == maxColumns - 1) {
                    columns.add(String.valueOf(12 - c));
                    layouts.add(new Layout().block(1).columns(columns).fields(fields));
                } else {
                    int j = 12 / maxColumns;
                    c += j;
                    columns.add(String.valueOf(j));
                }
                index++;
            }
        }while (!isnot);
        if(!layouts.isEmpty()) {
            layouts.add(new Layout().block(1).columns(Collections.singletonList("12")).fields(Collections.singletonList("activiti_divide0")));
        }
        return layouts;
    }

    //区块字段是否全部隐藏
    private Boolean blockFieldAllHide(List<Object> layout) {
        for(Object item: layout){
            if(item instanceof List){
                for(Object info: (List<?>)item){
                    if(info instanceof Map && Utils.isNotEmpty(((Map<?, ?>) info).get("field"))){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    //流程是否可写
    private Boolean getFlowReadonly(String record) {
        Object flowinfo = WorkflowUtils.getWorkflowFormSettings(this.getModuleName(),record);
        if(Utils.isNotEmpty(flowinfo)) {
            if (((Map<?, ?>) flowinfo).containsKey("FormSettings")) {
                Map<String, Object> mapPropertys = (Map<String, Object>) ((Map<String, Object>) flowinfo).get("FormSettings");
                for(String fieldname: mapPropertys.keySet()){
                    Map<String, Object> settings = (Map<String, Object>) mapPropertys.get(fieldname);
                        if (!Utils.isEmpty(settings.get("isedit")) && (Boolean) settings.get("isedit")) {
                            return false;
                        }
                        if (!Utils.isEmpty(settings.get("isrequire")) && (Boolean) settings.get("isrequire")) {
                            return false;
                        }
                    }
            }
        }
        return true;
    }
    //endregion
}
