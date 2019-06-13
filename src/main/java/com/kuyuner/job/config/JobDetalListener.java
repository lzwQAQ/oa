package com.kuyuner.job.config;

import org.quartz.listeners.JobListenerSupport;
import org.springframework.stereotype.Component;

/**
 * @author tangzj
 */
@Component
public class JobDetalListener extends JobListenerSupport {
    @Override
    public String getName() {
        return JobDetalListener.class.getName();
    }
}
