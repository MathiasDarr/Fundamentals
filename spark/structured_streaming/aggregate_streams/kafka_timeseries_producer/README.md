## RideShare Microservices ##

This directory contains Microservices developed using Spring Boot, Kafka Streams, with Cassandra & Postgres used for data persistence 

## Microservices ##
* Ride Request Service
    * exposes REST endpoint for authenticated users to request ride
    * Uses kafka producer API to post ride request message to kafka
* Ride Coordinate Service
    * communicates with clients via web sockets 
    * receives timestamps & coordinates from client, producing to kafka with avro serializaiton
* Dispatch Service
    * stream processing service leveraging kafka cloud streams API with Kafka binder
    * Reads from drivers topic & ride requests topic
* Rides Query Service
    * exposes REST endpoints for querying the rides


## Running the Services ##