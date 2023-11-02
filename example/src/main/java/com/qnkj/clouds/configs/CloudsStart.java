package com.qnkj.clouds.configs;


import com.qnkj.core.webconfigs.configure.FreemarkerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * create by 徐雁
 * create date 2021/11/8
 * create time 9:15 上午
 */

@Order(Integer.MIN_VALUE)
@Component
@Slf4j
@RequiredArgsConstructor
public class CloudsStart implements ApplicationRunner {
    private final ConfigurableApplicationContext context;



    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (context.isActive()) {
//            log.info(ColorUtils.blue() + "正在搜索普罗米修斯(Prometheus)服务......" + ColorUtils.white());

        }

    }
}
