package com.batman.bysj.common.redis.config.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("batman.cache")
public class CommCacheProperties {
    /**
     * 是否使用本地缓存(默认使用本地缓存)
     */
    private boolean local = true;

    /**
     * AOP的场合(默认过期时间 单位秒 1小时)
     */
    private long defaultExpiration = 3600;

    /**
     * 同步 {@link org.springframework.boot.autoconfigure.cache.CacheProperties#cacheNames}
     */
    private List<String> cacheNames = new ArrayList<>();

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public long getDefaultExpiration() {
        return defaultExpiration;
    }

    public void setDefaultExpiration(long defaultExpiration) {
        this.defaultExpiration = defaultExpiration;
    }

    public List<String> getCacheNames() {
        return cacheNames;
    }

    public void setCacheNames(List<String> cacheNames) {
        this.cacheNames = cacheNames;
    }
}
