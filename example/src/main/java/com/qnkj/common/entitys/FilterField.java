package com.qnkj.common.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class FilterField extends BaseRecordConfig {

    @ApiModelProperty("筛选名称")
    public String name = "";
    @ApiModelProperty("描述")
    public String describe = "";
    @ApiModelProperty("字段")
    public String fieldName = "";
    @ApiModelProperty("操作符")
    public Symbol symbol = Symbol.EQUAL;
    @ApiModelProperty("默认值")
    public String defaultValue = "";
    @ApiModelProperty("必填")
    public Boolean required = false;

    public FilterField() {
        this.id = "";
    }

    public FilterField(Object content) {
        this.id = "";
        if(Utils.isEmpty(content)) {
            return;
        }
        if(content instanceof Content) {
            this.fromContent(content);
            Content obj = (Content)content;
            if (obj.my.containsKey("symbol") && Utils.isNotEmpty(obj.my.get("symbol"))) {
                this.symbol = Symbol.of(obj.my.get("symbol").toString());
            }
        }else if(content instanceof HashMap){
            this.fromRequest(content);
            HashMap<String,Object> obj = (HashMap)content;
            if (obj.containsKey("symbol") && Utils.isNotEmpty(obj.get("symbol"))) {
                this.symbol = Symbol.of(obj.get("symbol").toString());
            }
        }
    }
    public FilterField name(String value) {
        this.name = value;
        return this;
    }
    public FilterField describe(String value) {
        this.describe = value;
        return this;
    }
    public FilterField fieldName(String value) {
        this.fieldName = value;
        return this;
    }
    public FilterField symbol(String value) {
        this.symbol = Symbol.of(value);
        return this;
    }
    public FilterField defaultValue(String value) {
        this.defaultValue = value;
        return this;
    }
    public FilterField required(Boolean value) {
        this.required = value;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new FilterField()");
        sb.append(".name(\"").append(Utils.replaceString(this.name)).append("\")");
        sb.append(".describe(\"").append(Utils.replaceString(this.describe)).append("\")");
        sb.append(".fieldName(\"").append(Utils.replaceString(this.fieldName)).append("\")");
        sb.append(".symbol(\"").append(this.symbol.getValue()).append("\")");
        sb.append(".defaultValue(\"").append(Utils.replaceString(this.defaultValue)).append("\")");
        if(this.required) {
            sb.append(".required(true)");
        }
        return sb.toString();
    }
}
