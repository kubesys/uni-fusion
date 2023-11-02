package com.qnkj.core.base.modules.lcdp.pagedesign.services;

import com.alibaba.fastjson.JSONObject;

/**
 * create by Auto Generator
 * create date 2021-06-25
 */
public interface IpageDataSourceService {

       String getTextElementHtml(JSONObject properties) throws Exception;
       String getServerElementHtml(JSONObject properties) throws Exception;
       String getVisitChartElementHtml(JSONObject properties) throws Exception;
       String getClockElementHtml(JSONObject properties) throws Exception;
       String getWeatherElementHtml(JSONObject properties) throws Exception;

       String getAnnouncementElementHtml(JSONObject properties) throws Exception;
       String getNoticeElementHtml(JSONObject properties) throws Exception;
       String getApprovalElementHtml(JSONObject properties) throws Exception;

       String getLabelElementHtml(JSONObject properties) throws Exception;

       String getLineChartElementHtml(JSONObject properties) throws Exception;
       String getBarChartElementHtml(JSONObject properties) throws Exception;
       String getPieChartElementHtml(JSONObject properties) throws Exception;
       String getRadarChartElementHtml(JSONObject properties) throws Exception;
       String getDoughnutChartElementHtml(JSONObject properties) throws Exception;
       String getFunnelChartElementHtml(JSONObject properties) throws Exception;

       String getNoUiTypeHtml(String uitype) throws Exception;
}
