package com.qnkj.core.base.modules.supplier.Supplierloginlog.entitys;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

/**
* @author Auto Generator
* @date 2021-05-23
*/
@NoArgsConstructor
public class supplierloginlog extends BaseRecordConfig {
	@ApiModelProperty("账户")
	public String profileid = "";

	@ApiModelProperty("登录IP")
	public String ip = "";

	@ApiModelProperty("登录时间")
	public String logintime = "";

	@ApiModelProperty("登录地区")
	public String location = "";

	@ApiModelProperty("操作系统")
	public String system = "";

	@ApiModelProperty("浏览器")
	public String browser = "";


	public supplierloginlog(Object content) {
		this.fromContent(content);
	}
}
