package com.kuyuner.bg.msg.util;

import groovy.util.logging.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AfterServiceStarted implements ApplicationRunner {
    /**
     * 会在服务启动完成后立即执行
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //连接短信服务器
        Msgclient.getClient();
    }

}
