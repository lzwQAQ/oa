package com.kuyuner.bg.msg.job;

import com.kuyuner.bg.email.service.EmailSendService;
import com.kuyuner.bg.msg.service.MessageService;
import com.kuyuner.common.utils.SpringContextHolder;
import org.quartz.JobKey;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @create 2018-11-18
 */
@Component
public class SendMessageJob {

    public void send(JobKey jobKey, String messageId) {
        MessageService messageService = SpringContextHolder.getBean(MessageService.class);
        messageService.sendScheduleMessage(jobKey, messageId);
    }

}
