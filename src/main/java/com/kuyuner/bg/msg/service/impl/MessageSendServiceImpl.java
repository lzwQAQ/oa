package com.kuyuner.bg.msg.service.impl;

import com.kuyuner.bg.article.entity.Article;
import com.kuyuner.bg.msg.dao.MessageSendDao;
import com.kuyuner.bg.msg.entity.Message;
import com.kuyuner.bg.msg.entity.MessageSend;
import com.kuyuner.bg.msg.service.MessageSendService;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.persistence.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageSendServiceImpl implements MessageSendService {

	@Autowired
	private MessageSendDao messageSendDao;
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Transactional
	@Override
	public ResultJson save(MessageSend messageSend) {
		messageSend.setId(IdGenerate.uuid());
		messageSendDao.insert(messageSend);
		return ResultJson.ok();
	}

	@Transactional
	@Override
	public void updateBySeqId(MessageSend messageSend) {
		messageSendDao.updateBySeqId(messageSend);
	}

	@Override
	public PageJson findList(String pageNum, String pageSize, MessageSend messageSend) {
		Page<Article> page = new Page<>(pageNum, pageSize);
		page.start();
		messageSendDao.findList(messageSend);
		page.end();
		return new PageJson(page);
	}
}
