#!/bin/bash
#$SPARK_HOME/bin/spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.0.1 org.apache.spark:spark-avro_2.12:3.0.1  spark_streams_kafka_integration.py
$SPARK_HOME/bin/spark-submit --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.0.1 $1