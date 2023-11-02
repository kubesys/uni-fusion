package com.qnkj.core.base.modules.management.announcements.entitys;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;

/**
* @author Auto Generator
* @date 2020-11-17
*/
public class Announcements extends BaseRecordConfig {

	@ApiModelProperty("发布者")
	public String publisher = "";

	@ApiModelProperty("是否置顶")
	public String istop = "0";

	@ApiModelProperty("发布内容")
	public String body = "";

	public Announcements() {
		this.id = "";
	}
	public Announcements(Object content) {
		this.fromContent(content);
	}

}
