package com.batman.bysj.common.mail.batmanbysjcommonmail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class MailClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailClientFactory.class);

    private static final String FILE = "mail.properties";

    private static final String PROPERTIES_PATH = FILE;

    private static final String CUSTOM_KEY_USERNAME = "mail.smtp.auth.username";

    private static final String CUSTOM_KEY_PASSWORD = "mail.smtp.auth.password";

    public static MailClient fromDefaultProperties() {
        return fromProperties(
                loadDefaultProperties()
        );
    }

    public static MailClient fromProperties(Properties properties) {

        String username = properties.getProperty(CUSTOM_KEY_USERNAME);
        String password = properties.getProperty(CUSTOM_KEY_PASSWORD);

        LOGGER.debug("创建邮件发送客户端: {} / {}", username, password);

        return new MailClient()
                .setUsername(username)
                .setPassword(password)
                .setProperties(properties);
    }

    private static Properties loadDefaultProperties() {

        Resource resource = new FileSystemResource(FILE);

        if (resource.exists()) {
            return loadProperties(resource);
        }

        resource = new ClassPathResource(PROPERTIES_PATH);

        if (resource.exists()) {
            return loadProperties(resource);
        }

        throw new MailClientException("没有找到邮件配置 " + FILE);
    }

    private static Properties loadProperties(Resource resource) {
        try {
            return PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            throw new MailClientException("加载默认邮件配置失败", e);
        }
    }
}
