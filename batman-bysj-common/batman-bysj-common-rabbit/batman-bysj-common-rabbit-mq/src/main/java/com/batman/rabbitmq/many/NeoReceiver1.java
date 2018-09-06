package com.batman.rabbitmq.many;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RabbitListener(queues = "neo")
public class NeoReceiver1 {

    @RabbitHandler
    public void process(String neo) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Receiver 1: " + neo);
    }

}
