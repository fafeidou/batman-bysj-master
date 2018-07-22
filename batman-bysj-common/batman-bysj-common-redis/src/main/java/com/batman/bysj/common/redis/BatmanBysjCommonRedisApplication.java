package com.batman.bysj.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import redis.clients.jedis.JedisCluster;

import java.util.List;
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
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        opsForValue.set("redisKey", "cluster test");
        System.out.println("11" + opsForValue.get("redisKey"));

//        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
//            StringRedisConnection conn = (StringRedisConnection) connection;
//            for (int i = 0; i < 10000; i++) {
//                conn.hSet("test-pipeline", "field" + i, "value" + i);
//            }
//            return null;
//        });
//        List<Object> results = redisTemplate.executePipelined(
//                new RedisCallback<Object>() {
//                    public Object doInRedis(RedisConnection connection) throws DataAccessException {
//                        StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
//                        for(int i=0; i< 22; i++) {
//                            stringRedisConn.rPop("myqueue");
//                        }
//                        return null;
//                    }
//                });


    }

//    @Configuration
//    public static class MasterDataRedisConfiguration {
//        public MasterDataRedisConfiguration(RedisConnectionFactory redisConnectionFactory) {
//            MasterDataCacheHelper.useRedisConnectionFactory(redisConnectionFactory);
//        }
//    }


}
