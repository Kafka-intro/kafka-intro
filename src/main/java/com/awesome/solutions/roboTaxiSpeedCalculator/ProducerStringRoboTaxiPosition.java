package com.awesome.solutions.roboTaxiSpeedCalculator;

import com.awesome.solutions.model.RoboTaxiPosition;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerStringRoboTaxiPosition {

    static ObjectMapper objectMapper = new ObjectMapper();
    private static String TOPIC = "tesla-robo-taxi-car-data";

    public static void main(String[] args) throws  Exception {

        Properties producerProperties = new Properties();

        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties)) {
            for (int i = 0; i < 10 ; i++) {
                RoboTaxiPosition record = new RoboTaxiPosition(0,5 + i*10, "W", System.currentTimeMillis());
                String jsonRecord = objectMapper.writeValueAsString(record);
                ProducerRecord producerRecord = new ProducerRecord(TOPIC, "id0", jsonRecord);
                producer.send(producerRecord);
                Thread.sleep(1000);
                System.out.println("send " );
            }



        }

    }
}
