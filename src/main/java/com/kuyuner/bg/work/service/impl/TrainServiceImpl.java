package com.kuyuner.bg.work.service.impl;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.bg.work.dao.TrainDao;
import com.kuyuner.bg.work.entity.Train;
import com.kuyuner.bg.work.service.TrainService;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 培训通知Service层实现
 *
 * @author administrator
 */
@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainDao trainDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, Train train) {
        train.setTrainType("0");
        train.setCreater(UserUtils.getPrincipal().getId());
        Page<Train> page = new Page<>(pageNum, pageSize);
        page.start();
        trainDao.findList(train);
        page.end();
        return new PageJson(page);
    }

    @Override
    public Train get(String id) {
        return trainDao.get(new Train(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson saveOrUpdate(Train train) {
        train.setId(IdGenerate.uuid());
        train.setCreater(UserUtils.getPrincipal().getId());
        train.setTrainType("1");
        train.setTrainer(UserUtils.getUser());
        trainDao.insert(train);
        sendToUsers(train);
        return ResultJson.ok();
    }

    /**
     * 将消息发送给参与人员
     *
     * @param train
     */
    private void sendToUsers(Train train) {
        String[] users = train.getJoinPeople().split(",");
        for (String user : users) {
            train.setId(IdGenerate.uuid());
            train.setCreater(user);
            train.setTrainType("0");
            trainDao.insert(train);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        trainDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public PageJson findSendPageList(String pageNum, String pageSize, Train train) {
        train.setTrainType("1");
        train.setCreater(UserUtils.getPrincipal().getId());
        Page<Train> page = new Page<>(pageNum, pageSize);
        page.start();
        trainDao.findList(train);
        page.end();
        return new PageJson(page);
    }

    @Override
    public ListJson findUserTree() {
        return new ListJson(trainDao.findUserTree());
    }

    @Override
    public List<User> findJoinPeoples(String[] joinPeoples) {
        return trainDao.findJoinPeoples(joinPeoples);
    }

}