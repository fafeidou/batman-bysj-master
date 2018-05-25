package com.batman.bysj.common.web.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author victor.qin
 * @date 2018/5/25 11:04
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.batman.bysj.common.web")
public class WebApiConfiguration extends WebMvcConfigurerAdapter {


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        converters.add(converter());
    }

    @Bean
    public CustomMessageConverter converter() {
        return new CustomMessageConverter();
    }


}
