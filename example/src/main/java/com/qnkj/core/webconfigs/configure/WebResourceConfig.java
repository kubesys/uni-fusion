package com.qnkj.core.webconfigs.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebResourceConfig implements WebMvcConfigurer {

//    @Resource
//    DomainInterceptor domainInterceptor;

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(domainInterceptor).addPathPatterns("/**");
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/layui/**")
                .addResourceLocations("classpath:/static/layui/","classpath:/layui/");
        registry.addResourceHandler("/zTree/**")
                .addResourceLocations("classpath:/zTree/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/","classpath:/images/");
        registry.addResourceHandler("/echarts/**")
                .addResourceLocations("classpath:/static/echarts/","classpath:/echarts/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/iconfont/**")
                .addResourceLocations("classpath:/static/iconfont/");
        registry.addResourceHandler("/font/**")
                .addResourceLocations("classpath:/font/");
        registry.addResourceHandler("/floweditor/**")
                .addResourceLocations("classpath:/floweditor/");
        registry.addResourceHandler("/tools/**")
                .addResourceLocations("classpath:/tools/");
        registry.addResourceHandler("/wiki/**")
                .addResourceLocations("classpath:/wiki/static/");
        registry.addResourceHandler("/tools/**")
                .addResourceLocations("classpath:/tools/static/");
        registry.addResourceHandler("/novnc/**")
                .addResourceLocations("classpath:/clouds/static/");
        registry.addResourceHandler("/highcharts/**")
                .addResourceLocations("classpath:/highcharts/");
        registry.addResourceHandler("/bpmn/**")
                .addResourceLocations("classpath:/bpmn/");

    }
}
