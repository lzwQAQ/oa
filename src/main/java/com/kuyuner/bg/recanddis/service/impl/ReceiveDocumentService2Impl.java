package com.kuyuner.bg.recanddis.service.impl;

import com.kuyuner.bg.approval.dao.ApprovalFileDao;
import com.kuyuner.bg.approval.entity.ApprovalFile;
import com.kuyuner.bg.recanddis.dao.ReceiveDocumentDao2;
import com.kuyuner.bg.recanddis.entity.ReceiveDocument2;
import com.kuyuner.bg.recanddis.service.ReceiveDocumentService2;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 */
@Service
public class ReceiveDocumentService2Impl implements ReceiveDocumentService2 {

    @Autowired
    private ReceiveDocumentDao2 receiveDocumentDao2;

    @Autowired
    private ApprovalFileDao approvalFileDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, ReceiveDocument2 receiveDocument) {
        Page<ReceiveDocument2> page = new Page<>(pageNum, pageSize);
        page.start();
        receiveDocument.setCreater(UserUtils.getUser().getId());
        receiveDocumentDao2.findList(receiveDocument);
        page.end();
        return new PageJson(page);
    }

    @Override
    public int update(ReceiveDocument2 receiveDocument2) {
        return receiveDocumentDao2.update(receiveDocument2);
    }

    @Override
    public ReceiveDocument2 get(String id) {
        return receiveDocumentDao2.get(new ReceiveDocument2(id));
    }

    @Override
    public ApprovalFile getApprovalFile(String id) {
        return approvalFileDao.getFileByAppId(id);
    }
}
