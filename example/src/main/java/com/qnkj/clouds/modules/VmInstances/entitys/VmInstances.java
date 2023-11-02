package com.qnkj.clouds.modules.VmInstances.entitys;

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
public class VmInstances extends BaseRecordConfig {

	@ApiModelProperty("名称")
	public String name = "";

	@ApiModelProperty("描述")
	public String describe = "";

	@ApiModelProperty("节点")
	public String node = "";

	@ApiModelProperty("事件ID")
	public String eventid = "";

	@ApiModelProperty("生成事件ID")
	public String make_event_id = "";

	@ApiModelProperty("就绪状态")
	public String state = "notconnected";

	@ApiModelProperty("创建时间")
	public String createtime = "";

	@ApiModelProperty("UID")
	public String uid = "";

	@ApiModelProperty("CPU")
	public String cpu_vendor = "";

	@ApiModelProperty("CPU数量")
	public String vcpu = "";

	@ApiModelProperty("操作系统")
	public String os_arch = "";

	@ApiModelProperty("机器名称")
	public String os_machine = "";

	@ApiModelProperty("当前内存")
	public String current_memory = "";

	@ApiModelProperty("分配内存")
	public String memory = "";

	@ApiModelProperty("计算规格")
	public String offering = "";

	@ApiModelProperty("镜像")
	public String image = "";

	@ApiModelProperty("云盘")
	public String disk = "";

	@ApiModelProperty("UUID")
	public String uuid = "";

	@ApiModelProperty("操作系统")
	public String osvariant = "";

	@ApiModelProperty("虚拟化类型")
	public String virt_type = "";

	@ApiModelProperty("光驱Livecd类型")
	public String livecd = "";

	@ApiModelProperty("VNC密码")
	public String graphics = "";

	@ApiModelProperty("端口")
	public String port = "";

	@ApiModelProperty("区域")
	public String zone = "";


	public VmInstances() {
		this.id = "";
	}

	public VmInstances(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) { return; }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
