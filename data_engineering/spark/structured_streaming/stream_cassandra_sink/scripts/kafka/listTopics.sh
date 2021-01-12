#!/bin/bash
docker exec -it kafka bash bin/kafka-topics.sh --list --bootstrap-server kafka:9092
