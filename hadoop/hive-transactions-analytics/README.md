### Hive Transactions Analytics ###

This directory contains an example of uploading data into a hive table.

### Reproducing the example ###
* start hive in docker
    * docker-compose -f hive-compose.yaml up
* start interactive beeline session
    * docker exec -it hive_server /opt/hive/bin/beeline -u jdbc:hive2://localhost:10000 
* copy data, .hql & create/populate table (or just run 'bash copydata.sh' script)
    * docker cp trans.txt hive_server:/opt/hive/examples/files/transactions.txt
    * docker cp transactions_hive_script.hql hive_server:/opt/hive/examples/files/transactions_hive_script.hql
    * docker exec hive_server /opt/hive/bin/beeline -u jdbc:hive2://localhost:10000 -f /opt/hive/examples/files/transactions_hive_script.hql



CREATE TABLE parsed_transcations (id INT, bar STRING, created_date TIMESTAMP)
ROW FORMAT SERDE 'org.apache.hadoop.hive.contrib.serde2.MultiDelimitSerDe'
WITH SERDEPROPERTIES (
    "field.delim"="<=>",
    "collection.delim"=":",
    "mapkey.delim"="@"
);


