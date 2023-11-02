package com.qnkj.core.base.modules.management.announcements.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;

import java.util.Arrays;
import java.util.Collections;

/**
* @author Auto Generator
* @date 2020-11-17
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("management").parent("home").modulename("announcements").label("公告").icon("lcdp-icon-gonggao1").modulename("announcements").url("/announcements/listview").order(1);

		/*模块配置*/
		tabs = new Tab().modulename("announcements").modulelabel("公告").tabname("announcements").iscreate(true).isdelete(true);

		/*视图配置*/
		customViews = Collections.singletonList(
				new CustomView().viewname("默认").columnlist(Arrays.asList("title", "publisher", "istop", "approvalstatus", "published"))
		);

		layouts = Arrays.asList(
				new Layout().columns(Arrays.asList("6","6")).fields(Arrays.asList("title","")),
				new Layout().columns(Arrays.asList("6","6")).fields(Arrays.asList("publisher","")),
				new Layout().columns(Arrays.asList("6","6")).fields(Arrays.asList("istop","")),
				new Layout().columns(Arrays.asList("10","2")).fields(Arrays.asList("body",""))
		);
		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("title").fieldlabel("标题").typeofdata("NS~M").listwidth("30%").align("left"),
			new TabField().fieldname("istop").fieldlabel("是否置顶").uitype(14).align("center").align("center").typeofdata("V~M").listwidth("15%").defaultvalue("0").defaultvalue("0"),
			new TabField().fieldname("body").fieldlabel("发布内容").uitype(24).listwidth("80%").editheight("800px"),
		    new TabField().fieldname("publisher").fieldlabel("发布者").typeofdata("V~M").listwidth("20%"),
			new TabField().fieldname("approvalstatus").fieldlabel("是否发布").uitype(14).align("center").align("center").displaytype(2).listwidth("15%").picklist("isrelease").defaultvalue("0")
		);

		/*列表视图查询条件配置*/
		searchColumn = Collections.singletonList(
				new SearchColumn().fieldname("published").fieldlabel("创建日期").colspan(2).width(95).quickbtn(true).searchtype("calendar")
		);

	}
}
