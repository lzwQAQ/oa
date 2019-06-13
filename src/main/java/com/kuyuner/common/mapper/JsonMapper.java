package com.kuyuner.common.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.util.JSONPObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.TimeZone;

/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper.
 * @author administrator
 */
public class JsonMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

    private static final Object OBJECT = new Object();

    private static JsonMapper mapper;

    private JsonMapper() {
        // 允许单引号、允许不带引号的字段名称
        configure(Feature.ALLOW_SINGLE_QUOTES, true);
        configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 空值处理为空串
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen,
                                  SerializerProvider provider) throws IOException {
                jgen.writeString("");
            }
        });
        // 进行HTML解码。(当作例子来看，不要删除)
        //        this.registerModule(new SimpleModule().addSerializer(String.class, new JsonSerializer<String>() {
        //            @Override
        //            public void serialize(String value, JsonGenerator jgen,
        //                                  SerializerProvider provider) throws IOException,
        //                    JsonProcessingException {
        //                jgen.writeString(StringEscapeUtils.unescapeHtml4(value));
        //            }
        //        }));
        // 设置时区
        this.setTimeZone(TimeZone.getDefault());//getTimeZone("GMT+8:00")
    }

    /**
     * 单例模式
     */
    public static JsonMapper getInstance() {
        if (mapper == null) {
            synchronized (OBJECT) {
                if (mapper == null) {
                    mapper = new JsonMapper();
                }
            }
        }
        return mapper;
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    private String toJson(Object object) {
        if (object == null) {
            throw new RuntimeException("object can not be null");
        }
        try {
            return this.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * <p>
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * <p>
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            throw new RuntimeException("json string can not be null");
        }
        try {
            return this.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 对象转换为JSON字符串
     *
     * @param object
     * @return
     */
    public static String toJsonString(Object object) {
        return JsonMapper.getInstance().toJson(object);
    }

    /**
     * JSON字符串转换为对象
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T fromJsonString(String jsonString, Class<T> clazz) {
        return JsonMapper.getInstance().fromJson(jsonString, clazz);
    }

    /**
     * 对象转换为JSONP字符串
     */
    public static String toJsonp(String functionName, Object object) {
        return JsonMapper.getInstance().toJsonpString(functionName, object);
    }

    /**
     * 输出JSONP格式数据.
     */
    public String toJsonpString(String functionName, Object object) {
        return toJsonString(new JSONPObject(functionName, object));
    }

}
