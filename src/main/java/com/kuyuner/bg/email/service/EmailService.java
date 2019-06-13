package com.kuyuner.bg.email.service;

import com.kuyuner.bg.email.entity.Email;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * @author administrator
 */
public interface EmailService {
    /**
     * 查询收藏夹
     *
     * @param email
     * @return
     */
    ListJson findStarEmailList(Email email);

    /**
     * 查看回收站
     *
     * @param email
     * @return
     */
    ListJson findRecyclebinList(Email email);

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    ResultJson deletes(String[] ids);

    /**
     * 假删
     *
     * @param ids
     * @return
     */
    ResultJson updateDeletes(String[] ids);

    /**
     * 恢复数据
     *
     * @param ids
     * @return
     */
    ResultJson recoverys(String[] ids);
}
