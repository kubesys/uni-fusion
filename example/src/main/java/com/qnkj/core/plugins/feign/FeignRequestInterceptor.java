package com.qnkj.core.plugins.feign;

import com.qnkj.common.utils.DESedeUtil;
import com.qnkj.common.utils.DateTimeUtils;
import com.qnkj.common.utils.MD5Util;
import com.qnkj.core.plugins.feign.authorization.FeignConfig;
import com.qnkj.core.plugins.feign.authorization.FeignUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;


@Slf4j
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {
    @SneakyThrows
    @Override
    public void apply(RequestTemplate requestTemplate) {
            String serviceName = requestTemplate.feignTarget().name();
            if (serviceName.equals("api")) {
                String url = requestTemplate.url();
                Map<String, Collection<String>> headers = requestTemplate.headers();
                String method = requestTemplate.method();
                Long timestamp = DateTimeUtils.gettimeStamp();
                String signature = FeignUtils.getSignature(url,timestamp);
                if ("/auth/credential".equals(url)) {
                    requestTemplate.header("signature", signature);
                    requestTemplate.header("timestamp", String.valueOf(timestamp));
                    requestTemplate.header("token", "");
                } else {
                    requestTemplate.header("signature", signature);
                    requestTemplate.header("timestamp", String.valueOf(timestamp));
                    requestTemplate.header("token", FeignApi.getToken());
                }
                if (method.equals("POST") || method.equals("POST") ) {
                    if (requestTemplate.requestBody().length() > 0) {
                        String jsonBody = requestTemplate.requestBody().asString();
                        if ("/auth/credential".equals(url)) {
                            String encryptBody = DESedeUtil.encrypt(jsonBody, MD5Util.get(FeignConfig.publicKey));
                            requestTemplate.body(encryptBody.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                        } else {
                            if ("/auth/credential".equals(url)) {
                                String encryptBody = DESedeUtil.encrypt(jsonBody, MD5Util.get(FeignApi.getPublicKey()));
                                requestTemplate.body(encryptBody.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                            }
                        }
                    }
                }
            }
    }
}
