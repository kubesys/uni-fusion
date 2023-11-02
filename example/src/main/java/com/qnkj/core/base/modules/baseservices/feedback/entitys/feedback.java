package com.qnkj.core.base.modules.baseservices.feedback.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
* @author Auto Generator
* @date 2021-12-17
*/
@Getter
@Setter
public class feedback extends BaseRecordConfig {

	@ApiModelProperty("姓名")
	public String name = "";

	@ApiModelProperty("手机号码")
	public String mobile = "";

	@ApiModelProperty("公司名称")
	public String company = "";

	@ApiModelProperty("行业")
	public String industry = "";

	@ApiModelProperty("备注")
	public String memo = "";


	public feedback() {
		this.id = "";
	}

	public feedback(Object dataObj) {
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
