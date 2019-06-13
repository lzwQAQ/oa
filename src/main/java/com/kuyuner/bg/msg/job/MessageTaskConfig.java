package com.kuyuner.bg.msg.job;

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
public class MessageTaskConfig {


    @Autowired
    private TaskManager taskManager;


    @PostConstruct
    private void initTask() {
        taskManager.loadMessage("send_message");
    }
}
