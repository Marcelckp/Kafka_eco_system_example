package com.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class SchemaRegistryProducerTask {

    private static final String TOPIC = "movies";
    
    public static void main(String[] args) {

        Map<String, String> moviesActorMap = new HashMap<>();

        moviesActorMap.put("the accountant", "ben affleck");
        moviesActorMap.put("bird box", "sandra bullock");
        moviesActorMap.put("shutter island", "leonardo di caprio");
        moviesActorMap.put("john wick", "keanu reeves");
        moviesActorMap.put("nobody", "bob odenkirk");

        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        // To see the production of this producer we need to open a topic view of the movies topic
        Producer<String, String> producer = new KafkaProducer<>(props);

        try {
            for (Map.Entry<String, String> movie : moviesActorMap.entrySet()) {
                String key = movie.getKey();
                String val = movie.getValue();
                ProducerRecord<String, String> record = new ProducerRecord<>(SchemaRegistryProducerTask.TOPIC, key, val);

                producer.send(record);

                Thread.sleep(1000);
            }   


            producer.flush();
            System.out.println("sent all messages to kafka topic");
        } catch (final SerializationException e) {
            e.printStackTrace();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

}
