package com.qnkj.core.base.modules.settings.roles.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.CustomView;
import com.qnkj.common.entitys.ModuleMenu;
import com.qnkj.common.entitys.TabField;

import java.util.Arrays;

public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        moduleMenu = new ModuleMenu().builtin(true).program("settings").parent("usermanager").label("权限管理").icon("lcdp-icon-quanxian").order(3).url("/roles/listview");

        tabs.modulename("roles").modulelabel("权限管理").tabname("roles").iscreate(true).isdelete(true);

        customViews = Arrays.asList(
                new CustomView().columnlist(Arrays.asList("rolename", "author", "published", "description"))
        );

        fields = Arrays.asList(
                new TabField().fieldname("rolename").fieldlabel("权限名称").unique(true).typeofdata("V~M").editwidth("100").listwidth("15%").issort(true),
                new TabField().fieldname("author").fieldlabel("创建人").typeofdata("V~M").editwidth("100").listwidth("15%").issort(true),
                new TabField().fieldname("published").fieldlabel("创建时间").typeofdata("V~M").editwidth("300").listwidth("15%").issort(true),
                new TabField().fieldname("description").fieldlabel("备注").merge_column(2).editwidth("300").listwidth("50%").align("left")
        );

        entityNames.fieldname("rolename");

        popupDialog.columns(Arrays.asList("rolename","description","author","published"))
                .search(Arrays.asList("rolename","author","description"));
    }
}
