package com.kuyuner.bg.msg.dao;

import com.kuyuner.bg.msg.entity.MessageSend;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;



/**
 * 收文Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface MessageSendDao extends CrudDao<MessageSend> {
    void updateBySeqId(MessageSend messageSend);

    void updateByMsgId(MessageSend messageSend);

    void saveOrUpdateReportByDuplicateKey(MessageSend messageSend);

    void deleteByMsgId(MessageSend messageSend);

    void updateSeqIdByMsgIdAndReceiver(MessageSend messageSend);
}