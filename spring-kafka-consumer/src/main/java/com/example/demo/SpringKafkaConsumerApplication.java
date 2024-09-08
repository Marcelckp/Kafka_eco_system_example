package com.example.demo;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class SpringKafkaConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaConsumerApplication.class, args);
    }
    
    // We can create a kafka consumer that listens with this decorator and supplying the decorator arguments for subscribing and listening to the correct topic.
    @KafkaListener(id = "demo.group", topics = "spring-kafka-demo-topic")
    public void listener(String msg) {
        // We have the abilities to mutate the message as we please.
        System.out.println(msg.toUpperCase());
    }
}

