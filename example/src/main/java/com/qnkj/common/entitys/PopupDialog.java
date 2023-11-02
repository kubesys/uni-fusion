package com.qnkj.common.entitys;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public class PopupDialog {
    @ApiModelProperty("记录ID")
    public String id = "";

    @ApiModelProperty("模块名称")
    public String modulename = "";

    @ApiModelProperty("弹窗显示的列")
    public List<String> columns = new ArrayList<>();

    @ApiModelProperty("弹窗可查询的列")
    public List<String> search = new ArrayList<>();

    @ApiModelProperty("记录ID")
    public long deleted = 0;

    /**
     * 数据表名
     */
    public PopupDialog modulename(String modulename){
        this.modulename = modulename;
        return this;
    }

    /**
     * 弹窗列表展示的字段列表
     */
    public PopupDialog columns(List<String> columns){
        this.columns = columns;
        return this;
    }

    /**
     * 弹窗时，可用于查询的字段列表
     */
    public PopupDialog search(List<String> search){
        this.search = search;
        return this;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String joinDelimiter = "\",\"";
        sb.append("new PopupDialog()");
        if (this.search.size() > 1) {
            sb.append(".search(Arrays.asList(\"").append(String.join(joinDelimiter, this.search)).append("\"))");
        }else if(!this.search.isEmpty()){
            sb.append(".search(Collections.singletonList(\"").append(String.join(joinDelimiter, this.search)).append("\"))");
        }
        if (this.columns.size() > 1) {
            sb.append(".columns(Arrays.asList(\"").append(String.join(joinDelimiter, this.columns)).append("\"))");
        }else if(!this.columns.isEmpty()){
            sb.append(".columns(Collections.singletonList(\"").append(String.join(joinDelimiter, this.columns)).append("\"))");
        }
        return sb.toString();
    }

    public String toConfigString() {
        if(!this.search.isEmpty() || !this.columns.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            String joinDelimiter = "\",\"";
            sb.append("popupDialog");
            if (this.search.size() > 1) {
                sb.append(".search(Arrays.asList(\"").append(String.join(joinDelimiter, this.search)).append("\"))");
            }else if(!this.search.isEmpty()){
                sb.append(".search(Collections.singletonList(\"").append(String.join(joinDelimiter, this.search)).append("\"))");
            }
            if (this.columns.size() > 1) {
                sb.append(".columns(Arrays.asList(\"").append(String.join(joinDelimiter, this.columns)).append("\"))");
            }else if(!this.columns.isEmpty()){
                sb.append(".columns(Collections.singletonList(\"").append(String.join(joinDelimiter, this.columns)).append("\"))");
            }
            return sb.toString();
        }
        return "";
    }
}
