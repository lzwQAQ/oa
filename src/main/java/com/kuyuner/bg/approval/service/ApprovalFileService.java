package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.ApprovalFile;

import java.util.List;

/**
 * 邮箱复件Service层接口
 *
 * @author administrator
 */
public interface ApprovalFileService {

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    ApprovalFile get(String id);

    /**
     * 查询审批的附件
     *
     * @param appId
     * @return
     */
    List<ApprovalFile> findFiles(String appId);

    /**
     * 查询历史审批的附件
     *
     * @param appId
     * @return
     */
    List<ApprovalFile> findLogFiles(String appId);

    /**
     * 获得文件
     *
     * @param id
     * @return
     */
    ApprovalFile getFile(String id);
}