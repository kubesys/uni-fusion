package com.qnkj.core.base.modules.lcdp.pagedesign.services.impl;

import cn.hutool.extra.template.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.restapi.XN_Filter;
import com.github.restapi.XN_Query;
import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.entitys.TabField;
import com.qnkj.common.utils.*;
import com.qnkj.core.base.entitys.ReportParam;
import com.qnkj.core.base.entitys.ReportResult;
import com.qnkj.core.base.modules.lcdp.developments.service.IDevelopmentService;
import com.qnkj.core.base.modules.lcdp.pagedesign.Util.PageDesignUtils;
import com.qnkj.core.base.modules.lcdp.pagedesign.entitys.PageDesignField;
import com.qnkj.core.base.modules.lcdp.pagedesign.entitys.User;
import com.qnkj.core.base.modules.lcdp.pagedesign.services.IpageDataSourceService;
import com.qnkj.core.base.modules.management.notices.entitys.NoticeLevel;
import com.qnkj.core.base.modules.management.notices.entitys.NoticeType;
import com.qnkj.core.base.modules.settings.loginlog.service.ILoginlogService;
import com.qnkj.core.base.modules.settings.supplier.utils.SupplierUtils;
import com.qnkj.core.base.services.ISystemInfo;
import com.qnkj.core.utils.IpUtil;
import com.qnkj.core.utils.ProfileUtils;
import com.qnkj.core.utils.ReportUtils;
import com.qnkj.core.webconfigs.aspect.LogUtils;
import com.qnkj.core.webconfigs.configure.GlobalConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * create by Auto Generator
 * create date 2021-06-25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class pageDataSourceServiceImpl implements IpageDataSourceService {
    private final ISystemInfo systemInfo;
    private final ILoginlogService loginLogService;
    private final IDevelopmentService developmentService;

    private final static List<String> colors = Arrays.asList("#5470c6","#91cc75","#fac858","#ee6666","#73c0de","#3ba272","#fc8452","#9a60b4","#ea7ccc");

    private HashMap<String,Object> getPieChartElementHtml(JSONObject properties,String chartType,Boolean hasTitle) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String dataLine = properties.containsKey("dataLine") ? properties.getString("dataLine") : "";
        final String timeFormat = properties.containsKey("timeFormat") ? properties.getString("timeFormat") : "";
        final String timeSection = properties.containsKey("timeSection") ? properties.getString("timeSection") : "";
        final String sort = properties.containsKey("sort") ? properties.getString("sort") : "sort";
        Integer topN = 10;
        final String xDataLine = properties.containsKey("xDataLine") ? properties.getString("xDataLine") : "";
        final String yColor = properties.containsKey("yColor") ? properties.getString("yColor") : "";
        final String yDataLine = properties.containsKey("yDataLine") ? properties.getString("yDataLine") : "";
        final String unit = properties.containsKey("unit") ? properties.getString("unit") : "";
        final String convert = properties.containsKey("convert") ? properties.getString("convert") : "";

        final JSONArray condition = properties.containsKey("condition") ? properties.getJSONArray("condition") : new JSONArray();

        HashMap<String,String> unitMaps = new HashMap<>();
        unitMaps.put(yDataLine,unit);

        HashMap<String,String> convertMaps = new HashMap<>();
        convertMaps.put(yDataLine,convert);
        if (properties.containsKey("topN")) {
            if ("all".equals(properties.getString("topN")) || "".equals(properties.getString("topN"))) {
                topN = -1;
            } else {
                topN = properties.getIntValue("topN");
            }
        }
        if (Utils.isEmpty(dataLine) || Utils.isEmpty(yDataLine) || Utils.isEmpty(xDataLine)) {
            throw new Exception("图表基础设置异常!");
        }
        Map<String, Object> moduleInfo = PageDesignUtils.getModuleInfo(dataLine);
        HashMap<String, PageDesignField> moduleFields = PageDesignUtils.getModuleFields(dataLine);
        String tabName = moduleInfo.get("TabName").toString();
        String moduleName = moduleInfo.get("ModuleName").toString();
        String moduleLabel = moduleInfo.get("ModuleLabel").toString();
        String dataType = moduleInfo.get("DataType").toString();
        String dataRole = moduleInfo.get("DataRole").toString();
        ReportParam reportparam = new ReportParam(dataLine, tabName, dataType, dataRole);
        reportparam.setXDataLine(xDataLine);
        reportparam.setYDataLine(yDataLine);
        reportparam.setTimeSection(timeSection);
        reportparam.setTimeFormat(timeFormat);
        reportparam.setFillxAxis(false);
        reportparam.setSort(sort);
        reportparam.setTopN(topN);
        reportparam.setFields(moduleFields);
        reportparam.setUnitMaps(unitMaps);
        reportparam.setConvertMaps(convertMaps);
        reportparam.setCondition(condition);
        List<ReportResult> reportResult = ReportUtils.getModuleData(reportparam);
        log.info("reportResult : {}",reportResult);

        if (reportResult.isEmpty()) {
            return getNoDataChartHtml();
        }

        List<Map<String, Object>> series = !reportResult.isEmpty() ? reportResult.get(0).getMaps() : new ArrayList<>();

        String yLabel = "";
        if ("id".equals(yDataLine)) {
            if (unitMaps.containsKey("id") && Utils.isNotEmpty(unitMaps.get("id"))) {
                yLabel = "记录数" + "(" + unitMaps.get("id") +")";
            } else {
                yLabel ="记录数";
            }
        } else {
            PageDesignField yDataField = moduleFields.get(yDataLine);
            if (unitMaps.containsKey(yDataField.getField()) && Utils.isNotEmpty(unitMaps.get(yDataField.getField()))) {
                yLabel = yDataField.getLabel() + "(" + unitMaps.get(yDataField.getField()) +")";
            } else {
                yLabel = yDataField.getLabel();
            }
        }

        HashMap<String, Object> charts = new HashMap<>();
        Map<String,Object> gridMap = new HashMap<>(1);
        gridMap.put("left","30");
        gridMap.put("right","30");
        gridMap.put("bottom","30");
        gridMap.put("containLabel",true);
        charts.put("grid",gridMap);
        if (hasTitle) {
            Map<String,Object> titleMap = new HashMap<>(1);
            if (Utils.isNotEmpty(title)) {
                titleMap.put("text", ReportUtils.getTimeSectionLabel(timeSection) + moduleLabel + "的" + title);
            } else {
                if ("pie".equals(chartType)) {
                    titleMap.put("text", ReportUtils.getTimeSectionLabel(timeSection) + moduleLabel + "的饼图");
                } else {
                    titleMap.put("text", ReportUtils.getTimeSectionLabel(timeSection) + moduleLabel + "的环形图");
                }
            }
            titleMap.put("left","center");
            Map<String,Object> textStyleMap = new HashMap<>(1);
            textStyleMap.put("fontSize",14);
            titleMap.put("textStyle",textStyleMap);
            charts.put("title",titleMap);
        }
        Map<String,Object> tooltipMap = new HashMap<>(1);
        tooltipMap.put("trigger","item");
        charts.put("tooltip",tooltipMap);
        Map<String,Object> legendMap = new HashMap<>(1);
        legendMap.put("orient","vertical");
        legendMap.put("left","left");
        charts.put("legend",legendMap);
        Map<String,Object> seriesMap = new HashMap<>(1);
        seriesMap.put("type","pie");
        seriesMap.put("name",yLabel);
        if ("pie".equals(chartType)) {
            seriesMap.put("radius", "60%");
        }else {
            seriesMap.put("radius", Arrays.asList("40%","60%"));
        }
        Map<String,Object> emphasisMap = new HashMap<>(1);
        Map<String,Object> labelMap = new HashMap<>(1);
        Map<String,Object> itemStyleMap = new HashMap<>(1);
        labelMap.put("show",true);
        labelMap.put("fontWeight","bold");
        emphasisMap.put("label",labelMap);
        itemStyleMap.put("shadowBlur",14);
        itemStyleMap.put("shadowOffsetX",0);
        itemStyleMap.put("shadowColor","rgba(0, 0, 0, 0.5)");
        emphasisMap.put("itemStyle",itemStyleMap);
        seriesMap.put("emphasis",emphasisMap);
        seriesMap.put("data",series);
        charts.put("series",seriesMap);
        return charts;
    }

    private HashMap<String,Object> getRadarChartElementHtml(JSONObject properties,Boolean hasTitle) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String dataLine = properties.containsKey("dataLine") ? properties.getString("dataLine") : "";
        final String timeFormat = properties.containsKey("timeFormat") ? properties.getString("timeFormat") : "";
        final String timeSection = properties.containsKey("timeSection") ? properties.getString("timeSection") : "";
        final String sort = properties.containsKey("sort") ? properties.getString("sort") : "sort";
        Integer topN = 10;
        final String xDataLine = properties.containsKey("xDataLine") ? properties.getString("xDataLine") : "";
        final String yColor = properties.containsKey("yColor") ? properties.getString("yColor") : "";
        final String yDataLine = properties.containsKey("yDataLine") ? properties.getString("yDataLine") : "";
        final String zDataLine = properties.containsKey("zDataLine") ? properties.getString("zDataLine") : "";

        final String unit = properties.containsKey("unit") ? properties.getString("unit") : "";
        final String convert = properties.containsKey("convert") ? properties.getString("convert") : "";

        final Boolean fillxAxis = properties.containsKey("fillxAxis") ? properties.getBooleanValue("fillxAxis") : false;

        final JSONArray condition = properties.containsKey("condition") ? properties.getJSONArray("condition") : new JSONArray();

        HashMap<String,String> unitMaps = new HashMap<>();
        unitMaps.put(yDataLine,unit);

        HashMap<String,String> convertMaps = new HashMap<>();
        convertMaps.put(yDataLine,convert);

        if (properties.containsKey("topN")) {
            if ("all".equals(properties.getString("topN")) || "".equals(properties.getString("topN"))) {
                topN = -1;
            } else {
                topN = properties.getIntValue("topN");
            }
        }
        if (Utils.isEmpty(dataLine) || Utils.isEmpty(yDataLine) || Utils.isEmpty(xDataLine)) {
            throw new Exception("图表基础设置异常!");
        }
        Map<String, Object> moduleInfo = PageDesignUtils.getModuleInfo(dataLine);
        HashMap<String, PageDesignField> moduleFields = PageDesignUtils.getModuleFields(dataLine);
        String tabName = moduleInfo.get("TabName").toString();
        String moduleName = moduleInfo.get("ModuleName").toString();
        String moduleLabel = moduleInfo.get("ModuleLabel").toString();
        String dataType = moduleInfo.get("DataType").toString();
        String dataRole = moduleInfo.get("DataRole").toString();
        List<ReportResult> reportResult;
        if (Utils.isNotEmpty(zDataLine)) {
            ReportParam reportparam = new ReportParam(dataLine, tabName, dataType, dataRole);
            reportparam.setXDataLine(xDataLine);
            reportparam.setYDataLine(yDataLine);
            reportparam.setZDataLine(zDataLine);
            reportparam.setTimeSection(timeSection);
            reportparam.setTimeFormat(timeFormat);
            reportparam.setFillxAxis(fillxAxis);
            reportparam.setSort(sort);
            reportparam.setTopN(topN);
            reportparam.setFields(moduleFields);
            reportparam.setUnitMaps(unitMaps);
            reportparam.setConvertMaps(convertMaps);
            reportparam.setCondition(condition);
            reportResult = ReportUtils.getModuleDataHasZAxis(reportparam);
            log.info("reportResult : {}",reportResult);

            if (reportResult.isEmpty()) {
                return getNoDataChartHtml();
            }
            List<String> xAxis = !reportResult.isEmpty() ? reportResult.get(0).getXAxis() : new ArrayList<>();
            double max = 0.0;
            for (ReportResult item : reportResult) {
                double item_max = item.getYAxis().stream().mapToDouble(v -> Double.parseDouble(v.toString())).max().getAsDouble();
                if (item_max > max) {
                    max = item_max;
                }
            }
            Double finalMax = max;
            List<Map<String, Object>> indicators = xAxis.stream().map(v -> {
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("name",v);
                infoMap.put("max", finalMax);
                return infoMap;
            }).collect(Collectors.toList());

            final String finalYLabel = "id".equals(yDataLine)?"记录数":moduleFields.get(yDataLine).getLabel();
            final String finalXLabel = moduleFields.get(xDataLine).getLabel();

            HashMap<String, Object> charts = new HashMap<>();
            Map<String,Object> gridMap = new HashMap<>(1);
            gridMap.put("left","30");
            gridMap.put("right","30");
            gridMap.put("bottom","30");
            gridMap.put("containLabel",true);
            charts.put("grid",gridMap);
            if (hasTitle) {
                Map<String,Object> titleMap = new HashMap<>(1);
                if (Utils.isNotEmpty(title)) {
                    titleMap.put("text", ReportUtils.getTimeSectionLabel(timeSection) + moduleLabel + "的" + title);
                } else {
                    titleMap.put("text", ReportUtils.getTimeSectionLabel(timeSection) + moduleLabel + "的雷达图");
                }
                titleMap.put("left","center");
                Map<String,Object> textStyleMap = new HashMap<>(1);
                textStyleMap.put("fontSize",14);
                titleMap.put("textStyle",textStyleMap);
                charts.put("title",titleMap);
            }
            Map<String,Object> tooltipMap = new HashMap<>(1);
            tooltipMap.put("trigger","item");
            charts.put("tooltip",tooltipMap);
            Map<String,Object> legendMap = new HashMap<>(1);
            legendMap.put("indicator", Collections.singletonList(finalYLabel));
            legendMap.put("left","left");
            charts.put("legend",legendMap);
            Map<String,Object> radarMap = new HashMap<>(1);
            radarMap.put("radius","60%");
            radarMap.put("indicator",indicators);
            charts.put("radar",radarMap);
            List<Map<String, Object>> series = reportResult.stream().map(v -> {
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("name",v.getName());
                infoMap.put("value",v.getYAxis());
                return infoMap;
            }).collect(Collectors.toList());
            Map<String,Object> infoMap = new HashMap<>(1);
            infoMap.put("type","radar");
            infoMap.put("name",finalXLabel);
            infoMap.put("data",series);
            charts.put("series",infoMap);
            return charts;
        } else {
            ReportParam reportparam = new ReportParam(dataLine, tabName, dataType, dataRole);
            reportparam.setXDataLine(xDataLine);
            reportparam.setYDataLine(yDataLine);
            reportparam.setTimeSection(timeSection);
            reportparam.setTimeFormat(timeFormat);
            reportparam.setFillxAxis(fillxAxis);
            reportparam.setSort(sort);
            reportparam.setTopN(topN);
            reportparam.setFields(moduleFields);
            reportparam.setUnitMaps(unitMaps);
            reportparam.setConvertMaps(convertMaps);
            reportparam.setCondition(condition);
            reportResult = ReportUtils.getModuleData(reportparam);
            log.info("reportResult : {}",reportResult);

            if (reportResult.isEmpty()) {
                return getNoDataChartHtml();
            }

            List<Map<String, Object>> maps = reportResult.size() >0 ? reportResult.get(0).getMaps() : new ArrayList<>();
            List<String> xAxis = reportResult.size() >0 ? reportResult.get(0).getXAxis() : new ArrayList<>();
            List<Object> yAxis = reportResult.size() >0 ? reportResult.get(0).getYAxis() : new ArrayList<>();

            Double max = yAxis.stream().mapToDouble(v -> Double.parseDouble(v.toString())).max().getAsDouble();
            List<Map<String, Object>> indicators = maps.stream().map(v -> {
                final String name = v.get("name").toString();
                Map<String,Object> infoMap = new HashMap<>(1);
                infoMap.put("name",name);
                infoMap.put("max",max);
                return infoMap;
            }).collect(Collectors.toList());

            final String finalYLabel = "id".equals(yDataLine)?"记录数":moduleFields.get(yDataLine).getLabel();
            final String finalXLabel = moduleFields.get(xDataLine).getLabel();

            HashMap<String, Object> charts = new HashMap<>();
            Map<String,Object> gridMap = new HashMap<>(1);
            gridMap.put("left","30");
            gridMap.put("right","30");
            gridMap.put("bottom","30");
            gridMap.put("containLabel",true);
            charts.put("grid",gridMap);
            if (hasTitle) {
                Map<String,Object> titleMap = new HashMap<>(1);
                Map<String,Object> textStyleMap = new HashMap<>(1);
                if (Utils.isNotEmpty(title)) {
                    titleMap.put("text", ReportUtils.getTimeSectionLabel(timeSection) + moduleLabel + "的" + title);
                } else {
                    titleMap.put("text", ReportUtils.getTimeSectionLabel(timeSection) + moduleLabel + "的雷达图");
                }
                titleMap.put("left","center");
                textStyleMap.put("fontSize",14);
                titleMap.put("textStyle",textStyleMap);
                charts.put("title",titleMap);
            }
            Map<String,Object> tooltipMap = new HashMap<>(1);
            tooltipMap.put("trigger","item");
            charts.put("tooltip",tooltipMap);
            Map<String,Object> legendMap = new HashMap<>(1);
            legendMap.put("indicator", Collections.singletonList(finalYLabel));
            legendMap.put("left","left");
            charts.put("legend",legendMap);
            Map<String,Object> radarMap = new HashMap<>(1);
            radarMap.put("radius","60%");
            radarMap.put("indicator",indicators);
            charts.put("radar",radarMap);
            Map<String,Object> infoMap = new HashMap<>(1);
            infoMap.put("name",finalYLabel);
            infoMap.put("value",yAxis);
            List<Map<String, Object>> series = Collections.singletonList(infoMap);
            Map<String,Object> seriesMap = new HashMap<>(1);
            seriesMap.put("type","radar");
            seriesMap.put("name",finalXLabel);
            seriesMap.put("data",series);
            charts.put("series",seriesMap);
            return charts;
        }
    }

    private HashMap<String,Object> getFunnelChartElementHtml(JSONObject properties,Boolean hasTitle) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String dataLine = properties.containsKey("dataLine") ? properties.getString("dataLine") : "";
        final String timeFormat = properties.containsKey("timeFormat") ? properties.getString("timeFormat") : "";
        final String timeSection = properties.containsKey("timeSection") ? properties.getString("timeSection") : "";
        final String sort = properties.containsKey("sort") ? properties.getString("sort") : "sort";
        Integer topN = 10;
        final String xDataLine = properties.containsKey("xDataLine") ? properties.getString("xDataLine") : "";
        final String yColor = properties.containsKey("yColor") ? properties.getString("yColor") : "";
        final String yDataLine = properties.containsKey("yDataLine") ? properties.getString("yDataLine") : "";
        final String unit = properties.containsKey("unit") ? properties.getString("unit") : "";
        final String convert = properties.containsKey("convert") ? properties.getString("convert") : "";

        final JSONArray condition = properties.containsKey("condition") ? properties.getJSONArray("condition") : new JSONArray();

        HashMap<String,String> unitMaps = new HashMap<>();
        unitMaps.put(yDataLine,unit);

        HashMap<String,String> convertMaps = new HashMap<>();
        convertMaps.put(yDataLine,convert);

        if (properties.containsKey("topN")) {
            if ("all".equals(properties.getString("topN")) || "".equals(properties.getString("topN"))) {
                topN = -1;
            } else {
                topN = properties.getIntValue("topN");
            }
        }
        if (Utils.isEmpty(dataLine) || Utils.isEmpty(yDataLine) || Utils.isEmpty(xDataLine)) {
            throw new Exception("图表基础设置异常!");
        }
        Map<String, Object> moduleInfo = PageDesignUtils.getModuleInfo(dataLine);
        HashMap<String, PageDesignField> moduleFields = PageDesignUtils.getModuleFields(dataLine);
        String tabName = moduleInfo.get("TabName").toString();
        String moduleName = moduleInfo.get("ModuleName").toString();
        String moduleLabel = moduleInfo.get("ModuleLabel").toString();
        String dataType = moduleInfo.get("DataType").toString();
        String dataRole = moduleInfo.get("DataRole").toString();
        ReportParam reportparam = new ReportParam(dataLine, tabName, dataType, dataRole);
        reportparam.setXDataLine(xDataLine);
        reportparam.setYDataLine(yDataLine);
        reportparam.setTimeSection(timeSection);
        reportparam.setTimeFormat(timeFormat);
        reportparam.setFillxAxis(false);
        reportparam.setSort(sort);
        reportparam.setTopN(topN);
        reportparam.setFields(moduleFields);
        reportparam.setUnitMaps(unitMaps);
        reportparam.setConvertMaps(convertMaps);
        reportparam.setCondition(condition);
        List<ReportResult> reportResult = ReportUtils.getModuleData(reportparam);
        log.info("reportResult : {}",reportResult);

        if (reportResult.isEmpty()) {
            return getNoDataChartHtml();
        }

        List<Map<String, Object>> maps = reportResult.size() >0 ? reportResult.get(0).getMaps() : new ArrayList<>();
        List<String> xAxis = reportResult.size() >0 ? reportResult.get(0).getXAxis() : new ArrayList<>();
        List<Object> yAxis = reportResult.size() >0 ? reportResult.get(0).getYAxis() : new ArrayList<>();

        Double total = yAxis.stream().mapToDouble(v -> Double.parseDouble(v.toString())).sum();
        List<Map<String, Object>> series = maps.stream().map(v -> {
            final String name = v.get("name").toString();
            final double count = Double.parseDouble(((HashMap)v).get("value").toString());
            Map<String,Object> infoMap = new HashMap<>(1);
            infoMap.put("name",name);
            infoMap.put("value",MathUtils.getDoubleByScale(count * 100 / total,2));
            return infoMap;
        }).collect(Collectors.toList());
        String yLabel = "";
        if ("id".equals(yDataLine)) {
            if (unitMaps.containsKey("id") && Utils.isNotEmpty(unitMaps.get("id"))) {
                yLabel = "记录数" + "(" + unitMaps.get("id") +")";
            } else {
                yLabel ="记录数";
            }
        } else {
            PageDesignField yDataField = moduleFields.get(yDataLine);
            if (unitMaps.containsKey(yDataField.getField()) && Utils.isNotEmpty(unitMaps.get(yDataField.getField()))) {
                yLabel = yDataField.getLabel() + "(" + unitMaps.get(yDataField.getField()) +")";
            } else {
                yLabel = yDataField.getLabel();
            }
        }
        final String finalYLabel = yLabel;
        final String finalXLabel = moduleFields.get(xDataLine).getLabel();

        HashMap<String, Object> charts = new HashMap<>();
        Map<String,Object> gridMap = new HashMap<>(1);
        gridMap.put("left","30");
        gridMap.put("right","30");
        gridMap.put("bottom","30");
        gridMap.put("containLabel",true);
        charts.put("grid",gridMap);
        if (hasTitle) {
            Map<String,Object> titleMap = new HashMap<>(1);
            if (Utils.isNotEmpty(title)) {
                titleMap.put("text", ReportUtils.getTimeSectionLabel(timeSection) + moduleLabel + "的" + title);
            } else {
                titleMap.put("text", ReportUtils.getTimeSectionLabel(timeSection) + moduleLabel + "的雷达图");
            }
            titleMap.put("left","center");
            Map<String,Object> textStyleMap = new HashMap<>(1);
            textStyleMap.put("fontSize",14);
            titleMap.put("textStyle",textStyleMap);
            charts.put("title",titleMap);
        }
        Map<String,Object> tooltipMap = new HashMap<>(1);
        tooltipMap.put("trigger","item");
        tooltipMap.put("formatter","{a} <br/>{b} : {c}%");
        charts.put("tooltip",tooltipMap);
        Map<String,Object> legendMap = new HashMap<>(1);
        legendMap.put("data",xAxis);
        charts.put("legend",legendMap);
        Map<String,Object> seriesMap = new HashMap<>(1);
        seriesMap.put("type","funnel");
        seriesMap.put("name",finalXLabel);
        seriesMap.put("data",series);
        seriesMap.put("left", "10%");
        seriesMap.put("top", 60);
        seriesMap.put("bottom", 60);
        seriesMap.put("width", "80%");
        seriesMap.put("min", 0);
        seriesMap.put("max", 100);
        seriesMap.put("minSize", "0%");
        seriesMap.put("maxSize", "100%");
        seriesMap.put("sort", "descending");
        seriesMap.put("gap", 2);
        Map<String,Object> seriesLabelMap = new HashMap<>(1);
        seriesLabelMap.put("show",true);
        seriesLabelMap.put("position","inside");
        seriesMap.put("label",seriesLabelMap);
        Map<String,Object> seriesLabelLineMap = new HashMap<>(1);
        seriesLabelLineMap.put("length",10);
        Map<String,Object> seriesLabelitemStyleMap = new HashMap<>(1);
        seriesLabelitemStyleMap.put("type","solid");
        seriesLabelitemStyleMap.put("width",1);
        seriesLabelLineMap.put("itemStyle",seriesLabelitemStyleMap);
        seriesMap.put("labelLine",seriesLabelLineMap);
        Map<String,Object> seriesitemStyleMap = new HashMap<>(1);
        seriesitemStyleMap.put("borderColor","#fff");
        seriesitemStyleMap.put("borderWidth",1);
        seriesMap.put("itemStyle",seriesitemStyleMap);
        Map<String,Object> seriesEmphasisMap = new HashMap<>(1);
        Map<String,Object> seriesEmphasisLabelMap = new HashMap<>(1);
        seriesEmphasisLabelMap.put("fontSize",20);
        seriesEmphasisMap.put("label",seriesEmphasisLabelMap);
        seriesMap.put("emphasis",seriesEmphasisMap);
        charts.put("series",seriesMap);
        return charts;
    }

    private HashMap<String,Object> getNoDataChartHtml() {
        HashMap<String, Object> charts = new HashMap<>(1);
        Map<String,Object> titleMap = new HashMap<>(1);
        Map<String,Object> textStyleMap = new HashMap<>(1);
        titleMap.put("text","暂无数据");
        titleMap.put("left","center");
        titleMap.put("top","30%");
        textStyleMap.put("color","rgb(50,197,233)");
        textStyleMap.put("fontSize","20");
        textStyleMap.put("align","center");
        charts.put("textStyle",textStyleMap);
        charts.put("title",titleMap);
        return charts;
    }

    private HashMap<String,Object> getChartElementHtml(JSONObject properties,String chartType,Boolean hasTitle) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String dataLine = properties.containsKey("dataLine") ? properties.getString("dataLine") : "";
        final String timeFormat = properties.containsKey("timeFormat") ? properties.getString("timeFormat") : "";
        final String timeSection = properties.containsKey("timeSection") ? properties.getString("timeSection") : "";
        final String sort = properties.containsKey("sort") ? properties.getString("sort") : "sort";
        int topN = 10;
        final String xDataLine = properties.containsKey("xDataLine") ? properties.getString("xDataLine") : "";
        final String zDataLine = properties.containsKey("zDataLine") ? properties.getString("zDataLine") : "";

        final String yColor = properties.containsKey("yColor") ? properties.getString("yColor") : "";
        final String yDataLine = properties.containsKey("yDataLine") ? properties.getString("yDataLine") : "";
        final String unit = properties.containsKey("unit") ? properties.getString("unit") : "";
        final String convert = properties.containsKey("convert") ? properties.getString("convert") : "";

        final Boolean fillxAxis = properties.containsKey("fillxAxis") ? properties.getBooleanValue("fillxAxis") : false;

        final JSONArray condition = properties.containsKey("condition") ? properties.getJSONArray("condition") : new JSONArray();

        final List<String> yDataLineMore = properties.containsKey("yDataLineMore") ? Arrays.asList(properties.getJSONArray("yDataLineMore").toArray()).stream().map(v -> v.toString()).collect(Collectors.toList()) : new ArrayList<>();
        final List<String> yColorMore = properties.containsKey("yColorMore") ? Arrays.asList(properties.getJSONArray("yColorMore").toArray()).stream().map(v -> v.toString()).collect(Collectors.toList()) : new ArrayList<>();
        final List<String> unitMore = properties.containsKey("unitMore") ? Arrays.asList(properties.getJSONArray("unitMore").toArray()).stream().map(v -> v.toString()).collect(Collectors.toList()) : new ArrayList<>();
        final List<String> convertMore = properties.containsKey("convertMore") ? Arrays.asList(properties.getJSONArray("convertMore").toArray()).stream().map(v -> v.toString()).collect(Collectors.toList()) : new ArrayList<>();

        HashMap<String,String> yColorMaps = new HashMap<>();
        if (yDataLineMore.size() == yColorMore.size()) {
            yColorMaps.put(yDataLine,yColor);
            for (int i=0;i<yDataLineMore.size();i++) {
                yColorMaps.put(yDataLineMore.get(i),yColorMore.get(i));
            }
        } else {
            yColorMaps.put(yDataLine,colors.get(0));
            for (int i=0;i<yDataLineMore.size();i++) {
                yColorMaps.put(yDataLineMore.get(i),colors.get(i+1));
            }
        }
        HashMap<String,String> unitMaps = new HashMap<>();
        unitMaps.put(yDataLine,unit);
        for (int i=0;i<yDataLineMore.size();i++) {
            if (unitMore.size() > i && Utils.isNotEmpty(unitMore.get(i))) {
                unitMaps.put(yDataLineMore.get(i),unitMore.get(i));
            } else {
                unitMaps.put(yDataLineMore.get(i),"");
            }
        }
        HashMap<String,String> convertMaps = new HashMap<>();
        convertMaps.put(yDataLine,convert);
        for (int i=0;i<yDataLineMore.size();i++) {
            if (convertMore.size() > i && Utils.isNotEmpty(convertMore.get(i))) {
                convertMaps.put(yDataLineMore.get(i),convertMore.get(i));
            } else {
                convertMaps.put(yDataLineMore.get(i),"");
            }
        }

        if (properties.containsKey("topN")) {
            if ("all".equals(properties.getString("topN")) || "".equals(properties.getString("topN"))) {
                topN = -1;
            } else {
                topN = properties.getIntValue("topN");
            }
        }
        if (Utils.isEmpty(dataLine) || Utils.isEmpty(yDataLine) || Utils.isEmpty(xDataLine)) {
            throw new Exception("图表基础设置异常!");
        }
        Map<String, Object> moduleInfo = PageDesignUtils.getModuleInfo(dataLine);
        HashMap<String, PageDesignField> moduleFields = PageDesignUtils.getModuleFields(dataLine);
        String tabName = moduleInfo.get("TabName").toString();
        String moduleLabel = moduleInfo.get("ModuleLabel").toString();
        String dataType = moduleInfo.get("DataType").toString();
        String dataRole = moduleInfo.get("DataRole").toString();
        List<ReportResult> reportResult;
        ReportParam reportparam = new ReportParam(dataLine, tabName, dataType, dataRole);
        reportparam.setXDataLine(xDataLine);
        reportparam.setYDataLine(yDataLine);
        reportparam.setTimeSection(timeSection);
        reportparam.setTimeFormat(timeFormat);
        reportparam.setFillxAxis(fillxAxis);
        reportparam.setSort(sort);
        reportparam.setTopN(topN);
        reportparam.setFields(moduleFields);
        reportparam.setUnitMaps(unitMaps);
        reportparam.setConvertMaps(convertMaps);
        reportparam.setCondition(condition);
        if (Utils.isNotEmpty(zDataLine)) {
            reportparam.setZDataLine(zDataLine);
            reportResult = ReportUtils.getModuleDataHasZAxis(reportparam);
        } else if (!yDataLineMore.isEmpty()) {
            reportparam.setYDataLineMore(yDataLineMore);
            reportResult = ReportUtils.getModuleDataHasMoreYAxis(reportparam);
        } else {
            reportResult = ReportUtils.getModuleData(reportparam);
        }
        log.info("reportResult : {}",reportResult);

        if (reportResult.isEmpty()) {
            return getNoDataChartHtml();
        }

        List<String> legends = reportResult.get(0).getLegends();
        List<String> xAxis = reportResult.get(0).getXAxis();


        final String finalYLabel = "id".equals(yDataLine)?"记录数":moduleFields.get(yDataLine).getLabel();
        final String finalXLabel = moduleFields.get(xDataLine).getLabel();


        HashMap<String, Object> charts = new HashMap<>();
        Map<String,Object> gridMap = new HashMap<>(1);
        gridMap.put("left","50");
        gridMap.put("right","50");
        gridMap.put("bottom","12%");
        gridMap.put("containLabel",true);
        charts.put("grid",gridMap);
        if (hasTitle) {
            Map<String,Object> titleMap = new HashMap<>(1);
            if (Utils.isNotEmpty(title)) {
                titleMap.put("text", ReportUtils.getTimeSectionLabel(timeSection) + moduleLabel + "的" + title);
            } else {
                if ("line".equals(chartType)) {
                    titleMap.put("text", ReportUtils.getTimeSectionLabel(timeSection) + moduleLabel + "的折线图");
                } else {
                    titleMap.put("text", ReportUtils.getTimeSectionLabel(timeSection) + moduleLabel + "的柱状图");
                }
            }
            titleMap.put("left","30");
            Map<String,Object> textStyleMap = new HashMap<>(1);
            textStyleMap.put("fontSize",14);
            titleMap.put("textStyle",textStyleMap);
            charts.put("title",titleMap);
        }
        Map<String,Object> tooltipMap = new HashMap<>(1);
        tooltipMap.put("trigger","axis");
        charts.put("tooltip",tooltipMap);
        Map<String,Object> legendMap = new HashMap<>(1);
        legendMap.put("data",legends);
        legendMap.put("right",50);
        charts.put("legend",legendMap);
        Map<String,Object> xAxisMap = new HashMap<>(1);
        xAxisMap.put("type","category");
        xAxisMap.put("name",finalXLabel);
        xAxisMap.put("nameLocation","end");
        xAxisMap.put("nameGap",30);
        Map<String,Object> xAxisNameTextStyleMap = new HashMap<>(1);
        xAxisNameTextStyleMap.put("padding",Arrays.asList(0,30,-40,0));
        xAxisNameTextStyleMap.put("align","right");
        xAxisNameTextStyleMap.put("verticalAlign","bottom");
        xAxisNameTextStyleMap.put("fontWeight","bold");
        xAxisNameTextStyleMap.put("fontSize",12);
        xAxisMap.put("nameTextStyle",xAxisNameTextStyleMap);
        xAxisMap.put("data",xAxis);
        Map<String,Object> xAxisTickMap = new HashMap<>(1);
        xAxisTickMap.put("alignWithLabel",true);
        xAxisMap.put("axisTick",xAxisTickMap);
        Map<String,Object> xAxisLabelMap = new HashMap<>(1);
        xAxisLabelMap.put("interval",0);
        xAxisLabelMap.put("width",50);
        xAxisLabelMap.put("lineHeight",12);
        xAxisLabelMap.put("height",24);
        xAxisLabelMap.put("overflow","break");
        xAxisMap.put("axisLabel",xAxisLabelMap);
        if ("bar".equals(chartType) || xAxis.size() < 5) {
            xAxisMap.put("minInterval",1);
            xAxisMap.put("boundaryGap",Arrays.asList(0, 0.01));
        }
        charts.put("xAxis",xAxisMap);
        Map<String,Object> yAxisMap = new HashMap<>(1);
        yAxisMap.put("type","value");
        yAxisMap.put("name",finalYLabel);
        yAxisMap.put("nameLocation","end");
        yAxisMap.put("nameGap",15);
        Map<String,Object> yAxisNameTextStyleMap = new HashMap<>(1);
        yAxisNameTextStyleMap.put("fontWeight","bold");
        yAxisNameTextStyleMap.put("fontSize",12);
        yAxisMap.put("nameTextStyle",yAxisNameTextStyleMap);
        Map<String,Object> yAxisLineMap = new HashMap<>(1);
        yAxisLineMap.put("show",true);
        yAxisMap.put("axisLine",yAxisLineMap);
        charts.put("yAxis",yAxisMap);
        List<Object> series =  reportResult.stream().map(v -> {
            final List<Object> yAxis = v.getYAxis();
            Map<String,Object> infoMap = new HashMap<>(1);
            infoMap.put("type",chartType);
            infoMap.put("name",v.getName());
            infoMap.put("width",3);
            if (Utils.isEmpty(zDataLine)) {
                if (yDataLineMore.isEmpty()) {
                    if (Utils.isNotEmpty(yColor)) {
                        Map<String,Object> itemStyleMap = new HashMap<>(1);
                        Map<String,Object> normalMap = new HashMap<>(1);
                        Map<String,Object> lineStyleMap = new HashMap<>(1);
                        lineStyleMap.put("color", yColor);
                        normalMap.put("color", yColor);
                        normalMap.put("lineStyle", lineStyleMap);
                        itemStyleMap.put("normal",normalMap);
                        infoMap.put("itemStyle", itemStyleMap);
                    }
                } else {
                    final String field = v.getField();
                    if (yColorMaps.containsKey(field)) {
                        final String color = yColorMaps.get(field);
                        Map<String,Object> itemStyleMap = new HashMap<>(1);
                        Map<String,Object> normalMap = new HashMap<>(1);
                        Map<String,Object> lineStyleMap = new HashMap<>(1);
                        lineStyleMap.put("color", color);
                        normalMap.put("color", color);
                        normalMap.put("lineStyle", lineStyleMap);
                        itemStyleMap.put("normal",normalMap);
                        infoMap.put("itemStyle", itemStyleMap);
                    }
                }
            } else {
                Map<String,Object> emphasisMap = new HashMap<>(1);
                emphasisMap.put("focus", "series");
                infoMap.put("barGap", 0);
                infoMap.put("emphasis", emphasisMap);
            }
            if ("bar".equals(chartType)) {
                infoMap.put("barMaxWidth",30);
            }
            infoMap.put("data",yAxis);
            return infoMap;
        }).collect(Collectors.toList());
        charts.put("series",series);
        return charts;
    }

    private HashMap<String,Object> getLabelDataElementHtml(JSONObject properties) throws Exception {
        final String dataLine = properties.containsKey("dataLine") ? properties.getString("dataLine") : "";
        final String timeSection = properties.containsKey("timeSection") ? properties.getString("timeSection") : "";
        final String yDataLine = properties.containsKey("yDataLine") ? properties.getString("yDataLine") : "";
        final String prefix = properties.containsKey("prefix") ? properties.getString("prefix") : "";
        final String suffix = properties.containsKey("suffix") ? properties.getString("suffix") : "";
        final String link = properties.containsKey("link") ? properties.getString("link") : "";
        final String convert = properties.containsKey("convert") ? properties.getString("convert") : "";

        final JSONArray condition = properties.containsKey("condition") ? properties.getJSONArray("condition") : new JSONArray();

        if (Utils.isEmpty(dataLine) || Utils.isEmpty(yDataLine)) {
            throw new Exception("基础设置异常!");
        }
        Map<String, Object> moduleInfo = PageDesignUtils.getModuleInfo(dataLine);
        HashMap<String, PageDesignField> moduleFields = PageDesignUtils.getModuleFields(dataLine);
        String tabName = moduleInfo.get("TabName").toString();
        String dataType = moduleInfo.get("DataType").toString();
        String dataRole = moduleInfo.get("DataRole").toString();
        ReportParam reportparam = new ReportParam(dataLine, tabName, dataType, dataRole);
        reportparam.setYDataLine(yDataLine);
        reportparam.setTimeSection(timeSection);
        reportparam.setConvert(convert);
        reportparam.setFields(moduleFields);
        reportparam.setCondition(condition);
        final Object value = ReportUtils.getModuleSimpleData(reportparam);
        log.info("getModuleSimpleData : {}",value);

        HashMap<String, Object> result = new HashMap<>(1);
        result.put("prefix",prefix);
        result.put("suffix",suffix);
        result.put("link",link);
        result.put("value",value);
        return result;
    }

    @Override
    public String getLabelElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        try {
            Map<String, Object> maps = new HashMap<>();
            maps.put("title", title);
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);

            HashMap<String,Object> labelInfos = getLabelDataElementHtml(properties);
            maps.put("infos", labelInfos);

            return generator(maps, "16");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    @Override
    public String getFunnelChartElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        final Boolean hasTitle = properties.containsKey("echartsTitle") ? properties.getBooleanValue("echartsTitle") : false;
        try {
            Map<String, Object> maps = new HashMap<>();
            if (hasTitle) {
                maps.put("title", "");
            } else {
                maps.put("title", title);
            }
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);

            HashMap<String,Object> charts = getFunnelChartElementHtml(properties, hasTitle);
            String jsonStr = JSONArray.toJSONString(charts);
            maps.put("charts", jsonStr);

            return generator(maps, "6");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    @Override
    public String getDoughnutChartElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        final Boolean hasTitle = properties.containsKey("echartsTitle") && properties.getBooleanValue("echartsTitle");
        try {
            Map<String, Object> maps = new HashMap<>();
            if (hasTitle) {
                maps.put("title", "");
            } else {
                maps.put("title", title);
            }
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);

            HashMap<String,Object> charts = getPieChartElementHtml(properties,"doughnut", hasTitle);
            String jsonStr = JSONArray.toJSONString(charts);
            maps.put("charts", jsonStr);

            return generator(maps, "7");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    @Override
    public String getRadarChartElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        final Boolean hasTitle = properties.containsKey("echartsTitle") && properties.getBooleanValue("echartsTitle");
        try {
            Map<String, Object> maps = new HashMap<>();
            if (hasTitle) {
                maps.put("title", "");
            } else {
                maps.put("title", title);
            }
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);

            HashMap<String,Object> charts = getRadarChartElementHtml(properties, hasTitle);
            String jsonStr = JSONArray.toJSONString(charts);
            maps.put("charts", jsonStr);

            return generator(maps, "5");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    @Override
    public String getPieChartElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        final Boolean hasTitle = properties.containsKey("echartsTitle") && properties.getBooleanValue("echartsTitle");
        try {
            Map<String, Object> maps = new HashMap<>();
            if (hasTitle) {
                maps.put("title", "");
            } else {
                maps.put("title", title);
            }
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);

            HashMap<String,Object> charts = getPieChartElementHtml(properties,"pie",hasTitle);
            String jsonStr = JSONArray.toJSONString(charts);
            maps.put("charts", jsonStr);

            return generator(maps, "4");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    @Override
    public String getBarChartElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        final Boolean hasTitle = properties.containsKey("echartsTitle") && properties.getBooleanValue("echartsTitle");
        try {
            Map<String, Object> maps = new HashMap<>();
            if (hasTitle) {
                maps.put("title", "");
            } else {
                maps.put("title", title);
            }
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);

            HashMap<String,Object> charts = getChartElementHtml(properties,"bar", hasTitle);
            String jsonStr = JSONArray.toJSONString(charts);
            maps.put("charts", jsonStr);

            return generator(maps, "3");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    @Override
    public String getLineChartElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        final Boolean hasTitle = properties.containsKey("echartsTitle") && properties.getBooleanValue("echartsTitle");
        try {
            Map<String, Object> maps = new HashMap<>();
            if (hasTitle) {
                maps.put("title", "");
            } else {
                maps.put("title", title);
            }
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);

            HashMap<String,Object> charts = getChartElementHtml(properties,"line", hasTitle);
            String jsonStr = JSONArray.toJSONString(charts);
            maps.put("charts", jsonStr);

            return generator(maps, "2");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    @Override
    public String getApprovalElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        final Integer length = properties.containsKey("numLength") ? properties.getIntValue("numLength") : 10;
        try {
            Map<String, Object> maps = new HashMap<>();
            maps.put("title", title);
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);

            maps.put("tableHeight", Integer.parseInt(height) - 80);

            List<TabField> fields = Arrays.asList(
                    new TabField().fieldname("modulename").fieldlabel("审批模块").align("center").listwidth("40%"),
                    new TabField().fieldname("sourcer").fieldlabel("提交人").align("center"),
                    new TabField().fieldname("published").fieldlabel("提交时间").align("center").listwidth(140),
                    new TabField().fieldname("oper").fieldlabel("操作").align("center").listwidth(40)
            );
            List<Object> entitys = getApprovalEntitys(length);
            maps.put("LISTHEADERS", Utils.objectToJson(getCustomListViewHeader(fields)));
            maps.put("ENTITYMAPS", Utils.objectToJson(entitys));
            return generator(maps, "11");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    private List<Object> getApprovalEntitys(Integer length) {
        List<HashMap<String,Object>> allModuleObject = (List<HashMap<String, Object>>)developmentService.getModuleList();
        Map<String, String> modules = allModuleObject.stream().collect(Collectors.toMap(v -> v.get("modulename").toString(), v -> v.get("modulelabel").toString(), (k1, k2) -> k1));

        List<Object> lists = new LinkedList<>();
        try{
            String profileid = ProfileUtils.getCurrentProfileId();
            List<Object> approvalcenters = XN_Query.contentQuery().tag("approvalcenters")
                    .filter("type","eic","approvalcenters")
                    .notDelete()
                    .filter("my.approvers","=",profileid)
                    .filter("my.approvalstatus","=","1")
                    .order("published","DESC")
                    .end(-1).execute();

            List<String> profileids =  new ArrayList<>();
            for(Object item: approvalcenters){
                Content content = (Content)item;
                 if (Utils.isNotEmpty(content.my.get("sourcer"))) {
                     if (!profileids.contains(content.my.get("sourcer").toString())) {
                         profileids.add(content.my.get("sourcer").toString());
                     }
                 }
            }
            Map<String, Object> givennames = new HashMap<>();
            if (!profileids.isEmpty()) {
                givennames = (Map<String, Object>)ProfileUtils.getProfileGivenNames(profileids);
            }
            for(Object item: approvalcenters){
                Content content = (Content)item;
                content.my.put("id",content.id);
                content.my.put("author",content.author);
                content.my.put("published",content.published);
                content.my.put("updated",content.updated);
                content.my.put("title",content.title);
                if (content.my.containsKey("modulename")) {
                    content.my.put("module", content.my.get("modulename"));
                    if (modules.containsKey(content.my.get("modulename"))) {
                        content.my.put("modulename", modules.get(content.my.get("modulename")));
                    }
                }
                if (content.my.containsKey("sourcer")) {
                    if (givennames.containsKey(content.my.get("sourcer"))) {
                        content.my.put("sourcer", givennames.get(content.my.get("sourcer")));
                    }
                }
                lists.add(content.my);
            }

            List<Object> workflowcenters = XN_Query.contentQuery().tag("workflowcenters")
                    .filter("type","eic","workflowcenters")
                    .notDelete()
                    .filter("my.chulirenyuan","=",profileid)
                    .order("published","DESC")
                    .end(-1).execute();

            List<String> workflow_profileids =  new ArrayList<>();
            for(Object item: workflowcenters){
                Content content = (Content)item;
                if (Utils.isNotEmpty(content.my.get("faqirenyuan"))) {
                    if (!workflow_profileids.contains(content.my.get("faqirenyuan").toString())) {
                        workflow_profileids.add(content.my.get("faqirenyuan").toString());
                    }
                }
            }
            Map<String, Object> workflow_givennames = new HashMap<>();
            if (!workflow_profileids.isEmpty()) {
                workflow_givennames = (Map<String, Object>)ProfileUtils.getProfileGivenNames(workflow_profileids);
            }
            for(Object item: workflowcenters){
                Content content = (Content)item;
                content.my.put("id",content.id);
                content.my.put("author",content.author);
                content.my.put("published",content.published);
                content.my.put("updated",content.updated);
                content.my.put("title",content.title);
                if (content.my.containsKey("suoshumokuai")) {
                    content.my.put("module", content.my.get("suoshumokuai"));
                    if (modules.containsKey(content.my.get("suoshumokuai"))) {
                        content.my.put("modulename", modules.get(content.my.get("suoshumokuai")));
                    }
                }
                if (content.my.containsKey("faqirenyuan")) {
                    if (workflow_givennames.containsKey(content.my.get("faqirenyuan"))) {
                        content.my.put("sourcer", workflow_givennames.get(content.my.get("faqirenyuan")));
                    }
                }
                lists.add(content.my);
            }
        }catch (Exception ignored){ }
        return lists;
    }

    @Override
    public String getNoticeElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        final Integer length = properties.containsKey("numLength") ? properties.getIntValue("numLength") : 10;
        try {
            Map<String, Object> maps = new HashMap<>();
            maps.put("title", title);
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);
            maps.put("tableHeight", Integer.parseInt(height) - 80);

            List<TabField> fields = Arrays.asList(
                    new TabField().fieldname("title").fieldlabel("标题").align("center").listwidth("30%"),
                    new TabField().fieldname("publisher").fieldlabel("发布者").align("center"),
                    new TabField().fieldname("published").fieldlabel("发布时间").align("center").listwidth(140),
                    new TabField().fieldname("oper").fieldlabel("操作").align("center").listwidth(40)
            );
            List<Object> entitys = getNoticeEntitys(length);
            maps.put("LISTHEADERS", Utils.objectToJson(getCustomListViewHeader(fields)));
            maps.put("ENTITYMAPS", Utils.objectToJson(entitys));
            return generator(maps, "8");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }
    private List<Object> getNoticeEntitys(Integer length) {
        List<Object> lists = new LinkedList<>();
        try{
            String profileid = ProfileUtils.getCurrentProfileId();
            String thisday = DateTimeUtils.getDatetime("yyyy-MM-dd");
            String ayearago = DateTimeUtils.getDatetime(DateTimeUtils.addYears(-1),"yyyy-MM-dd");
            List<Object> notices;
            if (ProfileUtils.isSupplier()) {
                String supplierid = SupplierUtils.getSupplierid();
                String filter1 = XN_Filter.all(XN_Filter.filter("my.noticetype","=","supplier"), XN_Filter.filter("my.supplierid", "=",supplierid));
                String filter2 = XN_Filter.all(XN_Filter.filter("my.noticetype","=","profile"), XN_Filter.filter("my.profileid", "=",profileid));
                notices = XN_Query.create("YearContent").tag("notices")
                        .filter("type","eic","notices")
                        .filter("published", ">=", ayearago + " 00:00:00")
                        .filter("published", "<=", thisday + " 23:59:59")
                        .filter(XN_Filter.any(filter1,filter2))
                        .order("published","DESC").notDelete()
                        .end(length).execute();
            } else {
                String filter1 = XN_Filter.filter("my.noticetype","=","system");
                String filter2 = XN_Filter.all(XN_Filter.filter("my.noticetype","=","profile"), XN_Filter.filter("my.profileid", "=",profileid));
                notices = XN_Query.create("YearContent").tag("notices")
                        .filter("type","eic","notices")
                        .filter("published", ">=", ayearago + " 00:00:00")
                        .filter("published", "<=", thisday + " 23:59:59")
                        .filter(XN_Filter.any(filter1,filter2))
                        .order("published","DESC").notDelete()
                        .end(length).execute();
            }
            for(Object item: notices){
                Content content = (Content)item;
                NoticeLevel noticelevel = NoticeLevel.valueOf(content.my.get("noticelevel").toString());
                NoticeType noticetype = NoticeType.valueOf(content.my.get("noticetype").toString());
                content.my.put("id",content.id);
                content.my.put("author",content.author);
                content.my.put("published",content.published);
                content.my.put("updated",content.updated);
                boolean alreadyread = false;
                if (content.my.containsKey("alreadyreads")) {
                    List<String> alreadyreads = (List<String>)content.my.get("alreadyreads");
                    if (Utils.isNotEmpty(alreadyreads) && alreadyreads.contains(ProfileUtils.getCurrentProfileId())) {
                        alreadyread = true;
                    }
                } else {
                    alreadyread = false;
                }
                if (!alreadyread) {
                    content.my.put("publisher","<div style='font-weight: bold'>" + content.my.get("publisher") + "</div>");
                    content.my.put("published","<div style='font-weight: bold'>" + content.published + "</div>");
                }
                if (content.my.containsKey("noticelevel")) {
                    content.my.put("noticelevel",noticelevel.getLabel());
                }
                if (content.my.containsKey("noticetype")) {
                    content.my.put("noticetype",noticetype.getLabel());
                }
                StringBuilder sb = new StringBuilder();
                sb.append("<div ");
                if (noticelevel == NoticeLevel.warn) {
                    sb.append("style='color: #c5a100;");
                } else if (noticelevel == NoticeLevel.error) {
                    sb.append("style='color: #ff6b68;");
                } else if (noticelevel == NoticeLevel.fatal) {
                    sb.append("style='color: #f92173;");
                }
                if (!alreadyread) {
                    sb.append("font-weight: bold'>");
                } else {
                    sb.append("'>");
                }
                if (noticelevel == NoticeLevel.warn) {
                    sb.append("【提醒通知】");
                 } else if (noticelevel == NoticeLevel.error) {
                    sb.append("【错误通知】");
                } else if (noticelevel == NoticeLevel.fatal) {
                    sb.append("【严重事件通知】");
                } else {
                    sb.append("【普通通知】");
                }
                sb.append(content.title);
                sb.append("</div>");
                content.my.put("title",sb.toString());
                lists.add(content.my);
            }
        }catch (Exception ignored){ }
        return lists;
    }

    @Override
    public String getAnnouncementElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        final Integer length = properties.containsKey("numLength") ? properties.getIntValue("numLength") : 10;
        try {
            Map<String, Object> maps = new HashMap<>();
            maps.put("title", title);
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);
            maps.put("tableHeight", Integer.parseInt(height) - 80);

            List<TabField> fields = Arrays.asList(
                    new TabField().fieldname("title").fieldlabel("标题").align("center").listwidth("30%"),
                    new TabField().fieldname("publisher").fieldlabel("发布者").align("center"),
                    new TabField().fieldname("published").fieldlabel("发布时间").align("center").listwidth(140),
                    new TabField().fieldname("oper").fieldlabel("操作").align("center").listwidth(40)
            );

            List<Object> entitys = getAnnouncementEntitys(length);
            maps.put("LISTHEADERS", Utils.objectToJson(getCustomListViewHeader(fields)));
            maps.put("ENTITYMAPS", Utils.objectToJson(entitys));
            return generator(maps, "9");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    private List<Object> getAnnouncementEntitys(Integer length) {
        List<Object> lists = new LinkedList<>();
        try{
            List<Object> announcements = XN_Query.contentQuery().tag("announcements")
                    .filter("type","eic","announcements")
                    .notDelete()
                    .filter("my.approvalstatus","in",Arrays.asList('2','4'))
                    .order("published","DESC")
                    .end(length).execute();
            for(Object item: announcements){
                Content content = (Content)item;
                content.my.put("id",content.id);
                content.my.put("author",content.author);
                content.my.put("published",content.published);
                content.my.put("updated",content.updated);
                content.my.put("title",content.title);
                lists.add(content.my);
            }
        }catch (Exception ignored){ }
        return lists;
    }

    @Override
    public String getWeatherElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        try {
            Map<String, Object> maps = new HashMap<>();
            maps.put("title", title);
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);

            HttpServletRequest theRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String ip = IpUtil.getIpAddr(theRequest);
            String address = LogUtils.getCityInfo(ip);
            maps.put("cityid", PageDesignUtils.getCityId(address));
            return generator(maps, "15");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    @Override
    public String getClockElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        try {
            Map<String, Object> maps = new HashMap<>();
            maps.put("title", title);
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);

            return generator(maps, "14");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    @Override
    public String getTextElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        try {
            Map<String, Object> maps = new HashMap<>();
            maps.put("title", title);
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);

            String html = "";
            if (properties.containsKey("htmlEdit")) {
                html = properties.getString("htmlEdit");
                html = StringEscapeUtils.unescapeHtml4(html);
            }


            try {
                Map<String, Object> htmlMaps = new HashMap<>();
                htmlMaps.put("projectVersion", GlobalConfig.getProjectVersion());
                Date date = DateTimeUtils.string2date(GlobalConfig.getVersion(), "yyyyMMddHHmm");
                htmlMaps.put("releaseDate", DateTimeUtils.getDatetime(date, "yyyy-MM-dd"));

                SaaSUtils saaSUtils = new SaaSUtils(BaseSaasConfig.getDomain());
                htmlMaps.put("copyright", saaSUtils.getCopyright());
                htmlMaps.put("softwareCompany", saaSUtils.getCompany());
                htmlMaps.put("softwareName", saaSUtils.getPlatformName());
                htmlMaps.put("softwareNickname", saaSUtils.getCompanyNickname());
                htmlMaps.put("softwareDesc", saaSUtils.getPlatformDesc());
                htmlMaps.put("icpBeian", saaSUtils.getICP());
                htmlMaps.put("policeBeian", saaSUtils.getPolice());

                htmlMaps.put("user",new User(ProfileUtils.getCurrentUser()));
                maps.put("html", processTemplate(htmlMaps,html));
            }catch (Exception ignored) {
                maps.put("html", html);
            }

            return generator(maps, "1");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    @Override
    public String getServerElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        try {
            Map<String, Object> maps = new HashMap<>();
            maps.put("title", title);
            maps.put("width", width);
            maps.put("height", height);
            maps.put("projectVersion", GlobalConfig.getProjectVersion());
            Date date = DateTimeUtils.string2date(GlobalConfig.getVersion(), "yyyyMMddHHmm");
            maps.put("releaseDate", DateTimeUtils.getDatetime(date, "yyyy-MM-dd"));
            maps.put("systemInfo", systemInfo.get());

            return generator(maps, "13");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    @Override
    public String getVisitChartElementHtml(JSONObject properties) throws Exception {
        final String title = properties.containsKey("title") ? properties.getString("title") : "";
        final String width = properties.containsKey("width") ? properties.getString("width") : "";
        final String height = properties.containsKey("height") ? properties.getString("height") : "300";
        try {
            Map<String, Object> maps = new HashMap<>();
            maps.put("title", title);
            maps.put("width", width);
            maps.put("height", height);

            String uuid = MD5Util.get(UUID.randomUUID().toString());
            maps.put("uuid", uuid);

            Map<String, Object> data = new HashMap<>(5);
            // 获取近期系统访问记录
            List<Map<String, Object>> lastSevenVisitCount = this.loginLogService.findLastTenDaysVisitCount(null);
            data.put("lastSevenVisitCount", lastSevenVisitCount);
            String profileid = ProfileUtils.getCurrentProfileId();
            List<Map<String, Object>> lastSevenUserVisitCount = this.loginLogService.findLastTenDaysVisitCount(profileid);
            data.put("lastSevenUserVisitCount", lastSevenUserVisitCount);
            maps.put("visitData", JSONObject.toJSONString(data));
            return generator(maps, "12");
        } catch (Exception e) {
            return getExceptionHtml(e.getMessage(),title,width,height);
        }
    }

    @Override
    public String getNoUiTypeHtml(String uitype) throws Exception {
        return getExceptionHtml("UITYPE("+uitype+")没有进行定义","警告","","");
    }

    public String getExceptionHtml(String message, String title, String width, String height) throws Exception {
        try {
            Map<String, Object> maps = new HashMap<>();
            maps.put("title", title);
            maps.put("width", width);
            maps.put("height", height);
            maps.put("message", message);
            return generator(maps, "DisplayError");
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("<div>");
            sb.append(e.getMessage());
            sb.append("</div>");
            return sb.toString();
        }
    }

    private String generator(Map<String, Object> map,String uiType) throws Exception {
        try {
            TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("html", TemplateConfig.ResourceMode.CLASSPATH));
            Template template = engine.getTemplate("views/modules/settings/pagedesigns/uitype/"+uiType+".html");
            return template.render(map);
        } catch (TemplateException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 解析字符串模板
     * @param maps
     * @param temp
     * @throws Exception
     */
    private String processTemplate(Map<String, Object> maps,String temp) throws Exception {
        try {
            TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
            Template template = engine.getTemplate(temp);
            return template.render(maps);
        } catch (TemplateException e) {
            throw new Exception(e.getMessage());
        }
    }


    private List<Object> getCustomListViewHeader(List<TabField> fields) {
        List<Object> viewHeader = new ArrayList<>();
        for (TabField item : fields) {
            String label = item.fieldlabel;
            String align = item.align;
            String width = item.listwidth;
            Map<String, Object> infoMap = new HashMap<>(1);
            infoMap.put("field", item.fieldname);
            infoMap.put("title", label);
            infoMap.put("align", align);
            if (!width.isEmpty()) {
                if (width.contains("%")) {
                    infoMap.put("width", width);
                }else {
                    infoMap.put("width", Integer.parseInt(width));
                }
            }
            infoMap.put("escape", false);
            if ("oper".equals(item.fieldname)) {
                infoMap.put("fixed", "right");
                infoMap.put("templet", "#page_design_oper");
            }
            viewHeader.add(infoMap);
        }
        return viewHeader;
    }

}
