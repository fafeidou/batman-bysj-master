package com.batman.bysj.common.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author victor.qin
 * @date 2018/8/11 17:27
 */
@Controller
@EnableSwagger2
@ServletComponentScan
@ComponentScan(basePackages = {"com.batman.bysj"})
@SpringBootApplication
@EnableTransactionManagement
public class BatmanBysjCommonWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatmanBysjCommonWebApplication.class, args);
    }
}
