package com.qnkj.clouds.modules.VmImages.entitys;

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
public class VmImages extends BaseRecordConfig {

	@ApiModelProperty("名称")
	public String name = "";

	@ApiModelProperty("简介")
	public String describe = "";

	@ApiModelProperty("主存储")
	public String poolname = "";

	@ApiModelProperty("主存储")
	public String pool = "";

	@ApiModelProperty("节点")
	public String node = "";

	@ApiModelProperty("镜像格式")
	public String mediatype = "iso";


	@ApiModelProperty("镜像服务器")
	public String imageserver = "";

	@ApiModelProperty("镜像文件")
	public String imagefile = "";

	@ApiModelProperty("事件ID")
	public String eventid = "";

	@ApiModelProperty("生成事件ID")
	public String make_event_id = "";

	@ApiModelProperty("启用")
	public String status = "Active";

	@ApiModelProperty("就绪状态")
	public String state = "NotReady";

	@ApiModelProperty("UID")
	public String uid = "";

	@ApiModelProperty("创建时间")
	public String createtime = "";

	@ApiModelProperty("URL")
	public String current = "";

	@ApiModelProperty("格式")
	public String format = "";

	@ApiModelProperty("源文件")
	public String source = "";

	@ApiModelProperty("区域")
	public String zone = "";



	public VmImages() {
		this.id = "";
	}

	public VmImages(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) { return; }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
