package com.qnkj.core.plugins.feign;

import com.github.restapi.XN_Rest;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.core.plugins.feign.authorization.FeignConfig;
import com.qnkj.core.plugins.feign.authorization.FeignResponse;
import com.qnkj.core.plugins.feign.authorization.TokenAuthEntity;
import com.qnkj.core.plugins.feign.authorization.TokenEntity;
import com.qnkj.core.webconfigs.NacosStart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 徐雁
 * create date 2022/2/13
 * create time 12:56 下午
 */

@Slf4j
@Component
public class FeignApi {
    private final ApiController apiController;
    protected static FeignApi feign;
    private static Map<String, TokenEntity> tokenEntitys = new HashMap<>();
    protected static final String TIMEOUT = "Timeout error";

    public FeignApi(ApiController apiController) {
        this.apiController = apiController;
        feign = this;
    }
    /**
     * token认证
     */
    private static void verifyAuthCredential() throws Exception {
        String application = XN_Rest.getApplication();
        if (tokenEntitys.containsKey(application)) {
            Long timestamp = DateTimeUtils.gettimeStamp();
            TokenEntity tokenEntity = tokenEntitys.get(application);
            if (timestamp < tokenEntity.getTimestamp() + 3000) {
                return;
            }
            tokenEntitys.remove(application);
        }
        try {
            TokenAuthEntity auth = new TokenAuthEntity();
            auth.setApplication(XN_Rest.getApplication());
            auth.setAppid(FeignConfig.appid);
            auth.setSecret(FeignConfig.secret);
            auth.setTimestamp(DateTimeUtils.gettimeStamp());
            Map<String, Object> result = feign.apiController.authCredential(auth);
            FeignResponse feignResponse = new FeignResponse(result,FeignConfig.publicKey);
            if (feignResponse.getCode() == 200) {
                Map<String,Object> response = (Map<String,Object>)feignResponse.getBody();
                TokenEntity tokenEntity = new TokenEntity(response.get("token").toString(),
                                                          response.get("publickey").toString(),
                                                          DateTimeUtils.gettimeStamp());
                tokenEntitys.put(application,tokenEntity);
            }

        }catch (Exception e) {
            throw e;
        }
    }
    public static String getToken() {
        String application = XN_Rest.getApplication();
        if (tokenEntitys.containsKey(application)) {
            return tokenEntitys.get(application).getToken();
        }
        return "";
    }
    public static String getPublicKey() {
        String application = XN_Rest.getApplication();
        if (tokenEntitys.containsKey(application)) {
            return tokenEntitys.get(application).getPublicKey();
        }
        return "";
    }

    /**
     * 生成Api代码接口
     * @param module 模块名称
     * @return true/false
     */
    public static Boolean generatorApiCode(String module) {
        try {
            if (!NacosStart.isApiServiceStarted()) { return false; }
            verifyAuthCredential();
            Map<String, Object> result = feign.apiController.generatorApiCode(module);
            FeignResponse feignResponse = new FeignResponse(result,FeignConfig.publicKey);
            if (feignResponse.getCode() == 200) {
                return true;
            }
        }catch (Exception e) {
            log.error("<generatorApiCode>，Error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 清除模块配置缓存信息
     * @param module 模块名称
     * @return true/false
     */
    public static Boolean clearModuleCacheInfo(String module) {
        try {
            if (!NacosStart.isApiServiceStarted()) { return false; }
            verifyAuthCredential();
            Map<String, Object> result = feign.apiController.clearModuleCacheInfo(module);
            FeignResponse feignResponse = new FeignResponse(result,FeignConfig.publicKey);
            if (feignResponse.getCode() == 200) {
                return true;
            }
        }catch (Exception e) {
            log.error("<clearModuleCacheInfo>，Error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 清除全部模块配置缓存信息
     * @return true/false
     */
    public static Boolean clearAllModuleCacheInfo() {
        try {
            if (!NacosStart.isApiServiceStarted()) { return false; }
            verifyAuthCredential();
            Map<String, Object> result = feign.apiController.clearAllModuleCacheInfo();
            FeignResponse feignResponse = new FeignResponse(result,FeignConfig.publicKey);
            if (feignResponse.getCode() == 200) {
                return true;
            }
        }catch (Exception e) {
            log.error("<clearAllModuleCacheInfo>，Error: {}", e.getMessage());
        }
        return false;
    }

}
