package com.qnkj.clouds.modules.VmDiskOfferings.entitys;

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
public class VmDiskOfferings extends BaseRecordConfig {

	@ApiModelProperty("名称")
	public String name = "";

	@ApiModelProperty("容量")
	public Integer capacity = 0;

	@ApiModelProperty("简介")
	public String describe = "";

	@ApiModelProperty("启用")
	public String status = "Active";


	public VmDiskOfferings() {
		this.id = "";
	}

	public VmDiskOfferings(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) { return; }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
