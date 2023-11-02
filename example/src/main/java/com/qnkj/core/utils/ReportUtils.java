package com.qnkj.core.utils;

import com.github.restapi.XN_Profile;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.github.restapi.models.Profile;
import com.qnkj.common.entitys.TabField;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.ExpressionUtils;
import com.qnkj.common.utils.MathUtils;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.BaseEntityUtils;
import com.qnkj.core.base.entitys.ReportParam;
import com.qnkj.core.base.entitys.ReportResult;
import com.qnkj.core.base.modules.lcdp.pagedesign.Util.PageDesignUtils;
import com.qnkj.core.base.modules.lcdp.pagedesign.entitys.PageDesignField;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.webconfigs.exception.WebException;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**

 */

@Slf4j
public class ReportUtils {

    public static List<ReportResult> fillReportResult(List<ReportResult> reportResult,String startDate,String endDate,String timeFormat) throws Exception {
        List<String> timeFormats = Arrays.asList("monthday","day","week","yearmonth","month","yearquarter","quarter","year");

        if (Utils.isNotEmpty(startDate) && Utils.isNotEmpty(endDate) && timeFormats.contains(timeFormat)) {
            Date start_date = DateTimeUtils.string2date(startDate);
            Date end_date = DateTimeUtils.string2date(endDate);
            List<String> xAxis = new ArrayList<>();
            if ("monthday".equals(timeFormat)) {
                int dayLength =  (int)DateTimeUtils.diff("day", end_date, start_date);
                SimpleDateFormat monthDayFormat = new SimpleDateFormat("MM-dd");
                xAxis.add(monthDayFormat.format(start_date));
                for(int i=0;i<dayLength;i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(start_date);
                    calendar.add(Calendar.DATE, i+1);
                    xAxis.add(monthDayFormat.format(calendar.getTime()));
                }
            } else if ("day".equals(timeFormat)) {
                int dayLength =  (int)DateTimeUtils.diff("day", end_date, start_date);
                SimpleDateFormat monthDayFormat = new SimpleDateFormat("d日");
                xAxis.add(monthDayFormat.format(start_date));
                for(int i=0;i<dayLength;i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(start_date);
                    calendar.add(Calendar.DATE, i+1);
                    xAxis.add(monthDayFormat.format(calendar.getTime()));
                }
            }else if ("yearmonth".equals(timeFormat)) {
                int dayLength =  (int) DateTimeUtils.diff("month", end_date, start_date);
                SimpleDateFormat monthDayFormat = new SimpleDateFormat("yyyy-MM");
                xAxis.add(DateTimeUtils.getDatetime(start_date,"yyyy-MM"));
                for(int i=0;i<dayLength;i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(start_date);
                    calendar.add(Calendar.MONTH, i+1);
                    xAxis.add(monthDayFormat.format(calendar.getTime()));
                }
            } else if ("month".equals(timeFormat)) {
                int dayLength =  (int)DateTimeUtils.diff("month", end_date, start_date);
                SimpleDateFormat monthDayFormat = new SimpleDateFormat("M月");
                xAxis.add(DateTimeUtils.getDatetime(start_date,"M月"));
                for(int i=0;i<dayLength;i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(start_date);
                    calendar.add(Calendar.MONTH, i+1);
                    xAxis.add(monthDayFormat.format(calendar.getTime()));
                }
            } else if ("year".equals(timeFormat)) {
                int dayLength =  (int)DateTimeUtils.diff("year", end_date, start_date);
                SimpleDateFormat monthDayFormat = new SimpleDateFormat("yyyy年");
                xAxis.add(DateTimeUtils.getDatetime(start_date,"yyyy年"));
                for(int i=0;i<dayLength;i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(start_date);
                    calendar.add(Calendar.YEAR, i+1);
                    xAxis.add(monthDayFormat.format(calendar.getTime()));
                }
            } else if ("yearquarter".equals(timeFormat)) {
                int dayLength =  (int) DateTimeUtils.diff("quarter", end_date, start_date);
                SimpleDateFormat monthDayFormat = new SimpleDateFormat("yyyy");
                xAxis.add(DateTimeUtils.getDatetime(start_date,"yyyy") + "Q" + DateTimeUtils.getQuarter(start_date));
                for(int i=0;i<dayLength;i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(start_date);
                    calendar.add(Calendar.MONTH, (i + 1) * 3);
                    xAxis.add(monthDayFormat.format(calendar.getTime()) + "Q" + DateTimeUtils.getQuarter(calendar.getTime()) );
                }
            } else if ("quarter".equals(timeFormat)) {
                int dayLength =  (int)DateTimeUtils.diff("quarter", end_date, start_date);
                xAxis.add("Q" + DateTimeUtils.getQuarter(start_date));
                for(int i=0;i<dayLength;i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(start_date);
                    calendar.add(Calendar.MONTH, (i + 1) * 3);
                    xAxis.add("Q" + DateTimeUtils.getQuarter(calendar.getTime()) );
                }
            } else if ("week".equals(timeFormat)) {
                int dayLength =  (int)DateTimeUtils.diff("week", end_date, start_date);
                SimpleDateFormat monthDayFormat = new SimpleDateFormat("第w周");
                xAxis.add(DateTimeUtils.getDatetime(start_date,"第w周") );
                for(int i=0;i<dayLength;i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(start_date);
                    calendar.add(Calendar.DATE, (i + 1) * 7);
                    xAxis.add(monthDayFormat.format(calendar.getTime()));
                }
            } else {
                return reportResult;
            }
            return reportResult.stream().peek(v -> {
                Map<String, Object> maps = v.getMaps().stream().collect(Collectors.toMap(v1 -> v1.get("name").toString(), v1 -> v1.get("value"), (k1, k2) -> k1));
                List<Map<String, Object>> fillMaps = xAxis.stream().map(v1 -> {
                    if (maps.containsKey(v1)) {
                        Map<String,Object> info = new HashMap<>(1);
                        info.put("name",v1);
                        info.put("value",maps.get(v1));
                        return info;
                    } else {
                        Map<String,Object> info = new HashMap<>(1);
                        info.put("name",v1);
                        info.put("value",0);
                        return info;
                    }
                }).collect(Collectors.toList());
                List<String> new_xAxis = fillMaps.stream().map(v1 -> v1.get("name").toString()).collect(Collectors.toList());
                List<Object> new_yAxis = fillMaps.stream().map(v1 -> v1.get("value")).collect(Collectors.toList());
                v.setMaps(fillMaps);
                v.setXAxis(new_xAxis);
                v.setYAxis(new_yAxis);
            }).collect(Collectors.toList());
        }
        return reportResult;
    }
    public static List<ReportResult> getModuleData(ReportParam report) throws Exception {
        String subject = report.getDataType() + "_Count";
        if (Utils.isEmpty(subject)) {
            throw new WebException("无法处理的数据类型");
        }
        if (Utils.isEmpty(report.getXDataLine())) {
            throw new WebException("没有设置X轴字段");
        }
        if (Utils.isEmpty(report.getYDataLine())) {
            throw new WebException("没有设置y轴字段");
        }
        if (!report.getFields().containsKey(report.getXDataLine())) {
            throw new WebException("X轴字段设置异常");
        }
        if (!"id".equals(report.getYDataLine()) && !report.getFields().containsKey(report.getYDataLine())) {
            throw new WebException("y轴字段设置异常");
        }
        final PageDesignField xDataField = report.getFields().get(report.getXDataLine());
        final PageDesignField yDataField = ("id".equals(report.getYDataLine()))?null:report.getFields().get(report.getYDataLine());

        XN_Query query = XN_Query.create(subject).tag(report.getTabName())
                .filter("type", "eic", report.getTabName())
                .notDelete().begin(0)
                .end(-1);

        dataRole(query,report.getDataRole());

        if (Utils.isNotEmpty(report.getStartDate())) {
            query.filter("published", ">=", report.getStartDate());
        }
        if (Utils.isNotEmpty(report.getEndDate())) {
            query.filter("published", "<=", report.getEndDate());
        }

        try {
            ExpressionUtils.executeQuery(report.getCondition(), query);
        }catch (Exception e) {
            log.error(e.getMessage());
        }

        if ("id".equals(report.getYDataLine())) {
            query.rollup();
            if ("descending".equals(report.getSort())) {
                query.order("my.count","desc");
            } else if ("ascending".equals(report.getSort())) {
                query.order("my.count","asc");
            }
        } else {
            if(yDataField != null) {
                query.rollup("my." + yDataField.getField());
                if ("descending".equals(report.getSort())) {
                    query.order("my." + yDataField.getField(), "desc");
                } else if ("ascending".equals(report.getSort())) {
                    query.order("my." + yDataField.getField(), "asc");
                }
            }
        }

        dataGroupField(query,xDataField,report.getTimeFormat(),report.getSort());

        List<Object> contentList = query.execute();
        if (contentList.isEmpty()) { return new ArrayList<>(); }
        List<Map<String, Object>> result = new ArrayList<>();
        for(Object item : contentList ) {
            Map<String,Object> info = new HashMap<>(1);
            Content content = (Content) item;
            if ("published".equals(report.getXDataLine())) {
                info.put("name", content.published);
            } else if ("author".equals(report.getXDataLine())) {
                info.put("name", content.author);
            } else {
                if (content.my.containsKey(report.getXDataLine())) {
                    info.put("name", content.my.get(report.getXDataLine()).toString());
                } else {
                    info.put("name", "");
                }
            }
            try {
                if ("id".equals(report.getYDataLine())) {
                    if (content.my.containsKey("count")) {
                        Integer value = Integer.parseInt(PageDesignUtils.getDigit(content.my.get("count").toString()));
                        if (report.getConvertMaps().containsKey("id") && Utils.isNotEmpty(report.getConvertMaps().get("id"))) {
                            info.put("value", MathUtils.divide(value,Integer.parseInt(report.getConvertMaps().get("id"))));
                        } else {
                            info.put("value", value);
                        }
                    } else {
                        info.put("value", 0);
                    }
                } else {
                    if (yDataField != null && content.my.containsKey(yDataField.getField())) {
                        Double value = Double.parseDouble(PageDesignUtils.getDigit(content.my.get(yDataField.getField()).toString()));
                        if (report.getConvertMaps().containsKey(yDataField.getField()) && Utils.isNotEmpty(report.getConvertMaps().get(yDataField.getField()))) {
                            info.put("value", MathUtils.divide(value,Integer.parseInt(report.getConvertMaps().get(yDataField.getField()))));
                        }else {
                            info.put("value", value);
                        }
                    } else {
                        info.put("value", 0);
                    }
                }
            }catch (Exception e) {
                info.put("value", 0);
            }
            result.add(info);
        }
        if ("descending".equals(report.getSort()) || "ascending".equals(report.getSort())) {
            if (report.getTopN() > 0 && result.size() > report.getTopN()) {
                result = result.subList(0,report.getTopN());
            }
        }
        if ("author".equals(xDataField.getField()) || "profile".equals(PageDesignUtils.getUiType(xDataField.getUitype()))) {
            List<String> ids = result.stream().map(v -> { return v.get("name").toString();}).collect(Collectors.toList());
            if (!ids.isEmpty()) {
                List<Object> profiles = XN_Profile.loadMany(ids,"profile");
                Map<String, String> maps = profiles.stream().collect(Collectors.toMap(v -> ((Profile)v).id, v -> ((Profile)v).givenname, (k1, k2) -> k1));
                result = result.stream().peek(v -> {
                    String name = v.get("name").toString();
                    v.put("name", maps.getOrDefault(name, "--"));
                }).collect(Collectors.toList());
            }
        } else if ("picklist".equals(PageDesignUtils.getUiType(xDataField.getUitype()))) {
            TabField tabfield = PageDesignUtils.getModuleFieldInfo(report.getModule(),xDataField.getField());
            if (tabfield != null) {
                String picklist = tabfield.fieldname;
                if (Utils.isNotEmpty(tabfield.picklist)) {
                    picklist = tabfield.picklist;
                }
                if (tabfield.uitype == 5) {
                    picklist = "yesno";
                }
                List<List<Object>> picklists = (List) PickListUtils.getPickList(picklist);
                if (!picklists.isEmpty()) {
                    Map<String, String> maps = picklists.stream().collect(Collectors.toMap(v -> v.get(0).toString(), v -> v.get(1).toString(), (k1, k2) -> k1));
                    Map<String, String> intMaps = picklists.stream().collect(Collectors.toMap(v -> v.get(2).toString(), v -> v.get(1).toString(), (k1, k2) -> k1));
                    result = result.stream().peek(v -> {
                        String name = v.get("name").toString();
                        if (maps.containsKey(name)) {
                            v.put("name", maps.get(name));
                        } else {
                            v.put("name", intMaps.getOrDefault(name, "--"));
                        }
                    }).collect(Collectors.toList());
                }
            } else {
                throw new WebException("获取字典失败");
            }
        } else if ("department".equals(PageDesignUtils.getUiType(xDataField.getUitype()))) {

        } else if ( "published".equals(report.getXDataLine()) || "datetime".equals(xDataField.getType())) {
            if (Boolean.TRUE.equals(report.getFillxAxis())) {
                result = transDateAxis(fillxAxis(result,report.getStartDate(),report.getEndDate(),report.getTimeFormat()), report.getTimeFormat());
            } else {
                result = transDateAxis(result, report.getTimeFormat());
            }
        } else if ( xDataField.getUitype() == 11) {
            result = transRelated(result,report.getModule(),xDataField.getField());
        }
        List<String> xAxis = result.stream().map(v -> v.get("name").toString()).collect(Collectors.toList());
        List<Object> yAxis = result.stream().map(v -> v.get("value")).collect(Collectors.toList());
        List<Map<String, Object>> finalResult = result;
        String yDataLineLabel = "";
        if ("id".equals(report.getYDataLine())) {
            if (report.getUnitMaps().containsKey("id") && Utils.isNotEmpty(report.getUnitMaps().get("id"))) {
                yDataLineLabel = "记录数" + "(" + report.getUnitMaps().get("id") +")";
            } else {
                yDataLineLabel = "记录数";
            }
        } else {
            if (yDataField != null) {
                if (report.getUnitMaps().containsKey(yDataField.getField()) && Utils.isNotEmpty(report.getUnitMaps().get(yDataField.getField()))) {
                    yDataLineLabel = yDataField.getLabel() + "(" + report.getUnitMaps().get(yDataField.getField()) + ")";
                } else {
                    yDataLineLabel = yDataField.getLabel();
                }
            }
        }
        String finalYDataLineLabel = yDataLineLabel;
        return new ArrayList<>(Collections.singletonList(
                new ReportResult(report.getYDataLine(), finalYDataLineLabel, Collections.singletonList(finalYDataLineLabel), xAxis, yAxis, finalResult)
        ));
    }

    public static List<ReportResult> getModuleDataHasZAxis(ReportParam report) throws Exception {
        String subject = report.getDataType() + "_Count";
        if (Utils.isEmpty(subject)) {
            throw new WebException("无法处理的数据类型");
        }
        if (!report.getFields().containsKey(report.getXDataLine())) {
            throw new WebException("X轴字段设置异常");
        }
        if (!"id".equals(report.getYDataLine()) && !report.getFields().containsKey(report.getYDataLine())) {
            throw new WebException("y轴字段设置异常");
        }
        final PageDesignField xDataField = report.getFields().get(report.getXDataLine());
        final PageDesignField zDataField = report.getFields().get(report.getZDataLine());
        final PageDesignField yDataField = ("id".equals(report.getYDataLine()))?null:report.getFields().get(report.getYDataLine());

        XN_Query query = XN_Query.create(subject).tag(report.getTabName())
                .filter("type", "eic", report.getTabName())
                .notDelete().begin(0)
                .end(-1);

        dataRole(query,report.getDataRole());

        if (Utils.isNotEmpty(report.getStartDate())) {
            query.filter("published", ">=", report.getStartDate());
        }
        if (Utils.isNotEmpty(report.getEndDate())) {
            query.filter("published", "<=", report.getEndDate());
        }

        try {
            ExpressionUtils.executeQuery(report.getCondition(), query);
        }catch (Exception e) {
            log.error(e.getMessage());
        }

        if ("id".equals(report.getYDataLine())) {
            query.rollup();
            if ("descending".equals(report.getSort())) {
                query.order("my.count","desc");
            } else if ("ascending".equals(report.getSort())) {
                query.order("my.count","asc");
            }
        } else if(yDataField != null) {
            query.rollup("my." + yDataField.getField());
            if ("descending".equals(report.getSort())) {
                query.order("my." + yDataField.getField(),"desc");
            } else if ("ascending".equals(report.getSort())) {
                query.order("my." + yDataField.getField(),"asc");
            }
        }
        dataGroupField(query,xDataField,report.getTimeFormat(),report.getSort());

        query.group("my." + zDataField.getField());

        List<Object> contentList = query.execute();
        if (contentList.isEmpty()) { return new ArrayList<>(); }
        List<Map<String, Object>> result = new ArrayList<>();
        for(Object item : contentList ) {
            Map<String,Object> info = new HashMap<>(1);
            Content content = (Content) item;
            if ("published".equals(report.getXDataLine())) {
                info.put("name", content.published);
            } else if ("author".equals(report.getXDataLine())) {
                info.put("name", content.author);
            } else {
                if (content.my.containsKey(report.getXDataLine())) {
                    info.put("name", content.my.get(report.getXDataLine()).toString());
                } else {
                    info.put("name", "");
                }
            }
            if ("published".equals(report.getZDataLine())) {
                info.put("z", content.published);
            } else if ("author".equals(report.getZDataLine())) {
                info.put("z", content.author);
            } else {
                if (content.my.containsKey(report.getZDataLine())) {
                    info.put("z", content.my.get(report.getZDataLine()).toString());
                } else {
                    info.put("z", "");
                }
            }
            try {
                if ("id".equals(report.getYDataLine()) || (yDataField != null && "string".equals(yDataField.getType()))) {
                    Integer value = Integer.parseInt(PageDesignUtils.getDigit(content.my.get("count").toString()));
                    if (report.getConvertMaps().containsKey("id") && Utils.isNotEmpty(report.getConvertMaps().get("id"))) {
                        info.put("value", MathUtils.divide(value,Integer.parseInt(report.getConvertMaps().get("id"))));
                    } else {
                        info.put("value", value);
                    }
                } else {
                    if (yDataField != null && content.my.containsKey(yDataField.getField())) {
                        Double value = Double.parseDouble(PageDesignUtils.getDigit(content.my.get(yDataField.getField()).toString()));
                        if (report.getConvertMaps().containsKey(yDataField.getField()) && Utils.isNotEmpty(report.getConvertMaps().get(yDataField.getField()))) {
                            info.put("value", MathUtils.divide(value,Integer.parseInt(report.getConvertMaps().get(yDataField.getField()))));
                        }else {
                            info.put("value", value);
                        }
                    } else {
                        info.put("value", 0);
                    }
                }
            }catch (Exception e) {
                info.put("value", 0);
            }
            result.add(info);
        }
        if ("author".equals(xDataField.getField()) || "profile".equals(PageDesignUtils.getUiType(xDataField.getUitype()))) {
            List<String> ids = result.stream().map(v -> { return v.get("name").toString();}).collect(Collectors.toList());
            if (!ids.isEmpty()) {
                List<Object> profiles = XN_Profile.loadMany(ids, "profile");
                Map<String, String> maps = profiles.stream().collect(Collectors.toMap(v -> ((Profile) v).id, v -> ((Profile) v).givenname, (k1, k2) -> k1));
                result = result.stream().peek(v -> {
                    String name = v.get("name").toString();
                    v.put("name", maps.getOrDefault(name, "--"));
                }).collect(Collectors.toList());
            }

        } else if ("picklist".equals(PageDesignUtils.getUiType(xDataField.getUitype()))) {
            TabField tabfield = PageDesignUtils.getModuleFieldInfo(report.getModule(),xDataField.getField());
            if(tabfield != null) {
                String picklist = tabfield.fieldname;
                if (Utils.isNotEmpty(tabfield.picklist)) {
                    picklist = tabfield.picklist;
                }
                if (tabfield.uitype == 5) {
                    picklist = "yesno";
                }
                List<List<Object>> picklists = (List) PickListUtils.getPickList(picklist);
                if (!picklists.isEmpty()) {
                    Map<String, String> maps = picklists.stream().collect(Collectors.toMap(v -> v.get(0).toString(), v -> v.get(1).toString(), (k1, k2) -> k1));
                    Map<String, String> intMaps = picklists.stream().collect(Collectors.toMap(v -> v.get(2).toString(), v -> v.get(1).toString(), (k1, k2) -> k1));
                    result = result.stream().map(v -> {
                        String name = v.get("name").toString();
                        if (maps.containsKey(name)) {
                            v.put("name", maps.get(name));
                        } else {
                            v.put("name", intMaps.getOrDefault(name, "--"));
                        }
                        return v;
                    }).collect(Collectors.toList());
                }
            } else {
                throw new WebException("获取字典失败");
            }
        } else if ("department".equals(PageDesignUtils.getUiType(xDataField.getUitype()))) {

        } else if ( xDataField.getUitype() == 11) {
            result = transRelated(result,report.getModule(),xDataField.getField());
        }
        if ("author".equals(zDataField.getField()) || "profile".equals(PageDesignUtils.getUiType(zDataField.getUitype()))) {
            List<String> ids = result.stream().map(v -> { return v.get("z").toString();}).collect(Collectors.toList());
            if (!ids.isEmpty()) {
                List<Object> profiles = XN_Profile.loadMany(ids, "profile");
                Map<String, String> maps = profiles.stream().collect(Collectors.toMap(v -> ((Profile) v).id, v -> ((Profile) v).givenname, (k1, k2) -> k1));
                result = result.stream().map(v -> {
                    String name = v.get("z").toString();
                    if (maps.containsKey(name)) {
                        v.put("z", maps.get(name));
                    } else {
                        v.put("name", "--");
                    }
                    return v;
                }).collect(Collectors.toList());
            }

        } else if ("picklist".equals(PageDesignUtils.getUiType(zDataField.getUitype()))) {
            TabField tabfield = PageDesignUtils.getModuleFieldInfo(report.getModule(),zDataField.getField());
            if(tabfield != null) {
                String picklist = tabfield.fieldname;
                if (Utils.isNotEmpty(tabfield.picklist)) {
                    picklist = tabfield.picklist;
                }
                if (tabfield.uitype == 5) {
                    picklist = "yesno";
                }
                List<List<Object>> picklists = (List) PickListUtils.getPickList(picklist);
                if (!picklists.isEmpty()) {
                    Map<String, String> maps = picklists.stream().collect(Collectors.toMap(v -> v.get(0).toString(), v -> v.get(1).toString(), (k1, k2) -> k1));
                    Map<String, String> intMaps = picklists.stream().collect(Collectors.toMap(v -> v.get(2).toString(), v -> v.get(1).toString(), (k1, k2) -> k1));
                    result = result.stream().peek(v -> {
                        String name = v.get("z").toString();
                        if (maps.containsKey(name)) {
                            v.put("z", maps.get(name));
                        } else {
                            v.put("z",intMaps.getOrDefault(name,"--"));
                        }
                    }).collect(Collectors.toList());
                }
            } else {
                throw new WebException("获取字典失败");
            }
        } else if ("department".equals(PageDesignUtils.getUiType(zDataField.getUitype()))) {

        } else if ( zDataField.getUitype() == 11) {
            result = transZRelated(result,report.getModule(),zDataField.getField());
        }
        final Map<String, Object> maps = result.stream().collect(Collectors.toMap(v -> {
            String name = v.get("name").toString();
            String z = v.get("z").toString();
            return name + "_" + z;
        }, v -> v.get("value"), (k1, k2) -> k1));

        List<String> xAxis = result.stream().map(v -> v.get("name").toString()).distinct().collect(Collectors.toList());
        if ("descending".equals(report.getSort()) || "ascending".equals(report.getSort())) {
            if (report.getTopN() > 0 && xAxis.size() > report.getTopN()) {
                xAxis = xAxis.subList(0,report.getTopN());
            }
        }
        if (Boolean.TRUE.equals(report.getFillxAxis())) {
            xAxis = fillxAxisByName(xAxis,report.getStartDate(),report.getEndDate(),report.getTimeFormat());
        }
        String yDataLineLabel = "";
        if ("id".equals(report.getYDataLine())) {
            if (report.getUnitMaps().containsKey("id") && Utils.isNotEmpty(report.getUnitMaps().get("id"))) {
                yDataLineLabel = "(" + report.getUnitMaps().get("id") +")";
            }
        } else {
            if (yDataField != null && report.getUnitMaps().containsKey(yDataField.getField()) && Utils.isNotEmpty(report.getUnitMaps().get(yDataField.getField()))) {
                yDataLineLabel = "(" + report.getUnitMaps().get(yDataField.getField()) +")";
            }
        }
        String finalYDataLineLabel = yDataLineLabel;
        List<String> zAxis = result.stream().map(v -> v.get("z").toString()).distinct().collect(Collectors.toList());
        List<String> zAxisLabels = result.stream().map(v -> {
            String label = v.get("z").toString() + finalYDataLineLabel;
            if (Utils.isNotEmpty(label)) {
                return label;
            }
            return "--";
        }).distinct().collect(Collectors.toList());
        List<String> finalXAxis = xAxis;
        final List<String> xAxisLabel = transDateAxisByName(xAxis,report.getTimeFormat());
        return zAxis.stream().map(z ->  {
            List<Object> yAxis = finalXAxis.stream().map(v -> {
                String key = v + "_" + z;
                return maps.getOrDefault(key, 0);
            }).collect(Collectors.toList());
            String label = z + finalYDataLineLabel;
            if (Utils.isNotEmpty(label)) {
                return new ReportResult(z, z + finalYDataLineLabel, xAxisLabel, yAxis, zAxisLabels);
            } else {
                return new ReportResult("--", "--", xAxisLabel, yAxis, zAxisLabels);
            }
        }).collect(Collectors.toList());
    }

    public static List<ReportResult> getModuleDataHasMoreYAxis(ReportParam report) throws Exception {
        String subject = report.getDataType() + "_Count";
        if (Utils.isEmpty(subject)) {
            throw new WebException("无法处理的数据类型");
        }
        if (!report.getFields().containsKey(report.getXDataLine())) {
            throw new WebException("X轴字段设置异常");
        }
        if (!"id".equals(report.getYDataLine()) && !report.getFields().containsKey(report.getYDataLine())) {
            throw new WebException("y轴字段设置异常");
        }
        final PageDesignField xDataField = report.getFields().get(report.getXDataLine());
        final List<PageDesignField> yDataFields = report.getYDataLineMore().stream().map(v -> {
            return "id".equals(v)? new PageDesignField("记录数", "id","string"): report.getFields().get(v);
        }).collect(Collectors.toList());
        final PageDesignField yDataField = ("id".equals(report.getYDataLine()))?null:report.getFields().get(report.getYDataLine());

        XN_Query query = XN_Query.create(subject).tag(report.getTabName())
                .filter("type", "eic", report.getTabName())
                .notDelete().begin(0)
                .end(-1);

        dataRole(query,report.getDataRole());

        if (Utils.isNotEmpty(report.getStartDate())) {
            query.filter("published", ">=", report.getStartDate());
        }
        if (Utils.isNotEmpty(report.getEndDate())) {
            query.filter("published", "<=", report.getEndDate());
        }

        try {
            ExpressionUtils.executeQuery(report.getCondition(), query);
        }catch (Exception e) {
            log.error(e.getMessage());
        }

        if ("id".equals(report.getYDataLine())) {
            query.rollup();
            if ("descending".equals(report.getSort())) {
                query.order("my.count","desc");
            } else if ("ascending".equals(report.getSort())) {
                query.order("my.count","asc");
            }
        } else if(yDataField != null) {
            query.rollup("my." + yDataField.getField());
            if ("descending".equals(report.getSort())) {
                query.order("my." + yDataField.getField(),"desc");
            } else if ("ascending".equals(report.getSort())) {
                query.order("my." + yDataField.getField(),"asc");
            }
        }

        for (PageDesignField field : yDataFields) {
            if ("id".equals(field.getField())) {
                query.rollup();
            } else {
                query.rollup("my." + field.getField());
            }
        }

        dataGroupField(query,xDataField,report.getTimeFormat(),report.getSort());

        List<Object> contentList = query.execute();
        if (contentList.isEmpty()) { return new ArrayList<>(); }
        List<Map<String, Object>> result = new ArrayList<>();
        for(Object item : contentList ) {
            Map<String,Object> info = new HashMap<>(1);
            Content content = (Content) item;
            if ("published".equals(report.getXDataLine())) {
                info.put("name", content.published);
            } else if ("author".equals(report.getXDataLine())) {
                info.put("name", content.author);
            } else {
                if (content.my.containsKey(report.getXDataLine())) {
                    info.put("name", content.my.get(report.getXDataLine()).toString());
                } else {
                    info.put("name", "");
                }
            }
            try {
                if ("id".equals(report.getYDataLine())) {
                    if (content.my.containsKey("count")) {
                        Integer value = Integer.parseInt(PageDesignUtils.getDigit(content.my.get("count").toString()));
                        if (report.getConvertMaps().containsKey("id") && Utils.isNotEmpty(report.getConvertMaps().get("id"))) {
                            info.put("value", MathUtils.divide(value,Integer.parseInt(report.getConvertMaps().get("id"))));
                        } else {
                            info.put("value", value);
                        }
                    } else {
                        info.put("value", 0);
                    }
                } else {
                    if (yDataField != null && content.my.containsKey(yDataField.getField())) {
                        Double value = Double.parseDouble(PageDesignUtils.getDigit(content.my.get(yDataField.getField()).toString()));
                        if (report.getConvertMaps().containsKey(yDataField.getField()) && Utils.isNotEmpty(report.getConvertMaps().get(yDataField.getField()))) {
                            info.put("value", MathUtils.divide(value,Integer.parseInt(report.getConvertMaps().get(yDataField.getField()))));
                        }else {
                            info.put("value", value);
                        }
                    } else {
                        info.put("value", 0);
                    }
                }
            }catch (Exception e) {
                info.put("value", 0);
            }
            for (PageDesignField field : yDataFields) {
                String fieldname = field.getField();
                try {
                    if ("id".equals(fieldname)) {
                        if (content.my.containsKey("count")) {
                            Integer value = Integer.parseInt(PageDesignUtils.getDigit(content.my.get("count").toString()));
                            if (report.getConvertMaps().containsKey("id") && Utils.isNotEmpty(report.getConvertMaps().get("id"))) {
                                info.put("more_value", MathUtils.divide(value,Integer.parseInt(report.getConvertMaps().get("id"))));
                            } else {
                                info.put("more_value", value);
                            }
                        } else {
                            info.put("more_value", 0);
                        }
                    } else {
                        if (content.my.containsKey(field.getField())) {
                            Double value = Double.parseDouble(PageDesignUtils.getDigit(content.my.get(field.getField()).toString()));
                            if (report.getConvertMaps().containsKey(field.getField()) && Utils.isNotEmpty(report.getConvertMaps().get(field.getField()))) {
                                info.put("more_" + fieldname, MathUtils.divide(value,Integer.parseInt(report.getConvertMaps().get(field.getField()))));
                            }else {
                                info.put("more_" + fieldname, value);
                            }
                        } else {
                            info.put("more_" + fieldname, 0);
                        }
                    }
                }catch (Exception e) {
                    info.put("more_" + fieldname, 0);
                }
            }
            result.add(info);
        }
        if ("author".equals(xDataField.getField()) || "profile".equals(PageDesignUtils.getUiType(xDataField.getUitype()))) {
            List<String> ids = result.stream().map(v -> { return v.get("name").toString();}).collect(Collectors.toList());
            if (!ids.isEmpty()) {
                List<Object> profiles = XN_Profile.loadMany(ids, "profile");
                Map<String, String> maps = profiles.stream().collect(Collectors.toMap(v -> ((Profile) v).id, v -> ((Profile) v).givenname, (k1, k2) -> k1));
                result = result.stream().peek(v -> {
                    String name = v.get("name").toString();
                    v.put("name", maps.getOrDefault(name, "--"));
                }).collect(Collectors.toList());
            }

        } else if ("picklist".equals(PageDesignUtils.getUiType(xDataField.getUitype()))) {
            TabField tabfield = PageDesignUtils.getModuleFieldInfo(report.getModule(),xDataField.getField());
            if(tabfield != null) {
                String picklist = tabfield.fieldname;
                if (Utils.isNotEmpty(tabfield.picklist)) {
                    picklist = tabfield.picklist;
                }
                if (tabfield.uitype == 5) {
                    picklist = "yesno";
                }
                List<List<Object>> picklists = (List) PickListUtils.getPickList(picklist);
                if (!picklists.isEmpty()) {
                    Map<String, String> maps = picklists.stream().collect(Collectors.toMap(v -> v.get(0).toString(), v -> v.get(1).toString(), (k1, k2) -> k1));
                    Map<String, String> intMaps = picklists.stream().collect(Collectors.toMap(v -> v.get(2).toString(), v -> v.get(1).toString(), (k1, k2) -> k1));
                    result = result.stream().peek(v -> {
                        String name = v.get("name").toString();
                        if (maps.containsKey(name)) {
                            v.put("name", maps.get(name));
                        } else {
                            v.put("name", intMaps.getOrDefault(name, "--"));
                        }
                    }).collect(Collectors.toList());
                }
            } else {
                throw new WebException("获取字典失败");
            }
        } else if ("department".equals(PageDesignUtils.getUiType(xDataField.getUitype()))) {

        } else if ( xDataField.getUitype() == 11) {
            result = transRelated(result,report.getModule(),xDataField.getField());
        }

        List<String> xAxis = result.stream().map(v -> v.get("name").toString()).distinct().collect(Collectors.toList());
        if ("descending".equals(report.getSort()) || "ascending".equals(report.getSort())) {
            if (report.getTopN() > 0 && xAxis.size() > report.getTopN()) {
                xAxis = xAxis.subList(0,report.getTopN());
            }
        }
        if (Boolean.TRUE.equals(report.getFillxAxis())) {
            xAxis = fillxAxisByName(xAxis,report.getStartDate(),report.getEndDate(),report.getTimeFormat());
        }

        final List<String> xAxisLabel = transDateAxisByName(xAxis,report.getTimeFormat());

        HashMap<String,String> yfields = new HashMap<>(1);
        if ("id".equals(report.getYDataLine())) {
            if (report.getUnitMaps().containsKey("id") && Utils.isNotEmpty(report.getUnitMaps().get("id"))) {
                yfields.put("id", "记录数" + "(" + report.getUnitMaps().get("id") +")");
            } else {
                yfields.put("id", "记录数");
            }
        } else if(yDataField != null){
            if (report.getUnitMaps().containsKey(yDataField.getField()) && Utils.isNotEmpty(report.getUnitMaps().get(yDataField.getField()))) {
                yfields.put(yDataField.getField(), yDataField.getLabel() + "(" + report.getUnitMaps().get(yDataField.getField()) +")");
            } else {
                yfields.put(yDataField.getField(), yDataField.getLabel());
            }
        }
        for (PageDesignField item : yDataFields) {
            if (report.getUnitMaps().containsKey(item.getField()) && Utils.isNotEmpty(report.getUnitMaps().get(item.getField()))) {
                yfields.put(item.getField(), item.getLabel() + "(" + report.getUnitMaps().get(item.getField()) +")");
            } else {
                yfields.put(item.getField(), item.getLabel());
            }
        }
        List<String> legends = new ArrayList<>(yfields.values());
        List<String> keys = new ArrayList<>(yfields.keySet());

        List<String> finalXAxis = xAxis;
        List<Map<String, Object>> finalResult = result;
        return keys.stream().map(field ->  {
            List<Object> yAxis = finalXAxis.stream().map(v -> {
                for (Map<String, Object> item : finalResult) {
                    if (v.equals(item.get("name"))) {
                        if (field.equals(report.getYDataLine())) {
                            if (item.containsKey("value")) {
                                return item.get("value");
                            }
                        } else {
                            if ("id".equals(field)) {
                                if (item.containsKey("more_value")) {
                                    return item.get("more_value");
                                }
                            } else {
                                if (item.containsKey("more_" + field)) {
                                    return item.get("more_" + field);
                                }
                            }
                        }
                    }
                }
                return 0;
            }).collect(Collectors.toList());
            return new ReportResult(field,yfields.get(field),xAxisLabel,yAxis,legends);
        }).collect(Collectors.toList());
    }

    public static Object getModuleSimpleData(ReportParam report) throws Exception {
        String subject = report.getDataType() + "_Count";
        if (Utils.isEmpty(subject)) {
            throw new WebException("无法处理的数据类型");
        }
        if (!"id".equals(report.getYDataLine()) && !report.getFields().containsKey(report.getYDataLine())) {
            throw new WebException("y轴字段设置异常");
        }
        final PageDesignField yDataField = ("id".equals(report.getYDataLine()))?null:report.getFields().get(report.getYDataLine());

        XN_Query query = XN_Query.create(subject).tag(report.getTabName())
                .filter("type", "eic", report.getTabName())
                .notDelete().begin(0)
                .end(-1);

        dataRole(query,report.getDataRole());

        if (Utils.isNotEmpty(report.getStartDate())) {
            query.filter("published", ">=", report.getStartDate());
        }
        if (Utils.isNotEmpty(report.getEndDate())) {
            query.filter("published", "<=", report.getEndDate());
        }

        try {
            ExpressionUtils.executeQuery(report.getCondition(), query);
        }catch (Exception e) {
            log.error(e.getMessage());
        }

        if ("id".equals(report.getYDataLine())) {
            query.rollup();
        } else if(yDataField != null){
            query.rollup("my." + yDataField.getField());
        }
        List<Object> contentList = query.execute();
        if (contentList.isEmpty()) {
            return 0;
        }
        Content content = (Content) contentList.get(0);
        if ("id".equals(report.getYDataLine())) {
            if (content.my.containsKey("count")) {
                Integer value =  Integer.parseInt(PageDesignUtils.getDigit(content.my.get("count").toString()));
                if (Utils.isNotEmpty(report.getConvert())) {
                    return MathUtils.divide(value,Integer.parseInt(report.getConvert()));
                }else {
                    return value;
                }
            }
        } else {
            if (yDataField != null && content.my.containsKey(yDataField.getField())) {
                Double value = Double.parseDouble(PageDesignUtils.getDigit(content.my.get(yDataField.getField()).toString()));
                if (Utils.isNotEmpty(report.getConvert())) {
                    return MathUtils.divide(value,Integer.parseInt(report.getConvert()));
                }else {
                    return value;
                }
            }
        }
        return 0;
    }

    private static void dataGroupField(XN_Query query,PageDesignField dataField,String timeFormat,String sort) {
        if ("published".equals(dataField.getField()) || "datetime".equals(dataField.getType())) {
            if ("published".equals(dataField.getField())) {
                if ("year".equals(timeFormat)) {
                    query.group("published@year");
                } else if ("quarter".equals(timeFormat)) {
                    query.group("published@quarter");
                } else if ("month".equals(timeFormat)) {
                    query.group("published@month");
                } else if ("week".equals(timeFormat)) {
                    query.group("published@week");
                } else if ("day".equals(timeFormat)) {
                    query.group("published@day");
                } else if ("monthday".equals(timeFormat)) {
                    query.group("published@monthday");
                } else if ("yearmonth".equals(timeFormat)) {
                    query.group("published@yearmonth");
                } else if ("yearquarter".equals(timeFormat)) {
                    query.group("published@yearquarter");
                } else if ("yearweek".equals(timeFormat)) {
                    query.group("published@yearweek");
                } else {
                    query.group("published@monthday");
                }
                if ("noSort".equals(sort) || "".equals(sort)) {
                    query.order("published", "asc");
                }
            } else {
                if ("year".equals(timeFormat)) {
                    query.group(dataField.getField() + "@year");
                } else if ("quarter".equals(timeFormat)) {
                    query.group(dataField.getField() + "@quarter");
                } else if ("month".equals(timeFormat)) {
                    query.group(dataField.getField() + "@month");
                } else if ("week".equals(timeFormat)) {
                    query.group(dataField.getField() + "@week");
                } else if ("day".equals(timeFormat)) {
                    query.group(dataField.getField() + "@day");
                } else if ("monthday".equals(timeFormat)) {
                    query.group(dataField.getField() + "@monthday");
                } else if ("yearmonth".equals(timeFormat)) {
                    query.group(dataField.getField() + "@yearmonth");
                } else if ("yearquarter".equals(timeFormat)) {
                    query.group(dataField.getField() + "@yearquarter");
                } else if ("yearweek".equals(timeFormat)) {
                    query.group(dataField.getField() + "@yearweek");
                } else {
                    query.group(dataField.getField() + "@monthday");
                }
            }
        } else {
            query.group("my." + dataField.getField());
            if ("noSort".equals(sort) || "".equals(sort)) {
                query.order("my." + dataField.getField(), "asc");
            }
        }
    }

    private static void dataRole(XN_Query query,String dataRole) {
        if ("1".equals(dataRole)) {
            if(Boolean.TRUE.equals(ProfileUtils.isSupplier())) {
                query.filter("my.supplierid", "=", SupplierUtils.getSupplierid());
            } else {
                if(!(ProfileUtils.isAdmin() || ProfileUtils.isAssistant())) {
                    query.filter( "my.supplierid", "=", "0");
                }
            }
        }
    }

    private static List<Map<String, Object>> transRelated(List<Map<String, Object>> axis,String module,String fieldname) {
        BaseEntityUtils baseEntity = BaseEntityUtils.init(module);
        if (Utils.isEmpty(baseEntity)) {
            return axis;
        }
        return axis.stream().peek(v -> {
            String fieldValue = v.get("name").toString();
            v.put("name", baseEntity.getOutsideLinkValue(fieldname, fieldValue, "--"));
        }).collect(Collectors.toList());
    }

    private static List<Map<String, Object>> transZRelated(List<Map<String, Object>> axis,String module,String fieldname) {
        BaseEntityUtils baseEntity = BaseEntityUtils.init(module);
        if (Utils.isEmpty(baseEntity)) {
            return axis;
        }
        return axis.stream().peek(v -> {
            String fieldValue = v.get("z").toString();
            v.put("z", baseEntity.getOutsideLinkValue(fieldname, fieldValue, "--"));
        }).collect(Collectors.toList());
    }

    private static List<Map<String, Object>> transDateAxis(List<Map<String, Object>> axis,String timeFormat) {
        return axis.stream().map(v -> {
            String name = v.get("name").toString();
            if (Utils.isEmpty(name)) {
                return v;
            }
            if ("year".equals(timeFormat)) {
                name += "年";
            } else if ("quarter".equals(timeFormat)) {
                name = "Q" + name;
            } else if ("month".equals(timeFormat)) {
                name = name + "月";
            } else if ("week".equals(timeFormat)) {
                name = "第" + name + "周";
            } else if ("day".equals(timeFormat)) {
                name = name + "日";
            } else if ("yearquarter".equals(timeFormat)) {
                name = name.replace("-","Q");
            } else if ("yearweek".equals(timeFormat)) {
                name = name.replace("-","第") + "周";
            }
            v.put("name",name);
            return v;
        }).collect(Collectors.toList());
    }

    private static List<String> transDateAxisByName(List<String> axis,String timeFormat) {
        return axis.stream().map(v -> {
            String name = v;
            if (Utils.isEmpty(name)) {
                return name;
            }
            if ("year".equals(timeFormat)) {
                name += "年";
            } else if ("quarter".equals(timeFormat)) {
                name = "Q" + name;
            } else if ("month".equals(timeFormat)) {
                name = name + "月";
            } else if ("week".equals(timeFormat)) {
                name = "第" + name + "周";
            } else if ("day".equals(timeFormat)) {
                name = name + "日";
            } else if ("yearquarter".equals(timeFormat)) {
                name = name.replace("-","Q");
            } else if ("yearweek".equals(timeFormat)) {
                name = name.replace("-","第") + "周";
            }
            return name;
        }).collect(Collectors.toList());
    }

    private static List<Map<String, Object>> fillxAxis(List<Map<String, Object>> axis,String startDate,String endDate,String timeFormat) {
        List<String> timeFormats = Arrays.asList("day","monthday");

        if (Utils.isNotEmpty(startDate) && Utils.isNotEmpty(endDate) && timeFormats.contains(timeFormat)) {
            Date start_date = DateTimeUtils.string2date(startDate);
            Date end_date = DateTimeUtils.string2date(endDate);

            int dayLength =  (int)DateTimeUtils.diff("day", end_date, start_date);
            List<String> xAxis = new ArrayList<>();
            SimpleDateFormat dayFormat = new SimpleDateFormat("d");
            SimpleDateFormat monthDayFormat = new SimpleDateFormat("MM-dd");
            for(int i=0;i<dayLength;i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start_date);
                calendar.add(Calendar.DATE, i+1);
                if ("day".equals(timeFormat)) {
                    xAxis.add(dayFormat.format(calendar.getTime()));
                } else if ("monthday".equals(timeFormat)) {
                    xAxis.add(monthDayFormat.format(calendar.getTime()));
                }
            }
            Map<String, String> maps = axis.stream().collect(Collectors.toMap(v -> v.get("name").toString(), v -> v.get("value").toString(), (k1, k2) -> k1));

            return xAxis.stream().map(v -> {
                if (maps.containsKey(v)) {
                    Map<String,Object> info = new HashMap<>(1);
                    info.put("name",v);
                    info.put("value",maps.get(v));
                    return info;
                } else {
                    Map<String,Object> info = new HashMap<>(1);
                    info.put("name",v);
                    info.put("value",0);
                    return info;
                }
            }).collect(Collectors.toList());
        }
        return axis;
    }

    private static List<String> fillxAxisByName(List<String> axis,String startDate,String endDate,String timeFormat) {
        List<String> timeFormats = Arrays.asList("day","monthday");

        if (Utils.isNotEmpty(startDate) && Utils.isNotEmpty(endDate) && timeFormats.contains(timeFormat)) {
            Date start_date = DateTimeUtils.string2date(startDate);
            Date end_date = DateTimeUtils.string2date(endDate);
            int dayLength =  (int) DateTimeUtils.diff("day", end_date, start_date);
            List<String> xAxis = new ArrayList<>();
            SimpleDateFormat dayFormat = new SimpleDateFormat("d");
            SimpleDateFormat monthDayFormat = new SimpleDateFormat("MM-dd");
            for(int i=0;i<dayLength;i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start_date);
                calendar.add(Calendar.DATE, i+1);
                if ("day".equals(timeFormat)) {
                    xAxis.add(dayFormat.format(calendar.getTime()));
                } else if ("monthday".equals(timeFormat)) {
                    xAxis.add(monthDayFormat.format(calendar.getTime()));
                }
            }
            return xAxis;
        }
        return axis;
    }

    public static String getTimeSectionLabel(String timeSection) {
        if ("today".equals(timeSection)) {
            return "当日";
        } else if ("onew".equals(timeSection)) {
            return "近一周";
        } else if ("tenday".equals(timeSection)) {
            return "近10天";
        } else if ("halfm".equals(timeSection)) {
            return "近半月";
        } else if ("onem".equals(timeSection)) {
            return "近一月";
        } else if ("twom".equals(timeSection)) {
            return "近二月";
        } else if ("threem".equals(timeSection)) {
            return "近三月";
        } else if ("halfy".equals(timeSection)) {
            return "近半年";
        } else if ("oney".equals(timeSection)) {
            return "近一年";
        } else if ("twoy".equals(timeSection)) {
            return "近二年";
        } else if ("threey".equals(timeSection)) {
            return "近三年";
        }
        return "";
    }
}


