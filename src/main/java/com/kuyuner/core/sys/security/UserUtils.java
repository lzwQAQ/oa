package com.kuyuner.core.sys.security;

import com.kuyuner.common.cache.EhCacheUtil;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.security.Principal;
import com.kuyuner.common.utils.SpringContextHolder;
import com.kuyuner.core.sys.dao.UserDao;
import com.kuyuner.core.sys.entity.User;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * @author Administrator
 */
public class UserUtils {

    public static final String USER_FLAG = "UserBean";
    public static final String CAPTCHA = "captcha";
    public static final String IS_CAPTCHA = "isCaptcha";
    private static final String ERROR_NUM = "error_num";
    private static final int MAX_ERROR_NUM = 3;
    private static final String SESSIONS_CACHE = "sessionsCache";

    private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);

    public static User getUser() {
        User user = (User) getSession().getAttribute(USER_FLAG);
        if (user == null) {
            user = userDao.get(new User(getPrincipal().getId()));
            getSession().setAttribute(USER_FLAG, user);
        }
        return user;
    }

    public static User getUserFromDB(String userId) {
        User user = userDao.get(new User(userId));
        return user;
    }

    /**
     * 获得登录凭证
     *
     * @return
     */
    public static Principal getPrincipal() {
        Subject subject = SecurityUtils.getSubject();
        Principal principal = (Principal) subject.getPrincipal();
        return principal;
    }

    /**
     * 获得当前Session
     *
     * @return
     */
    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    /**
     * 更新登录次数并设置是否显示验证码
     */
    public static void updateLoginNum() {
        Session session = getSession();
        String userId = (String) session.getAttribute("userId");
        if (StringUtils.isBlank(userId)) {
            userId = (String) session.getId();
        }
        int num = (int) EhCacheUtil.get(SESSIONS_CACHE, userId + "_" + ERROR_NUM, 0);
        EhCacheUtil.put(SESSIONS_CACHE, userId + "_" + ERROR_NUM, ++num);
        if (num >= MAX_ERROR_NUM) {
            session.setAttribute(UserUtils.IS_CAPTCHA, "true");
        }
    }

    /**
     * 重置登录次数
     */
    public static void clearLoginNum() {
        EhCacheUtil.remove(SESSIONS_CACHE, getPrincipal().getId() + "_" + ERROR_NUM);
        EhCacheUtil.remove(SESSIONS_CACHE, getSession().getId() + "_" + ERROR_NUM);
    }
}
