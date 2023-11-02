package com.qnkj.core.webconfigs;

import com.qnkj.common.configs.BaseSupplierPickListConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * create by 徐雁
 * create date 2020/12/23
 */
@Slf4j
@Component("CoreSupplierPicklistConfig")
public class SupplierPicklistConfig {

    @PostConstruct
    public void init() {
        /**
         * picklists：企业专属数据字典
         *
         * */
        BaseSupplierPickListConfig.addPicklists(
                Arrays.asList(

                )
        );
    }
}

