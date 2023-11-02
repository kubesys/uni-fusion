package com.qnkj.core.base.modules.supplier.Supplieroperationlog.entitys;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

/**
* @author Auto Generator
* @date 2021-05-23
*/
@NoArgsConstructor
public class supplieroperationlog extends BaseRecordConfig {
	@ApiModelProperty("操作用户")
	public String profileid = "";

	@ApiModelProperty("描述")
	public String description = "";

	@ApiModelProperty("访问路径")
	public String uri = "";

	@ApiModelProperty("方法名")
	public String method = "";

	@ApiModelProperty("日志类型")
	public String log_type = "";

	@ApiModelProperty("请求ip")
	public String ip = "";

	@ApiModelProperty("地址")
	public String address = "";

	@ApiModelProperty("浏览器")
	public String browser = "";

	@ApiModelProperty("请求耗时")
	public String time = "";

	@ApiModelProperty("异常详细")
	public String exception_detail = "";


	public supplieroperationlog(Object content) {
		this.fromContent(content);
	}
}
