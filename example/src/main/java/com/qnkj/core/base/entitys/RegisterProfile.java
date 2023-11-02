package com.qnkj.core.base.entitys;

import com.alibaba.fastjson.JSONObject;


/**
* @author oldhand
* @date 2019-12-27
*/
public class RegisterProfile {

    // 用户名称
	public String username = "";

    // 用户类型
	public String type = "";

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

    // 用户头像地址
	public String link = "";

    // 性别
	public String gender = "";

    // 所属国家
	public String country = "";

    // 地区
	public String region = "";

    // 出生日期
	public String birthdate = "";

    // 省份
	public String province = "";

    // 城市
	public String city = "";

    // 真实姓名
	public String realname = "";

    // 身份证号码
	public String identitycard = "";

    // 注册IP
	public String regIp = "";

    // 注册系统
	public String system = "";

    // 注册浏览器
	public String browser = "";

	public RegisterProfile() {}
	public RegisterProfile(String json) {
		try {
			JSONObject jsonbody = JSONObject.parseObject(json);
			this.username = jsonbody.get("username").toString();
			this.type = jsonbody.get("type").toString();
			this.regioncode = jsonbody.get("regioncode").toString();
			this.mobile = jsonbody.get("mobile").toString();
			this.password = jsonbody.get("password").toString();
			this.givenname = jsonbody.get("givenname").toString();
			this.email = jsonbody.get("email").toString();
			this.link = jsonbody.get("link").toString();
			this.system = jsonbody.get("system").toString();
			this.browser = jsonbody.get("browser").toString();

		}catch (Exception e) {
			 throw e;
		}
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"username\":\"").append(username).append("\"");
		sb.append(",\"type\":\"").append(type).append("\"");
		sb.append(",\"regioncode\":\"").append(regioncode).append("\"");
		sb.append(",\"mobile\":\"").append(mobile).append("\"");
		sb.append(",\"password\":\"").append(password).append("\"");
		sb.append(",\"givenname\":\"").append(givenname).append("\"");
		sb.append(",\"email\":\"").append(email).append("\"");
		sb.append(",\"link\":\"").append(link).append("\"");
		sb.append(",\"system\":\"").append(system).append("\"");
		sb.append(",\"browser\":\"").append(browser).append("\"");
		sb.append("}");
		return sb.toString();
	}
}