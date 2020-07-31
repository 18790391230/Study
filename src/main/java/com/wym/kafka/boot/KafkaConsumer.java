package com.wym.kafka.boot;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *
 */
@Component
public class KafkaConsumer {


    @KafkaListener(topics = "test-topic")
    public void process(ConsumerRecord record) {
        Optional.ofNullable(record).ifPresent(System.out::println);
    }
}
