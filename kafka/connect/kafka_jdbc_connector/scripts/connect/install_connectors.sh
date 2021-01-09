#!/bin/bash
docker exec connect confluent-hub install confluentinc/kafka-connect-jdbc:10.0.1 --no-prompt
docker restart connect
sleep 25

