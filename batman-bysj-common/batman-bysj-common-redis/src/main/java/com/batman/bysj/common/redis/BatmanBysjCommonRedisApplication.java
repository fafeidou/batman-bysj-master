package com.batman.bysj.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class BatmanBysjCommonRedisApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonRedisApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        MasterDataCacheHelper.setValue("aaa", "sdfsdf");
        Object aaa = MasterDataCacheHelper.getValue("aaa");
        MasterDataCacheHelper.delete("aaa");
        System.out.println(aaa);
    }

    @Configuration
    public static class MasterDataRedisConfiguration {
        public MasterDataRedisConfiguration(RedisConnectionFactory redisConnectionFactory) {
            MasterDataCacheHelper.useRedisConnectionFactory(redisConnectionFactory);
        }
    }

}
