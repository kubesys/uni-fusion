package com.qnkj.core.webconfigs.configure;

import com.google.common.base.Predicates;
import com.qnkj.common.configs.BaseSaasConfig;
import com.qnkj.common.utils.SaaSUtils;
import com.qnkj.core.webconfigs.xss.XssFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Oldhand
 */
@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class WebConfigure {
    @Value("${swagger.enabled}")
    private Boolean enabled;

    @Bean(WebConstant.ASYNC_POOL)
    public ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("Web-Async-Thread");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * XssFilter Bean
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean() {
        FilterRegistrationBean<XssFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>(2);
        initParameters.put("excludes", "/favicon.ico,/images/*,/js/*,/css/*");
        initParameters.put("isIncludeRichText", "true");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enabled)
                .select()
//                .paths(PathSelectors.any())
                .paths(Predicates.and(Predicates.not(PathSelectors.regex("/error.*")),Predicates.not(PathSelectors.regex("/actuator.*"))))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        SaaSUtils saaSUtils = new SaaSUtils(BaseSaasConfig.getDomain());
        String copyright = saaSUtils.getCopyright();
        String name = saaSUtils.getPlatformName();

        return new ApiInfoBuilder()
                .title(name + " by " + copyright)
                .version("1.0")
                .description(name + " 管理端")
                .contact(new Contact(copyright + " (手机: 15111122026)", "", ""))
                .build();
    }

}
