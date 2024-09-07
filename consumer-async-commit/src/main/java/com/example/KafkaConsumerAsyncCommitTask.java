package com.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class KafkaConsumerAsyncCommitTask {

    // Flag to indicate when to stop consuming
    private static volatile boolean isRunning = true;

    public static void main(String[] args) {

        String defaultTopicName = "input-topic";
        String defaultGroupName = "test-group-1";

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

        System.out.println("Started Kafka Consumer for Topic: "+ topicName + ", Consumer Group: " + groupName);


        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        try {
            while (isRunning) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Got message from topic=%s, partition=%d, offset=%d, key=%s, value=%s\n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                    
                    consumer.commitAsync(new OffsetCommitCallback() {
                        @Override
                        public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                            if (exception != null) {
                                System.out.println("Exception while committing offsets - "+ offsets);
                            } else {
                                for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {

                                    System.out.printf("Commit details: topic=%s, partition=%d, offset=%d\n",
                                    entry.getKey().topic(), entry.getKey().partition(), entry.getValue().offset());
                                }
                            }
                        }
                    });
                }
            }
        } finally {
            consumer.close();
        }
    }
}
