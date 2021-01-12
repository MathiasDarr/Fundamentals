
CREATE TABLE hive_transactions (id INT, bar STRING, date1 STRING)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ';';

LOAD DATA LOCAL INPATH '/opt/hive/examples/files/transactions.txt' OVERWRITE INTO TABLE hive_transactions;