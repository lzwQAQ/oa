package com.kuyuner.core.common.dict;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DictTypeSerializer.class)
public @interface DictType {
    /**
     * 字典类型
     *
     * @return
     */
    String value();

    /**
     * 字典默认值
     *
     * @return
     */
    String defaultValue() default "";

}
