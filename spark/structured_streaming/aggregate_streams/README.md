## Aggregate Streams ##
This directory contains a spark structured streaming application.  Random time series data (pulled from normal distributions) are written to kafka 'time-series
 topic with five possible keys (each with a normal distribution of values with different mean & standard deviation)

## Running the application ##
* ensure that spark 3.0.1 is installed
* 
* compile the java kafka producer & 
    - cd kafka_timeseries_producer
    - mvn clean package
    - java -jar  
    
* run the kafka to avro producer to write time series data to kafka , randomly select one of five keys, each of which
has a different normal distribution of values.
    - java -jar avro_producers/event-producer/target/original-event-producer-0.1.0-SNAPSHOT.jar (from the kafka_timeseries_producer directory)
      
* submit the spark submit job (ctrl-c to terminate)
    * bash spark_submit.sh stream_aggregate.py
    
* verify that deserialized data is arriving in the kafka sink topic 
    * bash scripts/kafka/console_consumer.sh time-series-out