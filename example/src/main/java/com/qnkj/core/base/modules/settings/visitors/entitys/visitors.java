package com.qnkj.core.base.modules.settings.visitors.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
* @author Auto Generator
* @date 2023-05-31
*/
@Getter
@Setter
public class visitors extends BaseRecordConfig {

	@ApiModelProperty("appid")
	public String appid = "";

	@ApiModelProperty("密钥")
	public String secret = "";

	@ApiModelProperty("随机生成32位密钥")
	public String makesecret = "";

	@ApiModelProperty("授权企业")
	public String supplierid = "";

	@ApiModelProperty("启用")
	public String status = "Active";


	public visitors() {
		this.id = "";
	}

	public visitors(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) { return; }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
