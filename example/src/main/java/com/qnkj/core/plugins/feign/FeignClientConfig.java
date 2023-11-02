package com.qnkj.core.plugins.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author 徐雁
 * create date 2022/2/14
 * create time 9:27 上午
 */

@Slf4j
@Configuration
@Import(FeignClientsConfiguration.class)
public class FeignClientConfig {
    private final ApplicationContext applicationContext;

    @Value("${spring.nacos.api-service-name:api}")
    private String apiServiceName;

    public FeignClientConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ApiController apiController() {
        FeignClientBuilder feignClientBuilder = new FeignClientBuilder(applicationContext);
        FeignClientBuilder.Builder<ApiController> builder = feignClientBuilder.forType(ApiController.class, apiServiceName);
        return builder.build();
    }


}
