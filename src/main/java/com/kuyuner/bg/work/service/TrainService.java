package com.kuyuner.bg.work.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.work.entity.Train;
import com.kuyuner.core.sys.entity.User;

import java.util.List;

/**
 * 培训通知Service层接口
 *
 * @author administrator
 */
public interface TrainService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param train
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, Train train);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Train get(String id);

    /**
     * 插入数据
     *
     * @param train
     * @return
     */
    ResultJson saveOrUpdate(Train train);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 发起培训列表
     *
     * @param pageNum
     * @param pageSize
     * @param train
     * @return
     */
    PageJson findSendPageList(String pageNum, String pageSize, Train train);

    /**
     * 查询用户部门树
     *
     * @return
     */
    ListJson findUserTree();

    /**
     * 查询参与人员
     *
     * @param joinPeoples
     * @return
     */
    List<User> findJoinPeoples(String[] joinPeoples);
}