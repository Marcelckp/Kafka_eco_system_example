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
        consumer.subscribe(Collections.singletonList(topicName));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping Kafka Consumer");
            isRunning = false;  // Set the flag to false when shutting down
        }));

        try {
            while (isRunning) {
                // Kafka consumers typically poll for messages from the Kafka broker. 
                // This means that the consumer requests batches of messages from the broker at regular intervals. 
                // The size of these batches can be configured, allowing consumers to pull messages one by one or in bulk.
                // For now every time we poll we will fetch all the records that were produced since the last time we polled for records.
                // The consumer will keep track of the message offset to prevent fetching records that have been shown/fetched already.
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                // Loop over the returned array of records to print out the information
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("topic=%s, partition=%d, offset=%d, key=%s, value=%s\n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }

}
