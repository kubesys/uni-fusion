package com.qnkj.common.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * create by 徐雁
 * create date 2021/5/11
 */

public class Layout extends BaseRecordConfig {
    @ApiModelProperty("模块名称")
    public String modulename = "";

    @ApiModelProperty("布局所属区块ID")
    public Integer block = 1;

    @ApiModelProperty("布局列")
    public List<String> columns = new ArrayList<>();

    @ApiModelProperty("列字段")
    public List<String> fields = new ArrayList<>();

    @ApiModelProperty("排序号")
    public Integer sequence = 0;

    public Layout() {
        this.id = "";
    }

    public Layout(Object content) {
        this.id = "";
        if(Utils.isEmpty(content)) {
            return;
        }
        if(content instanceof Content) {
            this.fromContent(content);
        }else if(content instanceof HashMap){
            this.fromRequest(content);
        }
    }

    public Layout modulename(String modulename) {
        this.modulename = modulename;
        return this;
    }

    public Layout block(Integer block) {
        this.block = block;
        return this;
    }

    public Layout sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public Layout columns(List<String> columns) {
        this.columns = columns;
        return this;
    }

    public Layout fields(List<String> fields) {
        this.fields = fields;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new Layout()");
        sb.append(".block(").append(this.block).append(")");
        if (!this.columns.isEmpty()) {
            if(this.columns.size() == 1){
                sb.append(".columns(Collections.singletonList(\"").append(String.join("\",\"", this.columns)).append("\"))");
            }else {
                sb.append(".columns(Arrays.asList(\"").append(String.join("\",\"", this.columns)).append("\"))");
            }
        }
        if (!this.fields.isEmpty()) {
            if(this.fields.size() == 1){
                sb.append(".fields(Collections.singletonList(\"").append(String.join("\",\"", this.fields)).append("\"))");
            }else {
                sb.append(".fields(Arrays.asList(\"").append(String.join("\",\"", this.fields)).append("\"))");
            }
        }
        return sb.toString();
    }

    @Override
    public Layout clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException ignored) {}
        Layout layout = new Layout();
        layout.setValues(this);
        return layout;
    }

    private void setValues(Layout layout){
        super.setValues(this);
        modulename(layout.modulename);
        block(layout.block);
        sequence(layout.sequence);
        columns(new ArrayList<>(layout.columns));
        fields(new ArrayList<>(layout.fields));
    }
}
