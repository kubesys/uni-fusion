package com.qnkj.core.plugins.quartz.utils;


import com.qnkj.core.plugins.quartz.domain.QuartzJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Slf4j
@Component
public class QuartzManage {

    private static final String JOB_NAME = "TASK_";

    @Resource(name = "scheduler")
    private Scheduler scheduler;

    public void addJob(QuartzJob quartzJob){
        try {
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ExecutionJob.class).
                    withIdentity(JOB_NAME + quartzJob.id).build();

            //通过触发器名和cron 表达式创建 Trigger
            Trigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(JOB_NAME + quartzJob.id)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.cronExpression))
                    .build();

            cronTrigger.getJobDataMap().put("JOB_KEY", quartzJob);

            //重置启动时间
            ((CronTriggerImpl)cronTrigger).setStartTime(new Date());

            //执行定时任务
            scheduler.scheduleJob(jobDetail,cronTrigger);

        } catch (Exception e){
            log.error("创建定时任务失败", e);
        }
    }

    /**
     * 更新job cron表达式
     * @param quartzJob /
     */
    public void updateJobCron(QuartzJob quartzJob){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.id);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if(trigger == null){
                addJob(quartzJob);
                trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            }
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.cronExpression);
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //重置启动时间
            ((CronTriggerImpl)trigger).setStartTime(new Date());
            trigger.getJobDataMap().put("JOB_KEY",quartzJob);

            scheduler.rescheduleJob(triggerKey, trigger);

        } catch (Exception e){
            log.error("更新定时任务失败", e);
        }

    }

    /**
     * 删除一个job
     * @param quartzJob /
     */
    public void deleteJob(QuartzJob quartzJob){
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.id);
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (Exception e){
            log.error("删除定时任务失败", e);
        }
    }

    /**
     * 恢复一个job
     * @param quartzJob /
     */
    public void resumeJob(QuartzJob quartzJob){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.id);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if(trigger == null) {
                addJob(quartzJob);
            }
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.id);
            scheduler.resumeJob(jobKey);
        } catch (Exception e){
            log.error("恢复定时任务失败", e);
        }
    }

    /**
     * 立即执行job
     * @param quartzJob /
     */
    public void runAJobNow(QuartzJob quartzJob){
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.id);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if(trigger == null) {
                addJob(quartzJob);
            }
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("JOB_KEY", quartzJob);
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.id);
            scheduler.triggerJob(jobKey,dataMap);
        } catch (Exception e){
            log.error("定时任务执行失败", e);
        }
    }

    /**
     * 暂停一个job
     * @param quartzJob /
     */
    public void pauseJob(QuartzJob quartzJob){
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.id);
            scheduler.pauseJob(jobKey);
        } catch (Exception e){
            log.error("定时任务暂停失败", e);
        }
    }
}
