package com.batman.rabbitmq.service;

import com.batman.rabbitmq.model.Spittle;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author victor.qin
 * @date 2018/9/4 14:51
 */
@Service
public class Sender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    int dots = 0;
    int count = 0;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        Spittle spittle = new Spittle(1, "");
        StringBuilder builder = new StringBuilder("Hello");
        if (dots++ == 3) {
            dots = 1;
        }
        for (int i = 0; i < dots; i++) {
            builder.append('.');
        }

        builder.append(Integer.toString(++count));
        String message = builder.toString();
        spittle.setMessage(message);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration("6000");
        Message msg = new Message(spittle.toString().getBytes(), messageProperties);
        rabbitTemplate.convertAndSend(queue.getName(), msg);
        System.out.println(" [x] Sent '" + message + "'");
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send2() {
        Spittle spittle = new Spittle(1, "");
        StringBuilder builder = new StringBuilder("Hello2");
        if (dots++ == 3) {
            dots = 1;
        }
        for (int i = 0; i < dots; i++) {
            builder.append('.');
        }

        builder.append(Integer.toString(++count));
        String message = builder.toString();
        spittle.setMessage(message);
        rabbitTemplate.convertAndSend(queue.getName(), spittle);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
