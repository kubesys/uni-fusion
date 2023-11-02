package com.qnkj.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * create by 徐雁
 * create date 2020/11/8
 */
@Component
public class SpringServiceUtil implements ApplicationContextAware, DisposableBean {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringServiceUtil.applicationContext == null) {
            SpringServiceUtil.applicationContext = applicationContext;
        }
        System.out.println("===========================================");
        System.out.println("    ApplicationContext配置成功");
        System.out.println("    在普通类可以通过调用：");
        System.out.println("    SpringServiceUtil.getBean()获取Class对象");
        System.out.println("===========================================");
    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    @Override
    public void destroy() throws Exception {
        applicationContext = null;
    }
}
