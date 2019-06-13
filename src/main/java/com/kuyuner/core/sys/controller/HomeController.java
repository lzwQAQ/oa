package com.kuyuner.core.sys.controller;


import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.image.CaptchaUtils;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Administrator
 */
@Controller
public class HomeController extends BaseController {

    /**
     * 欢迎页
     *
     * @return
     */
    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * 获得登录验证码
     *
     * @return
     */
    @RequestMapping("login/captcha")
    public void loginCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        HttpSession session = request.getSession();
        String captcha = CaptchaUtils.generateCaptcha(response.getOutputStream());
        session.setAttribute(UserUtils.CAPTCHA, captcha);
    }
}