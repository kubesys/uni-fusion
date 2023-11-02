package com.qnkj.core.webconfigs;

import com.qnkj.common.configs.BaseMenuConfig;
import com.qnkj.common.entitys.ParentTab;
import com.qnkj.common.entitys.Program;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * create by 徐雁
 * create date 2020/11/9
 */

@Component("MenuConfig")
@Order(0)
public class MenuConfig {
    @PostConstruct
    public void init() {
        BaseMenuConfig.addProgram(new Program().builtin(true).group("lcdp").label("低代码开发").icon("layui-icon-menugroup-settings").order(Integer.MAX_VALUE).authorize("system"));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("lcdp").name("developmentmanager").label("开发管理").icon("lcdp-icon-kaifaguanli").order(1));

        BaseMenuConfig.addProgram(new Program().builtin(true).group("settings").label("系统设置").icon("layui-icon-menugroup-settings").order(Integer.MAX_VALUE-1).authorize("system"));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("settings").name("usermanager").label("用户管理").icon("lcdp-icon-yonghuguanli").order(1));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("settings").name("generalmanager").label("常用设置").icon("lcdp-icon-jichushezhi").order(Integer.MAX_VALUE));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("settings").name("onlinedevelopmentmanager").label("开发设置").icon("lcdp-icon-kaifaguanli").order(Integer.MAX_VALUE));

        BaseMenuConfig.addProgram(new Program().builtin(true).group("management").label("公共服务").icon("lcdp-icon-gonggongfuwu").order(Integer.MAX_VALUE-2).authorize("system"));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("management").name("home").label("首页").icon("lcdp-icon-shouye1").order(1));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("management").name("baseservices").label("基础服务").icon("lcdp-icon-jichufuwu").order(2));

        BaseMenuConfig.addProgram(new Program().builtin(true).group("supplier").label("系统设置").icon("layui-icon-menugroup-managementmenus").order(Integer.MAX_VALUE-1).authorize("supplier"));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("supplier").name("suppliersettings").label("用户管理").icon("lcdp-icon-yonghuguanli").order(100));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("supplier").name("suppliergeneralmanager").label("常用设置").icon("lcdp-icon-jichushezhi").order(102));

        BaseMenuConfig.addProgram(new Program().builtin(true).group("suppliermanagement").label("公共服务").icon("lcdp-icon-gonggongfuwu").order(Integer.MAX_VALUE-2).authorize("supplier"));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("suppliermanagement").name("shouye").label("首页").icon("lcdp-icon-shouye1").order(0));
    }


}
