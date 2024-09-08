package com.example.demo;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Map;
import java.util.HashMap;

@Configuration
public class KafkaProducerConfig {

  @Bean
  public ProducerFactory<String, String> producerFactory() {
      Map<String, Object> configProps = new HashMap<>();

      // Setting up global producer config options for all producers in the spring application
      configProps.put(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
        "localhost:9092");
      configProps.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
        StringSerializer.class);
      configProps.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
        StringSerializer.class);

      // Return a factory kafka producer that will generate kafka producers with the necessary configurations
      return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
      return new KafkaTemplate<>(producerFactory());
  }
}
