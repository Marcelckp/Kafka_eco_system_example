package com.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class SchemaRegistryConsumerTask {

    // Flag to indicate when to stop consuming
    private static volatile boolean isRunning = true;

    private static final String TOPIC = "movies";

    public static void main(String[] args) {
        Properties props = new Properties();

        props.put(ConsumerConfig.GROUP_ID_CONFIG, "movies-cg");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        // This is a clean up function - A shut down hook is a hook that will be run when the JVM has been shut down.
        // We use this to stop the kafka consumer poll from running when the application has been shut down.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping Kafka Consumer");
            isRunning = false;  // Set the flag to false when shutting down
        }));
        
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        try (final KafkaConsumer<String, Movie> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(TOPIC));
            System.out.println("avro based consumer application started.....");

            while (isRunning) {
                final ConsumerRecords<String, Movie> records = consumer.poll(Duration.ofMillis(100));
                for (final ConsumerRecord<String, Movie> record : records) {
                    final String movieName = record.key();
                    final Movie movie = record.value();
                    System.out.printf("lead actor in %s is %s%n", movieName, movie.getLeadActor());
                }
            }
        }

    }

}
