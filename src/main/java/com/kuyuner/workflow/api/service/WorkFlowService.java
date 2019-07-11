package com.kuyuner.workflow.api.service;

import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * 工作流对外服务接口
 *
 * @author tangzj
 */
public interface WorkFlowService {

    static final String SEQUENCE_NAME = "goto";
    static final String COUNTERSIGN_USER = "countersignUser";
    static final String COUNTERSIGN_USERS = "countersignUsers";
    static final String CANDIDATE_USERS = "candidateUsers";
    static final String NEXT_STEP = "提交下一步";
    static final String END_STEP = "结束";

    /**
     * 提交任务，用于开启流程实例或完成任务
     *
     * @param httpRequest http请求,用于获得处理人的信息
     * @param task        任务处理信息，来自于前端传递过来的task信息
     * @param businessKey 业务key
     * @return
     */
    TaskInfo submitTask(String task, BusinessKey businessKey,String userId);

    /**
     * 提交任务，用于开启流程实例或完成任务
     *
     * @param httpRequest http请求,用于获得处理人的信息
     * @param task        任务处理信息，来自于前端传递过来的task信息
     * @param businessKey 业务key
     * @param useLastCandidates 是否使用上一次的环节处理人
     * @return
     */
    TaskInfo submitTask(String task, BusinessKey businessKey, boolean useLastCandidates,String userId);

}
