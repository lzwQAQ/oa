package com.kuyuner.shiro.config;

import com.kuyuner.core.config.autoconfigure.KuyunerProperties;
import com.kuyuner.shiro.*;

import net.sf.ehcache.CacheManager;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/**
 * Filter 配置
 *
 * @author administrator
 */
@Configuration
public class ShiroConfig {
    private Map<String, String> getFilterChainDefinitionMap(KuyunerProperties kuyunerProperties) {
        Map<String, String> map = new HashMap<>(4);
        for (String anon : kuyunerProperties.getShiro().getAnons()) {
            map.put(anon, "anon");
        }
        map.put("/static/**", "anon");
        map.put("/login/captcha", "anon");
        map.put("/logout", "logout");
        map.put("/login", "authc");
        map.put("/logininfo", "anon");
        map.put("/", "anon");
        map.put("/**", "user");
        return map;
    }

    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        registration.setFilter(proxy);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager,
                                                         FormAuthenticationFilter formAuthenticationFilter,
                                                         KuyunerProperties kuyunerProperties) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        Map<String, Filter> filters = new HashMap<>();
        filters.put("authc", formAuthenticationFilter);
        shiroFilterFactoryBean.setFilters(filters);

        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/login");
        //shiroFilterFactoryBean.setUnauthorizedUrl("/welcome");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(getFilterChainDefinitionMap(kuyunerProperties));

        return shiroFilterFactoryBean;
    }

    /**
     * Shiro安全管理配置
     *
     * @param systemAuthorizingRealm
     * @return
     */
    @Bean
    public SecurityManager securityManager(SystemAuthorizingRealm systemAuthorizingRealm,
                                           CookieRememberMeManager cookieRememberMeManager,
                                           CacheManager cacheManager, DefaultWebSessionManager defaultWebSessionManager) {
        systemAuthorizingRealm.setAuthorizationCache(new ShiroCache(cacheManager.getCache("shiroCache")));
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(systemAuthorizingRealm);
        securityManager.setRememberMeManager(cookieRememberMeManager);
        securityManager.setSessionManager(defaultWebSessionManager);
        return securityManager;
    }

    @Bean
    @ConfigurationProperties("spring.shiro.cookie")
    public SimpleCookie simpleCookie() {
        return new SimpleCookie();
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager(SimpleCookie simpleCookie, KuyunerProperties kuyunerProperties) {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(simpleCookie);
        try {
            cookieRememberMeManager.setCipherKey(kuyunerProperties.getShiro().getCipherKey().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return cookieRememberMeManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false);
        return defaultWebSessionManager;
    }

    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
