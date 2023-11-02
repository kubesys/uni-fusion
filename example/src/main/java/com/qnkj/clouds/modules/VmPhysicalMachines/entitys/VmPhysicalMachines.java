package com.qnkj.clouds.modules.VmPhysicalMachines.entitys;

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
public class VmPhysicalMachines extends BaseRecordConfig {

	@ApiModelProperty("名称")
	public String name = "";

	@ApiModelProperty("UID")
	public String uid = "";

	@ApiModelProperty("就绪状态")
	public String state = "";

	@ApiModelProperty("创建时间")
	public String createtime = "";

	@ApiModelProperty("IP")
	public String internalip = "";

	@ApiModelProperty("主机名")
	public String hostname = "";

	@ApiModelProperty("CPU")
	public String cpu = "";

	@ApiModelProperty("磁盘容量")
	public String ephemeral_storage = "";

	@ApiModelProperty("内存")
	public String memory = "";

	@ApiModelProperty("架构")
	public String architecture = "";

	@ApiModelProperty("内核版本")
	public String kernelVersion = "";

	@ApiModelProperty("K8S版本")
	public String kubeletVersion = "";

	@ApiModelProperty("操作系统")
	public String operatingSystem = "";

	@ApiModelProperty("镜像")
	public String osImage = "";

	@ApiModelProperty("启用")
	public String status = "Active";

	@ApiModelProperty("区域")
	public String zone = "";

	@ApiModelProperty("集群")
	public String cluster = "";


	public VmPhysicalMachines() {
		this.id = "";
	}

	public VmPhysicalMachines(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) { return; }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
