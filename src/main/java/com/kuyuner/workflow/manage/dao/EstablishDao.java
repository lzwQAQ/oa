package com.kuyuner.workflow.manage.dao;

import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.workflow.manage.bean.EditorSource;
import com.kuyuner.workflow.manage.bean.ModelInfo;
import com.kuyuner.workflow.manage.bean.TaskAuth;
import com.kuyuner.workflow.manage.bean.TaskForm;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 流程管理-创建流程
 *
 * @author chenxy
 */
@MyBatisDao
public interface EstablishDao {

    /**
     * 查询流程模板
     */
    List<ModelInfo> findWorkFlowModel(@Param("modelKey") String modelKey, @Param("modelName") String modelName);

    /**
     * 检查流程Key是否存在
     */
    int checkWorkFlowModelKey1(@Param("modelKey") String modelKey);

    /**
     * 检查流程Key是否存在
     */
    int checkWorkFlowModelKey2(@Param("modelId") String modelId, @Param("modelKey") String modelKey);

    /**
     * 根据模型ID，查询所有可编辑数据
     *
     * @param ids
     * @return
     */
    List<EditorSource> findModelEditorSource(@Param("ids") String[] ids);

    /**
     * 查询角色信息，查询角色的时候，不管部门状态
     *
     * @param searchText
     * @return
     */
    List<Map<String, Object>> findRole(@Param("searchText") String searchText);

    /**
     * 查询部门信息，查询部门的时候，要管机构和部门的状态
     *
     * @param searchText
     * @return
     */
    List<Map<String, Object>> findDept(@Param("searchText") String searchText);

    /**
     * 查询机构信息，查询机构的时候，要管状态
     *
     * @param searchText
     * @return
     */
    List<Map<String, Object>> findOrg(@Param("searchText") String searchText);

    /**
     * 保存任务权限
     */
    int deleteFormAuth(@Param("modelKey") String modelKey, @Param("taskKey") String taskKey);

    /**
     * 保存任务权限
     *
     * @param modelKey
     * @param taskKey
     * @return
     */
    List<Map<String, Object>> findTaskAuth(@Param("modelKey") String modelKey, @Param("taskKey") String taskKey);

    /**
     * 查询表单路径
     *
     * @param modelKey
     * @param taskKey
     * @return
     */
      Map<String, Object> findFormPath(@Param("modelKey") String modelKey, @Param("taskKey") String taskKey);

    int insertTaskAuth(TaskAuth auth);

    int insertForm(TaskForm taskForm);

    int updateForm(TaskForm taskForm);
}
