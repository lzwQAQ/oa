package com.kuyuner.core.config;

import com.kuyuner.common.properties.PropertiesHolder;
import com.kuyuner.core.config.autoconfigure.KuyunerProperties;

import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Administrator
 */
@Configuration
@ComponentScan(basePackages = {"com.kuyuner"}, excludeFilters = {@ComponentScan.Filter(Controller.class)})
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableConfigurationProperties(KuyunerProperties.class)
public class RootConfig implements EnvironmentAware {

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        return validatorFactoryBean;
    }

    @Override
    public void setEnvironment(Environment environment) {
        PropertiesHolder.init(environment);
    }
}
