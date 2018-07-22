package com.batman.bysj.common.redis.config.boot.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis 配置
 *
 * @author jonas on 2017/3/10.
 * @version 1.0.0
 */
@Configuration
@EnableConfigurationProperties(CommRedisProperties.class)
public class CommRedisAutoConfiguration {

    @Bean(name ="redisConnectionFactory")
    @Primary
    @ConditionalOnProperty(prefix = "batman.cache", name = "local", havingValue = "false")
    public RedisConnectionFactory redisConnectionFactory(CommRedisProperties properties) {

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();

        jedisConnectionFactory.setHostName(properties.getHost());
        jedisConnectionFactory.setPassword(properties.getPassword());
        jedisConnectionFactory.setPort(properties.getPort());
        jedisConnectionFactory.setUseSsl(properties.isSsl());
        jedisConnectionFactory.setTimeout(properties.getTimeout());

        CommRedisProperties.Pool pool = properties.getPool();
        if (pool == null) {
            jedisConnectionFactory.setUsePool(false);
            return jedisConnectionFactory;
        }

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        if (pool.getMinEvictableIdleTimeMillis() != null)
            jedisPoolConfig.setMinEvictableIdleTimeMillis(pool.getMinEvictableIdleTimeMillis());
        if (pool.getNumTestsPerEvictionRun() != null)
            jedisPoolConfig.setNumTestsPerEvictionRun(pool.getNumTestsPerEvictionRun());
        if (pool.getTimeBetweenEvictionRunsMillis() != null)
            jedisPoolConfig.setTimeBetweenEvictionRunsMillis(pool.getTimeBetweenEvictionRunsMillis());
        if (pool.getTestOnBorrow() != null)
            jedisPoolConfig.setTestOnBorrow(pool.getTestOnBorrow());
        if (pool.getTestOnReturn() != null)
            jedisPoolConfig.setTestOnReturn(pool.getTestOnReturn());
        if (pool.getTestWhileIdle() != null)
            jedisPoolConfig.setTestWhileIdle(pool.getTestWhileIdle());
        if (pool.getMaxIdle() != null)
            jedisPoolConfig.setMaxIdle(pool.getMaxIdle());
        if (pool.getMinIdle() != null)
            jedisPoolConfig.setMinIdle(pool.getMinIdle());
        if (pool.getMaxActive() != null)
            jedisPoolConfig.setMaxTotal(pool.getMaxActive());
        if (pool.getMaxWait() != null)
            jedisPoolConfig.setMaxWaitMillis(pool.getMaxWait());

        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);

        return jedisConnectionFactory;
    }
}
