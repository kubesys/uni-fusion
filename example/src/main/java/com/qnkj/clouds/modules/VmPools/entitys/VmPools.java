package com.qnkj.clouds.modules.VmPools.entitys;

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
public class VmPools extends BaseRecordConfig {

	@ApiModelProperty("名称")
	public String name = "";

	@ApiModelProperty("描述")
	public String describe = "";

	@ApiModelProperty("来源")
	public String vmpooltype = "";

	@ApiModelProperty("类型")
	public String content = "";

	@ApiModelProperty("节点")
	public String node = "";

	@ApiModelProperty("挂载路径")
	public String url = "";


	@ApiModelProperty("事件ID")
	public String eventid = "";

	@ApiModelProperty("生成事件ID")
	public String make_event_id = "";

	@ApiModelProperty("自动启动")
	public Integer autostart = 0;

	@ApiModelProperty("就绪状态")
	public String state = "notactive";

	@ApiModelProperty("运行时间")
	public String age = "";

	@ApiModelProperty("创建时间")
	public String createtime = "";

	@ApiModelProperty("UID")
	public String uid = "";

	@ApiModelProperty("启用")
	public String status = "Active";

	@ApiModelProperty("容量")
	public String capacity = "";

	@ApiModelProperty("区域")
	public String zone = "";


	public VmPools() {
		this.id = "";
	}

	public VmPools(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) { return; }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
