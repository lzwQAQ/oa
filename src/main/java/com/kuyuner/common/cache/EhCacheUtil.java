package com.kuyuner.common.cache;

import com.kuyuner.common.utils.SpringContextHolder;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Ehcache
 *
 * @author administrator
 */
public class EhCacheUtil {
    private static Logger logger = LoggerFactory.getLogger(EhCacheUtil.class);

    private static final CacheManager ehcacheManager = SpringContextHolder.getBean(CacheManager.class);

    private static final String DEFAULT_CACHE = "sysCache";

    /**
     * 获取DEFAULT_CACHE缓存
     *
     * @param key 键
     * @return
     */
    public static Object get(String key) {
        return get(DEFAULT_CACHE, key);
    }

    /**
     * 获取DEFAULT_CACHE缓存
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return
     */
    public static Object get(String key, Object defaultValue) {
        Object value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 写入DEFAULT_CACHE缓存
     *
     * @param key 键
     * @return
     */
    public static void put(String key, Object value) {
        put(DEFAULT_CACHE, key, value);
    }

    /**
     * 从DEFAULT_CACHE缓存中移除
     *
     * @param key 键
     * @return
     */
    public static void remove(String key) {
        remove(DEFAULT_CACHE, key);
    }

    /**
     * 获取缓存
     *
     * @param cacheName 缓存名称
     * @param key       键
     * @return
     */
    public static Object get(String cacheName, String key) {
        Element ele = getCache(cacheName).get(key);
        return ele == null ? null : ele.getObjectValue();
    }

    /**
     * 获取缓存
     *
     * @param cacheName    缓存名称
     * @param key          键
     * @param defaultValue 默认值
     * @return
     */
    public static Object get(String cacheName, String key, Object defaultValue) {
        Object value = get(cacheName, key);
        return value != null ? value : defaultValue;
    }

    /**
     * 写入缓存
     *
     * @param cacheName 缓存名称
     * @param key       键
     * @param value     值
     */
    public static void put(String cacheName, String key, Object value) {
        getCache(cacheName).put(new Element(key, value));
    }

    /**
     * 从缓存中移除
     *
     * @param cacheName 缓存名称
     * @param key       键
     */
    public static void remove(String cacheName, String key) {
        getCache(cacheName).remove(key);
    }

    /**
     * 从缓存中移除所有
     *
     * @param cacheName 缓存名称
     */
    public static void removeAll(String cacheName) {
        Cache cache = getCache(cacheName);
        List keys = cache.getKeys();
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            cache.remove(it.next());
        }
        logger.info("清理缓存： {} => {}", cacheName, keys);
    }

    /**
     * 获得一个Cache，没有则显示日志。
     *
     * @param cacheName
     * @return
     */
    private static Cache getCache(String cacheName) {
        Cache cache = ehcacheManager.getCache(cacheName);
        if (cache == null) {
            throw new RuntimeException("缓存" + cacheName + "不存在！");
        }
        return cache;
    }
}
