package com.qnkj.core.plugins.quartz.utils;


import com.qnkj.core.plugins.quartz.domain.QuartzJob;
import com.qnkj.core.plugins.quartz.thread.ThreadPoolExecutorUtil;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Async
public class ExecutionJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 该处仅供参考*/
    private final static ThreadPoolExecutor EXECUTOR_JOB = ThreadPoolExecutorUtil.getPoll();

    @Override
    @SuppressWarnings("unchecked")
    protected void executeInternal(JobExecutionContext context) {
        QuartzJob quartzJob = (QuartzJob) context.getMergedJobDataMap().get("JOB_KEY");
        long startTime = System.currentTimeMillis();
        try {
            // 执行任务
            if (quartzJob.showLog) {
                logger.info("任务准备执行，任务名称：{}", quartzJob.jobName);
            }
            QuartzRunnable task = new QuartzRunnable(quartzJob.beanName, quartzJob.methodName,"");
            Future<?> future = EXECUTOR_JOB.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            if (quartzJob.showLog) {
                logger.info("任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", quartzJob.jobName, times);
            }
        } catch (Exception e) {
            logger.error("任务执行失败，任务名称：{}" + quartzJob.jobName, e);
            Thread.currentThread().interrupt();
        }
    }
}
