package com.qnkj.core.base.modules.supplier.SupplierAuth.entity;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

public class SupplierAuth extends BaseRecordConfig {
    @ApiModelProperty("权限名称")
    public String authorize = "";

    @ApiModelProperty("授予人ID")
    public List<String> userid = new ArrayList<>();

    @ApiModelProperty("授予人名称")
    public List<String> userlist = new ArrayList<>();

    public SupplierAuth() {
        this.id = "";
    }

    public SupplierAuth(Object content) {
        this.id = "";
        this.fromContent(content);
    }

}
