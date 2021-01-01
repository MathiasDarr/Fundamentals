# PYSPARK STRUCTURED STREAMING EXAMPLES #
This directory contains examples of spark structured streaming and pyspark.

## PyYSPARK EXAMPLES IN THIS DIRECTORY ##
* [Aggregate Streams](aggregate_streams/README.md)
    * produce records to kafka with avro serialization with various keys i.e process1, process2 etc & a value of type double
    * consume records using spark structured streaming
        - confluent_kafka python library deserializes avro messages from the schema registry
        - before a group by operation on the key value and aggregate with average value  

java -jar avro_producers/event-producer/target/event-producer-0.1.0-SNAPSHOT.jar 

