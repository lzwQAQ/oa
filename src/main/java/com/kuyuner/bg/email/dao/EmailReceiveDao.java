package com.kuyuner.bg.email.dao;

import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.bg.email.entity.EmailReceive;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 收件箱Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface EmailReceiveDao extends CrudDao<EmailReceive> {

    /**
     * 获取邮件的html内容
     *
     * @param id
     * @return
     */
    String getEmailContent(@Param("id") String id);

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
    EmailReceive getEmail(@Param("id") String id);

    /**
     * 查询已经选择的收件人等信息
     *
     * @param emailId
     * @return
     */
    EmailReceive findSelectedUsers(@Param("emailId") String emailId);

    /**
     * 获得最新的未读邮件
     *
     * @param userId
     * @return
     */
    Map<String, Object> getLatestEmail(@Param("userId") String userId);
}