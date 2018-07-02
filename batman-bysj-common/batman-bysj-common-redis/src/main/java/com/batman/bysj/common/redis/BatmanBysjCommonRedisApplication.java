package com.batman.bysj.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

@SpringBootApplication
public class BatmanBysjCommonRedisApplication implements CommandLineRunner {
    @Autowired
    private TransactionId transactionId;
    @Autowired
    private RedisTemplate redisTemplate;

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
//        for (int i = 0; i < 100; i++) {
//            System.out.println(transactionId.nextTransactionId());
//        }
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();

        SetOperations setOperations = redisTemplate.opsForSet();

        for (int i = 1; i <= 100; i += 10) {
            // 初始化CommentId索引 SortSet

            zSetOperations.add("topicId", i, i);
            // 初始化Comment数据 Hash
//            setOperations.add("Comment_Key", i, "comment content .......");
        }
        Set topicId = zSetOperations.rangeByScore("topicId", 80, 100, 0, 2);
        System.out.println(topicId.toString());
    }

//    @Configuration
//    public static class MasterDataRedisConfiguration {
//        public MasterDataRedisConfiguration(RedisConnectionFactory redisConnectionFactory) {
//            MasterDataCacheHelper.useRedisConnectionFactory(redisConnectionFactory);
//        }
//    }

}
