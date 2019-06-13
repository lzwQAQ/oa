package com.kuyuner.bg.recanddis.service;

import com.kuyuner.bg.approval.entity.ApprovalFile;
import com.kuyuner.bg.recanddis.entity.ReceiveDocument2;
import com.kuyuner.common.controller.PageJson;

/**
 * @author admin
 */
public interface ReceiveDocumentService2 {
    /**
     * 查询分页数据
     *
     * @param pageNum
     * @param pageSize
     * @param receiveDocument
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, ReceiveDocument2 receiveDocument);

    /**
     * 更新数据
     *
     * @param receiveDocument2
     * @return
     */
    int update(ReceiveDocument2 receiveDocument2);

    /**
     * 获得单个数据
     *
     * @param id
     * @return
     */
    ReceiveDocument2 get(String id);

    /**
     * 获得收文文件
     *
     * @param id
     * @return
     */
    ApprovalFile getApprovalFile(String id);
}
