package com.example;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
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

        //enter solution here

    }

}
