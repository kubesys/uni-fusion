package com.qnkj.core.base.modules.baseservices.modifylog.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
* @author Auto Generator
* @date 2021-09-18
*/
@Getter
@Setter
public class modifylog extends BaseRecordConfig {

	@ApiModelProperty("所属模块")
	public String belongmodule = "";

	@ApiModelProperty("记录ID")
	public String record = "";

	@ApiModelProperty("表名")
	public String table = "";

	@ApiModelProperty("编辑内容")
	public String body = "";

	public modifylog() {
		this.id = "";
	}

	public modifylog(Object dataObj) {
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
