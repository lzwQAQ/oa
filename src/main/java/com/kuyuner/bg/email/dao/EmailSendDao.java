package com.kuyuner.bg.email.dao;

import com.kuyuner.bg.email.entity.EmailFile;
import com.kuyuner.bg.email.entity.EmailReceive;
import com.kuyuner.bg.email.entity.EmailSend;
import com.kuyuner.common.io.FileInfo;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.core.sys.entity.User;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 发件箱Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface EmailSendDao extends CrudDao<EmailSend> {

    List<Map<String, Object>> findReceivers();

    /**
     * 根据账号查询用户
     *
     * @param list
     * @return
     */
    List<User> findUsersByAccounts(@Param("list") List<String> list);

    /**
     * 创建接收人的收件
     *
     * @param emails
     * @return
     */
    int saveReceiverEmail(@Param("emails") List<EmailReceive> emails);

    /**
     * 获取邮件html内容
     *
     * @param id
     * @return
     */
    String getEmailContent(@Param("id") String id);

    /**
     * 删除之前已经存在的文件
     *
     * @param emailId
     * @return
     */
    int deleteFiles(@Param("emailId") String emailId);

    /**
     * 保存附件
     *
     * @param emailSend
     * @param fileInfos
     * @return
     */
    int saveSendEmailFiles(@Param("email") EmailSend emailSend, @Param("files") List<FileInfo> fileInfos);

    /**
     * 保存附件
     *
     * @param emailFiles
     * @return
     */
    int saveReceiveEmailFiles(@Param("files") List<EmailFile> emailFiles);

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    int deletes(@Param("ids") String[] ids);

    /**
     * 假删
     *
     * @param ids
     * @return
     */
    int updateDeletes(@Param("ids") String[] ids);

    /**
     * 恢复
     *
     * @param ids
     * @return
     */
    int recoverys(@Param("ids") String[] ids);

    /**
     * 查询一封邮件
     *
     * @param id
     * @return
     */
    EmailSend getEmail(@Param("id") String id);

    /**
     * 查询已经选择的收件人等信息
     *
     * @param emailId
     * @return
     */
    EmailSend findSelectedUsers(@Param("emailId") String emailId);

    /**
     * 查询定时任务的时间
     *
     * @param emailId
     * @return
     */
    String getScheduleTime(@Param("emailId") String emailId);

    /**
     * 删除定时任务
     *
     * @param emailIds
     * @return
     */
    int deleteSchedules(@Param("emailIds") String[] emailIds);

    /**
     * 查询任务ID
     *
     * @param ids
     * @return
     */
    String[] findSchedules(@Param("emailIds") String[] ids);
}