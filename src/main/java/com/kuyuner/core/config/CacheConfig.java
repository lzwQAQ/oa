package com.kuyuner.core.config;

import com.kuyuner.core.config.autoconfigure.KuyunerProperties;

import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * 缓存相关设置
 *
 * @author administrator
 */
@Configuration
public class CacheConfig {

    @Bean
    public EhCacheManagerFactoryBean ehCacheManager(KuyunerProperties kuyunerProperties) {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource(kuyunerProperties.getEhcacheConfigFile()));
        return ehCacheManagerFactoryBean;
    }

}
