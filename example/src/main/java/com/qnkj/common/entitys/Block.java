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

public class Block extends BaseRecordConfig {
    @ApiModelProperty("模块名称")
    public String modulename = "";

    @ApiModelProperty("区块标签")
    public String blocklabel = "";

    @ApiModelProperty("区块ID")
    public Integer blockid;

    @ApiModelProperty("区块列数")
    public Integer columns = 2;

    @ApiModelProperty("是否显示标签")
    public Boolean showtitle = true;

    @ApiModelProperty("区块排序号")
    public Integer sequence;

    public Block() {
        this.id = "";
    }

    public Block(Object content) {
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
     * 数据库表名
     */
    public Block modulename(String modulename) {
        this.modulename = modulename;
        return this;
    }

    /**
     * 区块标签
     */
    public Block blocklabel(String blocklabel) {
        this.blocklabel = blocklabel;
        return this;
    }

    /**
     * 区块ID
     */
    public Block blockid(Integer blockid) {
        this.blockid = blockid;
        return this;
    }

    /**
     * 区块展示列表
     */
    public Block columns(Integer columns) {
        this.columns = columns;
        return this;
    }

    /**
     * 是否显示区块标签
     */
    public Block showtitle(Boolean showtitle) {
        this.showtitle = showtitle;
        return this;
    }

    /**
     * 区块排序号
     */
    public Block sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new Block()");
        sb.append(".blocklabel(\"").append(Utils.replaceString(this.blocklabel)).append("\")");
        sb.append(".blockid(").append(this.blockid).append(")");
        if(this.columns != 2){
            sb.append(".columns(").append(this.columns).append(")");
        }
        if(Boolean.FALSE.equals(this.showtitle)){
            sb.append(".showtitle(false)");
        }
        return sb.toString();
    }
}
