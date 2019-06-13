package com.kuyuner.job.config;

import org.quartz.listeners.TriggerListenerSupport;
import org.springframework.stereotype.Component;

/**
 * @author tangzj
 */
@Component
public class JobTriggerListener extends TriggerListenerSupport {
    @Override
    public String getName() {
        return JobTriggerListener.class.getName();
    }
}
