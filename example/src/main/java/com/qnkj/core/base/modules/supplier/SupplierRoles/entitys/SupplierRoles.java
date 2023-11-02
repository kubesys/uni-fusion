package com.qnkj.core.base.modules.supplier.SupplierRoles.entitys;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;

public class SupplierRoles extends BaseRecordConfig {
    @ApiModelProperty("是否允许删除")
    public boolean allowdeleted = true;

    @ApiModelProperty("权限描述")
    public String description = "";

    @ApiModelProperty("权限名称")
    public String rolename = "";

    @ApiModelProperty("是否超级删除权限")
    public boolean superdelete = false;

    @ApiModelProperty("是否全局查看权限")
    public boolean globalview = false;

    @ApiModelProperty("是否全局编辑权限")
    public boolean globaledit = false;

    public SupplierRoles() {
        this.id = "";
    }

    public SupplierRoles(Object content) {
        this.id = "";
        this.fromContent(content);
    }

}
