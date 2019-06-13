package com.kuyuner.common.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Validator;

/**
 * 控制器支持类
 * @author Administrator
 */
public abstract class BaseController {

    /**
     * 验证Bean实例对象
     */
    @Autowired
    private Validator validator;

}
