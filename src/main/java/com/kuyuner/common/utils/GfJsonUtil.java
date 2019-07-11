package com.kuyuner.common.utils;

import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class GfJsonUtil {
    private GfJsonUtil() {
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        return json == null ? null : JSON.parseObject(json, clazz);
    }

    public static <T> String toJSONString(T t) {
        return t == null ? null : JSON.toJSONString(t);
    }

    public static <T> List<T> parseList(Iterable<String> jsonList, Class<T> clazz) {
        List<T> retList = new ArrayList();
        Iterator var3 = jsonList.iterator();

        while(var3.hasNext()) {
            String json = (String)var3.next();
            retList.add(parseObject(json, clazz));
        }

        return retList;
    }

    public static <T> List<List<T>> parseArrayList(Iterable<String> jsonList, Class<T> clazz) {
        List<List<T>> retList = new ArrayList();
        Iterator var3 = jsonList.iterator();

        while(var3.hasNext()) {
            String json = (String)var3.next();
            retList.add(parseArray(json, clazz));
        }

        return retList;
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return json == null ? Collections.emptyList() : JSON.parseArray(json, clazz);
    }

    public static <T> Map<Long, T> parseMapByUid(List<String> jsonList, List<Long> uidList, Class<T> clazz, boolean isContainsNull) {
        if (jsonList.size() != uidList.size()) {
            return null;
        } else {
            Map<Long, T> uidMap = new HashMap(uidList.size());

            for(int i = 0; i < uidList.size(); ++i) {
                T t = parseObject((String)jsonList.get(i), clazz);
                if (isContainsNull || t != null) {
                    uidMap.put(uidList.get(i), t);
                }
            }

            return uidMap;
        }
    }

    public static <T> Map<Long, List<T>> parseArrayMapByUid(List<String> jsonList, List<Long> uidList, Class<T> clazz, boolean isContainsNull) {
        if (jsonList.size() != uidList.size()) {
            return null;
        } else {
            Map<Long, List<T>> uidMap = new HashMap(uidList.size());

            for(int i = 0; i < uidList.size(); ++i) {
                List<T> t = parseArray((String)jsonList.get(i), clazz);
                if (isContainsNull || t != null) {
                    uidMap.put(uidList.get(i), t);
                }
            }

            return uidMap;
        }
    }
}
