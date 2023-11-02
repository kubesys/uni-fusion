package com.github.restapi;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Oldhand
 **/

@Configuration
public class RestConfig {

    @Getter
    public static String application;

    @Value("${rest.application}")
    public void setApplication(String value) {
        application = value;
    }

    @Getter
    public static String appid;

    @Value("${rest.appid}")
    public void setAppid(String value) {
        appid = value;
    }

    @Getter
    public static String secret;

    @Value("${rest.secret}")
    public void setSecret(String value) {
        secret = value;
    }

    @Getter
    public static String baseurl;

    @Value("${rest.baseUrl}")
    public void setBaseUrl(String value) { baseurl = value; }

    @Getter
    public static String publicKey;

    @Value("${rest.publicKey}")
    public void setPublicKey(String value) { publicKey = value; }

}

