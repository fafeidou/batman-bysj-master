package com.batman.bysj;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatmanBysjCommonBaseApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonBaseApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Test test = (Test) Class.forName("com.batman.bysj.Test").newInstance();
        test.afterPropertiesSet();

    }

}
