package com.batman.rabbitmq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author victor.qin
 * @date 2018/8/16 18:33
 */
@Component
@ConfigurationProperties(prefix = "mq")
public class MQProperties {
    private String defaultExchange;
    private String routeKey;
    private String queue;

    public String getDefaultExchange() {
        return defaultExchange;
    }

    public void setDefaultExchange(String defaultExchange) {
        this.defaultExchange = defaultExchange;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
