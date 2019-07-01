package com.kuyuner.bg.email.service;

import com.kuyuner.bg.email.entity.EmailReceive;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 收件箱Service层接口
 *
 * @author administrator
 */
public interface EmailReceiveService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param emailReceive
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, EmailReceive emailReceive,String userId);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    EmailReceive get(String id);

    /**
     * 插入数据
     *
     * @param emailReceive
     * @return
     */
    ResultJson saveOrUpdate(EmailReceive emailReceive);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 获取邮件html内容
     *
     * @param id
     * @return
     */
    String getEmailContent(String id);

    /**
     * @param emailReceive
     * @return
     */
    ResultJson update(EmailReceive emailReceive);

    /**
     * 假删
     *
     * @param ids
     * @return
     */
    ResultJson updateDeletes(String[] ids);

    /**
     * 查询一份邮件
     *
     * @param id
     * @return
     */
    EmailReceive getEmail(String id);

    /**
     * 查询已经选择的收件人等信息
     *
     * @param emailId
     * @return
     */
    ResultJson findSelectedUsers(String emailId);

    /**
     * 查询需要回复的人员
     *
     * @param emailId
     * @param type
     * @return
     */
    ResultJson findReplyusers(String emailId, String type);

    /**
     * 获得最新的未读邮件
     *
     * @return
     */
    ResultJson getLatestEmail();
}