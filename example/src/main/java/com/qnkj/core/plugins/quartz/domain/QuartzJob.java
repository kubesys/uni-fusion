package com.qnkj.core.plugins.quartz.domain;

/**
 * @author oldhand
 * @date 2019-12-16
*/

public class QuartzJob {

    public int id;
    // 定时器名称
    public String jobName;

    // Bean名称
    public String beanName;

    // 方法名称
    public String methodName;

    // cron表达式
    public String cronExpression;

    // 是否显示日志信息
    public Boolean showLog;

    public QuartzJob(int id,String jobName,String beanName,String methodName,String cronExpression) {
        this.id = id;
        this.jobName = jobName;
        this.beanName = beanName;
        this.methodName = methodName;
        this.cronExpression = cronExpression;
        this.showLog = false;
    }
    public QuartzJob(int id,String jobName,String beanName,String methodName,String cronExpression,Boolean showLog) {
        this.id = id;
        this.jobName = jobName;
        this.beanName = beanName;
        this.methodName = methodName;
        this.cronExpression = cronExpression;
        this.showLog = showLog;
    }


}