package com.kuyuner.bg.recanddis.service.impl;

import com.kuyuner.bg.approval.dao.ApprovalFileDao;
import com.kuyuner.bg.approval.entity.ApprovalFile;
import com.kuyuner.bg.recanddis.bean.ReleaseDocumentHistoricListView;
import com.kuyuner.bg.recanddis.bean.ReleaseDocumentPendingListView;
import com.kuyuner.bg.recanddis.dao.ReceiveDocumentDao2;
import com.kuyuner.bg.recanddis.dao.ReleaseDocumentDao;
import com.kuyuner.bg.recanddis.entity.ReceiveDocument2;
import com.kuyuner.bg.recanddis.entity.ReleaseDocument;
import com.kuyuner.bg.recanddis.service.ReleaseDocumentService;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.io.FileInfo;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.dao.UserDao;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.api.bean.TaskInfo;
import com.kuyuner.workflow.api.service.WorkFlowService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 * 发文Service层实现
 *
 * @author administrator
 */
@Service
public class ReleaseDocumentServiceImpl implements ReleaseDocumentService {

    @Autowired
    private ReleaseDocumentDao releaseDocumentDao;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private ApprovalFileDao fileDao;

    @Autowired
    private ReceiveDocumentDao2 receiveDocumentDao2;

    @Resource
    private UserDao userDao;

    @Override
    public ReleaseDocument get(String id) {
        return releaseDocumentDao.get(new ReleaseDocument(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        releaseDocumentDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public ReleaseDocument getLog(String businessLogId) {
        ReleaseDocument releaseDocumentLog = new ReleaseDocument();
        releaseDocumentLog.setId(businessLogId);
        return releaseDocumentDao.getLog(releaseDocumentLog);
    }

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, ReleaseDocument releaseDocument) {
        Page<ReleaseDocumentPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        releaseDocumentDao.findPendingList(releaseDocument, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, ReleaseDocument releaseDocument) {
        Page<ReleaseDocumentHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        releaseDocumentDao.findHistoricList(releaseDocument, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, ReleaseDocument releaseDocument) {
        Page<ReleaseDocumentHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        releaseDocumentDao.findSendList(releaseDocument, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson submitForm(ReleaseDocument releaseDocument, FileInfo fileInfo, String taskResult,String userId) {
        //保存业务数据
        ApprovalFile file;
        if (StringUtils.isBlank(releaseDocument.getId())) {
            if (fileInfo == null) {
                return ResultJson.failed("未上传文件");
            }
            releaseDocument.setId(IdGenerate.uuid());
            releaseDocument.setSenderId(UserUtils.getPrincipal().getId());
            releaseDocument.setCreater(UserUtils.getPrincipal().getId());
            releaseDocumentDao.insert(releaseDocument);
        } else {
            releaseDocumentDao.update(releaseDocument);
            releaseDocument = releaseDocumentDao.get(new ReleaseDocument(releaseDocument.getId()));
            if (fileInfo != null) {
                fileDao.deleteByAppId(releaseDocument.getId());
            }
        }
        if (fileInfo != null) {
            file = new ApprovalFile();
            file.setFileName(fileInfo.getName());
            file.setFilePath(fileInfo.getUrl());
            file.setFileSize(fileInfo.getSize());
            file.setAppId(releaseDocument.getId());
            file.setId(IdGenerate.uuid());
            fileDao.insert(file);
        }

        file = fileDao.getFileByAppId(releaseDocument.getId());
        _submitForm(releaseDocument, file, taskResult, null,userId);

        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson approvalForm(String id, String approvalResult, String taskResult, String taskName, String sendTo,String userId) {
        ReleaseDocument releaseDocument = new ReleaseDocument();
        releaseDocument.setId(id);
        if ("核稿".equals(taskName)) {
            releaseDocument.setExamineAuthor(UserUtils.getPrincipal().getId());
        }
        String approvalResultContent = releaseDocumentDao.getApprovalResult(releaseDocument.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        releaseDocument.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, UserUtils.getPrincipal().getName(), approvalResult, taskBean.getSequenceFlowName()));
        releaseDocumentDao.update(releaseDocument);

        releaseDocument = releaseDocumentDao.get(new ReleaseDocument(releaseDocument.getId()));
        ApprovalFile file = fileDao.getFileByAppId(releaseDocument.getId());
        _submitForm(releaseDocument, file, taskResult, sendTo,userId);

        return ResultJson.ok();
    }

    @Override
    public List<User> findUserList() {
        return releaseDocumentDao.findUserList();
    }

    /**
     * 提交任务
     *
     * @param releaseDocument
     * @param file
     * @param taskResult
     */
    private void _submitForm(ReleaseDocument releaseDocument, ApprovalFile file, String taskResult, String sendTo,String userId) {
        //记录日志
        ReleaseDocument releaseDocumentLog = new ReleaseDocument();
        BeanUtils.copyProperties(releaseDocument, releaseDocumentLog);
        releaseDocumentLog.setId(IdGenerate.uuid());
        releaseDocumentDao.insertLog(releaseDocumentLog);
        ApprovalFile fileLog = new ApprovalFile();
        BeanUtils.copyProperties(file, fileLog);
        fileLog.setId(IdGenerate.uuid());
        fileLog.setAppId(releaseDocumentLog.getId());
        fileDao.insertLog(fileLog);

        //提交流程
        BusinessKey businessKey = new BusinessKey();
        businessKey.setId(releaseDocument.getId());
        businessKey.setLogId(releaseDocumentLog.getId());
        TaskInfo taskInfo = workFlowService.submitTask(taskResult, businessKey,userId);

        //更新业务表的当前环节字段
        ReleaseDocument releaseDocument2 = new ReleaseDocument();
        releaseDocument2.setId(releaseDocument.getId());
        releaseDocument2.setTaskName(taskInfo.getTaskName());
        releaseDocumentDao.update(releaseDocument2);

        if ("结束".equals(taskInfo.getTaskName())) {
            releaseDocument2 = releaseDocumentDao.get(new ReleaseDocument(releaseDocument.getId()));
            String[] sendTos = sendTo.split(",");
            for (String to : sendTos) {
                ReceiveDocument2 receiveDocument2 = new ReceiveDocument2();
                receiveDocument2.setStar("0");
                receiveDocument2.setReceiveDate(new Date());
                receiveDocument2.setUrgentLevel(releaseDocument2.getUrgentLevel());
                receiveDocument2.setNumbers(releaseDocument2.getNumbers());
                receiveDocument2.setReleaseOrg(userDao.get(new User(releaseDocument2.getCreater())).getDept().getOrg().getId());
                receiveDocument2.setSecret(releaseDocument2.getSecret());
                receiveDocument2.setTitle(releaseDocument2.getTitle());
                receiveDocument2.setId(IdGenerate.uuid());
                receiveDocument2.setCreater(to);
                receiveDocumentDao2.insert(receiveDocument2);
                ApprovalFile approvalFile = fileDao.getFileByAppId(releaseDocument.getId());
                approvalFile.setId(IdGenerate.uuid());
                approvalFile.setAppId(receiveDocument2.getId());
                fileDao.insert(approvalFile);
            }
        }
    }

}