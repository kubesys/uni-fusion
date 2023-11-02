package com.qnkj.core.base.modules.management.notices.entitys;

import com.qnkj.common.configs.BaseRecordConfig;
import com.qnkj.core.utils.ProfileUtils;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
* @author Auto Generator
* @date 2020-11-17
*/
public class Notices extends BaseRecordConfig {

	@ApiModelProperty("标题")
	public String title = "";

	@ApiModelProperty("发布者")
	public String publisher = "";

	@ApiModelProperty("被通知人")
	public String profileid = "";

	@ApiModelProperty("通知类型")
	public String noticetype;

	@ApiModelProperty("通知类型")
	public String noticelevel;

	@ApiModelProperty("已读列表")
	public List<String> alreadyreads = new ArrayList<>();

	@ApiModelProperty("是否已读")
	public String alreadyread = "";

	@ApiModelProperty("内容")
	public String body = "";

	@ApiModelProperty("唯一键")
	public String md5 = "";


	public Notices() {
		this.id = "";
	}
	public Notices(Object content) {
		this.fromContent(content);
		if (this.alreadyreads.indexOf(ProfileUtils.getCurrentProfileId()) != -1) {
			this.alreadyread = "1";
		} else {
			this.alreadyread = "0";
		}
	}

}
