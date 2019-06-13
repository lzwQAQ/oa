package com.kuyuner.bg.work.service;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.work.entity.Meeting;
import com.kuyuner.core.sys.entity.User;

import java.util.List;

/**
 * 会议通知Service层接口
 *
 * @author administrator
 */
public interface MeetingService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param meeting
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, Meeting meeting);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Meeting get(String id);

    /**
     * 插入数据
     *
     * @param meeting
     * @return
     */
    ResultJson saveOrUpdate(Meeting meeting);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 查询所有参与人员
     *
     * @param joinPeoples
     * @return
     */
    List<User> findJoinPeoples(String[] joinPeoples);

    /**
     * 发起培训列表
     *
     * @param pageNum
     * @param pageSize
     * @param meeting
     * @return
     */
    PageJson findSendPageList(String pageNum, String pageSize, Meeting meeting);
}