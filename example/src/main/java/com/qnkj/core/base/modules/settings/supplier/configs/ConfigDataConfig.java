package com.qnkj.core.base.modules.settings.supplier.configs;

import com.google.common.collect.ImmutableSet;
import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
* @author Auto Generator
* @date 2020-11-23
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("settings").parent("generalmanager").label("注册企业").icon("lcdp-icon-zhuceqiye").modulename("supplier").url("/supplier/listview").order(6);

		/*模块配置*/
		tabs = new Tab().modulename("supplier").modulelabel("注册企业").tabname("suppliers").searchcolumn(3).iscreate(false).isdelete(false).isreadonly(true);

		/*视图配置*/
		customViews = Arrays.asList(
			new CustomView().viewname("默认").columnlist(Arrays.asList("suppliers_no","suppliers_name","legal_person_name","mobile","province","city","business_license","givenname","published"))
		);

		blocks = new ArrayList<>(ImmutableSet.of(
				new Block().blocklabel("基本信息").blockid(1).columns(3)
		));

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("suppliers_no").fieldlabel("客户编号").uitype(4).typeofdata("V~M").listwidth("10%"),
			new TabField().fieldname("suppliers_name").fieldlabel("客户全称").typeofdata("V~M").listwidth("20%"),
			new TabField().fieldname("suppliers_shortname").fieldlabel("客户简称").typeofdata("V~O"),
			new TabField().fieldname("suppliers_code").fieldlabel("客户编码"),
			new TabField().fieldname("username").fieldlabel("用户名称").typeofdata("V~M"),
			new TabField().fieldname("mobile").fieldlabel("手机号码").typeofdata("V~M").listwidth("10%"),
			new TabField().fieldname("regioncode").fieldlabel("国家代码").typeofdata("V~O").listwidth("8%"),
			new TabField().fieldname("password").fieldlabel("密码").typeofdata("V~M").displaytype(2),
			new TabField().fieldname("givenname").fieldlabel("姓名").typeofdata("V~M").listwidth("10%"),
			new TabField().fieldname("email").fieldlabel("用户邮箱").typeofdata("V~O"),
			new TabField().fieldname("province").fieldlabel("省份").typeofdata("V~M").listwidth("10%").align("center"),
			new TabField().fieldname("city").fieldlabel("城市").typeofdata("V~M").listwidth("10%").align("center"),
			new TabField().fieldname("regip").fieldlabel("注册IP").typeofdata("V~O"),
			new TabField().fieldname("system").fieldlabel("注册系统").typeofdata("V~O"),
			new TabField().fieldname("browser").fieldlabel("注册浏览器").typeofdata("V~O"),
			new TabField().fieldname("legal_person_name").fieldlabel("企业法人").typeofdata("V~M").listwidth("10%").align("center"),
			new TabField().fieldname("legal_person_identity_card").fieldlabel("法人身份证号码").typeofdata("V~M"),
			new TabField().fieldname("legal_person_certificate_img_url").fieldlabel("法人证件(正面)").uitype(17).merge_column(2),
			new TabField().fieldname("legal_person_certificate_reverse_img_url").fieldlabel("法人证件(反面)").uitype(17),
			new TabField().fieldname("business_license").fieldlabel("营业执照").typeofdata("V~M").listwidth("10%").merge_column(2),
			new TabField().fieldname("business_license_img_url").fieldlabel("营业执照").uitype(17).merge_column(2),
			new TabField().fieldname("profileid").fieldlabel("负责人").uitype(25).displaytype(2)
		);

		/*列表视图查询条件配置*/
		searchColumn = Arrays.asList(
			new SearchColumn().fieldname("published").fieldlabel("创建日期").order(1).colspan(2).width(95).quickbtn(true).searchtype("calendar")
		);

		/*关联显示配置*/
		entityNames = new EntityLink().fieldname("suppliers_name").entityfield("xn_id");

		/*编号配置*/
		modentityNums.modulename("supplier").prefix("S").include_date(false).length(4);

		/*外部关联配置*/
		outsideLinks = Arrays.asList(

		);

		/*弹出对话框配置*/
		popupDialog.search(Arrays.asList("suppliers_name")).columns(Arrays.asList("suppliers_no","suppliers_name","legal_person_name","mobile","province","city","published"));


	}
}
