package com.batman.bysj.common.redis.cache;

import org.springframework.data.redis.core.*;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * MockRedisTemplate
 *
 * @author jonas 2017/1/5.
 * @version 2.0.0
 * @since 2.0.0
 */
public class MockRedisTemplate<V> extends RedisTemplate<String, V> {
    private boolean local = false;

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    @Override
    public <HK, HV> HashOperations<String, HK, HV> opsForHash() {
        return opsForHash(isLocal());
    }

    public <HK, HV> HashOperations<String, HK, HV> opsForHash(boolean local) {
        return local ? new LocalHashOperations<>() : super.opsForHash();
    }

    @Override
    public ListOperations<String, V> opsForList() {
        return isLocal() ? new LocalListOperations<>() : super.opsForList();
    }

    @Override
    public ZSetOperations<String, V> opsForZSet() {
        return isLocal() ? new LocalZSetOperations<>() : super.opsForZSet();
    }

    @Override
    public ValueOperations<String, V> opsForValue() {
        return isLocal() ? new LocalValueOperations<>() : super.opsForValue();
    }

    @Override
    public void delete(String key) {
        if (isLocal()) {
            LocalCacheContainer.getInstance().remove(key);
        } else {
            super.delete(key);
        }
    }

    @Override
    public Boolean hasKey(String key) {
        return isLocal() ? LocalCacheContainer.getInstance().hasKey(key) : super.hasKey(key);
    }

    @PostConstruct
    @Override
    public void afterPropertiesSet() {
        if (!isLocal()) {
            super.afterPropertiesSet();
        }
    }

    @Override
    public Boolean expire(String key, final long timeout, final TimeUnit unit) {
        return true;
    }
}



