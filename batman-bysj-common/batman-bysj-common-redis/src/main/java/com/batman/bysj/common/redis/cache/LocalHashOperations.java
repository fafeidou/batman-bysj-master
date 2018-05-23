package com.batman.bysj.common.redis.cache;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;

import java.util.*;
import java.util.Map.Entry;

/**
 * LocalHashOperations
 *
 * @author aooer 2016/4/5.
 * @version 2.0.0
 * @since 2.0.0
 */
public class LocalHashOperations<K, HK, HV> implements HashOperations<K, HK, HV> {

    public void delete(K key) {
        LocalCacheContainer.getInstance().remove(key, LocalCacheContainer.CacheType.Hash);
    }

    @Override
    public Long delete(K key, Object... hashKeys) {

        Map map = LocalCacheContainer.getInstance().getHashCache(key);

        if (map != null) {
            Arrays.asList(hashKeys).forEach(map::remove);
        }

        return (long) hashKeys.length;
    }

    @Override
    public Boolean hasKey(K key, Object hashKey) {

        Map map = LocalCacheContainer.getInstance().getHashCache(key);

        return map != null && map.containsKey(hashKey);
    }

    Boolean hasKey(K key) {
        return LocalCacheContainer.getInstance().getHashCache(key) != null;
    }

    @Override
    public HV get(K key, Object hashKey) {
        Map<HK, HV> map = getCacheOrCreate(key);
        return map.get(hashKey);
    }

    @Override
    public List<HV> multiGet(K key, Collection<HK> hashKeys) {

        Map<HK, HV> map = LocalCacheContainer.getInstance().getHashCache(key);
        List<HV> hvs = new ArrayList<>();
        if (map != null) {
            hashKeys.forEach(k -> hvs.add(map.get(k)));
        }
        return hvs;
    }

    @Override
    public Set<HK> keys(K key) {

        Map<HK, HV> map = LocalCacheContainer.getInstance().getHashCache(key);
        if (map != null) {
            return map.keySet();
        }
        return new HashSet<>();
    }

    @Override
    public Long size(K key) {

        Map map = LocalCacheContainer.getInstance().getHashCache(key);
        if (map != null) {
            return Integer.toUnsignedLong(map.size());
        }
        return 0L;
    }

    @Override
    public void putAll(K key, Map<? extends HK, ? extends HV> m) {
        Map<HK, HV> map = getCacheOrCreate(key);
        map.putAll(m);
    }

    @Override
    public void put(K key, HK hashKey, HV value) {
        Map<HK, HV> map = getCacheOrCreate(key);
        map.put(hashKey, value);
    }

    private Map<HK, HV> getCacheOrCreate(K key) {
        Map<HK, HV> map = LocalCacheContainer.getInstance().getHashCache(key);
        if (map == null) {
            LocalCacheContainer.getInstance().put(key, map = new LinkedHashMap<>());
        }
        return map;
    }

    @Override
    public Boolean putIfAbsent(K key, HK hashKey, HV value) {

        Map<HK, HV> map = getCacheOrCreate(key);

        if (!map.containsKey(hashKey)) {
            map.put(hashKey, value);
            return true;
        }

        return false;
    }

    @Override
    public List<HV> values(K key) {

        Map<HK, HV> map = LocalCacheContainer.getInstance().getHashCache(key);
        List<HV> values = new ArrayList<>();
        if (map != null) {
            values.addAll(map.values());
        }
        return values;
    }

    @Override
    public Map<HK, HV> entries(K key) {
        return LocalCacheContainer.getInstance().getHashCache(key);
    }

    @Override
    public RedisOperations<K, ?> getOperations() {
        return null;
    }

    @Override
    public Cursor<Entry<HK, HV>> scan(K key, ScanOptions options) {
        return null;
    }

    @Override
    public Long increment(K key, HK hashKey, long delta) {
        return null;
    }

    @Override
    public Double increment(K key, HK hashKey, double delta) {
        return null;
    }
}

