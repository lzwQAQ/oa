package com.kuyuner.core.config;

import com.kuyuner.common.properties.PropertiesHolder;
import com.kuyuner.core.common.dict.DictHolder;
import com.kuyuner.core.config.autoconfigure.KuyunerProperties;
import com.kuyuner.core.config.beans.MyObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * springmvc 配置
 *
 * @author Administrator
 */
@Configuration
@EnableWebMvc
@ComponentScan(value = "com.kuyuner", useDefaultFilters = false, includeFilters = {@ComponentScan.Filter(Controller.class)})
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private DictHolder dictHolder;

    @Autowired
    private MyObjectMapper objectMapper;

    @Autowired
    private KuyunerProperties kuyunerProperties;

    /**
     * 配置静态资源的处理
     * 将请求交由Servlet处理,不经过DispatchServlet
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        super.configureDefaultServletHandling(configurer);
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //将所有/static/** 访问都映射到classpath:/static/ 目录下
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/files/**").addResourceLocations("file:" + kuyunerProperties.getFileBasePath());
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        super.configureViewResolvers(registry);

        //thymeleaf添加全局静态变量
        Map<String, Object> map = new HashMap<>(2);
        map.put("dictHolder", dictHolder);
        map.put("adminPath", kuyunerProperties.getAdminPath());
        map.put("env", PropertiesHolder.class);
        thymeleafViewResolver.setStaticVariables(map);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        messageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        converters.add(messageConverter);

        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new AllEncompassingFormHttpMessageConverter());
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> {
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error/page/404");
            container.addErrorPages(error404Page);
        };
    }

}
