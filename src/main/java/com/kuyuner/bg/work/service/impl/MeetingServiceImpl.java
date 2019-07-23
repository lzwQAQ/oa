package com.kuyuner.bg.work.service.impl;

import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.bg.work.dao.MeetingDao;
import com.kuyuner.bg.work.entity.Meeting;
import com.kuyuner.bg.work.service.MeetingService;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 会议通知Service层实现
 *
 * @author administrator
 */
@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private MeetingDao meetingDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, Meeting meeting,String userId) {
        meeting.setMeetingType("0");
        meeting.setCreater(StringUtils.isBlank(userId)?UserUtils.getPrincipal().getId():userId);
        Page<Meeting> page = new Page<>(pageNum, pageSize);
        page.start();
        List<Meeting> meetings = meetingDao.findList(meeting);
        if(StringUtils.isNotBlank(userId)){
            meetings.forEach(m->{
                m.setSender(UserUtils.getUserFromDB(userId).getName());
            });
        }
        page.end();
        return new PageJson(page);
    }

    @Override
    public Meeting get(String id) {
        return meetingDao.get(new Meeting(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson saveOrUpdate(Meeting meeting,String userId) {
        meeting.setId(IdGenerate.uuid());
        meeting.setCreater(StringUtils.isBlank(userId)?UserUtils.getPrincipal().getId():UserUtils.getUserFromDB(userId).getId());
        meeting.setMeetingType("1");
        meeting.setTrainer(StringUtils.isBlank(userId)?UserUtils.getUser():UserUtils.getUserFromDB(userId));
        meetingDao.insert(meeting);
        sendToUsers(meeting);
        return ResultJson.ok();
    }

    private void sendToUsers(Meeting meeting) {
        String[] users = meeting.getJoinPeople().split(",");
        for (String user : users) {
            meeting.setId(IdGenerate.uuid());
            meeting.setCreater(user);
            meeting.setMeetingType("0");
            meetingDao.insert(meeting);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        meetingDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public List<User> findJoinPeoples(String[] joinPeoples) {
        return meetingDao.findJoinPeoples(joinPeoples);
    }

    @Override
    public PageJson findSendPageList(String pageNum, String pageSize, Meeting meeting,String userId) {
        meeting.setMeetingType("1");
        meeting.setCreater(StringUtils.isBlank(userId)?UserUtils.getPrincipal().getId():userId);
        Page<Meeting> page = new Page<>(pageNum, pageSize);
        page.start();
        List<Meeting> meetings = meetingDao.findList(meeting);
        if(StringUtils.isNotBlank(userId)){
            String creatorName = UserUtils.getUserFromDB(userId).getName();
            meetings.forEach(m->{
                m.setSender(creatorName);
            });
        }
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson appList(String pageNum, String pageSize, Meeting meeting, String userId) {
        meeting.setCreater(StringUtils.isBlank(userId)?UserUtils.getPrincipal().getId():userId);
        Page<Meeting> page = new Page<>(pageNum, pageSize);
        page.start();
        List<Meeting> meetings = meetingDao.findList(meeting);
        if(StringUtils.isNotBlank(userId)){
            meetings.forEach(m->{
                m.setSender(UserUtils.getUserFromDB(userId).getName());
            });
        }
        page.end();
        return new PageJson(page);
    }

}