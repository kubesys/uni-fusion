package com.qnkj.clouds.modules.VmInstanceOfferings.entitys;

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
public class VmInstanceOfferings extends BaseRecordConfig {

	@ApiModelProperty("名称")
	public String name = "";

	@ApiModelProperty("CPU")
	public Integer cpu = 0;

	@ApiModelProperty("内存")
	public Integer memory = 0;

	@ApiModelProperty("简介")
	public String describe = "";

	@ApiModelProperty("启用")
	public String status = "Active";


	public VmInstanceOfferings() {
		this.id = "";
	}

	public VmInstanceOfferings(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) { return; }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
