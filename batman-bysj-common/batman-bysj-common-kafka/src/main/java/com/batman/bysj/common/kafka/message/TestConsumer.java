package com.batman.bysj.common.kafka.message;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author victor.qin
 * @date 2018/4/24 18:25
 */
@Component
public class TestConsumer extends AbstractMessageConsumer<String>{
    public TestConsumer(Map<String, String> handlerMap) {
        super(handlerMap);
    }

    @Override
    public String getTopic() {
        return "test-topic";
    }

    @Override
    protected void handleMessage(String message, List<String> handlers) {

    }
}
