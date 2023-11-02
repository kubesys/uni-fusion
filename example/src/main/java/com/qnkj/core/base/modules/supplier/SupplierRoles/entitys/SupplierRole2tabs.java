package com.qnkj.core.base.modules.supplier.SupplierRoles.entitys;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;

public class SupplierRole2tabs extends BaseRecordConfig {
    @ApiModelProperty("权限ID")
    public String record = "";
    @ApiModelProperty("模块分类")
    public String classify = "";
    @ApiModelProperty("父ID")
    public String parentid = "";
    @ApiModelProperty("模块ID")
    public String moduleid = "";
    @ApiModelProperty("模块名称")
    public String module = "";
    @ApiModelProperty("是否可视")
    public boolean isview = false;
    @ApiModelProperty("是否可编辑")
    public boolean isedit = false;
    @ApiModelProperty("是否可删除")
    public boolean isdelete = false;

    public SupplierRole2tabs() {
        this.id = "";
    }

    public SupplierRole2tabs(Object content) {
        this.id = "";
        this.fromContent(content);
    }

}
