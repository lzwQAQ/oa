package com.kuyuner.bg.recanddis.dao;

import com.kuyuner.bg.recanddis.bean.ReleaseDocumentHistoricListView;
import com.kuyuner.bg.recanddis.bean.ReleaseDocumentPendingListView;
import com.kuyuner.bg.recanddis.entity.ReleaseDocument;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.core.sys.entity.User;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 发文Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface ReleaseDocumentDao extends CrudDao<ReleaseDocument> {
    /**
     * 查询历史消息
     *
     * @param releaseDocumentLog
     * @return
     */
    ReleaseDocument getLog(ReleaseDocument releaseDocumentLog);

    /**
     * 查询待办事项
     *
     * @param releaseDocument
     * @param userId
     * @return
     */
    List<ReleaseDocumentPendingListView> findPendingList(@Param("releaseDocument") ReleaseDocument releaseDocument, @Param("userId") String userId);

    /**
     * 查询审批历史
     *
     * @param releaseDocument
     * @param userId
     * @return
     */
    List<ReleaseDocumentHistoricListView> findHistoricList(@Param("releaseDocument") ReleaseDocument releaseDocument, @Param("userId") String userId);

    /**
     * 查询申请历史
     *
     * @param releaseDocument
     * @param userId
     * @return
     */
    List<ReleaseDocumentHistoricListView> findSendList(@Param("releaseDocument") ReleaseDocument releaseDocument, @Param("userId") String userId);

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
     * @param releaseDocument
     * @return
     */
    int insertLog(ReleaseDocument releaseDocument);

    /**
     * 查询所有用户
     *
     * @return
     */
    List<User> findUserList();
}