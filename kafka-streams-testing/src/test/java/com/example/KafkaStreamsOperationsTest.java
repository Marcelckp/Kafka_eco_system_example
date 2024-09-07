package com.example;


import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;

import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class KafkaStreamsOperationsTest {

    private TopologyTestDriver td;
    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> outputTopic;

    private Topology topology;
    private final Properties config;

    public KafkaStreamsOperationsTest() {

        config = new Properties();
        config.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, KafkaStreamsOperations.KAFKA_STREAMS_APP_ID);
        config.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    }

    @After
    public void tearDown() {
        td.close();
    }

    @Test()
    public void testFilterTopologyPass() {
        //enter solution here
    }

    @Test
    public void testFilterTopologyFail() {
        //enter solution here
    }
}
