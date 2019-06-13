package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.bean.CarApplyHistoricListView;
import com.kuyuner.bg.approval.bean.CarApplyPendingListView;
import com.kuyuner.bg.approval.entity.Car;
import com.kuyuner.bg.approval.entity.CarApply;
import com.kuyuner.bg.approval.entity.Driver;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆申请Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface CarApplyDao extends CrudDao<CarApply> {

    /**
     * 查询历史消息
     *
     * @param carApplyLog
     * @return
     */
    CarApply getLog(CarApply carApplyLog);

    /**
     * 查询待办事项
     *
     * @param carApply
     * @param userId
     * @return
     */
    List<CarApplyPendingListView> findPendingList(@Param("carApply") CarApply carApply, @Param("userId") String userId);

    /**
     * 查询审批历史
     *
     * @param carApply
     * @param userId
     * @return
     */
    List<CarApplyHistoricListView> findHistoricList(@Param("carApply") CarApply carApply, @Param("userId") String userId);

    /**
     * 查询申请历史
     *
     * @param carApply
     * @param userId
     * @return
     */
    List<CarApplyPendingListView> findSendList(@Param("carApply") CarApply carApply, @Param("userId") String userId);

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
     * @param carApplyLog
     * @return
     */
    int insertLog(CarApply carApplyLog);

    /**
     * 查询驾驶员
     *
     * @return
     */
    List<Driver> findDrivers();

    /**
     * 查询车辆
     *
     * @return
     */
    List<Car> findCars();
}