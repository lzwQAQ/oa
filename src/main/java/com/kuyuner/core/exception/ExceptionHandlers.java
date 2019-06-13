package com.kuyuner.core.exception;

import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.exception.ServiceException;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.config.autoconfigure.KuyunerProperties;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常拦截处理，仅处理ajax请求及{@link com.kuyuner.common.exception.ServiceException }异常
 *
 * @author administrator
 */
@ControllerAdvice
public class ExceptionHandlers {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KuyunerProperties kuyunerProperties;

    /**
     * 业务异常捕捉，将异常内容展示在前端
     *
     * @param reqest
     * @param response
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public ResultJson handlerServiceException(HttpServletRequest reqest, HttpServletResponse response, Exception e) {
        logger.error(e.getMessage(), e);
        return ResultJson.failed(e.getMessage());
    }

    /**
     * Shiro权限验证异常捕捉
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler({AuthorizationException.class, UnauthorizedException.class})
    public ModelAndView handlerAuthorizationException(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      Exception e) throws Exception {
        response.setStatus(403);
        if (isAjax(request)) {
            return errorJson("no permissions error");
        } else {
            String page403 = kuyunerProperties.getErrorPage().getPage403();
            if (StringUtils.isBlank(page403)) {
                RuntimeException ex = new RuntimeException("page403 is empty");
                logger.error(ex.getMessage(), ex);
                throw e;
            }
            return new ModelAndView(page403);
        }
    }

    /**
     * 全局异常捕捉
     *
     * @param request
     * @param response
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handlerException(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        logger.error(e.getMessage(), e);
        if (isAjax(request)) {
            return errorJson(e instanceof ServiceException ? e.getMessage() : "");
        } else {
            String page500 = kuyunerProperties.getErrorPage().getPage500();
            if (StringUtils.isBlank(page500)) {
                RuntimeException ex = new RuntimeException("page500 is empty");
                logger.error(ex.getMessage(), ex);
                throw e;
            }
            return new ModelAndView(page500);
        }
    }

    private ModelAndView errorJson(String errorMsg) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 500);
        map.put("errorMsg", errorMsg);
        map.put("data", "");
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView(), map);
        return mav;
    }

    /**
     * 判断是否是ajax请求
     *
     * @param httpRequest
     * @return
     */
    private static boolean isAjax(HttpServletRequest httpRequest) {
        return (httpRequest.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With").toString()));
    }
}