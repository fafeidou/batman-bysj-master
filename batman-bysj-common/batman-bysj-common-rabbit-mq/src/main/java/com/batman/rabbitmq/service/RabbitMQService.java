package com.batman.rabbitmq.service;

import com.batman.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

/**
 * @author victor.qin
 * @date 2018/8/16 18:38
 */
@Service
public class RabbitMQService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQService.class);

    @RabbitListener(queues = "${mq.queue}")
    public void receive(String payload, Channel channel,
                        @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        LOGGER.info("消费内容为：{}", payload);
        RabbitMQUtils.askMessage(channel, tag, LOGGER);
    }
}
