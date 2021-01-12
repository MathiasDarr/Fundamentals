#!/bin/bash

docker exec kafka bin/kafka-console-consumer.sh --topic $1 --from-beginning  --property print.key=true --bootstrap-server kafka:9092  --property schema.registry.url=http://schema-registry:8081

#docker exec kafka bin/kafka-console-consumer.sh $1 -- --value-format avro --from-beginning --property print.key=true
