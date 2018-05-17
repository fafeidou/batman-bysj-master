package com.batman.bysj.common.kafka;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
}
