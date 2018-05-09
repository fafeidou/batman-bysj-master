package com.batman.bysj.common.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Sms配置信息
 *
 * @author victor.qin
 * @date 2018/5/9 12:51
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfig implements Serializable {

    /**
     * 1、阿里云
     */
    private Integer type;

    private String aliyunAccessKeyId;

    private String aliyunAccessKeySecret;


    public String getAliyunAccessKeyId() {
        return aliyunAccessKeyId;
    }

    public void setAliyunAccessKeyId(String aliyunAccessKeyId) {
        this.aliyunAccessKeyId = aliyunAccessKeyId;
    }

    public String getAliyunAccessKeySecret() {
        return aliyunAccessKeySecret;
    }

    public void setAliyunAccessKeySecret(String aliyunAccessKeySecret) {
        this.aliyunAccessKeySecret = aliyunAccessKeySecret;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
