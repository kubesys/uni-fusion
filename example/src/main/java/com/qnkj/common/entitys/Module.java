package com.qnkj.common.entitys;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * create by 徐雁
 * create date 2020/11/6
 */

public class Module {
    @ApiModelProperty("菜单组信息")
    public List<Object> MenuGroup;
    @ApiModelProperty("模块信息")
    public Tab Tabinfo;
    @ApiModelProperty("模块菜单信息")
    public ModuleMenu moduleMenu;
    @ApiModelProperty("模块按钮信息")
    public List<Action> actions;
    @ApiModelProperty("模块字段信息")
    public List<TabField> Fields;
    @ApiModelProperty("模块区块信息")
    public List<Block> Blocks;
    @ApiModelProperty("模块布局信息")
    public List<Layout> Layouts;
    @ApiModelProperty("模块视图信息")
    public List<CustomView> CustomViews;
    @ApiModelProperty("模块内联信息")
    public EntityLink Entitylink;
    @ApiModelProperty("模块外联信息")
    public List<OutsideLink> OutsideLinks;
    @ApiModelProperty("模块搜索信息")
    public List<SearchColumn> SearchColumns;
    @ApiModelProperty("模块弹窗信息")
    public PopupDialog Popupdialog;
    @ApiModelProperty("模块自动编号信息")
    public ModentityNum Modentitynum;
    @ApiModelProperty("模块字典信息")
    public List<PickList> picklists;
    @ApiModelProperty("企业模块字典信息")
    public List<SupplierPickList> supplierPickLists;
    @ApiModelProperty("模块角色验证信息")
    public DataPermission dataPermission;
    @ApiModelProperty("模块开放接口配置")
    public Api api;
}
