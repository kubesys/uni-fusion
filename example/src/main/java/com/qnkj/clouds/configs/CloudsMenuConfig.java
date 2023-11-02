package com.qnkj.clouds.configs;

import com.qnkj.common.configs.BaseMenuConfig;
import com.qnkj.common.entitys.ParentTab;
import com.qnkj.common.entitys.Program;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Auto Generator
 * @date 2021-03-30
 */
@Component("CloudsMenuConfig")
public class CloudsMenuConfig {
    @PostConstruct
    public void init() {
        BaseMenuConfig.addProgram(new Program().builtin(true).group("kubestack").label("虚机管理").icon("lcdp-icon-DevOps").order(Integer.MAX_VALUE-3).authorize("system"));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("kubestack").name("CloudResources").label("云资源").icon("iconfont icon-quan").order(10));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("kubestack").name("Hardwares").label("硬件设施").icon("iconfont icon-dingdan").order(11));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("kubestack").name("NetworkResources").label("网络资源").icon("iconfont icon-shezhi").order(12));
        BaseMenuConfig.addParentMenu(new ParentTab().builtin(true).program("kubestack").name("NetworkServices").label("网络服务").icon("iconfont icon-shengchan").order(14));
    }
}
