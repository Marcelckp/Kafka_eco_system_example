package com.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class KafkaConsumerTask {

    // Flag to indicate when to stop consuming
    private static volatile boolean isRunning = true;
    
    public static void main(String[] args) {

        String defaultTopicName = "input-topic";
        String defaultGroupName = "test-group";

        String topicName = System.getenv("TOPIC_NAME") != null ? System.getenv("TOPIC_NAME") : defaultTopicName;
        String groupName = System.getenv("CONSUMER_GROUP_NAME") != null ? System.getenv("CONSUMER_GROUP_NAME") : defaultGroupName;

        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupName);
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping Kafka Consumer");
            isRunning = false;  // Set the flag to false when shutting down
        }));

        //enter solution here
    }

}
