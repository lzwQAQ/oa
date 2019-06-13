package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.LeaveRecord;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.User;

import java.util.List;

/**
 * 请假Service层接口
 *
 * @author administrator
 */
public interface LeaveRecordService {

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    LeaveRecord get(String id);

    /**
     * 查询员工请假记录
     *
     * @param pageNum
     * @param pageSize
     * @param leaveRecord
     * @return
     */
    PageJson findRecordList(String pageNum, String pageSize, LeaveRecord leaveRecord);

    /**
     * 条件查询所有的记录
     *
     * @param leaveRecord
     * @return
     */
    List<LeaveRecord> findAllRecordList(LeaveRecord leaveRecord);

    /**
     * 查询所有的用户
     *
     * @return
     */
    List<User> findUsers();

    /**
     * 保存数据
     *
     * @param leaveRecord
     * @return
     */
    ResultJson save(LeaveRecord leaveRecord);
}