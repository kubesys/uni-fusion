package com.qnkj.core.base.modules.supplier.SupplierDepartments.entitys;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
* @author Auto Generator
* @date 2021-03-31
*/
@NoArgsConstructor
public class Supplierdepartments extends BaseRecordConfig {

	@ApiModelProperty("上级部门")
	public String parentid = "";

	@ApiModelProperty("部门名称")
	public String name = "";

	@ApiModelProperty("部门领导")
	public List<String> leadership = new ArrayList<>();

	@ApiModelProperty("主管领导")
	public String mainleadership = "";

	@ApiModelProperty("排序号")
	public String sequence = "";


	public Supplierdepartments(Object content) {
		this.fromContent(content);
	}
}
