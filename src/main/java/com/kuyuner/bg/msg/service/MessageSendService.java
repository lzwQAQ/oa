package com.kuyuner.bg.msg.service;


import com.kuyuner.bg.msg.entity.MessageSend;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

public interface MessageSendService {

	ResultJson save(MessageSend messageSend);

	PageJson findList(String pageNum, String pageSize,  MessageSend message);

	void updateBySeqId(MessageSend messageSend);
}
