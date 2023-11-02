package com.qnkj.core.base.modules.settings.users.configs;

import com.qnkj.common.configs.BaseDataConfig;
import com.qnkj.common.entitys.*;
import com.qnkj.core.base.modules.settings.departments.service.IDepartmentsService;
import com.qnkj.core.base.modules.settings.roles.service.IRolesService;

import java.util.Arrays;
import java.util.Collections;

public class ConfigDataConfig extends BaseDataConfig {
    public ConfigDataConfig() {
        moduleMenu = new ModuleMenu().builtin(true).program("settings").parent("usermanager").label("用户管理").icon("lcdp-icon-yonghu").order(1).url("/users/listview");

        tabs.modulename("users").modulelabel("用户管理").tabname("users").iscreate(true).isexport(true).searchcolumn(3);

        customViews = Collections.singletonList(new CustomView()
                .viewname("默认")
                .orderby("my.sequence")
                .order("A_N")
                .isdefault(true)
                .columnlist(Arrays.asList("account", "username", "departmentid", "roleid", "directsuperior",
                        "mobile", "sequence", "is_admin", "status", "published")));

        fields = Arrays.asList(
                new TabField().fieldname("account").fieldlabel("账号名称").unique(true).uitype(2).typeofdata("NS~M").listwidth("10%"),
                new TabField().fieldname("username").fieldlabel("姓名").typeofdata("V~M").listwidth("8%"),
                new TabField().fieldname("password").fieldlabel("账号密码").uitype(3).typeofdata("V~M").displaytype(1),
                new TabField().fieldname("confirm_password").fieldlabel("密码确认").uitype(3).typeofdata("V~M").displaytype(1).relation("password"),
                new TabField().fieldname("mobile").fieldlabel("手机号码").typeofdata("MO~M").listwidth("10%").issort(true).isnumsort(true),
                new TabField().fieldname("is_admin").fieldlabel("用户类型").uitype(19).typeofdata("V~M").listwidth("10%").defaultvalue("pt").align("center"),
                new TabField().fieldname("status").fieldlabel("启用").uitype(13).align("center").listwidth("8%").defaultvalue("Active").issort(true),
                new TabField().fieldname("sequence").fieldlabel("排序号").typeofdata("IN~M").listwidth("8%").defaultvalue("100").issort(true).isnumsort(true).align("center"),
                new TabField().fieldname("departmentid").fieldlabel("部门").uitype(10).typeofdata("V~M").listwidth("8%"),
                new TabField().fieldname("roleid").fieldlabel("权限").uitype(11).typeofdata("V~M").listwidth("10%"),
                new TabField().fieldname("directsuperior").fieldlabel("直接上级").uitype(25).listwidth("8%"),
                new TabField().fieldname("mailbox").fieldlabel("邮箱").typeofdata("EM~O").listwidth("10%").issort(true)
        );

        searchColumn = Arrays.asList(
                new SearchColumn().fieldname("published").fieldlabel("创建日期").searchtype("calendar").colspan(2).quickbtn(true),
                new SearchColumn().fieldname("status").fieldlabel("启用").searchtype("text"),
                new SearchColumn().fieldname("account").fieldlabel("账号名称").searchtype("vague_input"),
                new SearchColumn().fieldname("username").fieldlabel("姓名").searchtype("vague_input"),
                new SearchColumn().fieldname("mobile").fieldlabel("手机号码").searchtype("vague_input").width(204),
                new SearchColumn().fieldname("is_admin").fieldlabel("用户类型").searchtype("select")

        );

        entityNames.fieldname("username");

        outsideLinks = Arrays.asList(
                new OutsideLink().fieldname("departmentid").serviceclass(IDepartmentsService.class.getName()).relmodule("departments").placeholder("部门").url("departments/popupview/tree"),
                new OutsideLink().fieldname("roleid").serviceclass(IRolesService.class.getName()).relmodule("roles").placeholder("权限").url("roles/popupview"),
                new OutsideLink().fieldname("directsuperior").serviceclass(IDepartmentsService.class.getName()).relmodule("departments").placeholder("直接上级").url("departments/popupview/users")
        );

        popupDialog.search(Arrays.asList(
                "username", "account", "mobile", "mailbox"
        )).columns(Arrays.asList(
                "username", "account", "mobile", "mailbox"
        ));

        modentityNums.modulename("users").prefix("USE");
    }
}
