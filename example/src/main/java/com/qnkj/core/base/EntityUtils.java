package com.qnkj.core.base;

import com.qnkj.core.base.services.IBaseEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
class EntityUtils {
    @Autowired
    private IBaseEntityService entityService;

    public static EntityUtils entityUtils;

    @PostConstruct
    public void init() {
        entityUtils = this;
        entityUtils.entityService = this.entityService;
    }

    static List<Object> getEntityUtils(String tabname) {
        return entityUtils.entityService.getEntityUtils(tabname);
    }
}
