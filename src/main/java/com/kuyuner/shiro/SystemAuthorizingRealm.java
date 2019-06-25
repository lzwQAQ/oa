package com.kuyuner.shiro;

import com.kuyuner.common.codec.EncodeUtils;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.security.PasswordUtils;
import com.kuyuner.common.security.Principal;
import com.kuyuner.core.sys.dao.LoginDao;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;

import com.kuyuner.shiro.config.ShiroConfig;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.PostConstruct;

/**
 * 系统用户认证
 *
 * @author tangzj
 */
@Component
public class SystemAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private LoginDao loginDao;

    @Value("${kuyuner.shiro.captcha}")
    private boolean isCaptcha;

    /**
     * 设定密码校验的Hash算法与迭代次数
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(PasswordUtils.MD5);
        matcher.setHashIterations(PasswordUtils.HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }

    /**
     * 用户认证
     *
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

        //是否是验证码登录
        String captcha = (String) UserUtils.getSession().getAttribute(UserUtils.CAPTCHA);
        UserUtils.getSession().setAttribute(UserUtils.CAPTCHA, null);
        UserUtils.getSession().setAttribute("channel", token.getChannel());
        String isCaptchaStr = (String) UserUtils.getSession().getAttribute(UserUtils.IS_CAPTCHA);
        if (false || StringUtils.equals(isCaptchaStr, "true")) {
            if (captcha == null || !StringUtils.equalsIgnoreCase(token.getCaptcha(), captcha.toString())) {
                throw new AuthenticationException("msg:验证码错误！");
            }
        }
        User user = loginDao.login(token.getUsername());
        if (user != null) {
            if (StringUtils.equals(user.getState(), "S")) {
                throw new AuthenticationException("msg:该账户已经被锁定！");
            }
            byte[] salt = EncodeUtils.decodeHex(user.getPassword().substring(0, 16));
            UserUtils.getSession().setAttribute("userId", user.getId());
            return new SimpleAuthenticationInfo(new Principal(user.getId(), user.getUsername(), user.getName()),
                    user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
        }
        throw new AuthenticationException("msg:用户名或密码错误！");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal principal = (Principal) getAvailablePrincipal(principals);
        List<String> permissions = loginDao.findUserPermissions(principal.getId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (String permission : permissions) {
            info.addStringPermission(permission);
        }
        return info;
    }
}
