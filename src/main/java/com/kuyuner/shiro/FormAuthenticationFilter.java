package com.kuyuner.shiro;

import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.web.http.ServletUtils;
import com.kuyuner.core.sys.security.UserUtils;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 表单验证（包含验证码）过滤类
 *
 * @author tangzj
 */
@Component
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

    public static final String DEFAULT_LOGIN_CHANNEL = "channel";
    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
    public static final String DEFAULT_MESSAGE_PARAM = "errorMessage";

    private String channelParam = DEFAULT_LOGIN_CHANNEL;
    private String captchaParam = DEFAULT_CAPTCHA_PARAM;
    private String messageParam = DEFAULT_MESSAGE_PARAM;

    private Logger logger = LoggerFactory.getLogger(FormAuthenticationFilter.class);

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        if (password == null) {
            password = "";
        }
        boolean rememberMe = isRememberMe(request);
        String host = ServletUtils.getRemoteAddr((HttpServletRequest) request);
        String captcha = getCaptcha(request);
        String loginType = getLoginType(request);
        return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha,loginType);
    }

    public String getCaptchaParam() {
        return captchaParam;
    }

    public String getChannelParam(){
        return channelParam;
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    protected String getLoginType(ServletRequest request) {
        return WebUtils.getCleanParam(request, getChannelParam());
    }

    public String getMessageParam() {
        return messageParam;
    }

    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        UserUtils.clearLoginNum();
        return super.onLoginSuccess(token, subject, request, response);
    }

    /**
     * 登录失败调用事件
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        String message = "";
        if (IncorrectCredentialsException.class.isInstance(e) || UnknownAccountException.class.isInstance(e)) {
            message = "用户或密码错误！";
        } else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
            message = StringUtils.replace(e.getMessage(), "msg:", "");
        } else {
            message = "系统故障，请稍后登录！";
            logger.error(e.getMessage(), e);
        }
        request.setAttribute(getMessageParam(), message);
        return true;
    }

}