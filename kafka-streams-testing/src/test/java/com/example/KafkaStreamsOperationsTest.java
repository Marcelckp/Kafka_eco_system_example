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
        topology = KafkaStreamsOperations.filterTopology();
        td = new TopologyTestDriver(topology, config);

        inputTopic = td.createInputTopic(KafkaStreamsOperations.INPUT_TOPIC, Serdes.String().serializer(), Serdes.String().serializer());
        outputTopic = td.createOutputTopic(KafkaStreamsOperations.OUTPUT_TOPIC, Serdes.String().deserializer(), Serdes.String().deserializer());

        assertThat(outputTopic.isEmpty(), is(true));

        inputTopic.pipeInput("user1", "new york");
        // When we read the value we consume it and move the consumers offset and will leave the output topic as empty
        assertThat(outputTopic.readValue(), equalTo("new york"));
        assertThat(outputTopic.isEmpty(), is(true));

        inputTopic.pipeInput("user2", "texas");
        assertThat(outputTopic.isEmpty(), is(true));
    }

    @Test
    public void testFilterTopologyFail() {
        topology = KafkaStreamsOperations.filterTopology();
        td = new TopologyTestDriver(topology, config);

        inputTopic = td.createInputTopic(KafkaStreamsOperations.INPUT_TOPIC, Serdes.String().serializer(), Serdes.String().serializer());
        outputTopic = td.createOutputTopic(KafkaStreamsOperations.OUTPUT_TOPIC, Serdes.String().deserializer(), Serdes.String().deserializer());

        assertThat(outputTopic.isEmpty(), is(true));

        inputTopic.pipeInput("KeyLongerThan5", "texastexasToLong");
        // We check that our message made it to the output task as the key and value are larger than 5. We check that our topic is not empty then we consume it and check the value
        assertThat(outputTopic.isEmpty(), is(false));
        assertThat(outputTopic.readValue(), equalTo("texastexasToLong"));
    }
}
