package com.kuyuner.bg.email.dao;

import com.kuyuner.bg.email.bean.EmailView;
import com.kuyuner.bg.email.entity.Email;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * @author administrator
 */
@MyBatisDao
public interface EmailDao {
    /**
     * 查询收藏的邮件
     *
     * @param email
     * @return
     */
    List<EmailView> findStarEmailList(Email email);

    /**
     * 查看回收站
     *
     * @param email
     * @return
     */
    List<EmailView> findRecyclebinList(Email email);

}
