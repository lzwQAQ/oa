package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.dao.ApprovalFileDao;
import com.kuyuner.bg.approval.entity.ApprovalFile;
import com.kuyuner.bg.approval.service.ApprovalFileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批附件Service层实现
 *
 * @author administrator
 */
@Service
public class ApprovalFileServiceImpl implements ApprovalFileService {

    @Autowired
    private ApprovalFileDao fileDao;


    @Override
    public ApprovalFile get(String id) {
        return fileDao.get(new ApprovalFile(id));
    }

    @Override
    public List<ApprovalFile> findFiles(String appId) {
        ApprovalFile file = new ApprovalFile();
        file.setAppId(appId);
        return fileDao.findList(file);
    }

    @Override
    public List<ApprovalFile> findLogFiles(String appId) {
        ApprovalFile file = new ApprovalFile();
        file.setAppId(appId);
        return fileDao.findLogList(file);
    }

    @Override
    public ApprovalFile getFile(String id) {
        return fileDao.getFile(id);
    }
}