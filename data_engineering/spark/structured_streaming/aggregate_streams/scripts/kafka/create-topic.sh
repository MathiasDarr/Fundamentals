#!/bin/bash

docker exec -it kafka bash bin/kafka-topics.sh --create --bootstrap-server kafka:9092 --replication-factor 1 --partitions 1 --topic $1
