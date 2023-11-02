package com.qnkj.common.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;

/**
 * create by 徐雁
 * create date 2020/11/05
 */

public class ModentityNum extends BaseRecordConfig {
    @ApiModelProperty("模块名称")
    public String semodule = "";

    @ApiModelProperty("使用前缀")
    public String prefix = "";

    @ApiModelProperty("起始编号")
    public Integer start_id = 1;

    @ApiModelProperty("当前编号")
    public Integer cur_id = 1;

    @ApiModelProperty("流水号长度")
    public Integer length = 3;

    @ApiModelProperty("是否包含日期")
    public Boolean include_date = true;

    public ModentityNum() {
        this.id = "";
    }

    public ModentityNum(Object content) {
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

    /**
     * 模块名称
     */
    public ModentityNum modulename(String modulename){
        this.semodule = modulename;
        return this;
    }

    /**
     * 编号前缀
     */
    public ModentityNum prefix(String prefix){
        this.prefix = prefix;
        return this;
    }

    /**
     * 开始索引
     */
    public ModentityNum start_id(Integer start_id){
        this.start_id = start_id;
        return this;
    }

    /**
     * 当前索引
     */
    public ModentityNum cur_id(Integer cur_id){
        this.cur_id = cur_id;
        return this;
    }

    /**
     * 流水号长度
     */
    public ModentityNum length(Integer length){
        this.length = length;
        return this;
    }

    /**
     * 编码是否包含日期
     */
    public ModentityNum include_date(Boolean include_date){
        this.include_date = include_date;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("modentityNums");
        sb.append(".modulename(\"").append(this.semodule).append("\")");
        sb.append(".prefix(\"").append(Utils.replaceString(this.prefix)).append("\")");
        if (this.start_id != 1) {
            sb.append(".start_id(").append(this.start_id).append(")");
        }
        if (this.cur_id != 1) {
            sb.append(".cur_id(").append(this.cur_id).append(")");
        }
        if (this.length != 3) {
            sb.append(".length(").append(this.length).append(")");
        }
        if (!this.include_date) {
            sb.append(".include_date(false)");
        }
        return sb.toString();
    }
}
