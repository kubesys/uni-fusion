package com.qnkj.clouds.modules.VmClusters.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
* @author Auto Generator
* @date 2023-07-09
*/
@Getter
@Setter
public class VmClusters extends BaseRecordConfig {

	@ApiModelProperty("名称")
	public String name = "";

	@ApiModelProperty("简介")
	public String describe = "";

	@ApiModelProperty("UID")
	public String uid = "";

	@ApiModelProperty("启用")
	public String status = "Active";

	@ApiModelProperty("虚拟化技术")
	public String virtualization = "kvm";

	@ApiModelProperty("区域")
	public String zone = "";

	@ApiModelProperty("物理机数量")
	public String physicalmachine = "0";

	public VmClusters() {
		this.id = "";
	}

	public VmClusters(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) { return; }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
