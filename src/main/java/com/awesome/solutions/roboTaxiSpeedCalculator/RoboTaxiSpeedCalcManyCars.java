package com.awesome.solutions.roboTaxiSpeedCalculator;

import com.awesome.solutions.model.RoboTaxiPosition;
import com.awesome.solutions.model.RoboTaxiPositionWithSpeed;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

//works fine for any number of cars
public class RoboTaxiSpeedCalcManyCars {

    private static ObjectMapper json = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        Properties consumerProperties = new Properties();

        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "robo-taxi-group-0");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(List.of("carsInfo"));

        Map<String, RoboTaxiPositionWithSpeed> positionsWithSpeed = new HashMap<>();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
            //group consumer records by key
            Map<String, List<ConsumerRecord<String, String>>> groupedByKey =
                    StreamSupport.stream(records.spliterator(), false)
                            .collect(Collectors.groupingBy(ConsumerRecord::key));

            //for each <key, List<ConsumerRecord>> take 2 latest records
            //and calculate speed based on them
            //now it's possible for current and previous RoboTaxiPositions to only have equal car id
            for (Map.Entry<String, List<ConsumerRecord<String, String>>> entry : groupedByKey.entrySet()) {
                String key = entry.getKey();
                List<ConsumerRecord<String, String>> recordsList = entry.getValue();

                if (recordsList.size() >= 2) {
                    RoboTaxiPosition current = json.readValue(recordsList.get(recordsList.size() - 1).value(), RoboTaxiPosition.class);
                    RoboTaxiPosition previous = json.readValue(recordsList.get(recordsList.size() - 2).value(), RoboTaxiPosition.class);
                    RoboTaxiPositionWithSpeed positionWithSpeed = positionWithSpeed(current, previous);
                    positionsWithSpeed.put(key, positionWithSpeed);
                }
            }
            System.out.println(positionsWithSpeed);
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
