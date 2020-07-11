package com.wym.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.text.MessageFormat;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.166:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int i = 10; i < 20; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("test-topic", MessageFormat.format("我是第{0}个消息", i));
            RecordMetadata recordMetadata = producer.send(record).get();
            System.out.println("recordMetadata=" + recordMetadata);
        }
        producer.close();
    }
}
