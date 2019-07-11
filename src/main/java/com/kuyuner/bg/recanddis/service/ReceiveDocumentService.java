package com.kuyuner.bg.recanddis.service;

import com.kuyuner.bg.recanddis.entity.ReceiveDocument;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.io.FileInfo;

import java.util.List;
import java.util.Map;

/**
 * 收文Service层接口
 *
 * @author administrator
 */
public interface ReceiveDocumentService {

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    ReceiveDocument get(String id);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 获得历史数据
     *
     * @param businessLogId
     * @return
     */
    ReceiveDocument getLog(String businessLogId);

    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @param receiveDocument
     * @return
     */
    PageJson findPendingList(String pageNum, String pageSize, ReceiveDocument receiveDocument);

    /**
     * 查询历史审批
     *
     * @param pageNum
     * @param pageSize
     * @param receiveDocument
     * @return
     */
    PageJson findHistoricList(String pageNum, String pageSize, ReceiveDocument receiveDocument);

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @param receiveDocument
     * @return
     */
    PageJson findSendList(String pageNum, String pageSize, ReceiveDocument receiveDocument);

    /**
     * 提交申请
     *
     * @param receiveDocument
     * @param fileInfo
     * @param taskResult
     * @return
     */
    ResultJson submitForm(ReceiveDocument receiveDocument, FileInfo fileInfo, String taskResult,String userId);

    /**
     * 审批操作
     *
     * @param id
     * @param approvalResult
     * @param taskResult
     * @return
     */
    ResultJson approvalForm(String id, String approvalResult, String taskResult,String userId);


    /**
     * 查询所有的机构
     *
     * @return
     */
    List<Map<String, Object>> findAllOrgList();
}