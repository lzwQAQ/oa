package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.dao.LeaveRecordDao;
import com.kuyuner.bg.approval.entity.Leave;
import com.kuyuner.bg.approval.entity.LeaveRecord;
import com.kuyuner.bg.approval.service.LeaveRecordService;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.dao.UserDao;
import com.kuyuner.core.sys.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 * 请假Service层实现
 *
 * @author administrator
 */
@Service
public class LeaveRecordServiceImpl implements LeaveRecordService {

    @Autowired
    private LeaveRecordDao leaveRecordDao;

    @Resource
    private UserDao userDao;

    @Override
    public LeaveRecord get(String id) {
        return leaveRecordDao.get(new LeaveRecord(id));
    }

    @Override
    public PageJson findRecordList(String pageNum, String pageSize, LeaveRecord leaveRecord) {
        Page<Leave> page = new Page<>(pageNum, pageSize);
        page.start();
        leaveRecordDao.findList(leaveRecord);
        page.end();
        return new PageJson(page);
    }

    @Override
    public List<LeaveRecord> findAllRecordList(LeaveRecord leaveRecord) {
        return leaveRecordDao.findList(leaveRecord);
    }

    @Override
    public List<User> findUsers() {
        return userDao.findList(new User());
    }

    @Override
    public ResultJson save(LeaveRecord leaveRecord) {
        leaveRecord.setId(IdGenerate.uuid());
        leaveRecordDao.insert(leaveRecord);
        return ResultJson.ok();
    }
}