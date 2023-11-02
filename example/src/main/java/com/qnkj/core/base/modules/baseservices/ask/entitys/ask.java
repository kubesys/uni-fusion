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
public class ask extends BaseRecordConfig {

	@ApiModelProperty("咨询人")
	public String profileid = "";

	@ApiModelProperty("最后咨询内容")
	public String lastaskbody = "";

	@ApiModelProperty("最后咨询时间")
	public String lastasktime = "";

	@ApiModelProperty("最后回复时间")
	public String lastreplytime = "";

	@ApiModelProperty("已回复")
	public String replied = "";

	@ApiModelProperty("回复人")
	public String respondent = "";

	@ApiModelProperty("状态")
	public String askstatus = "";


	public ask() {
		this.id = "";
	}

	public ask(Object dataObj) {
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
