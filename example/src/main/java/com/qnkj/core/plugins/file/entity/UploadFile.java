package com.qnkj.core.plugins.file.entity;


import com.github.restapi.models.Content;
import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;


/**
* @author oldhand
* @date 2019-12-27
*/

public class UploadFile extends BaseRecordConfig {

	@ApiModelProperty("唯一主键")
	public String uniquekey = "";

	@ApiModelProperty("URL")
	public String url;

	@ApiModelProperty("文件ID")
	public String fileid = "";

	@ApiModelProperty("文件大小")
	public int filesize = 0;

	@ApiModelProperty("文件名")
	public String filename = "";

	@ApiModelProperty("文件类型")
	public String filetype = "";

	@ApiModelProperty("企业ID")
	public String supplierid = "";


	public UploadFile() {}

	public UploadFile(Content content) {
		this.id = "";
		this.fromContent(content);
	}

}
