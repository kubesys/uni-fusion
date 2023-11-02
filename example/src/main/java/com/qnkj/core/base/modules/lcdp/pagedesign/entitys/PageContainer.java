package com.qnkj.core.base.modules.lcdp.pagedesign.entitys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
* @author Auto Generator
* @date 2021-06-25
*/
@Getter
@Setter
public class PageContainer {
	@ApiModelProperty("容器类型")
	public String dataType = "";

	@ApiModelProperty("容器方向")
	public String layoutType = ""; //horizontal,vertical

	@ApiModelProperty("容器内容")
	public List<Object> list = new ArrayList<>();

	@ApiModelProperty("布局宽度")
	public String layoutWidth = "";

	@ApiModelProperty("布局高度")
	public String layoutHeight = "";

	public PageContainer() {
	}
}
