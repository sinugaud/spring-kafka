package com.motadata.itsm;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import java.util.Properties;
import java.util.Arrays;
import java.util.regex.Pattern;

public class KafkaStreamsExample {
    public static void main(String[] args) {
        // Set up Kafka Streams properties
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        // Build the processing topology
        StreamsBuilder builder = new StreamsBuilder();
        // Read messages from the input topic
            KStream<String, String> textLines = builder.stream("input-topic");

        // Stateless processing: convert each message to uppercase
        KStream<String, String> uppercased = textLines.mapValues(value -> value.toUpperCase());
        // Write the transformed messages to a new topic
        uppercased.to("uppercased-topic");

        // Stateful processing: word count aggregation
        Pattern pattern = Pattern.compile("\\W+");
        KTable<String, Long> wordCounts = textLines
            .flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase())))
            .groupBy((key, word) -> word)
            .count(Materialized.as("CountsStore"));

        // Write the aggregated word counts to an output topic
        wordCounts.toStream().to("wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));

        // Start the Kafka Streams application
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        // Add shutdown hook for graceful exit
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        // ----- Interactive Querying -----
        // Wait a little for some data to be processed
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Query the state store ("CountsStore") for the current count of the word "kafka"
        ReadOnlyKeyValueStore<String, Long> keyValueStore =
            streams.store(StoreQueryParameters.fromNameAndType("CountsStore", QueryableStoreTypes.keyValueStore()));
        Long kafkaCount = keyValueStore.get("kafka");
        System.out.println("Current count for word 'kafka': " + kafkaCount);
    }
}
