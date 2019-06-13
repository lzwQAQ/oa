package com.kuyuner.workflow.api.bean;

/**
 * 工作流相关信息
 *
 * @author tangzj
 */
public class TaskInfo {
    /**
     * 当前环节名称，当遇到并行网关的任务时，随机取一个
     */
    private String taskName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isEnd() {
        return "结束".equals(taskName);
    }

}
