package com.kuyuner.job.jobs;

import org.springframework.stereotype.Component;

/**
 * @author tangzj
 */
@Component
public class HelloJob {
    public void sayHello() throws InterruptedException {
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("start at "+dateFormat.format(new Date()));
        Thread.sleep(5 * 1000);
        System.out.println("finish at "+dateFormat.format(new Date()));*/
    }
}
