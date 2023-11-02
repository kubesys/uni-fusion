package com.qnkj.core.base.entitys;


import com.alibaba.fastjson.JSONArray;
import com.qnkj.common.utils.Utils;
import com.qnkj.core.base.modules.lcdp.pagedesign.entitys.PageDesignField;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
public class ReportParam {
    /**
     * 模块名称
     */
    String module = "";
    /**
     * 表名
     */
    String tabName = "";
    /**
     * 表存储类型
     */
    String dataType = "";
    /**
     * 模块权限
     */
    String dataRole = "";
    /**
     * X轴
     */
    String xDataLine = "";
    /**
     * Z轴
     */
    String zDataLine = "";
    /**
     * 单一Y轴
     */
    String yDataLine = "";
    /**
     * Y轴折算
     */
    String convert = "";
    /**
     * 多Y轴
     */
    List<String> yDataLineMore = new ArrayList<>();
    /**
     * 时间区间
     */
    String timeSection = "";
    /**
     * 数据起始时间
     */
    String startDate = "";
    /**
     * 数据结束时间
     */
    String endDate = "";
    /**
     * 时间格式,X轴是published生效
     */
    String timeFormat = "";
    /**
     * 是否填充X轴，补充时间轴，对短期，按天统计生效
     */
    Boolean fillxAxis = false;
    /**
     * 是否排序，对第一个Y轴生效
     */
    String sort = "";
    /**
     * 取前N个
     */
    Integer topN = -1;
    /**
     * 模块相关的字段数据
     */
    HashMap<String, PageDesignField> fields = new HashMap<>();
    /**
     * Y轴单位
     */
    HashMap<String,String> unitMaps = new HashMap<>();
    /**
     * Y轴折算
     */
    HashMap<String,String> convertMaps = new HashMap<>();
    /**
     * Y轴颜色
     */
    HashMap<String,String> colorMaps = new HashMap<>();
    /**
     * 复杂自定义查询条件
     */
    JSONArray condition  = null;

    public ReportParam(String module, String tabName, String dataType,String dataRole){
        this.module=module;
        this.tabName=tabName;
        this.dataType=dataType;
        this.dataRole=dataRole;
    }

    public void setTimeSection(String timeSection) {
        this.timeSection = timeSection;
        if (Utils.isNotEmpty(timeSection) && Arrays.asList("today","onew","tenday","halfm","onem","twom","threem","halfy","oney","twoy","threey").contains(timeSection)) {
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            if ("today".equals(timeSection)) {
            } else if ("onew".equals(timeSection)) {
                calendar.add(Calendar.DATE, -7);
            } else if ("tenday".equals(timeSection)) {
                calendar.add(Calendar.DATE, -10);
            } else if ("halfm".equals(timeSection)) {
                calendar.add(Calendar.DATE, -15);
            } else if ("onem".equals(timeSection)) {
                calendar.add(Calendar.MONTH, -1);
            } else if ("twom".equals(timeSection)) {
                calendar.add(Calendar.MONTH, -2);
            } else if ("threem".equals(timeSection)) {
                calendar.add(Calendar.MONTH, -3);
            } else if ("halfy".equals(timeSection)) {
                calendar.add(Calendar.MONTH, -6);
            } else if ("oney".equals(timeSection)) {
                calendar.add(Calendar.MONTH, -12);
            } else if ("twoy".equals(timeSection)) {
                calendar.add(Calendar.MONTH, -24);
            } else if ("threey".equals(timeSection)) {
                calendar.add(Calendar.MONTH, -36);
            }
            SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            this.startDate = startDateFormat.format(calendar.getTime());
            this.endDate = endDateFormat.format(today);
        }
    }
}
