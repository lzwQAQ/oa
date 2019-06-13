package com.kuyuner.core.config.beans;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.TimeZone;

/**
 * 自定义ObjectMapper，集中处理时区问题，将null转成空字符串
 *
 * @author Administrator
 */
@Component
public class MyObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = -1L;

    public MyObjectMapper() {
        // 允许单引号、允许不带引号的字段名称
        configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //在对象转json的时候，将null变为""
        getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString("");
            }
        });
        // 进行HTML解码。
        registerModule(new SimpleModule().addSerializer(String.class, new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString(StringEscapeUtils.unescapeHtml4(value));
            }
        }));
        setTimeZone(TimeZone.getDefault());
    }

}
