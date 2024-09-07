package com.example;


import java.util.Properties;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.kafka.common.serialization.Serdes;

public class KafkaStreamsStatelessOpsTask {

  public static void main(String[] args) {
    Properties config = new Properties();

    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "stateless-ops-task");
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> input = builder.stream("input-topic");

    //enter solution here

    Topology topology = builder.build();
    KafkaStreams streams = new KafkaStreams(topology, config);

    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    System.out.println("started kafka streams app.....");

    streams.start();
  }
}
