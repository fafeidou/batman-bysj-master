package com.batman.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author victor.qin
 * @date 2018/9/4 14:42
 */
@Configuration
@EnableConfigurationProperties(RabbitProperties.class)
public class AmqpConfig {
    public static final String QUEUE_NAME = "test-work-queue";
    /**
     * 消息交换机的名字
     */
    public static final String EXCHANGE = "delay";
    /**
     * 队列key1
     */
    public static final String ROUTINGKEY1 = "delay";
    /**
     * 队列key2
     */
    public static final String ROUTINGKEY2 = "delay_key";

    @Autowired
    ConnectionFactory connectionFactory;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    //必须是prototype类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //如果rabbitmq挂了，那么存储在内存中的消息就会丢失了，这里涉及到durable的问题
    //如果为true 内存中的消息会恢复
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Queue queue2() {
        return new Queue(ROUTINGKEY2, true);
    }

    /**
     * 配置消息交换机
     * 针对消费者配置
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    /**
     * 将消息队列2与交换机绑定
     * 针对消费者配置
     *
     * @return
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(ROUTINGKEY2);
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> jsaFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        //并发消费者数量
        factory.setMaxConcurrentConsumers(1);
        //prefetchCount的值设为1 这告诉RabbitMQ不要同时将多个消息分派给一个工作者
        //换句话说，在某个工作者处理完一条消息并确认它之前，RabbitMQ不会给该工作者分派新的消息
        //而是将新的消息分派给下一个不是很繁忙的工作者 这就是fair dispatch

        //如果不设置，默认是round-robin
        factory.setPrefetchCount(1);

        //AcknowledgeMode.AUTO 队列把消息发送给消费者之后就从队列中删除消息
        //AcknowledgeMode.MANUAL 关闭自动应答，说明队列把消息给消费者之后要等待消费者的确认处理完成消息。
        //如果未收到消费者的确认处理完成消息，就会分发给其他消费者，保证消息不丢失。
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
}
