package com.qnkj.clouds.modules.VmZones.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
* @author Auto Generator
* @date 2023-06-27
*/
@Getter
@Setter
public class VmZones extends BaseRecordConfig {

	@ApiModelProperty("名称")
	public String name = "";

	@ApiModelProperty("简介")
	public String describe = "";

	@ApiModelProperty("启用")
	public String status = "Active";

	@ApiModelProperty("连接配置")
	public String yaml = "";

	@ApiModelProperty("连接状态")
	public String state = "";


	public VmZones() {
		this.id = "";
	}

	public VmZones(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) { return; }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
