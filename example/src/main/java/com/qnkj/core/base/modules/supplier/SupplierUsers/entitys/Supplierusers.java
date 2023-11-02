package com.qnkj.core.base.modules.supplier.SupplierUsers.entitys;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

/**
* @author Auto Generator
* @date 2021-03-31
*/
@NoArgsConstructor
public class Supplierusers extends BaseRecordConfig {
	@ApiModelProperty("账号名称")
	public String account = "";

	@ApiModelProperty("姓名")
	public String username = "";

	@ApiModelProperty("部门")
	public String departmentid = "";

	@ApiModelProperty("权限")
	public String roleid = "";

	@ApiModelProperty("直接上级")
	public String directsuperior = "";

	@ApiModelProperty("排序号")
	public String sequence = "";

	@ApiModelProperty("手机号码")
	public String mobile = "";

	@ApiModelProperty("邮箱")
	public String mailbox = "";

	@ApiModelProperty("启用")
	public String status = "";

	@ApiModelProperty("用户唯一ID")
	public String profileid = "";

	@ApiModelProperty("用户类型")
	public String usertype = "";

	public Supplierusers(Object content) {
		this.fromContent(content);
	}
}
