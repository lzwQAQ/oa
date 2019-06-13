package com.kuyuner.workflow.engine.service;

import com.kuyuner.common.idgen.IdGenerate;

import org.activiti.engine.impl.cfg.IdGenerator;

/**
 * Activiti自定义ID生成器
 * @author chenxy
 *
 */
public class UUIDGenerator implements IdGenerator {

	@Override
	public String getNextId() {
		return IdGenerate.uuid();
	}

}
