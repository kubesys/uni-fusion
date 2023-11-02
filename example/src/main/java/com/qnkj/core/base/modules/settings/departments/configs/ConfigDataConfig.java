package com.qnkj.core.base.modules.settings.departments.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.TabField;

import java.util.Arrays;

public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig(){
        moduleMenu = new ModuleMenu().builtin(true).program("settings").parent("usermanager").label("部门管理").icon("lcdp-icon-bumen").order(2).url("/departments/listview");

        tabs.modulename("departments").modulelabel("部门管理").tabname("departments").moduletype(2);

        fields = Arrays.asList(
                new TabField().fieldname("departmentname").fieldlabel("部门名称").unique(true).typeofdata("V~M").editwidth("100").listwidth("15%"),
                new TabField().fieldname("parentid").fieldlabel("上级部门").uitype(10).typeofdata("V~M").editwidth("100").listwidth("15%"),
                new TabField().fieldname("sequence").fieldlabel("部门排序").editwidth("100").listwidth("15%").defaultvalue("100"),
                new TabField().fieldname("leadership").fieldlabel("部门领导").uitype(25).editwidth("100").listwidth("15%"),
                new TabField().fieldname("mainleadership").fieldlabel("主管领导").uitype(25).editwidth("100").listwidth("15%"),
                new TabField().fieldname("ishide").fieldlabel("是否隐藏").editwidth("100").listwidth("15%")
        );

        entityNames.fieldname("departmentname");

    }
}
