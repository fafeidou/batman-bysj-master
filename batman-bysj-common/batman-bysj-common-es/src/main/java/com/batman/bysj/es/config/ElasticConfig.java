package com.batman.bysj.es.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author victor.qin
 * @date 2018/7/28 18:01
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.batman.bysj.es")
public class ElasticConfig {
}
