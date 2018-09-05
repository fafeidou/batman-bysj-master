package com.batman.rabbitmq.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author victor.qin
 * @date 2018/9/4 14:55
 */
@Component
public class Receiver3 implements ChannelAwareMessageListener {


    @Override
    @RabbitListener(queues = "${rabbitmq.queue}", containerFactory = "jsaFactory")
    public void onMessage(Message message, Channel channel) throws Exception {
        byte[] body = message.getBody();
        System.out.println("消费端3接收到消息 : " + new String(body));
        Thread.sleep(6000);
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
    }
}
