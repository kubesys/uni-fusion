package com.qnkj.common.configs;

import com.github.restapi.XN_Rest;
import com.qnkj.common.utils.ContextUtils;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.common.utils.Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author clubs
 */
@Configuration
public class BaseSaasConfig {
    private static String baseApplication;
    @Value("${rest.application}")
    public void setBaseApplication(String value) { baseApplication = value; }

    private static final ThreadLocal<String> LOCAL_DOMAIN = new ThreadLocal<>();
    private static final ThreadLocal<String> LOCAL_APPLICATION = new ThreadLocal<>();
    protected static  Map<String,Object> saasSettings = new HashMap<>();

    private static void loadConfig() {
        saasSettings.clear();
        List<String> sections = SaaSUtils.getDomains();
        for (String section : sections) {
            SaaSUtils saasUtils = new SaaSUtils(section);
            String applocation = saasUtils.getApplication();
            if(section != null && applocation != null) {
                String dataKey = section+applocation;
                String md5 = DigestUtils.md5Hex(dataKey);
                saasSettings.computeIfAbsent(md5,v -> {
                    Map<String,Object> info = new HashMap<>(1);
                    info.put("domain", section);
                    info.put("application", applocation);
                    return info;
                });
            }
        }
    }
    public static String getApplication() {
        return LOCAL_APPLICATION.get();
    }

    public static List<String> getApplicationList() {
        List<String> lists = new ArrayList<>();
        if (saasSettings.isEmpty()){
            lists.add(baseApplication);
        }else {
            for(Map.Entry<String,Object> entry: saasSettings.entrySet()){
                Map<String, String> item = (Map<String, String>) entry.getValue();
                if (!lists.contains(item.get("application"))) {
                    lists.add(item.get("application"));
                }
            }
        }
        return lists;
    }

    public static String getDomain() {
        String domain = LOCAL_DOMAIN.get();
        if (domain == null){
            domain = "localhost";
        }
        return domain;
    }

    public static Boolean existDomain(String domain) {
        if(domain == null || domain.isEmpty()) {
            return false;
        }
        loadConfig();
        if (domain.contains("192.168.") || domain.contains("127.0.0.1")) {
            domain = "localhost";
        }
        LOCAL_APPLICATION.remove();
        LOCAL_DOMAIN.remove();
        if (saasSettings == null || saasSettings.isEmpty()){
            if(Boolean.FALSE.equals(ContextUtils.isJar()) && domain.contains("localhost")){
                XN_Rest.setApplication(baseApplication);
                LOCAL_APPLICATION.set(baseApplication);
                LOCAL_DOMAIN.set(domain);
                return true;
            }
        }else {
            try {
                for(Map.Entry<String,Object> entry: saasSettings.entrySet()){
                    Map<String, String> setting = (HashMap<String, String>)entry.getValue();
                    if (!Utils.isEmpty(setting.getOrDefault("domain","")) && domain.contains(setting.getOrDefault("domain",""))) {
                        XN_Rest.setApplication(setting.get("application"));
                        LOCAL_APPLICATION.set(setting.get("application"));
                        LOCAL_DOMAIN.set(setting.get("domain"));
                        return true;
                    }
                }
            }catch (Exception e) {
                return false;
            }
            if(Boolean.FALSE.equals(ContextUtils.isJar()) && domain.contains("localhost")){
                XN_Rest.setApplication(baseApplication);
                LOCAL_APPLICATION.set(baseApplication);
                LOCAL_DOMAIN.set(domain);
                return true;
            }
        }
        return false;
    }

    public static void unload() {
        LOCAL_DOMAIN.remove();
        LOCAL_APPLICATION.remove();
    }
}
