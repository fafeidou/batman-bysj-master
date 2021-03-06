package com.batman.bysj.common.kafka;

import com.batman.bysj.common.redis.MasterDataCacheHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class BatmanBysjCommonKafkaApplication implements CommandLineRunner {

//	@Autowired
//	private MessageProducer messageProducer;

    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonKafkaApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
//		messageProducer.sendMessage("test-topic","111111111111111111111111");
    }

    @Configuration
    public static class MasterDataRedisConfiguration {
        public MasterDataRedisConfiguration(RedisConnectionFactory redisConnectionFactory) {
            MasterDataCacheHelper.useRedisConnectionFactory(redisConnectionFactory);
        }
    }
}
