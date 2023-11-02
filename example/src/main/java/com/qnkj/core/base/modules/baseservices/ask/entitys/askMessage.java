package com.qnkj.core.base.modules.baseservices.ask.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
* @author Auto Generator
* @date 2021-08-18
*/
@Getter
@Setter
public class askMessage extends BaseRecordConfig {

	@ApiModelProperty("发送人")
	public String from = "";

	@ApiModelProperty("接受人")
	public String to = "";

	@ApiModelProperty("主记录ID")
	public String record = "";

	@ApiModelProperty("消息内容")
	public String body = "";

	@ApiModelProperty("是否签收")
	public String issign = "";

	@ApiModelProperty("是否已读")
	public String isread = "";

	@ApiModelProperty("阅读时间")
	public String readtime = "";

	public askMessage() {
		this.id = "";
	}

	public askMessage(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) {
            return;
        }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
