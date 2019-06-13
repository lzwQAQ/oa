package com.kuyuner.bg.email.job;

import com.kuyuner.bg.email.service.EmailSendService;
import com.kuyuner.common.utils.SpringContextHolder;

import org.quartz.JobKey;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @create 2018-11-18
 */
@Component
public class SendEmailJob {

    public void send(JobKey jobKey, String emailId) {
        EmailSendService emailSendService = SpringContextHolder.getBean(EmailSendService.class);
        emailSendService.sendScheduleEmail(jobKey, emailId);
    }

}
