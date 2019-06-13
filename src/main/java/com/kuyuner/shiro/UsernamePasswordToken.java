package com.kuyuner.shiro;

/**
 * 用户和密码（包含验证码）令牌类
 *
 * @author tangzj
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

    private static final long serialVersionUID = 1L;

    private String captcha;

    private String channel = "H5";

    public UsernamePasswordToken() {
        super();
    }

    public UsernamePasswordToken(String username, char[] password, boolean rememberMe, String host, String captcha,String channel) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.channel = channel;
    }

    public String getCaptcha() {
        return captcha;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}