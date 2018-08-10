package com.batman.bysj.mongo;

import com.batman.bysj.mongo.dao.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatmanBysjCommonMongoApplication implements CommandLineRunner {
    @Autowired
    private TestDao testDao;

    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonMongoApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
    }
}
