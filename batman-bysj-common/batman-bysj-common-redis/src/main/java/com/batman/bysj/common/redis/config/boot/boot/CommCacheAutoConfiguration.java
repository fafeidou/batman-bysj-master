package com.batman.bysj.common.redis.config.boot.boot;

import com.batman.bysj.common.base.Loggable;
import com.batman.bysj.common.redis.cache.CacheKeyGenerator;
import com.batman.bysj.common.redis.cache.MockRedisTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.util.List;

/**
 * 缓存配置
 *
 * @author victor.qin
 * @date 2018/5/23 11:16
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CommCacheProperties.class)
public class CommCacheAutoConfiguration extends Loggable {

    @Bean
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate,
                                     CommCacheProperties cacheProperties) {

        if (cacheProperties.isLocal()) {
            $info("current using GuavaCacheManager");
            return new GuavaCacheManager();
        }

        $info("current using MockRedisCacheManager, Local is [{}]", cacheProperties.isLocal());

        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setUsePrefix(true);
        cacheManager.setLoadRemoteCachesOnStartup(true);
        cacheManager.setDefaultExpiration(cacheProperties.getDefaultExpiration());
        List<String> cacheNames = cacheProperties.getCacheNames();
        if (!cacheNames.isEmpty()) {
            cacheManager.setCacheNames(cacheNames);
        }
        return cacheManager;
    }

    /**
     * Cache Key 生成器
     */
    @Bean
    public KeyGenerator cacheKeyGenerator() {
        return new CacheKeyGenerator();
    }

    /**
     * 有 redis 连接的时候使用这个
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(ObjectProvider<RedisConnectionFactory> redisConnectionFactoryProvider,
                                                       CommCacheProperties properties) {

        MockRedisTemplate<Object> redisTemplate = new MockRedisTemplate<>();
        //<property name="defaultSerializer" ref="genericJackson2JsonRedisSerializer"/>
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        //<property name="hashValueSerializer" ref="genericJackson2JsonRedisSerializer"/>
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        RedisConnectionFactory redisConnectionFactory = redisConnectionFactoryProvider.getIfAvailable();

        if (properties.isLocal() || redisConnectionFactory == null) {
            redisTemplate.setLocal(true);
            return redisTemplate;
        }

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
