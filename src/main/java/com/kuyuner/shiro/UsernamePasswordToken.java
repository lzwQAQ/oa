package com.kuyuner.shiro;

/**
 * 用户和密码（包含验证码）令牌类
 *
 * @author tangzj
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

    private static final long serialVersionUID = 1L;

    private String captcha;

    private String loginType;

    public UsernamePasswordToken() {
        super();
    }

    public UsernamePasswordToken(String username, char[] password, boolean rememberMe, String host, String captcha,String loginType) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.loginType = loginType;
    }

    public String getCaptcha() {
        return captcha;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}