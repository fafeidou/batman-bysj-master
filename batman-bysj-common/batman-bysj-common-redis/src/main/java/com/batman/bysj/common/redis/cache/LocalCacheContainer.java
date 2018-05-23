package com.batman.bysj.common.redis.cache;

import com.google.common.collect.Maps;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 本地缓存的管理容器
 * Created by jonas on 2017/1/13.
 */
class LocalCacheContainer {

    /**
     * 本地缓存支持的缓存类型
     */
    enum CacheType {
        Hash,
        List,
        Value,
        ZSet
    }

    /**
     * 缓存键管理 Map。管理所有类型缓存的缓存键，防止重复
     */
    private final Map<Object, CacheType> _cacheKeyMap = Maps.newConcurrentMap();

    private final Map<Object, Object> _hashCache = Maps.newConcurrentMap();
    private final Map<Object, Object> _listCache = Maps.newConcurrentMap();
    private final Map<Object, Object> _zSetCache = Maps.newConcurrentMap();
    private final Map<Object, Object> _valueCache = Maps.newConcurrentMap();

    private static final LocalCacheContainer LOCAL_CACHE_CONTAINER = new LocalCacheContainer();

    private LocalCacheContainer() {
    }

    static LocalCacheContainer getInstance() {
        return LOCAL_CACHE_CONTAINER;
    }

    private void checkCacheType(Object key, CacheType targetCacheType) {
        CacheType cacheType = _cacheKeyMap.get(key);
        if (cacheType != null && !cacheType.equals(targetCacheType))
            throw new InvalidDataAccessApiUsageException("WRONGTYPE Operation against a key holding the wrong kind of value");
    }

    <K, HK, HV> void put(K key, Map<HK, HV> map) {
        checkCacheType(key, CacheType.Hash);

        _cacheKeyMap.put(key, CacheType.Hash);

        _hashCache.put(key, map);

    }

    <K, V> void put(K key, List<V> list) {
        checkCacheType(key, CacheType.List);

        _cacheKeyMap.put(key, CacheType.List);

        _listCache.put(key, list);

    }

    <K, V extends ZSetOperations.TypedTuple> void put(K key, Set<V> set) {
        checkCacheType(key, CacheType.ZSet);

        _cacheKeyMap.put(key, CacheType.ZSet);

        _zSetCache.put(key, set);
    }

    <K, V> void put(K key, V value) {
        checkCacheType(key, CacheType.Value);

        _cacheKeyMap.put(key, CacheType.Value);

        _valueCache.put(key, value);
    }

    <K> void remove(K key, CacheType cacheType) {
        checkCacheType(key, cacheType);
        _cacheKeyMap.remove(key);
        evict(key, cacheType);
    }

    <K> void remove(K key) {
        CacheType cacheType = _cacheKeyMap.get(key);
        if (cacheType == null)
            return;
        _cacheKeyMap.remove(key);
        evict(key, cacheType);
    }

    <K> Boolean hasKey(K key) {
        return _cacheKeyMap.containsKey(key);
    }

    private <K> void evict(K key, CacheType cacheType) {
        switch (cacheType) {
            case Hash:
                _hashCache.remove(key);
                break;
            case List:
                _listCache.remove(key);
                break;
            case Value:
                _valueCache.remove(key);
                break;
            case ZSet:
                _zSetCache.remove(key);
                break;
        }
    }

    @SuppressWarnings("unchecked")
    <K, HK, HV> Map<HK, HV> getHashCache(K key) {
        checkCacheType(key, CacheType.Hash);
        return (Map<HK, HV>) _hashCache.get(key);
    }

    @SuppressWarnings("unchecked")
    <K, V> List<V> getListCache(K key) {
        checkCacheType(key, CacheType.List);
        return (List<V>) _listCache.get(key);
    }

    @SuppressWarnings("unchecked")
    <K, V extends ZSetOperations.TypedTuple> Set<V> getZSetCahce(K key) {
        checkCacheType(key, CacheType.ZSet);
        return (Set<V>) _zSetCache.get(key);
    }

    @SuppressWarnings("unchecked")
    <K, V> V getValueCache(K key) {
        checkCacheType(key, CacheType.Value);
        return (V) _valueCache.get(key);
    }

    Set<Object> keySet() {
        return Collections.unmodifiableSet(_cacheKeyMap.keySet());
    }
}
