package com.batman.bysj;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author victor.qin
 * @date 2018/4/18 9:40
 */
@ConfigurationProperties(prefix = "hello")
public class HelloServiceProperteis {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
