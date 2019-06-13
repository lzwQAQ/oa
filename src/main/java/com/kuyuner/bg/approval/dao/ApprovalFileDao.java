package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.entity.ApprovalFile;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 邮箱复件Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface ApprovalFileDao extends CrudDao<ApprovalFile> {
    /**
     * 根据审批删除附件
     *
     * @param id
     * @return
     */
    int deleteByAppId(@Param("appId") String id);

    /**
     * 记录日志
     *
     * @param fileLog
     * @return
     */
    int insertLog(ApprovalFile fileLog);

    /**
     * 查询历史的审批文件
     *
     * @param file
     * @return
     */
    List<ApprovalFile> findLogList(ApprovalFile file);

    /**
     * 获得到查询文件
     *
     * @param id
     * @return
     */
    ApprovalFile getFile(@Param("id") String id);

    /**
     * 根据审批ID获得文件
     *
     * @param appId
     * @return
     */
    ApprovalFile getFileByAppId(@Param("appId") String appId);
}