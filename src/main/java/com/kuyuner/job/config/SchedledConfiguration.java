package com.kuyuner.job.config;

import com.kuyuner.job.jobs.HelloJob;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @author tangzj
 */
@Configuration
public class SchedledConfiguration {

    @Bean(name = "helloJobDetail")
    public MethodInvokingJobDetailFactoryBean firstJobDetail(HelloJob helloJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(helloJob);
        // 需要执行的方法
        jobDetail.setTargetMethod("sayHello");
        return jobDetail;
    }

    @Bean(name = "helloJobTrigger")
    public SimpleTriggerFactoryBean firstTrigger(JobDetail helloJobDetail) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(helloJobDetail);
        // 设置任务启动延迟
        trigger.setStartDelay(0);
        // 每5秒执行一次
        trigger.setRepeatInterval(3000);
        return trigger;
    }

    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactory(Trigger helloJobTrigger, JobDetalListener jobDetalListener,
                                                 JobSchedulerListener jobSchedulerListener,
                                                 JobTriggerListener jobTriggerListener) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setTriggers(helloJobTrigger);
        bean.setSchedulerListeners(jobSchedulerListener);
        bean.setGlobalJobListeners(jobDetalListener);
        bean.setGlobalTriggerListeners(jobTriggerListener);
        return bean;
    }

}