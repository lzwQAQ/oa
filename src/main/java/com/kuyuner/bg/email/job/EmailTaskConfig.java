package com.kuyuner.bg.email.job;

import com.kuyuner.bg.job.TaskManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Administrator
 * @create 2018-11-18
 */
@Component
@Lazy(value = false)
public class EmailTaskConfig {


    @Autowired
    private TaskManager taskManager;


    @PostConstruct
    private void initTask() {
        taskManager.load("send_email");
    }
}
