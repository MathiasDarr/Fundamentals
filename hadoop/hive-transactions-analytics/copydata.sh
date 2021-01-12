#!/bin/bash

docker cp trans.txt hive_server:/opt/hive/examples/files/transactions.txt
docker cp transactions_hive_script.hql hive_server:/opt/hive/examples/files/transactions_hive_script.hql
