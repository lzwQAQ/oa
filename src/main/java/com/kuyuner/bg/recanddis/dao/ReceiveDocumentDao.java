package com.kuyuner.bg.recanddis.dao;

import com.kuyuner.bg.recanddis.bean.ReceiveDocumentHistoricListView;
import com.kuyuner.bg.recanddis.bean.ReceiveDocumentPendingListView;
import com.kuyuner.bg.recanddis.entity.ReceiveDocument;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 收文Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface ReceiveDocumentDao extends CrudDao<ReceiveDocument> {
    /**
     * 查询历史消息
     *
     * @param receiveDocumentLog
     * @return
     */
    ReceiveDocument getLog(ReceiveDocument receiveDocumentLog);

    /**
     * 查询待办事项
     *
     * @param receiveDocument
     * @param userId
     * @return
     */
    List<ReceiveDocumentPendingListView> findPendingList(@Param("receiveDocument") ReceiveDocument receiveDocument, @Param("userId") String userId);

    /**
     * 查询审批历史
     *
     * @param receiveDocument
     * @param userId
     * @return
     */
    List<ReceiveDocumentHistoricListView> findHistoricList(@Param("receiveDocument") ReceiveDocument receiveDocument, @Param("userId") String userId);

    /**
     * 查询申请历史
     *
     * @param receiveDocument
     * @param userId
     * @return
     */
    List<ReceiveDocumentHistoricListView> findSendList(@Param("receiveDocument") ReceiveDocument receiveDocument, @Param("userId") String userId);

    /**
     * 获得处理意见
     *
     * @param id
     * @return
     */
    String getApprovalResult(String id);

    /**
     * 保存业务日志
     *
     * @param receiveDocument
     * @return
     */
    int insertLog(ReceiveDocument receiveDocument);

    /**
     * 查询所有的机构
     *
     * @return
     */
    List<Map<String, Object>> findAllOrgList();
}