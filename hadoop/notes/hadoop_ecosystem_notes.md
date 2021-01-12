### Hadoop Ecosystem ###

This directory contains examples of using vaerious tools in the hadoop ecosystem


### HBase ###
* Hadoop was not originally meant to efficiently handle transactions, particularly writes to HDFS.  HBase attempts to give Hadoop the ability to make fast, transactional writes to its filesystem.  Takes advantage of Hadoop's distributed computing environement to build a durable consistent, yet high performing database.  

* HBase is a wide column key-value store 
* Each row has a specific key known as a rowkey.
* data in the row are represented by columns, which are grouped together into column families.  Columns within a column famil yare stored physically close to each other.