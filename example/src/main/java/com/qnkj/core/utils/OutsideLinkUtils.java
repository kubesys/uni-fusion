package com.qnkj.core.utils;

import com.qnkj.common.entitys.OutsideLink;
import com.qnkj.common.services.IOutsideLinkServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class OutsideLinkUtils {

    @Autowired
    private IOutsideLinkServices outsideLinkServices;

    public static OutsideLinkUtils utils;

    @PostConstruct
    public void init() {
        utils = this;
        utils.outsideLinkServices = this.outsideLinkServices;
    }

    public static List<OutsideLink> getRelMeLinkModules(String moduleName) {
        return utils.outsideLinkServices.getRelMeLinkModules(moduleName);
    }
}
