package com.qnkj.core.base.modules.settings.modentitynums.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.CustomView;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.SearchColumn;
import com.qnkj.common.entitys.TabField;

import java.util.Arrays;

/**
 * create by 徐雁
 */
public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        moduleMenu = new ModuleMenu().builtin(true).program("settings").parent("generalmanager").label("系统编号定制").icon("lcdp-icon-xitongbianhaodingzhi").order(4).url("/modentitynums/listview");

        tabs.modulename("modentitynums").modulelabel("自动编号").tabname("modentity_nums");

        customViews = Arrays.asList(
                new CustomView().viewname("默认").isdefault(true)
                        .columnlist(Arrays.asList("semodule", "prefix", "include_date", "length", "cur_id","start_id","date","published"))
        );

        fields = Arrays.asList(
                new TabField().fieldname("semodule").fieldlabel("模块名称").listwidth("15%"),
                new TabField().fieldname("prefix").fieldlabel("使用前缀").listwidth("15%"),
                new TabField().fieldname("include_date").fieldlabel("是否包含日期").uitype(20).listwidth("10%"),
                new TabField().fieldname("length").fieldlabel("流水号长度").listwidth("10%"),
                new TabField().fieldname("cur_id").fieldlabel("当前编号").listwidth("10%"),
                new TabField().fieldname("start_id").fieldlabel("起始编号").listwidth("10%"),
                new TabField().fieldname("date").fieldlabel("日期前缀").listwidth("10%")
        );
        /*列表视图查询条件配置*/
        searchColumn = Arrays.asList(
                new SearchColumn().fieldname("published").fieldlabel("创建日期").colspan(2).width(95).quickbtn(true).searchtype("calendar")
        );
    }
}
