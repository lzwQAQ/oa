package com.kuyuner.common.network;

import com.kuyuner.common.lang.ObjectUtils;
import com.kuyuner.common.lang.StringUtils;

import org.springframework.web.util.WebUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web层辅助类
 * @author tangzj
 */
public final class WebUtil {

    /**
     * 设置文件名
     */
    public static void setResponseFileName(HttpServletRequest request, HttpServletResponse response,
                                           String displayName) {
        String userAgent = request.getHeader("User-Agent");
        boolean isIE = false;
        if (userAgent != null && userAgent.toLowerCase().contains("msie")) {
            isIE = true;
        }
        String displayName2;
        try {
            if (isIE) {
                displayName2 = URLEncoder.encode(displayName, "UTF-8");
                // 修正URLEncoder将空格转换成+号的BUG
                displayName2 = displayName2.replaceAll("\\+", "%20");
                response.setHeader("Content-Disposition", "attachment;filename=" + displayName2);
            } else {
                displayName2 = new String(displayName.getBytes("UTF-8"), "ISO8859-1");
                // firefox空格截断
                response.setHeader("Content-Disposition", "attachment;filename=\"" + displayName2 + "\"");
            }
            String extStr = displayName2.substring(displayName2.indexOf(".") + 1);
            if ("xls".equalsIgnoreCase(extStr)) {
                response.setContentType("application/vnd.ms-excel charset=UTF-8");
            } else {
                response.setContentType("application/octet-stream");
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取指定Cookie的值
     *
     * @param request
     * @param cookieName   cookie名字
     * @param defaultValue 缺省值
     * @return
     */
    public static final String getCookieValue(HttpServletRequest request, String cookieName, String defaultValue) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie == null) {
            return defaultValue;
        }
        return cookie.getValue();
    }

    /**
     * 获取客户端IP地址
     *
     * @param request
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return StringUtils.split(ObjectUtils.toString(ip), ",")[0];
    }
}
