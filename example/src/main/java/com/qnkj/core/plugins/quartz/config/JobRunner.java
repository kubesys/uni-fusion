package com.qnkj.core.plugins.quartz.config;


import com.qnkj.core.plugins.quartz.domain.QuartzJob;
import com.qnkj.core.plugins.quartz.utils.QuartzManage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Slf4j
@Component
@Order(value = Integer.MIN_VALUE + 98)
public class JobRunner implements ApplicationRunner {
    private final ConfigurableApplicationContext context;
    private final QuartzManage quartzManage;

    public JobRunner(ConfigurableApplicationContext context, QuartzManage quartzManage) {
        this.context = context;
        this.quartzManage = quartzManage;
    }

    /**
     * 项目启动时重新激活启用的定时任务
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments){
        if(context.isActive()) {
            log.info("正在注入定时任务......");
            List<QuartzJob> quartzJobs = JobConfigs.get();
            quartzJobs.forEach(quartzManage::addJob);
            log.info("定时任务注入完成");
        }
    }
}
