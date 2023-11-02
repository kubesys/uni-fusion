package com.qnkj.clouds.modules.VmInstances.configs;

import com.qnkj.clouds.modules.VmDisks.services.IVmDisksService;
import com.qnkj.clouds.modules.VmImages.services.IVmImagesService;
import com.qnkj.clouds.modules.VmInstanceOfferings.services.IVmInstanceOfferingsService;
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
		moduleMenu = new ModuleMenu().builtin(true).program("kubestack").parent("CloudResources").label("云主机").icon("iconfont icon-kucun").url("/VmInstances/listview").order(1);

		/*模块配置*/
		tabs = new Tab().modulename("VmInstances").modulelabel("云主机").tabname("vm_instances").iscreate(true).isdelete(true).isenabledisable(true).customview(false);

		/*区块配置*/
		blocks = Collections.singletonList(
				new Block().blocklabel("基本信息").blockid(1)
		);

		/*字段布局配置*/
		layouts = Arrays.asList(
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("name","")),
				new Layout().block(1).columns(Collections.singletonList("12")).fields(Collections.singletonList("describe")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("offering","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("node","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("image","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("disk","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("osvariant","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("virt_type","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("livecd","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("graphics","")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("uuid","make_uuid")),
				new Layout().block(1).columns(Arrays.asList("6","6")).fields(Arrays.asList("eventid","make_event_id"))
		);

		/*字段配置*/
		fields = Arrays.asList(
				new TabField().fieldname("name").fieldlabel("名称").typeofdata("NS~M").issort(true).unique(true),
				new TabField().fieldname("describe").fieldlabel("简介").uitype(23).issort(true),
				new TabField().fieldname("eventid").fieldlabel("事件ID").issort(true),
				new TabField().fieldname("make_event_id").fieldlabel("生成事件ID").uitype(34).show_title(false),
				new TabField().fieldname("make_uuid").fieldlabel("生成UUID").uitype(34).show_title(false),
				new TabField().fieldname("state").fieldlabel("就绪状态").uitype(19).picklist("vmstate").issort(true),
				new TabField().fieldname("offering").fieldlabel("计算规格").uitype(11).typeofdata("V~M").issort(true),
				new TabField().fieldname("image").fieldlabel("镜像").uitype(11).typeofdata("V~M").issort(true),
				new TabField().fieldname("disk").fieldlabel("云盘").uitype(11).typeofdata("V~M").issort(true),
				new TabField().fieldname("livecd").fieldlabel("光驱Livecd类型").uitype(5).defaultvalue("0").issort(true),
				new TabField().fieldname("graphics").fieldlabel("VNC密码").typeofdata("NS~O").issort(true),
				new TabField().fieldname("virt_type").fieldlabel("虚拟化类型").uitype(13).picklist("virttype").defaultvalue("kvm").issort(true),
				new TabField().fieldname("uuid").fieldlabel("UUID").typeofdata("NS~M").issort(true),
				new TabField().fieldname("osvariant").fieldlabel("操作系统").uitype(19).typeofdata("V~M").picklist("vmosvariant").issort(true),
				new TabField().fieldname("cpu_vendor").fieldlabel("CPU").issort(true),
				new TabField().fieldname("vcpu").fieldlabel("CPU数量").issort(true),
				new TabField().fieldname("os_arch").fieldlabel("架构").issort(true),
				new TabField().fieldname("os_machine").fieldlabel("机器名称").issort(true),
				new TabField().fieldname("memory").fieldlabel("分配内存").issort(true),
				new TabField().fieldname("port").fieldlabel("端口").issort(true),
				new TabField().fieldname("uid").fieldlabel("UID").issort(true),
				new TabField().fieldname("node").fieldlabel("节点").uitype(19).typeofdata("V~M").issort(true),
				new TabField().fieldname("zone").fieldlabel("区域").uitype(11).typeofdata("V~M").issort(true),
				new TabField().fieldname("createtime").fieldlabel("创建时间").issort(true)
		);

		/*视图配置*/
		customViews = Collections.singletonList(
				new CustomView().viewname("默认").columnlist(Arrays.asList("zone","name","state","node","vcpu","os_arch","memory","createtime"))
		);

		/*列表视图查询条件配置*/
		searchColumn = Arrays.asList(
				new SearchColumn().fieldname("state").fieldlabel("就绪状态").order(1).searchtype("select"),
				new SearchColumn().fieldname("name").fieldlabel("名称").order(2).searchtype("vague_input")
		);

		/*关联显示配置*/
		entityNames.fieldname("name");

		/*外部关联配置*/
		outsideLinks = Arrays.asList(
				new OutsideLink().fieldname("offering").relmodule("VmInstanceOfferings").serviceclass(IVmInstanceOfferingsService.class.getName()).url("VmInstanceOfferings/popupview"),
				new OutsideLink().fieldname("image").relmodule("VmImages").serviceclass(IVmImagesService.class.getName()).url("VmImages/popupview"),
				new OutsideLink().fieldname("zone").relmodule("VmZones").serviceclass(IVmZonesService.class.getName()).url("VmZones/popupview"),
				new OutsideLink().fieldname("disk").relmodule("VmDisks").serviceclass(IVmDisksService.class.getName()).url("VmDisks/popupview")

				);

		/*弹出对话框配置*/
		popupDialog.search(Collections.singletonList("name")).columns(Arrays.asList("name","vmpooltype","url","state","age"));


	}
}
