package com.qnkj.core.webconfigs.configure;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestApiConfig {

    @Getter
    public static String supplierid;

    @Value("${rest.supplierid}")
    public void setSupplierid(String value) {
        supplierid = value;
    }

}

