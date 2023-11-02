package com.qnkj.core.base.modules.settings.users.entity;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Users extends BaseRecordConfig {
    @ApiModelProperty("账户名称")
    public String account = "";

    @ApiModelProperty("姓名")
    public String username = "";

    @ApiModelProperty("是否管理员")
    public String is_admin  = "";

    @ApiModelProperty("部门ID")
    public String departmentid = "";

    @ApiModelProperty("权限ID")
    public String roleid = "";

    @ApiModelProperty("直接上级ID")
    public String directsuperior = "";

    @ApiModelProperty("邮箱")
    public String mailbox = "";

    @ApiModelProperty("手机号码")
    public String mobile = "";

    @ApiModelProperty("排序号")
    public String sequence = "";

    @ApiModelProperty("启用")
    public String status = "";

    @ApiModelProperty("用户唯一ID")
    public String profileid = "";

    public Users() {
        this.id = "";
    }

    public Users(Object content) {
        this.id = "";
        this.fromContent(content);
    }

}
