package com.example.demo;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    private static final String TOPIC = "spring-kafka-demo-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    //enter solution here
}
