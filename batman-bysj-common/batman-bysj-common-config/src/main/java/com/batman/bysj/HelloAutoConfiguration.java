package com.batman.bysj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author victor.qin
 * @date 2018/4/18 9:42
 */
@Configuration
@EnableConfigurationProperties(value = HelloServiceProperteis.class)
public class HelloAutoConfiguration {

    @Autowired
    private HelloServiceProperteis helloServiceProperteis;

    @Bean
    public HelloServiceProperteis helloServiceProperteis(){
        System.out.println(helloServiceProperteis.getMsg());
        return helloServiceProperteis;
    }
}
