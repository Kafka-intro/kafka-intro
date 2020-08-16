package com.awesome.solutions;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class ConsumerStringString {
    public static void main(String[] args) {
        Properties consumerProperties = new Properties();

        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "robo-taxi-group-0");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(List.of("taxi-car-data"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));


            records.forEach(record -> {
                System.out.println("key= " + record.key() + "\nvalue= " + record.value());
                System.out.println(record.timestamp());
            });
        }

    }
}
