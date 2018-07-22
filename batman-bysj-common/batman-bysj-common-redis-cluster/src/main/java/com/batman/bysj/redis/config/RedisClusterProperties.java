package com.batman.bysj.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author victor.qin
 * @date 2018/7/22 11:27
 */
//@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisClusterProperties {
    //集群节点
    private List<String> nodes=new ArrayList<>();

    public List<String> getNodes() {
        return nodes;
    }
    private Integer maxRedirects = 3;
    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Integer getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(Integer maxRedirects) {
        this.maxRedirects = maxRedirects;
    }
}
