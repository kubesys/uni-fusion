package com.qnkj.core.base.entitys;

import com.alibaba.fastjson.JSONObject;


/**
* @author oldhand
* @date 2019-12-27
*/
public class RegisterInfo {

	// ID
	public String id = "";

    // 用户名称
	public String username = "";

    // 国家代码
	public String regioncode = "";

    // 手机号码
	public String mobile = "";

    // 密码
	public String password = "";

    // 昵称
	public String givenname = "";

    // 用户邮箱
	public String email = "";

    // 省份
	public String province = "";

    // 城市
	public String city = "";

    // 注册IP
	public String regip = "";

    // 注册系统
	public String system = "";

    // 注册浏览器
	public String browser = "";

	// 客户全称
	public String suppliers_name = "";

	// 企业法人
	public String legal_person_name = "";

	// 营业执照
	public String business_license = "";

	// 营业执照 url
	public String business_license_img_url = "";

	// 法人身份证号码
	public String legal_person_identity_card = "";

	// 法人证件（正面）url
	public String legal_person_certificate_img_url = "";

	// 法人证件（反面）url
	public String legal_person_certificate_reverse_img_url = "";

	public RegisterInfo() {}
	public RegisterInfo(String json) {
		try {
			JSONObject jsonbody = JSONObject.parseObject(json);
			this.id = jsonbody.get("id").toString();
			this.username = jsonbody.get("username").toString();
			this.regioncode = jsonbody.get("regioncode").toString();
			this.mobile = jsonbody.get("mobile").toString();
			this.password = jsonbody.get("password").toString();
			this.givenname = jsonbody.get("givenname").toString();
			this.email = jsonbody.get("email").toString();
			this.browser = jsonbody.get("browser").toString();
			this.system = jsonbody.get("system").toString();
			this.regip = jsonbody.get("regip").toString();
			this.province = jsonbody.get("province").toString();
			this.city = jsonbody.get("city").toString();
			this.suppliers_name = jsonbody.get("suppliers_name").toString();
			this.legal_person_name = jsonbody.get("legal_person_name").toString();
			this.business_license = jsonbody.get("business_license").toString();
			this.business_license_img_url = jsonbody.get("business_license_img_url").toString();
			this.legal_person_identity_card = jsonbody.get("legal_person_identity_card").toString();
			this.legal_person_certificate_img_url = jsonbody.get("legal_person_certificate_img_url").toString();
			this.legal_person_certificate_reverse_img_url = jsonbody.get("legal_person_certificate_reverse_img_url").toString();
		}catch (Exception e) {
			 throw e;
		}
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"id\":\"").append(id).append("\"");
		sb.append(",\"username\":\"").append(username).append("\"");
		sb.append(",\"regioncode\":\"").append(regioncode).append("\"");
		sb.append(",\"mobile\":\"").append(mobile).append("\"");
		sb.append(",\"password\":\"").append(password).append("\"");
		sb.append(",\"givenname\":\"").append(givenname).append("\"");
		sb.append(",\"email\":\"").append(email).append("\"");
		sb.append(",\"system\":\"").append(system).append("\"");
		sb.append(",\"browser\":\"").append(browser).append("\"");
		sb.append(",\"regip\":\"").append(regip).append("\"");
		sb.append(",\"province\":\"").append(province).append("\"");
		sb.append(",\"city\":\"").append(city).append("\"");
		sb.append(",\"suppliers_name\":\"").append(suppliers_name).append("\"");
		sb.append(",\"legal_person_name\":\"").append(legal_person_name).append("\"");
		sb.append(",\"business_license\":\"").append(business_license).append("\"");
		sb.append(",\"business_license_img_url\":\"").append(business_license_img_url).append("\"");
		sb.append(",\"legal_person_identity_card\":\"").append(legal_person_identity_card).append("\"");
		sb.append(",\"legal_person_certificate_img_url\":\"").append(legal_person_certificate_img_url).append("\"");
		sb.append(",\"legal_person_certificate_reverse_img_url\":\"").append(legal_person_certificate_reverse_img_url).append("\"");
		sb.append("}");
		return sb.toString();
	}
}