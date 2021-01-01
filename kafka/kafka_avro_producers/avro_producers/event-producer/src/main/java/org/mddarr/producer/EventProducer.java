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

    public static void populateTimeSeries() throws Exception{
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
        KafkaTemplate<String, AvroTimeSeriesDataPoint> rideRequestKafkaTemplate = new KafkaTemplate<>(pf1, true);
        rideRequestKafkaTemplate.setDefaultTopic(Constants.TIME_SERIES_TOPIC);
        List<String> keys = new ArrayList<>(Arrays.asList("proccess1", "proccess2", "proccess3"));

        Random rand = new Random();


        WeightedRandomBag<String> itemDrops = new EventProducer.WeightedRandomBag<>();

// Setup - a real game would read this information from a configuration file or database
        itemDrops.addEntry("10 Gold",  5.0);
        itemDrops.addEntry("Sword",   20.0);
        itemDrops.addEntry("Shield",  45.0);
        itemDrops.addEntry("Armor",   20.0);
        itemDrops.addEntry("Potion",  10.0);

        for (int i = 0; i < 20; i++) {
            System.out.println(itemDrops.getRandom());
        }


//        while(true){
//            int randomIndex = rand.nextInt(keys.size());
//            String random_process = keys.get(randomIndex);
//
//
////            System.out.println("Writing ride request for '" + rideRequest.getRequestId() + "' to input topic " +
////                    Constants.RIDE_REQUEST_TOPIC);
////            rideRequestKafkaTemplate.sendDefault(rideRequest);
//        }

    }
//
//
//    public static void populateDrivers() throws Exception{
//        final Map<String, String> serdeConfig = Collections.singletonMap(
//                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//        // Set serializers and
//        final SpecificAvroSerializer<AvroDriver> purchaseEventSerializer = new SpecificAvroSerializer<>();
//        purchaseEventSerializer.configure(serdeConfig, false);
//
//
//        Map<String, Object> props = new HashMap<>();
//        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ProducerConfig.RETRIES_CONFIG, 0);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, purchaseEventSerializer.getClass());
//
//
//        DefaultKafkaProducerFactory<String, AvroDriver> pf1 = new DefaultKafkaProducerFactory<>(props);
//        KafkaTemplate<String, AvroDriver> driverKafkaTemplate = new KafkaTemplate<>(pf1, true);
//        driverKafkaTemplate.setDefaultTopic(Constants.DRIVERS_TOPIC);
//
//        List<AvroDriver> drivers = DataService.getProductsFromDB();
//
//        drivers.forEach(driver -> {
//            System.out.println("Writing driver for '" + driver.getFirstname() + "' to input topic " +
//                    Constants.DRIVERS_TOPIC);
//            driverKafkaTemplate.sendDefault(driver);
//        });
//    }
//
//    public static void populateCoordinates() throws Exception{
//        final Map<String, String> serdeConfig = Collections.singletonMap(
//                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//        // Set serializers and
//        final SpecificAvroSerializer<AvroDriver> purchaseEventSerializer = new SpecificAvroSerializer<>();
//        purchaseEventSerializer.configure(serdeConfig, false);
//
//        Map<String, Object> props = new HashMap<>();
//        props.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ProducerConfig.RETRIES_CONFIG, 0);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, purchaseEventSerializer.getClass());
//
//
//        DefaultKafkaProducerFactory<String, AvroRideCoordinate> pf1 = new DefaultKafkaProducerFactory<>(props);
//        KafkaTemplate<String, AvroRideCoordinate> driverKafkaTemplate = new KafkaTemplate<>(pf1, true);
//        driverKafkaTemplate.setDefaultTopic(Constants.COORDINATES_TOPIC);
//
//        while(true){
//            AvroRideCoordinate avroRideCoordinate = new AvroRideCoordinate("ride1", 12.1, 12.0);
//            driverKafkaTemplate.sendDefault(avroRideCoordinate);
//            System.out.println("Writing ride coordinate for '" + avroRideCoordinate.getRideid() + "' to input topic " + Constants.COORDINATES_TOPIC);
//            Thread.sleep(3000);
//
//            avroRideCoordinate = new AvroRideCoordinate("ride2", 12.1, 12.0);
//            driverKafkaTemplate.sendDefault(avroRideCoordinate);
//            System.out.println("Writing ride coordinate for '" + avroRideCoordinate.getRideid() + "' to input topic " + Constants.COORDINATES_TOPIC);
//            Thread.sleep(3000);
//        }
//    }
}
