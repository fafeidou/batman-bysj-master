package com.batman.bysj.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class BatmanBysjDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjDiscoveryApplication.class, args);
    }
}