import findspark
findspark.init()
import pyspark as ps
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.serialization import SerializationContext
from pyspark.sql import functions as F

from pyspark.sql.functions import explode
from pyspark.sql.functions import split
from pyspark.sql.functions import window

windowSize = 10
slideSize = 5
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

def process_row(serialized_data):
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
    # print("DESERIALIZED ROW")
    # print(str(deserialized_row))
    return deserialized_row['value']


def proces_grouped_by_dataframe(row):
    print("THE ROW LOOKS LIKE")
    print(row)

def process_deserialized_row(row):
    print("THE DESERIALIZED ROW LOOKS LIKE ")
    print(row)

# `from_avro` requires Avro schema in JSON string format.

streamingDF = spark\
  .readStream\
  .format("kafka")\
  .option("kafka.bootstrap.servers", "localhost:9092")\
  .option("subscribe", "time-series")\
  .option('includeTimestamp', 'true')\
  .load()


windowedCounts = streamingDF.groupBy(
    window(streamingDF.timestamp, windowDuration, slideDuration),
    streamingDF.key
).count().orderBy('window')

print("THE TYPE IS")
print(type(streamingDF))


from pyspark.sql.functions import udf, array
from pyspark.sql.types import DoubleType

# deserialize_row_udf = udf(lambda x: process_row(x), DoubleType())
#
# deserialized_value_dataframe = streamingDF.withColumn('deserialized_value', deserialize_row_udf(streamingDF['value']))


print("THE TYPE OF deserialized_value_dataframe  ")
print(type(streamingDF))

streamingDF.writeStream\
    .format("parquet")\
    .outputMode("append")\
    .option("path", "data")\
    .option("checkpointLocation","checkpoints")\
    .trigger(processingTime="5 seconds")\
    .start()

# deserialized_value_dataframe.writeStream \
#     .foreach(process_deserialized_row) \
#     .start()


# # deserializedDF = streamingDF\
# #   .writeStream\
# #   .foreach(process_row)\
# #   .start()
# #
#

#
#
# deserializedStream = deserialized_value_dataframe\
#   .writeStream()\

#
# # outputDF = deserializedDF\
# #   .writeStream\
# #   .foreach(process_deserialized_row)\
# #   .start()
# #
spark.streams.awaitAnyTermination()