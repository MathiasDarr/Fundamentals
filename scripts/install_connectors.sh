#!/bin/bash
docker exec connect confluent-hub install  debezium/debezium-connector-postgresql:0.9.4 --no-prompt
docker restart connect
sleep 25
bash scripts/kafka/post.sh scripts/kafka/connectors/debezium_source_connector.json

