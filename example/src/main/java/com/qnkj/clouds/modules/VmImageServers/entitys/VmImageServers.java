package com.qnkj.clouds.modules.VmImageServers.entitys;

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
public class VmImageServers extends BaseRecordConfig {

	@ApiModelProperty("区域")
	public String zone = "";

	@ApiModelProperty("名称")
	public String name = "";

	@ApiModelProperty("简介")
	public String describe = "";

	@ApiModelProperty("类型")
	public String vmimageservertype = "";

	@ApiModelProperty("服务器IP")
	public String serverip = "";

	@ApiModelProperty("挂载路径")
	public String url = "";

	@ApiModelProperty("端口")
	public String port = "";

	@ApiModelProperty("用户名")
	public String username = "";

	@ApiModelProperty("密码")
	public String password = "";

	@ApiModelProperty("启用")
	public String status = "Active";

	@ApiModelProperty("就绪状态")
	public String state = "notconnected";



	public VmImageServers() {
		this.id = "";
	}

	public VmImageServers(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) { return; }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
