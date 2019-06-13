package com.kuyuner.core.sys.controller;

import com.kuyuner.core.config.autoconfigure.KuyunerProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 404页面处理
 * @author tangzj
 */
@Controller
public class Page404Controller {

    @Autowired
    private KuyunerProperties kuyunerProperties;

    @RequestMapping("/error/page/404")
    public String page404() {
        return kuyunerProperties.getErrorPage().getPage404();
    }
}
