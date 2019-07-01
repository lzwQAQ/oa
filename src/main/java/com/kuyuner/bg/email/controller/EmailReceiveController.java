package com.kuyuner.bg.email.controller;

import com.kuyuner.bg.email.entity.EmailReceive;
import com.kuyuner.bg.email.service.EmailReceiveService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

import com.kuyuner.common.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 收件箱Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/emailreceive")
public class EmailReceiveController extends BaseController {

    @Autowired
    private EmailReceiveService emailReceiveService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("receive")
    public String showEmailReceiveList() {
        return "email/emailReceiveList";
    }

    /**
     * 查询分页集合JSON数据
     *
     * @param emailReceive
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageJson list(EmailReceive emailReceive, String pageNum, String pageSize,String userId) {
        if(StringUtils.isBlank(pageNum) || StringUtils.isBlank(pageSize)){
            pageNum = "1";
            pageSize = "10000";
        }
        return emailReceiveService.findPageList(pageNum, pageSize, emailReceive,userId);
    }

    /**
     * 收藏或取消收藏
     *
     * @param id
     * @param star
     * @return
     */
    @ResponseBody
    @RequestMapping("star/{id}")
    public ResultJson star(@PathVariable("id") String id, String star) {
        EmailReceive emailReceive = new EmailReceive();
        emailReceive.setId(id);
        emailReceive.setStar("1".equals(star) ? "1" : "0");
        emailReceiveService.update(emailReceive);
        return ResultJson.ok("1".equals(emailReceive.getStar()) ? "收藏成功！" : "取消成功！");
    }


    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    public ResultJson delete(String ids) {
        return emailReceiveService.deletes(ids.split(","));
    }

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("update/deletes")
    public ResultJson updateDelete(String ids) {
        return emailReceiveService.updateDeletes(ids.split(","));
    }

    /**
     * 查询已经选择的收件人等信息
     *
     * @param emailId
     * @return
     */
    @ResponseBody
    @RequestMapping("selectedusers")
    public ResultJson findSelectedUsers(String emailId) {
        return emailReceiveService.findSelectedUsers(emailId);
    }

    /**
     * 获得最新的未读邮件
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("latestemail")
    public ResultJson findLatestEmail() {
        return emailReceiveService.getLatestEmail();
    }
}