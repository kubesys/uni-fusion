package com.qnkj.core.plugins.feign.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author 徐雁
 * create date 2022/2/13
 * create time 4:14 下午
 */

@Configuration
public class FeignConfig {
    public static String appid;
    public static String secret;
    public static String publicKey;


    @Value("${spring.feign.appid:demo}")
    protected void setApId(String value) {
        appid = value;
    }

    @Value("${spring.feign.secret:5235d9f95f1fac044adb3aff65ecbab2}")
    protected void setSecret(String value) {
        secret = value;
    }

    @Value("${spring.feign.public-key:-----BEGIN PUBLIC KEY-----\nMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI6dGvkKSHB6Q3TE6TKGFR4Nyt\n6XH3gc7/LAzvW0aDNGZjkoA7jrMTBd/T0N/R4miBK7XNMI+4Z/gd8OgS0wShPwyq\nFwv8Q54goPr6dAXAQifzwK+eOs+Avu9rrVfT31i8wJeIzpk1aySoYB40ozasTdXg\nQ2AHZH0AqU/Rne5GuQIDAQAB\n-----END PUBLIC KEY-----}")
    protected void setPublicKey(String value) {
        publicKey = value;
    }

}
