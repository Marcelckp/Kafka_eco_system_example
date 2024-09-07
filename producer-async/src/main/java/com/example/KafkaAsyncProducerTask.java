package com.example;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class KafkaAsyncProducerTask {

  public static void main(String[] args) throws Exception {
    String topicName = "input-topic";

    System.out.println("Executing asynchronous Kafka producer");

    Properties properties = new Properties();
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");

    Producer<String, String> producer = new KafkaProducer<>(properties);

    CountDownLatch counter = new CountDownLatch(5);

    //enter the solution
    for (int i = 0; i < 5; i++) {
      String value = "async-loop-message-" + i;
      String key = "async-kafka-msg-" + i;
      ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, value);
    
      producer.send(record, new Callback() {
        @Override
        public void onCompletion(RecordMetadata metadata, Exception e) {
          if (e != null) {
            System.out.println("Error sending data " + key + e.getMessage());
          } else {
            System.out.println("Produced data: " + key);
          }
          counter.countDown();
        }
      });

      Thread.sleep(1000);
    }

    counter.await();

    producer.flush();
    producer.close();

    System.out.println("End of program");
  }
}
