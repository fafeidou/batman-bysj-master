package com.batman.bysj.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

@SpringBootApplication
public class BatmanBysjCommonRedisClusterApplication implements CommandLineRunner {
    @Autowired
    private RedisTemplate redisTemplate;
    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonRedisClusterApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
//            StringRedisConnection conn = (StringRedisConnection) connection;
//            for (int i = 0; i < 10000; i++) {
//                conn.hSet("test-pipeline", "field" + i, "value" + i);
//            }
//            return null;
//
//        });
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        opsForValue.set("redisKey", "cluster test");
        System.out.println("11" + opsForValue.get("redisKey"));
    }


}
