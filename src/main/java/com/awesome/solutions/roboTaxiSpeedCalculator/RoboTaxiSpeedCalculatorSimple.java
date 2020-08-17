package com.awesome.solutions.roboTaxiSpeedCalculator;

import com.awesome.solutions.model.RoboTaxiPosition;
import com.awesome.solutions.model.RoboTaxiPositionWithSpeed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.util.*;

//works fine only if there is one car producing records to input topic
public class RoboTaxiSpeedCalculatorSimple {

    private static RoboTaxiPosition previous;
    private static RoboTaxiPosition current;

    private static ObjectMapper json = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        Properties consumerProperties = new Properties();

        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "robo-taxi-group-0");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(List.of("carsInfo"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
            ArrayList<ConsumerRecord<String, String>> recordsList = Lists.newArrayList(records.iterator());

            if (recordsList.size() == 1) {
                previous = current;
                current = json.readValue(recordsList.get(0).value(), RoboTaxiPosition.class);
            } else if (recordsList.size() > 1) {
                current = json.readValue(recordsList.get(recordsList.size() - 1).value(), RoboTaxiPosition.class);
                previous = json.readValue(recordsList.get(recordsList.size() - 2).value(), RoboTaxiPosition.class);
            }

            if (current != null && previous != null) {
                RoboTaxiPositionWithSpeed positionWithSpeed = positionWithSpeed(current, previous);
                System.out.println(positionWithSpeed);
            }
        }
    }

    public static RoboTaxiPositionWithSpeed positionWithSpeed(RoboTaxiPosition current, RoboTaxiPosition previous) {

        RoboTaxiPositionWithSpeed result = new RoboTaxiPositionWithSpeed();
        result.setCoordX(current.getCoordX());
        result.setCoordY(current.getCoordY());
        result.setDirection(current.getDirection());
        result.setTimestamp(current.getTimestamp());

        Double speed = 0d;
        if (previous.getDirection().equals(current.getDirection())) {
            Double distance = Point2D.distance(previous.getCoordX(), previous.getCoordY(), current.getCoordX(), current.getCoordY());
            speed = distance / (current.getTimestamp() - previous.getTimestamp());
        }

        result.setSpeed(speed);
        return result;
    }
}
