package com.kuyuner.bg.email.service;

import com.kuyuner.bg.email.entity.EmailSend;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.io.FileInfo;

import org.quartz.JobKey;

import java.util.Date;
import java.util.List;

/**
 * 发件箱Service层接口
 *
 * @author administrator
 */
public interface EmailSendService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param emailSend
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, EmailSend emailSend,String userId);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    EmailSend get(String id);

    /**
     * 查询一份邮件
     *
     * @param id
     * @return
     */
    EmailSend getEmail(String id);

    /**
     * 插入数据
     *
     * @param emailSend
     * @param fileInfos
     * @param scheduleTime
     * @return
     */
    ResultJson saveOrUpdate(EmailSend emailSend, List<FileInfo> fileInfos, String scheduleTime);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 查询收件人
     *
     * @return
     */
    ListJson findReceivers();

    /**
     * 获取邮件html内容
     *
     * @param id
     * @return
     */
    String getEmailContent(String id);

    /**
     * 更新
     *
     * @param emailSend
     * @return
     */
    ResultJson update(EmailSend emailSend);

    /**
     * 假删
     *
     * @param ids
     * @return
     */
    ResultJson updateDeletes(String[] ids);

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
     * 发送定时邮件
     *
     * @param scheduleTaskId
     * @param emailId
     */
    void sendScheduleEmail(JobKey scheduleTaskId, String emailId);

    /**
     * 查询定时任务的时间
     *
     * @param id
     * @return
     */
    Date getScheduleTime(String id);
}