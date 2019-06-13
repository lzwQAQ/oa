package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.Accounts;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.User;

import java.util.List;

/**
 * @author admin
 */
public interface AccountsService {
    Accounts get(String id);

    /**
     * 查询收支记录
     *
     * @param pageNum
     * @param pageSize
     * @param accounts
     * @return
     */
    PageJson findRecordList(String pageNum, String pageSize, Accounts accounts);

    /**
     * 查询所有的收支记录
     *
     * @param accounts
     * @return
     */
    List<Accounts> findAllRecordList(Accounts accounts);

    /**
     * 添加账目
     *
     * @param accounts
     * @return
     */
    ResultJson save(Accounts accounts);

    /**
     * 查询所有的用户
     *
     * @return
     */
    List<User> findUsers();
}
