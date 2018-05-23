package com.batman.bysj.common.redis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class BatmanBysjCommonRedisApplication implements CommandLineRunner {
    @Autowired
    private TransactionId transactionId;
    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonRedisApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        MasterDataCacheHelper.setValue("aaa", "sdfsdf");
//        Object aaa = MasterDataCacheHelper.getValue("aaa");
//        MasterDataCacheHelper.delete("aaa");
//        System.out.println(aaa);
//        String autoIncrStr = StringUtils.leftPad(String.valueOf(1), 13, "0");
//        System.out.println(autoIncrStr);
         for(int i = 0;i < 100;i ++){
             System.out.println(transactionId.nextTransactionId());
         }
    }

//    @Configuration
//    public static class MasterDataRedisConfiguration {
//        public MasterDataRedisConfiguration(RedisConnectionFactory redisConnectionFactory) {
//            MasterDataCacheHelper.useRedisConnectionFactory(redisConnectionFactory);
//        }
//    }

}
