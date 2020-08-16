package com.awesome.solutions;

import com.awesome.solutions.model.RoboTaxiPosition;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Future;

public class ProducerStringString {

    static ObjectMapper objectMapper = new ObjectMapper();

    private static String TOPIC = "taxi-car-data";

    public static void main(String[] args) throws Exception {

        Properties producerProperties = new Properties();

        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties)) {
            RoboTaxiPosition record = new RoboTaxiPosition(0,5, "W", 1591613136l );
            String jsonRecord = objectMapper.writeValueAsString(record);
            ProducerRecord<String, String> producerRecord = new ProducerRecord(TOPIC, "id" + new Random().nextInt(50), jsonRecord);
            Future send = producer.send(producerRecord);
            System.out.println(send.get());
        }

    }
}
