#!/bin/bash
docker exec kafka /bin/kafka-console-consumer --topic $1 --from-beginning --property print.key=true --bootstrap-server kafka:9092
