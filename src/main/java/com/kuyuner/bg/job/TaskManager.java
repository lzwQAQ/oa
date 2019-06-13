package com.kuyuner.bg.job;

import com.kuyuner.bg.job.dao.ScheduleTaskDao;
import com.kuyuner.bg.job.entity.ScheduleTask;
import com.kuyuner.common.lang.StringUtils;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @create 2018-11-18
 */
@Component
public class TaskManager {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ScheduleTaskDao taskDao;

    /**
     * 将定时任务加载到计划中
     *
     * @param tasks
     */
    public void scheduleJob(ScheduleTask... tasks) {
        try {
            for (ScheduleTask task : tasks) {
                MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
                JobKey jobKey = new JobKey(task.getId());
                jobDetail.setConcurrent(false);
                jobDetail.setName(task.getId());
                jobDetail.setTargetObject(Class.forName(task.getClassName()).newInstance());
                jobDetail.setTargetMethod(task.getMethodName());
                jobDetail.setArguments(jobKey, task.getData());
                jobDetail.afterPropertiesSet();
                JobDetailImpl job = (JobDetailImpl) jobDetail.getObject();
                job.setKey(jobKey);

                CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
                trigger.setName(task.getId());
                trigger.setJobDetail(job);
                trigger.setStartDelay(0);
                trigger.setCronExpression(task.getCron());
                trigger.afterPropertiesSet();

                scheduler.scheduleJob(jobDetail.getObject(), trigger.getObject());
            }
        } catch (ReflectiveOperationException | ParseException | SchedulerException e) {
            logger.error("定时任务异常...{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加任务到数据库并加载任务到计划中
     *
     * @param tasks
     */
    public void addTasks(ScheduleTask... tasks) {
        for (ScheduleTask task : tasks) {
            taskDao.insert(task);
        }
        scheduleJob(tasks);
    }

    /**
     * 将定时任务加载到计划中
     *
     * @param tasks
     */
    public void scheduleJob(List<ScheduleTask> tasks) {
        ScheduleTask[] scheduleTasks = new ScheduleTask[tasks.size()];
        tasks.toArray(scheduleTasks);
        scheduleJob(scheduleTasks);
    }

    /**
     * 加载指定类型的定时任务
     *
     * @param taskType
     */
    public void load(String taskType) {
        List<ScheduleTask> tasks = getAllTasks(taskType);
        tasks.removeIf(sysTask -> !checkOneData(sysTask));
        scheduleJob(tasks);
        logger.info("邮件定时发送任务,启动数量：" + tasks.size());
    }

    /**
     * 加载指定类型的定时任务
     *
     * @param taskType
     */
    public void loadMessage(String taskType) {
        List<ScheduleTask> tasks = getAllTasks(taskType);
        tasks.removeIf(sysTask -> !checkOneData(sysTask));
        scheduleJob(tasks);
        logger.info("短信定时发送任务,启动数量：" + tasks.size());
    }


    /**
     * 删除数据仓库中的定时任务
     *
     * @param jobKey
     */
    public void deleteInStorage(JobKey jobKey) {
        taskDao.deletes(jobKey.getName());
    }

    /**
     * 从数据库里取得所有要执行的定时任务
     *
     * @param taskType
     * @return
     */
    private List<ScheduleTask> getAllTasks(String taskType) {
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setTaskType(taskType);
        return taskDao.findList(scheduleTask);
    }

    private boolean checkOneData(ScheduleTask task) {
        String errorMeg = "success";
        try {
            Class.forName(task.getClassName());
            String cron = task.getCron();
            if (StringUtils.isBlank(cron)) {
                errorMeg = "定时任务[" + task.getClassName() + "]启动错误，无cron";
                logger.error(errorMeg);
            }
        } catch (ClassNotFoundException e) {
            errorMeg = "定时任务[" + task.getClassName() + "]启动错误，找不到类:" + task.getClassName() + e.getMessage();
            logger.error(errorMeg);
        } catch (Exception e) {
            errorMeg = "error";
            logger.error(e.getMessage());
        }
        return "success".equals(errorMeg);
    }

    public void deleteSchedules(String... taskIds) throws SchedulerException {
        if (taskIds == null) {
            return;
        }
        List<JobKey> jobKeys = new ArrayList<>();
        for (String taskId : taskIds) {
            jobKeys.add(new JobKey(taskId));
        }
        if (jobKeys.size() > 0) {
            scheduler.deleteJobs(jobKeys);
        }
    }
}
