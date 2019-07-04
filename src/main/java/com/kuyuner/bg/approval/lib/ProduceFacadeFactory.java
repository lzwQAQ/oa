package com.kuyuner.bg.approval.lib;

import com.kuyuner.bg.approval.service.ProduceFaced;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProduceFacadeFactory implements InitializingBean {

    @Autowired
    private List<ProduceFaced> produceFacedList;

    public ProduceFaced getBizFacad(String handlerCode) {
        if (!innerIdBizFacadeMap.containsKey(handlerCode)) {
            throw new RuntimeException(String.format("[初始化系统]找不到ProduceFacade实现:%s", handlerCode));
        }
        return innerIdBizFacadeMap.get(handlerCode);
    }

    private Map<String, ProduceFaced> innerIdBizFacadeMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        produceFacedList.forEach(r -> innerIdBizFacadeMap.put(r.getCode(), r));
    }
}
