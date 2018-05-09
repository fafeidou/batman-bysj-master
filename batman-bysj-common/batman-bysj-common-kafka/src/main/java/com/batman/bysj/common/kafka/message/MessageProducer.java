package com.batman.bysj.common.kafka.message;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;

/**
 * Kafka消息生产者
 *
 * @author victor.qin
 * @date 2018/4/24 17:44
 */
@Component
public class MessageProducer implements EnvironmentAware {

    private static Environment env;

    private KafkaProducer<String, String> producer;

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put("metadata.broker.list", env.getProperty("kafka.metadata.broker.list"));
        props.put("bootstrap.servers", env.getProperty("kafka.bootstrap.servers"));
        props.put("acks", env.getProperty("kafka.acks"));
        props.put("retries", Integer.valueOf(env.getProperty("kafka.retries")));
        props.put("batch.size", Integer.valueOf(env.getProperty("kafka.batch.size")));
        props.put("linger.ms", Integer.valueOf(env.getProperty("kafka.linger.ms")));
        props.put("buffer.memory", Integer.valueOf(env.getProperty("kafka.buffer.memory")));
        props.put("key.serializer", env.getProperty("kafka.key.serializer"));
        props.put("value.serializer", env.getProperty("kafka.value.serializer"));
        producer = new KafkaProducer<>(props);
    }

    public void sendMessage(String topic, String message) {
        producer.send(new ProducerRecord<>(topic, message));
    }

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }

    @PreDestroy
    public void destroy() throws Exception {
        if (producer != null) {
            producer.close();
        }
    }

}
