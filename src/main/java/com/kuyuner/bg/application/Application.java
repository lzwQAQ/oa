package com.kuyuner.bg.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * {@code @MapperScan} 是mybatis的扫描
 *
 * @author administrator
 */
@SpringBootApplication(scanBasePackages = {"com.kuyuner"})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        //System.out.println(System.getProperty("java.io.tmpdir"));
        SpringApplication.run(Application.class, args);
    }

    /**
     * 仅在使用tomcat启动的时候，该方法会被调用并起作用
     *
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 错误页面由容器来处理，而不是SpringBoot
        this.setRegisterErrorPageFilter(false);
        return builder.sources(Application.class);
    }


}

