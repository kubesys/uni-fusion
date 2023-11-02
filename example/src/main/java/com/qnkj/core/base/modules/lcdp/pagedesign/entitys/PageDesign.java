package com.qnkj.core.base.modules.lcdp.pagedesign.entitys;

import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.common.utils.Utils;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;

/**
* @author Auto Generator
* @date 2021-06-25
*/
public class PageDesign extends BaseRecordConfig {
	@ApiModelProperty("菜单组")
	public String program = "";

	@ApiModelProperty("模块组")
	public String parent = "";

	@ApiModelProperty("模块名称")
	public String modulename = "";

	@ApiModelProperty("模块标签")
	public String module = "";

	@ApiModelProperty("设计内容")
	public String template_editor = "";

	@ApiModelProperty("是否生成模块代码")
	public Boolean generate = false;

	public PageDesign() {
		this.id = "";
	}

	public PageDesign(Object dataObj) {
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
