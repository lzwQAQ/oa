package com.kuyuner.core.sys.controller;


import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.sys.entity.Menu;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.model.ResultModel;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.MenuService;
import com.kuyuner.core.sys.service.UserService;

import com.kuyuner.shiro.UsernamePasswordToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    /**
     * 主页
     *
     * @return
     */
    @RequestMapping("")
    public String index(ModelMap modelMap) {
        if (UserUtils.getPrincipal() == null) {
            return "login";
        }

        if (UserUtils.getSession().getAttribute(UserUtils.USER_FLAG) == null) {
            UserUtils.getSession().setAttribute(UserUtils.USER_FLAG, userService.get(UserUtils.getPrincipal().getId()));
        }

        List<Menu> menus = (List<Menu>) UserUtils.getSession().getAttribute("menus");
        if (menus == null) {
            menus = menuService.findAllListBySort(UserUtils.getPrincipal().getId());
            UserUtils.getSession().setAttribute("menus", menus);
        }
        modelMap.addAttribute("menus", menus);
        return "index";
    }

    @RequestMapping("logininfo")
    @ResponseBody
    public ResultJson logininfo(String username, String password) {
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return ResultJson.failed("请输入用户名或密码");
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password.toCharArray(),true,"","aaaa","app");
        try {
            subject.login(token);
        }catch (AuthenticationException e){
            return ResultJson.failed("用户名或密码错误");
        }

        if (UserUtils.getSession().getAttribute(UserUtils.USER_FLAG) == null) {
            UserUtils.getSession().setAttribute(UserUtils.USER_FLAG,userService.get(UserUtils.getPrincipal().getId()) );
        }
        List<Menu> menus = (List<Menu>) UserUtils.getSession().getAttribute("menus");
        if (menus == null) {
            menus = menuService.findAllListBySort(UserUtils.getPrincipal().getId());
            UserUtils.getSession().setAttribute("menus", menus);
        }
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("menus", menus);
        return ResultJson.ok(UserUtils.getSession().getAttribute(UserUtils.USER_FLAG));
    }

    /**
     * 登录失败跳转的页面
     *
     * @return
     */
    @RequestMapping("login")
    public String loginFail(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            String message = (String) request.getAttribute("errorMessage");
            redirectAttributes.addFlashAttribute("errorMessage", message);
            //更新登录出错次数，账号存在就跟账号绑定，账号不存在就和session绑定
            UserUtils.updateLoginNum();
            String channel = UserUtils.getSession().getAttribute("channel") == null ? "" : UserUtils.getSession().getAttribute("channel").toString();
            if("PC".equals(channel)){
                return "redirect:/";
            }
            return "redirect:/logininfo?errorMessage="+ URLEncoder.encode(message == null ? "" : message,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "redirect:/logininfo";
    }
}