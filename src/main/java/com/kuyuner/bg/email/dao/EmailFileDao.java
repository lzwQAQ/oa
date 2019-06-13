package com.kuyuner.bg.email.dao;

import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.bg.email.entity.EmailFile;

import org.apache.ibatis.annotations.Param;

/**
 * 邮箱复件Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface EmailFileDao extends CrudDao<EmailFile> {

    /**
     * 根据邮件删除对应附件
     *
     * @param emailIds
     * @return
     */
    int deleteFiles(@Param("emailIds") String[] emailIds);
}