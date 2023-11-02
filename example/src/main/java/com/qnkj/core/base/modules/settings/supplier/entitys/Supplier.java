package com.qnkj.core.base.modules.settings.supplier.entitys;

import com.qnkj.common.configs.BaseRecordConfig;
import io.swagger.annotations.ApiModelProperty;

/**
* @author Auto Generator
* @date 2020-11-23
*/
public class Supplier extends BaseRecordConfig {

	@ApiModelProperty("客户编号")
	public String suppliers_no = "";

	@ApiModelProperty("客户全称")
	public String suppliers_name = "";

	@ApiModelProperty("客户简称")
	public String suppliers_shortname = "";

	@ApiModelProperty("客户编码")
	public String suppliers_code = "";

	@ApiModelProperty("用户名称")
	public String username = "";

	@ApiModelProperty("手机号码")
	public String mobile = "";

	@ApiModelProperty("密码")
	public String password = "";

	@ApiModelProperty("国家代码")
	public String regioncode = "";

	@ApiModelProperty("姓名")
	public String givenname = "";

	@ApiModelProperty("用户邮箱")
	public String email = "";

	@ApiModelProperty("省份")
	public String province = "";

	@ApiModelProperty("城市")
	public String city = "";

	@ApiModelProperty("注册IP")
	public String regip = "";

	@ApiModelProperty("注册系统")
	public String system = "";

	@ApiModelProperty("注册浏览器")
	public String browser = "";

	@ApiModelProperty("企业法人")
	public String legal_person_name = "";

	@ApiModelProperty("法人身份证号码")
	public String legal_person_identity_card = "";

	@ApiModelProperty("法人证件(正面)")
	public String legal_person_certificate_img_url = "";

	@ApiModelProperty("法人证件(反面)")
	public String legal_person_certificate_reverse_img_url = "";

	@ApiModelProperty("营业执照")
	public String business_license = "";

	@ApiModelProperty("营业执照")
	public String business_license_img_url = "";

	@ApiModelProperty("负责人用户")
	public String profileid = "";

	public Supplier() {
		this.id = "";
	}
	public Supplier(Object content) {
		if(content instanceof String){
			this.fromJson(content.toString());
		}else {
			this.fromContent(content);
		}
	}
}
