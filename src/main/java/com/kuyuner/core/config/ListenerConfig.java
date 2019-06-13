package com.kuyuner.core.config;

import com.kuyuner.job.config.QuartzServletContextListener;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextListener;

/**
 * Listener 配置
 *
 * @author administrator
 */
@Configuration
public class ListenerConfig {

    @Bean
    public ServletListenerRegistrationBean<ServletContextListener> requestContextListener(QuartzServletContextListener quartzServletContextListener) {
        ServletListenerRegistrationBean<ServletContextListener> bean = new ServletListenerRegistrationBean<>();
        bean.setListener(quartzServletContextListener);
        return bean;
    }

}
