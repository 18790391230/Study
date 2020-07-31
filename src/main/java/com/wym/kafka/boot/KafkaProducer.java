package com.wym.kafka.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> template;

    public void send() {
        template.send("test-topic", "来自spring-boot的消息");
    }
}
