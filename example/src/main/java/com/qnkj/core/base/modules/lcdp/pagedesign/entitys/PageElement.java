package com.qnkj.core.base.modules.lcdp.pagedesign.entitys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @author Auto Generator
* @date 2021-06-25
*/
@Getter
@Setter
public class PageElement {

	@ApiModelProperty("类型")
	public String dataType = "element";

	@ApiModelProperty("布局宽度")
	public String layoutWidth = "";

	@ApiModelProperty("组件类型")
	public String uiType = "";

	@ApiModelProperty("模板引擎生成后的HTML")
	public String html = "";

	public PageElement() {
	}
}
