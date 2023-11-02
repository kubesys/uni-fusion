package com.qnkj.core.base.entitys;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ReportResult {
    /**
     * Y轴字段
     */
    String field = "";
    /**
     * Y轴名称，带单位
     */
    String name = "";
    /**
     * 键值对数据
     */
    List<Map<String, Object>> maps = new ArrayList<>();
    /**
     * X轴列表
     */
    List<String> xAxis = new ArrayList<>();
    /**
     * Y轴数据列表
     */
    List<Object> yAxis = new ArrayList<>();
    /**
     * 多Y轴名称列表
     */
    List<String> legends = new ArrayList<>();

    public ReportResult(String field, String name){
        this.field=field;
        this.name=name;
    }
    public ReportResult(String field, String name,List<String> xAxis,List<Object> yAxis,List<String> legends){
        this.field=field;
        this.name=name;
        this.xAxis=xAxis;
        this.yAxis=yAxis;
        this.legends=legends;
        this.maps.clear();
        if (xAxis.size() == yAxis.size()) {
            for(int i=0;i<xAxis.size();i++) {
                Map<String,Object> info = new HashMap<>(1);
                info.put("name",xAxis.get(i));
                info.put("value",yAxis.get(i));
                maps.add(info);
            }
        }
    }
    public ReportResult(String field, String name,List<String> legends,List<String> xAxis,List<Object> yAxis,List<Map<String, Object>> maps){
        this.field=field;
        this.name=name;
        this.xAxis=xAxis;
        this.yAxis=yAxis;
        this.legends=legends;
        this.maps=maps;
    }

}
