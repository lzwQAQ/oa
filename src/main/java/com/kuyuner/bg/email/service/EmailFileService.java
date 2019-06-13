package com.kuyuner.bg.email.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.email.entity.EmailFile;

/**
 * 邮箱复件Service层接口
 *
 * @author administrator
 */
public interface EmailFileService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param emailFile
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, EmailFile emailFile);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    EmailFile get(String id);

    /**
     * 插入数据
     *
     * @param emailFile
     * @return
     */
    ResultJson saveOrUpdate(EmailFile emailFile);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 查询邮件的附件
     *
     * @param emailId
     * @return
     */
    ListJson findEmailFiles(String emailId);
}