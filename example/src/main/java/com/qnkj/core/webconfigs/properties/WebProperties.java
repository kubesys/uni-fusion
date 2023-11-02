package com.qnkj.core.webconfigs.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Oldhand
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:web.properties"})
@ConfigurationProperties(prefix = "web")
public class WebProperties {

    private ShiroProperties shiro = new ShiroProperties();
    private boolean autoOpenBrowser = true;

    private int maxBatchInsertNum = 1000;

    private ValidateCodeProperties code = new ValidateCodeProperties();
}
