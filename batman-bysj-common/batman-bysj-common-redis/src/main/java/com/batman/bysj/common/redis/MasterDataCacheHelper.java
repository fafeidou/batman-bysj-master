package com.batman.bysj.common.redis;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@Component
public final class MasterDataCacheHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(MasterDataCacheHelper.class);

    private static RedisTemplate<String, Object> redisTemplate;

    private static RedisTemplate<String, Object> getRedisTemplate() {
        Objects.requireNonNull(MasterDataCacheHelper.redisTemplate, "'redisTemplate' is null, please config 'redisConnectionFactory' property for MasterDataCacheHelper");
        return MasterDataCacheHelper.redisTemplate;
    }

    public static void useRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory) {

        if (redisConnectionFactory instanceof JedisConnectionFactory) {
            LOGGER.info("Masterdata Redis 配置 => {}", ((JedisConnectionFactory) redisConnectionFactory).getHostName());
        }
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
    }

    public static long getSize(String key) {
        return getHashOperation().size(key);
    }

    public static <T> T getBean(String key, String cellKey) {
        HashOperations<String, String, T> hashOperation = getHashOperation();
        return hashOperation.get(key, cellKey);
    }

    public static void setBean(String key, String cellKey, Object model) {
        HashOperations<String, String, Object> hashOperation = getHashOperation();
        hashOperation.put(key, cellKey, model);
    }

    public static <T> List<T> getBeans(String key, List<String> keyList) {
        HashOperations<String, String, T> hashOperation = getHashOperation();
        return hashOperation.multiGet(key, keyList);
    }

    public static <T> List<T> getBeans(String key) {
        HashOperations<String, String, T> hashOperation = getHashOperation();
        Map<String, T> map = hashOperation.entries(key);
        if (map != null) {
            return new ArrayList<>(map.values());
        } else {
            return new ArrayList<>();
        }
    }

    public static void reFreshSSB(String key, Map refreshMap) {
        callCache(key, refreshMap);
    }

    public static void delete(String key) {
        getRedisTemplate().delete(key);
    }

    private static <HK, HV> HashOperations<String, HK, HV> getHashOperation() {
        return getRedisTemplate().opsForHash();
    }

    /**
     * 调用服务器缓存
     */
    private static void callCache(String cacheKey, Map mapData) {
        if (StringUtils.isEmpty(cacheKey) || MapUtils.isEmpty(mapData)) return;
        getHashOperation().putAll(cacheKey, mapData);
    }

    /**
     * 使用 value 操作获取值
     */
    public static Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 使用 value 操作存储指定的值
     */
    public static void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

}
