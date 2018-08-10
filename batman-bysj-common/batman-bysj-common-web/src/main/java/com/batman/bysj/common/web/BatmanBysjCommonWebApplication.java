package com.batman.bysj.common.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Controller
@EnableSwagger2
@ComponentScan(basePackages = {"com.batman.bysj"})
public class BatmanBysjCommonWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonWebApplication.class, args);
    }
}
