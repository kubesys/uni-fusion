package com.qnkj.clouds.modules.VmDisks.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
* @author Auto Generator
* @date 2023-06-26
*/
@Getter
@Setter
public class VmDisks extends BaseRecordConfig {

	@ApiModelProperty("名称")
	public String name = "";

	@ApiModelProperty("格式")
	public String format = "";

	@ApiModelProperty("简介")
	public String describe = "";

	@ApiModelProperty("云盘规格")
	public String diskoffering = "";

	@ApiModelProperty("主存储")
	public String poolname = "";

	@ApiModelProperty("主存储")
	public String pool = "";

	@ApiModelProperty("URL")
	public String current = "";

	@ApiModelProperty("类型")
	public String vmdisktype = "";

	@ApiModelProperty("事件ID")
	public String eventid = "";

	@ApiModelProperty("生成事件ID")
	public String make_event_id = "";

	@ApiModelProperty("启用")
	public String status = "Active";

	@ApiModelProperty("UID")
	public String uid = "";

	@ApiModelProperty("容量")
	public Integer capacity = 0;

	@ApiModelProperty("节点")
	public String node = "";

	@ApiModelProperty("创建时间")
	public String createtime = "";

	@ApiModelProperty("就绪状态")
	public String state = "NotReady";

	@ApiModelProperty("区域")
	public String zone = "";

	public VmDisks() {
		this.id = "";
	}

	public VmDisks(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) { return; }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
