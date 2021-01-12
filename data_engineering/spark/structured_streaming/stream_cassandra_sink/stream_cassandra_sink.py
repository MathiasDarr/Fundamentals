import findspark

findspark.init()
import pyspark as ps
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.serialization import SerializationContext
from pyspark.sql.functions import udf, array
from pyspark.sql.types import StringType, DoubleType
from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider

windowSize = 5
slideSize = 3
windowDuration = '{} seconds'.format(windowSize)
slideDuration = '{} seconds'.format(slideSize)


def getSparkInstance():
    """
    @return: Return Spark session
    """
    # java8_location = '/usr/lib/jvm/java-8-openjdk-amd64' # Set your own
    # os.environ['JAVA_HOME'] = java8_location

    spark = ps.sql.SparkSession.builder \
        .master("local[4]") \
        .appName("individual") \
        .getOrCreate()
    return spark


spark = getSparkInstance()


def deserialize_avro_column_row(serialized_data):
    schema = '''
    {
    "namespace": "org.mddarr.rides.event.dto",
     "type": "record",
     "name": "AvroRideCoordinate",
     "fields": [
         {"name": "dataID", "type": "string"},
         {"name": "value", "type": "double"}
     ]
    }
    '''
    schemaRegistryClient = SchemaRegistryClient({"url": "http://localhost:8081"})
    avroDeserializer = AvroDeserializer(schema, schemaRegistryClient)
    serializationContext = SerializationContext("time-series", schema)
    deserialized_row = avroDeserializer(serialized_data, serializationContext)
    return deserialized_row['value']

    # def close(self, error):
    #     # Close the connection. This method in optional in Python.
    #     pass


streamingDF = spark \
    .readStream \
    .format("kafka") \
    .option("kafka.bootstrap.servers", "localhost:9092") \
    .option("subscribe", "time-series") \
    .option('includeTimestamp', 'true') \
    .load()

deserialize_row_udf = udf(lambda x: deserialize_avro_column_row(x), DoubleType())

deserialized_value_dataframe = streamingDF.withColumn('deserialized_value', deserialize_row_udf("value"))
deserialized_value_dataframe = deserialized_value_dataframe.select(['key', 'timestamp', 'deserialized_value'])

deserialized_value_dataframe.drop('value')
deserialized_value_dataframe = deserialized_value_dataframe.withColumnRenamed('deserialized_value', 'value')




#     .outputMode("append")\

class ForeachWriter:
    def open(self, partition_id, epoch_id):
        # Open connection. This method is optional in Python.
        pass

    def process(self, row):
        # Write row to connection. This method is NOT optional in Python.
        print("THE ROW LOOKS LIKE")

    def close(self, error):
        # Close the connection. This method in optional in Python.
        pass

def process_row(row):
    print("THE ROW LOOKS LIKE")
    print(row)


query = deserialized_value_dataframe.writeStream.foreach(ForeachWriter()).start()


ds = deserialized_value_dataframe \
  .writeStream \
  .foreach(process_row) \
  .trigger(processingTime="5 seconds") \
  .start()



# cassandra_sink_stream = deserialized_value_dataframe \
#     .writeStream \
#     .foreach(process_row)\
#     .format("console")\
#     .trigger(processingTime="5 seconds")\
#     .start()

spark.streams.awaitAnyTermination()
