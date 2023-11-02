package com.qnkj.clouds.modules.VmSDNControllers.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
* @author Auto Generator
* @date 2023-07-09
*/
@Getter
@Setter
public class VmSDNControllers extends BaseRecordConfig {

	public VmSDNControllers() {
		this.id = "";
	}

	public VmSDNControllers(Object dataObj) {
		this.id = "";
		if(Utils.isEmpty(dataObj)) { return; }
		if(dataObj instanceof HashMap){
			this.fromRequest(dataObj);
		}else if(dataObj instanceof Content) {
			this.fromContent(dataObj);
		}
	}
}
