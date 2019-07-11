package com.kuyuner.bg.recanddis.service;

import com.kuyuner.bg.recanddis.entity.ReleaseDocument;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.io.FileInfo;
import com.kuyuner.core.sys.entity.User;

import java.util.List;

/**
 * 发文Service层接口
 *
 * @author administrator
 */
public interface ReleaseDocumentService {

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    ReleaseDocument get(String id);

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
    ReleaseDocument getLog(String businessLogId);

    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @param releaseDocument
     * @return
     */
    PageJson findPendingList(String pageNum, String pageSize, ReleaseDocument releaseDocument);

    /**
     * 查询历史审批
     *
     * @param pageNum
     * @param pageSize
     * @param releaseDocument
     * @return
     */
    PageJson findHistoricList(String pageNum, String pageSize, ReleaseDocument releaseDocument);

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @param releaseDocument
     * @return
     */
    PageJson findSendList(String pageNum, String pageSize, ReleaseDocument releaseDocument);

    /**
     * 提交申请
     *
     * @param releaseDocument
     * @param fileInfo
     * @param taskResult
     * @return
     */
    ResultJson submitForm(ReleaseDocument releaseDocument, FileInfo fileInfo, String taskResult,String userId);

    /**
     * 审批操作
     *
     * @param id
     * @param approvalResult
     * @param taskResult
     * @param taskName
     * @return
     */
    ResultJson approvalForm(String id, String approvalResult, String taskResult, String taskName, String sendTo,String userId);


    /**
     * 查询需要发送的用户
     *
     * @return
     */
    List<User> findUserList();
}