package com.kuyuner.shiro;

import net.sf.ehcache.Element;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author tangzj
 */
public class ShiroCache implements Cache {
    private net.sf.ehcache.Cache cache;

    public ShiroCache(net.sf.ehcache.Cache cache) {
        if (cache == null) {
            throw new RuntimeException("cache is null");
        }
        this.cache = cache;
    }

    @Override
    public Object get(Object key) throws CacheException {
        Element ele = cache.get(key);
        return ele == null ? null : ele.getObjectValue();
    }

    @Override
    public Object put(Object key, Object value) throws CacheException {
        cache.put(new Element(key, value));
        return value;
    }

    @Override
    public Object remove(Object key) throws CacheException {
        Object obj = get(key);
        cache.remove(key);
        return obj;
    }

    @Override
    public void clear() throws CacheException {
        cache.removeAll();
    }

    @Override
    public int size() {
        return cache.getSize();
    }

    @Override
    public Set<Object> keys() {
        Set<Object> set = new HashSet<>(size());
        for (Object key : cache.getKeys()) {
            set.add(key);
        }
        return set;
    }

    @Override
    public Collection values() {
        List<Object> list = new ArrayList<>(size());
        for (Object key : cache.getKeys()) {
            list.add(get(key));
        }
        return list;
    }
}
