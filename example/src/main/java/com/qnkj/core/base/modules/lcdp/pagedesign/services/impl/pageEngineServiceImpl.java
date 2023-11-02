package com.qnkj.core.base.modules.lcdp.pagedesign.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.restapi.XN_Query;
import com.qnkj.common.utils.Base64Util;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.lcdp.pagedesign.entitys.PageContainer;
import com.qnkj.core.base.modules.lcdp.pagedesign.entitys.PageDesign;
import com.qnkj.core.base.modules.lcdp.pagedesign.entitys.PageElement;
import com.qnkj.core.base.modules.lcdp.pagedesign.services.IpageDataSourceService;
import com.qnkj.core.base.modules.lcdp.pagedesign.services.IpageEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * create by Auto Generator
 * create date 2021-06-25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class pageEngineServiceImpl implements IpageEngineService {
    private final IpageDataSourceService pageDataSourceService;

    @Override
    public void execute(String modulename, Model model) throws Exception {
        List<Object> pagedesigns = XN_Query.contentQuery().tag("pagedesigns")
                .filter("type", "eic", "pagedesigns")
                .notDelete()
                .filter("my.modulename", "=", modulename)
                .end(1).execute();
        if (!pagedesigns.isEmpty()) {
            PageDesign design = new PageDesign(pagedesigns.get(0));
            if (Utils.isNotEmpty(design.template_editor)) {
                try {
                    JSONArray jsonArray = JSONArray.parseArray(Base64Util.base64Decode(design.template_editor));
                    List<Object> htmls = analysisJonsArray(jsonArray);
                    log.info("analysisJonsArray : {} ",htmls);
                    model.addAttribute("HTMLS", htmls);
                } catch (Exception e) {
                    throw new Exception("模板解析出错!");
                }
            }
        }
    }
    /**
     * 解析模板JSON内容
     * @param jsonArray 页面设计JSON数组
     * @throws Exception
     */
    private List<Object> analysisJonsArray(JSONArray jsonArray)throws Exception {
        List<Object> htmls = new ArrayList<>();
        for (int i=0,size=jsonArray.size(); i<size; i++) {
            JSONObject obj = (JSONObject)jsonArray.get(i);
            if (obj.containsKey("datatype")) {
                if ("list".equals(obj.get("datatype"))) {
                    JSONArray zAry = (JSONArray)obj.get("list");
                    PageContainer pagecontainer = new PageContainer();
                    pagecontainer.dataType = "list";
                    if ("horizontal".equals(obj.get("layoutType"))) {
                        pagecontainer.layoutType = "horizontal";
                    } else {
                        pagecontainer.layoutType = "vertical";
                    }
                    if (obj.containsKey("layoutWidth")) {
                        pagecontainer.layoutWidth = obj.getString("layoutWidth");
                    }
                    if (obj.containsKey("layoutHeight")) {
                        pagecontainer.layoutHeight = obj.getString("layoutHeight");
                    }
                    pagecontainer.list = analysisJonsArray(zAry);
                    htmls.add(pagecontainer);
                } else if ("element".equals(obj.get("datatype"))) {
                    if (obj.containsKey("uiType")) {
                        String uiType = obj.get("uiType").toString();
                        JSONObject properties = new JSONObject();
                        if (obj.containsKey("properties")) {
                            properties = (JSONObject)obj.get("properties");
                        }
                        PageElement element = new PageElement();
                        element.uiType = uiType;
                        if (obj.containsKey("layoutWidth")) {
                            element.layoutWidth = obj.getString("layoutWidth");
                        }
                        if ("1".equals(uiType)) {
                            element.html = pageDataSourceService.getTextElementHtml(properties);
                            htmls.add(element);
                        } else if ("2".equals(uiType)) {
                            element.html = pageDataSourceService.getLineChartElementHtml(properties);
                            htmls.add(element);
                        } else if ("3".equals(uiType)) {
                            element.html = pageDataSourceService.getBarChartElementHtml(properties);
                            htmls.add(element);
                        } else if ("4".equals(uiType)) {
                            element.html = pageDataSourceService.getPieChartElementHtml(properties);
                            htmls.add(element);
                        } else if ("5".equals(uiType)) {
                            element.html = pageDataSourceService.getRadarChartElementHtml(properties);
                            htmls.add(element);
                        } else if ("6".equals(uiType)) {
                            element.html = pageDataSourceService.getFunnelChartElementHtml(properties);
                            htmls.add(element);
                        } else if ("7".equals(uiType)) {
                            element.html = pageDataSourceService.getDoughnutChartElementHtml(properties);
                            htmls.add(element);
                        } else if ("12".equals(uiType)) {
                            element.html = pageDataSourceService.getVisitChartElementHtml(properties);
                            htmls.add(element);
                        } else if ("13".equals(uiType)) {
                            element.html = pageDataSourceService.getServerElementHtml(properties);
                            htmls.add(element);
                        } else if ("14".equals(uiType)) {
                            element.html = pageDataSourceService.getClockElementHtml(properties);
                            htmls.add(element);
                        } else if ("15".equals(uiType)) {
                            element.html = pageDataSourceService.getWeatherElementHtml(properties);
                            htmls.add(element);
                        } else if ("8".equals(uiType)) {
                            element.html = pageDataSourceService.getNoticeElementHtml(properties);
                            htmls.add(element);
                        } else if ("9".equals(uiType)) {
                            element.html = pageDataSourceService.getAnnouncementElementHtml(properties);
                            htmls.add(element);
                        } else if ("11".equals(uiType)) {
                            element.html = pageDataSourceService.getApprovalElementHtml(properties);
                            htmls.add(element);
                        } else if ("16".equals(uiType)) {
                            element.html = pageDataSourceService.getLabelElementHtml(properties);
                            htmls.add(element);
                        } else {
                            element.html = pageDataSourceService.getNoUiTypeHtml(uiType);
                            htmls.add(element);
                        }
                    }
                }
            }
        }
//        Collections.reverse(htmls);
        return htmls;
    }


}
