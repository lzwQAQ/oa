package com.kuyuner.job.config;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author tangzj
 */
@Component
public class QuartzServletContextListener implements ServletContextListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Scheduler scheduler;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            System.out.println("111111111111");
            scheduler.shutdown(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}