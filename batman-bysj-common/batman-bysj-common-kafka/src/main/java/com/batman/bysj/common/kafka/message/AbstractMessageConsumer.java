package com.batman.bysj.common.kafka.message;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 抽象的Kafka消息消费处理器
 *
 * @param <H> 处理器类型
 * @author Wangtd 2017/10/25
 */
public abstract class AbstractMessageConsumer<H> implements Runnable, EnvironmentAware {

    private static Environment env;

    private KafkaConsumer<String, String> consumer;


    private final List<H> handlers = new ArrayList<>();

    public AbstractMessageConsumer(Map<String, H> handlerMap) {
        super();
        handlers.addAll(handlerMap.values());
    }

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put("metadata.broker.list", env.getProperty("kafka.metadata.broker.list"));
        props.put("bootstrap.servers", env.getProperty("kafka.bootstrap.servers"));
        // 动态生成新的分组ID，以便每次取得最新的消息
        String groupId = new SimpleDateFormat("'AutoGroupID-'yyyyMMddHHmmss").format(new Date());
        props.put("group.id", groupId);
        //props.put("group.id", env.getProperty("kafka.group.id"));
        props.put("key.deserializer", env.getProperty("kafka.key.deserializer"));
        props.put("value.deserializer", env.getProperty("kafka.value.deserializer"));
        props.put("auto.offset.reset", env.getProperty("kafka.auto.offset.reset"));
        props.put("request.timeout.ms", env.getProperty("kafka.request.timeout.ms"));
        props.put("session.timeout.ms", env.getProperty("kafka.session.timeout.ms"));
        props.put("enable.auto.commit", false);
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(getTopic()));
    }

    public abstract String getTopic();

    protected abstract void handleMessage(String message, List<H> handlers);

    @Override
    public void run() {
        while (true) {
            // 中断退出Kafka消费线程
            if (Thread.currentThread().isInterrupted()) {
                destroy();
                break;
            }
            // 取出Kafka中的消息并处理
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (TopicPartition partition : records.partitions()) {
                List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                for (ConsumerRecord<String, String> record : partitionRecords) {
                    try {
                        record.value();
                        // submit
                        Map<TopicPartition, OffsetAndMetadata> offsets = Collections.singletonMap(partition,
                                new OffsetAndMetadata(record.offset() + 1));
                        consumer.commitSync(offsets);
                        // handle
                        handleMessage(record.value(), handlers);
                    } catch (Exception e) {
                        // log issue
                    }
                }
            }
        }
    }

    protected void submitHandler(List<Runnable> handlers, List<Future<?>> futures) {
        submitHandler(handlers.size(), handlers, futures);
    }

    protected void submitHandler(int threadSize, List<Runnable> handlers, List<Future<?>> futures) {
        try {
            threadSize = threadSize > 20 ? 20 : threadSize;
            ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
            ExecutorService service = executorService;

            handlers.forEach(handler -> {
                Future<?> future = service.submit(handler);
                futures.add(future);
            });
            service.shutdown();
            service.awaitTermination(30, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }

    public void destroy() {
        if (consumer != null) {
            consumer.close();
        }
    }

}
