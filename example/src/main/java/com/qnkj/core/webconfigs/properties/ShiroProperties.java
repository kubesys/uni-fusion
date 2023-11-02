package com.qnkj.core.webconfigs.properties;

import lombok.Data;

/**
 * @author Oldhand
 */
@Data
public class ShiroProperties {

    private long sessionTimeout;
    private int cookieTimeout;
    private String anonUrl;
}
