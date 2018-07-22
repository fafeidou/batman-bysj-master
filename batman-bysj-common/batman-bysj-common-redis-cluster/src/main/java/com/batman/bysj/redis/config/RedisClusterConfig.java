package com.batman.bysj.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author victor.qin
 * @date 2018/7/22 11:28
 */
@Configuration
@EnableConfigurationProperties(RedisClusterProperties.class)
public class RedisClusterConfig {
    @Autowired
    private RedisClusterProperties clusterProperties;

    @Bean
    public RedisClusterConfiguration getClusterConfig() {
        RedisClusterConfiguration rcc = new RedisClusterConfiguration(clusterProperties.getNodes());
        rcc.setMaxRedirects(clusterProperties.getMaxRedirects().intValue());
        return rcc;
    }

    @Bean
    @Primary
    public JedisConnectionFactory getConnectionFactory(RedisClusterConfiguration cluster) {
        return new JedisConnectionFactory(cluster);
    }

    @Bean
    @Primary
    public RedisTemplate getRedisTemplate(JedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        return redisTemplate;
    }

//    @Bean
//    public StringRedisTemplate getStringRedisTemplate(RedisConnectionFactory factory) {
//        StringRedisTemplate stringTemplate = new StringRedisTemplate();
//        stringTemplate.setConnectionFactory(factory);
//        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
//        stringTemplate.setKeySerializer(redisSerializer);
//        stringTemplate.setHashKeySerializer(redisSerializer);
//        stringTemplate.setValueSerializer(redisSerializer);
//        return stringTemplate;
//    }
}
