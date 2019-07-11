package com.kuyuner.bg.recanddis.service.impl;

import com.kuyuner.bg.approval.dao.ApprovalFileDao;
import com.kuyuner.bg.approval.entity.ApprovalFile;
import com.kuyuner.bg.recanddis.bean.ReceiveDocumentHistoricListView;
import com.kuyuner.bg.recanddis.bean.ReceiveDocumentPendingListView;
import com.kuyuner.bg.recanddis.dao.ReceiveDocumentDao;
import com.kuyuner.bg.recanddis.entity.ReceiveDocument;
import com.kuyuner.bg.recanddis.service.ReceiveDocumentService;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.io.FileInfo;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.api.bean.TaskInfo;
import com.kuyuner.workflow.api.service.WorkFlowService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 收文Service层实现
 *
 * @author administrator
 */
@Service
public class ReceiveDocumentServiceImpl implements ReceiveDocumentService {

    @Autowired
    private ReceiveDocumentDao receiveDocumentDao;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private ApprovalFileDao fileDao;

    @Override
    public ReceiveDocument get(String id) {
        return receiveDocumentDao.get(new ReceiveDocument(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        receiveDocumentDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public ReceiveDocument getLog(String businessLogId) {
        ReceiveDocument receiveDocumentLog = new ReceiveDocument();
        receiveDocumentLog.setId(businessLogId);
        return receiveDocumentDao.getLog(receiveDocumentLog);
    }

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, ReceiveDocument receiveDocument) {
        Page<ReceiveDocumentPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        receiveDocumentDao.findPendingList(receiveDocument, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, ReceiveDocument receiveDocument) {
        Page<ReceiveDocumentHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        receiveDocumentDao.findHistoricList(receiveDocument, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, ReceiveDocument receiveDocument) {
        Page<ReceiveDocumentHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        receiveDocumentDao.findSendList(receiveDocument, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson submitForm(ReceiveDocument receiveDocument, FileInfo fileInfo, String taskResult,String userId) {
        //保存业务数据
        ApprovalFile file;
        if (StringUtils.isBlank(receiveDocument.getId())) {
            if (fileInfo == null) {
                return ResultJson.failed("未上传文件");
            }
            receiveDocument.setId(IdGenerate.uuid());
            receiveDocument.setSenderId(UserUtils.getPrincipal().getId());
            receiveDocument.setCreater(UserUtils.getPrincipal().getId());
            receiveDocumentDao.insert(receiveDocument);
        } else {
            receiveDocumentDao.update(receiveDocument);
            receiveDocument = receiveDocumentDao.get(new ReceiveDocument(receiveDocument.getId()));
            if (fileInfo != null) {
                fileDao.deleteByAppId(receiveDocument.getId());
            }
        }
        if (fileInfo != null) {
            file = new ApprovalFile();
            file.setFileName(fileInfo.getName());
            file.setFilePath(fileInfo.getUrl());
            file.setFileSize(fileInfo.getSize());
            file.setAppId(receiveDocument.getId());
            file.setId(IdGenerate.uuid());
            fileDao.insert(file);
        }

        file = fileDao.getFileByAppId(receiveDocument.getId());
        _submitForm(receiveDocument, file, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson approvalForm(String id, String approvalResult, String taskResult,String userId) {
        ReceiveDocument receiveDocument = new ReceiveDocument();
        receiveDocument.setId(id);
        String approvalResultContent = receiveDocumentDao.getApprovalResult(receiveDocument.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        receiveDocument.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, UserUtils.getPrincipal().getName(), approvalResult, taskBean.getSequenceFlowName()));
        receiveDocumentDao.update(receiveDocument);

        receiveDocument = receiveDocumentDao.get(new ReceiveDocument(receiveDocument.getId()));
        ApprovalFile file = fileDao.getFileByAppId(receiveDocument.getId());
        _submitForm(receiveDocument, file, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    public List<Map<String, Object>> findAllOrgList() {
        return receiveDocumentDao.findAllOrgList();
    }

    /**
     * 提交任务
     *
     * @param receiveDocument
     * @param file
     * @param taskResult
     */
    private void _submitForm(ReceiveDocument receiveDocument, ApprovalFile file, String taskResult,String userId) {
        //记录日志
        ReceiveDocument receiveDocumentLog = new ReceiveDocument();
        BeanUtils.copyProperties(receiveDocument, receiveDocumentLog);
        receiveDocumentLog.setId(IdGenerate.uuid());
        receiveDocumentDao.insertLog(receiveDocumentLog);
        ApprovalFile fileLog = new ApprovalFile();
        BeanUtils.copyProperties(file, fileLog);
        fileLog.setId(IdGenerate.uuid());
        fileLog.setAppId(receiveDocumentLog.getId());
        fileDao.insertLog(fileLog);

        //提交流程
        BusinessKey businessKey = new BusinessKey();
        businessKey.setId(receiveDocument.getId());
        businessKey.setLogId(receiveDocumentLog.getId());
        TaskInfo taskInfo = workFlowService.submitTask(taskResult, businessKey,userId);

        //更新业务表的当前环节字段
        ReceiveDocument receiveDocument2 = new ReceiveDocument();
        receiveDocument2.setId(receiveDocument.getId());
        receiveDocument2.setTaskName(taskInfo.getTaskName());
        receiveDocumentDao.update(receiveDocument2);
    }

}