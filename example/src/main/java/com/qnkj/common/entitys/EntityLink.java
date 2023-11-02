package com.qnkj.common.entitys;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public class EntityLink extends BaseRecordConfig {
    @ApiModelProperty("模块名称")
    public String modulename = "";

    @ApiModelProperty("关联字段名")
    public String entityfield = "xn_id";

    @ApiModelProperty("返回数据字段")
    public String fieldname = "";

    public EntityLink() {
        this.id = "";
    }

    public EntityLink(Object content) {
        this.id = "";
        this.fromContent(content);
    }

    public EntityLink modulename(String modulename){
        this.modulename = modulename;
        return this;
    }

    /**
     * 外部模块传入值所对应的本模块字段名
     */
    public EntityLink entityfield(String entityfield){
        this.entityfield = entityfield;
        return this;
    }

    /**
     * 返回本模块数据的字段名
     */
    public EntityLink fieldname(String fieldname){
        this.fieldname = fieldname;
        return this;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new EntityLink()");
        sb.append(".fieldname(\"").append(this.fieldname).append("\")");
        sb.append(".entityfield(\"").append(this.entityfield).append("\")");
        return sb.toString();
    }

    public String toConfigString() {
        StringBuilder sb = new StringBuilder();
        sb.append("entityNames").append(".fieldname(\"").append(this.fieldname).append("\")");
        if(!"xn_id".equals(this.entityfield)){
            sb.append(".entityfield(\"").append(this.entityfield).append("\")");
        }
        return sb.toString();
    }
}
