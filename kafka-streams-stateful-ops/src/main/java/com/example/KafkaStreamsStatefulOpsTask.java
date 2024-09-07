package com.example;

import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindowedKStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.kstream.WindowedSerdes;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class KafkaStreamsStatefulOpsTask {

  public static void main(String[] args) {

    Properties configurations = new Properties();
    configurations.put(StreamsConfig.APPLICATION_ID_CONFIG, "counts-app");
    configurations.put(StreamsConfig.APPLICATION_SERVER_CONFIG, "localhost:8080");
    configurations.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configurations.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    configurations.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> source = builder.stream("input-topic");

    // This will take the key and count how many times the key has been received in the stream and parse the key to a hash table to keep track
    // Of the amount of times that specific key has come through the data pipeline
    source.groupByKey()
      // Counts the number of records for each key. Each key will be linked to a count
      .count()
      // This converts the table into  a stream that will contain records where the key is the original key and the value passed is the time the value has appeared
      .toStream()
      // This outputs that value to the output-topic so that we can view our data in its end state
      .to("output-topic", Produced.with(Serdes.String(), Serdes.Long()));

    KafkaStreams app = new KafkaStreams(builder.build(), configurations);

    app.start();
    System.out.println("Started kafka streams (counts) application");


  }

}
