package com.kuyuner.core.common.dict;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.utils.SpringContextHolder;
import com.kuyuner.core.sys.entity.DictData;

import org.springframework.core.annotation.AnnotationUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 序列化字典字段
 *
 * @author administrator
 */
public class DictTypeSerializer extends JsonSerializer<String> {

    private static final Map<String, DictType> DICT_VALUE_MAP = new HashMap<>();
    /**
     * 分隔符
     */
    private static final String SPLIT = ",";
    private static final String POINTER = ".";

    private DictHolder dictHolder = SpringContextHolder.getBean(DictHolder.class);

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        DictType dictType = getDictType(gen.getCurrentValue().getClass(), gen.getOutputContext().getCurrentName());
        gen.writeObject(getDictValue(value, dictType));
    }

    /**
     * 根据key，type获得字典的值
     *
     * @param code
     * @param dictType
     * @return
     */
    private String getDictValue(String code, DictType dictType) {
        if (StringUtils.isBlank(code)) {
            return dictType.defaultValue();
        }
        List<DictData> list = dictHolder.getValues(dictType.value());
        StringBuilder builder = new StringBuilder();
        String[] codes = code.split(SPLIT);
        for (String c : codes) {
            for (DictData dictData : list) {
                if (StringUtils.equals(dictData.getDictCode(), c)) {
                    builder.append(dictData.getDictValue());
                    builder.append(SPLIT);
                    break;
                }
            }
        }
        builder.setLength(builder.length() == 0 ? 0 : builder.length() - 1);
        return builder.toString();
    }

    /**
     * 获得字典注解的信息
     *
     * @param clz
     * @param fieldName
     * @return
     */
    private static DictType getDictType(Class<?> clz, String fieldName) {
        DictType dictValue;
        if ((dictValue = DICT_VALUE_MAP.get(getKey(clz.getName(), fieldName))) == null) {
            synchronized (clz) {
                if (DICT_VALUE_MAP.get(getKey(clz.getName(), fieldName)) == null) {
                    for (Class<?> clzz = clz; clzz != Object.class; clzz = clzz.getSuperclass()) {
                        for (Field field : clzz.getDeclaredFields()) {
                            for (Annotation annotation : field.getDeclaredAnnotations()) {
                                dictValue = AnnotationUtils.getAnnotation(annotation, DictType.class);
                                if (dictValue != null) {
                                    DICT_VALUE_MAP.put(getKey(clz.getName(), field.getName()), dictValue);
                                    break;
                                }
                            }
                        }
                    }
                }
                dictValue = DICT_VALUE_MAP.get(getKey(clz.getName(), fieldName));
            }
        }

        return dictValue;
    }

    private static String getKey(String className, String field) {
        return className + POINTER + field;
    }
}
