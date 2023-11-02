package com.qnkj.clouds.modules.VmImageServers.configs;

import com.qnkj.clouds.modules.VmZones.services.IVmZonesService;
import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;

import java.util.Arrays;
import java.util.Collections;

/**
* @author Auto Generator
* @date 2023-06-27
*/
public class ConfigDataConfig extends BaseDataConfig {
	public ConfigDataConfig() {
		/*菜单配置*/
		moduleMenu = new ModuleMenu().builtin(true).program("kubestack").parent("Hardwares").label("镜像服务器").icon("iconfont icon-kucun").url("/VmImageServers/listview").order(5);

		/*模块配置*/
		tabs = new Tab().modulename("VmImageServers").modulelabel("镜像服务器").tabname("vm_imageservers").iscreate(true).isdelete(true).isenabledisable(true).customview(false);

		/*区块配置*/
		blocks = Collections.singletonList(
			new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段布局配置*/
		layouts = Arrays.asList(

			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("name","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("zone","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("vmimageservertype","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("url","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("serverip","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("port","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("username","")),
			new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("password","")),
			new Layout().block(1).columns(Collections.singletonList("12")).fields(Collections.singletonList("describe"))
		);

		/*字段配置*/
		fields = Arrays.asList(
			new TabField().fieldname("name").fieldlabel("名称").typeofdata("NS~M").issort(true),
			new TabField().fieldname("describe").fieldlabel("简介").uitype(23).issort(true),
			new TabField().fieldname("vmimageservertype").fieldlabel("类型").uitype(19).typeofdata("V~M").issort(true),
			new TabField().fieldname("serverip").fieldlabel("服务器IP").typeofdata("V~M").issort(true).defaultvalue("127.0.0.1"),
			new TabField().fieldname("url").fieldlabel("挂载路径").typeofdata("V~M").issort(true).defaultvalue("/var/lib/libvirt/ftp"),
			new TabField().fieldname("port").fieldlabel("端口").typeofdata("V~M").issort(true).defaultvalue("6000"),
			new TabField().fieldname("username").fieldlabel("用户名").typeofdata("NS~M").issort(true),
			new TabField().fieldname("password").fieldlabel("密码").uitype(3).typeofdata("V~M").issort(true),
			new TabField().fieldname("state").fieldlabel("就绪状态").uitype(19).picklist("vmimageserverstate").issort(true),
			new TabField().fieldname("zone").fieldlabel("区域").uitype(11).typeofdata("V~M").issort(true),
			new TabField().fieldname("status").fieldlabel("启用").uitype(13).displaytype(2).defaultvalue("Active").issort(true).isnumsort(true)
		);

		/*视图配置*/
		customViews = Collections.singletonList(
			new CustomView().viewname("默认").columnlist(Arrays.asList("zone","name","vmimageservertype","url","status","state","published"))
		);

		/*列表视图查询条件配置*/
		searchColumn = Collections.singletonList(
			new SearchColumn().fieldname("status").fieldlabel("启用").order(1).searchtype("text")
		);

		/*关联显示配置*/
		entityNames.fieldname("name");

		/*外部关联配置*/
		outsideLinks = Arrays.asList(
				new OutsideLink().fieldname("zone").relmodule("VmZones").serviceclass(IVmZonesService.class.getName()).url("VmZones/popupview")
		);

		/*弹出对话框配置*/
		popupDialog.search(Collections.singletonList("name")).columns(Arrays.asList("name","vmimageservertype","url","status","published"));

		/*编号配置*/
		/*modentityNums.modulename("").prefix("");*/

		/*模块角色数据验证信息*/
		/*dataPermission.setExpressions(new HashMap<String, List<Expression>>(){{
			put("",Arrays.asList(
				new Expression()
			));
		}});*/

		/*模块API接口配置信息*/
		/*api.modulename("VmImageServers");*/

	}
}
