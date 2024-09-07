package com.example;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Materialized;

public class KafkaStreamsOperations {

    static String INPUT_TOPIC = "input-topic";
    static String OUTPUT_TOPIC = "output-topic";

    static final String KAFKA_STREAMS_APP_ID = "kafka-streams-testing-app";
    
    static Topology filterTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> stream = builder.stream(INPUT_TOPIC);
        stream.filter((k, v) -> v.length() > 5).to(OUTPUT_TOPIC);

        return builder.build();
    }

}
