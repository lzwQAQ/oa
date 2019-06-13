package com.kuyuner.workflow.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.kuyuner.workflow.engine.image.CustomProcessDiagramGenerator;
import com.kuyuner.workflow.engine.service.UUIDGenerator;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author tangzj
 */
@Configuration
@ComponentScan(value = {"org.activiti.rest.editor", "org.activiti.rest.diagram"}, includeFilters = {@ComponentScan.Filter(Controller.class)})
public class WorkFlowConfig {

    @Bean
    public SpringProcessEngineConfiguration engineConfiguration(DruidDataSource druidDataSource,
                                                                PlatformTransactionManager transactionManager) {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(druidDataSource);
        configuration.setTransactionManager(transactionManager);
        configuration.setDatabaseSchemaUpdate("false");
        configuration.setJobExecutorActivate(false);
        configuration.setCreateDiagramOnDeploy(true);
        configuration.setHistory("audit");
        configuration.setProcessDefinitionCacheLimit(50);
        configuration.setActivityFontName("微软雅黑");
        configuration.setLabelFontName("微软雅黑");
        configuration.setIdGenerator(new UUIDGenerator());
        configuration.setProcessDiagramGenerator(new CustomProcessDiagramGenerator());
        return configuration;
    }

    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean(SpringProcessEngineConfiguration configuration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(configuration);
        return processEngineFactoryBean;
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }


    @Bean
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }


    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

}
