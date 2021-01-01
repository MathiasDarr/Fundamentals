package org.mddarr.producer;



import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;


import org.mddarr.rides.event.dto.AvroTimeSeriesDataPoint;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.sql.*;
import java.util.*;

public class EventProducer {
    static class WeightedRandomBag<T extends Object> {

        private class Entry {
            double accumulatedWeight;
            T object;
        }

        private List<Entry> entries = new ArrayList<>();
        private double accumulatedWeight;
        private Random rand = new Random();

        public void addEntry(T object, double weight) {
            accumulatedWeight += weight;
            Entry e = new Entry();
            e.object = object;
            e.accumulatedWeight = accumulatedWeight;
            entries.add(e);
        }

        public T getRandom() {
            double r = rand.nextDouble() * accumulatedWeight;

            for (Entry entry: entries) {
                if (entry.accumulatedWeight >= r) {
                    return entry.object;
                }
            }
            return null; //should only happen when there are no entries
        }
    }



    public static void main(String[] args) throws Exception {
        populateTimeSeries();
        // populateDrivers();
        // populateRideRequests();
    }

    public static void populateTimeSeries() throws Exception {
        final Map<String, String> serdeConfig = Collections.singletonMap(
                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        // Set serializers and
        final SpecificAvroSerializer<AvroTimeSeriesDataPoint> timeSeriesEventSerializer = new SpecificAvroSerializer<>();
        timeSeriesEventSerializer.configure(serdeConfig, false);
//
        Map<String, Object> props = new HashMap<>();
        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, timeSeriesEventSerializer.getClass());


        DefaultKafkaProducerFactory<String, AvroTimeSeriesDataPoint> pf1 = new DefaultKafkaProducerFactory<>(props);
        KafkaTemplate<String, AvroTimeSeriesDataPoint> timeSeriesKafkaTemplate = new KafkaTemplate<>(pf1, true);
        timeSeriesKafkaTemplate.setDefaultTopic(Constants.TIME_SERIES_TOPIC);
        List<String> keys = new ArrayList<>(Arrays.asList("proccess1", "proccess2", "proccess3"));

        Random rand = new Random();


        WeightedRandomBag<String> processes = new EventProducer.WeightedRandomBag<>();

        processes.addEntry("process1", 5.0);
        processes.addEntry("process2", 20.0);
        processes.addEntry("process3", 45.0);
        processes.addEntry("process4", 20.0);
        processes.addEntry("process5", 10.0);

//        put("process2",5000L);
//        put("process3",7500L);
//        put("process4",15000L);
//        put("process5",2000L);
        Map<String, List<Long>> process_average_value_and_standard_deviations = new HashMap<String, List<Long>>() {{
            put("process1", new ArrayList<>(Arrays.asList(1000L, 3L)));
            put("process2", new ArrayList<>(Arrays.asList(5000L, 10L)));
            put("process3", new ArrayList<>(Arrays.asList(1500L, 15L)));
            put("process4", new ArrayList<>(Arrays.asList(15L, 2L)));
            put("process5", new ArrayList<>(Arrays.asList(75L, 15L)));
        }};


        while (true) {
            String random_process = processes.getRandom();
            List<Long> average_and_mean = process_average_value_and_standard_deviations.get(random_process);
            Long average_value = average_and_mean.get(0);
            Long standard_deviation = average_and_mean.get(1);

            Double random = rand.nextGaussian() * standard_deviation + average_value;

            System.out.println("Writing ride request for '" + random_process + "' to input topic " + Constants.TIME_SERIES_TOPIC + "with value " + random);
            AvroTimeSeriesDataPoint avroTimeSeriesDataPoint = new AvroTimeSeriesDataPoint(UUID.randomUUID().toString(), random);
            timeSeriesKafkaTemplate.sendDefault(random_process, avroTimeSeriesDataPoint);

            Thread.sleep(100);
        }
    }
}
