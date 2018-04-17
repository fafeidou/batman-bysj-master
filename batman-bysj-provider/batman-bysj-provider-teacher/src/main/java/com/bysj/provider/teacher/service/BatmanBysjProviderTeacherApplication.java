package com.bysj.provider.teacher.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;

@EnableHystrix
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
@ComponentScan( basePackages = "com.batman.bysj.common.service.*")
@MapperScan(basePackages = "com.batman.bysj.common.dao.mapper")
public class BatmanBysjProviderTeacherApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatmanBysjProviderTeacherApplication.class, args);
	}
}
