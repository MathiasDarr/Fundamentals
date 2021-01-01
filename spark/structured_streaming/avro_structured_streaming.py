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

def process_row(row):
    # print("THE ROW LOOKS LIKE " + str(row))
    # print(row.asDict().keys())
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
    # print("THE ROW LOOKS LIKE")
    # print(row)
    schemaRegistryClient = SchemaRegistryClient({"url": "http://localhost:8081"})
    avroDeserializer = AvroDeserializer(schema, schemaRegistryClient)
    serializationContext = SerializationContext("time-series", schema)
    deserialized_row = avroDeserializer(row.value, serializationContext)
    # print("DESERIALIZED ROW")
    # print(str(deserialized_row))


def proces_grouped_by_dataframe(row):
    print("THE ROW LOOKS LIKE")
    print(row)


# `from_avro` requires Avro schema in JSON string format.

streamingDF = spark\
  .readStream\
  .format("kafka")\
  .option("kafka.bootstrap.servers", "localhost:9092")\
  .option("subscribe", "time-series")\
  .option('includeTimestamp', 'true')\
  .load()


# streamingDataFrameGroupedByKeyCount = streamingDF \
#     .withWatermark("timestamp", "1 minutes") \
#     .groupBy(F.col("key"), "timestamp") \
#     .count()

windowedCounts = streamingDF.groupBy(
    window(streamingDF.timestamp, windowDuration, slideDuration),
    streamingDF.key
).count().orderBy('window')


windowedDF = windowedCounts\
  .writeStream \
    .outputMode('complete') \
    .format('console') \
    .option('truncate', 'false') \
    .start()

# avroDF = streamingDF\
#   .writeStream\
#   .format("kafka")\
#   .foreach(proces_grouped_by_dataframe)\
#   .start()





# avroDF = streamingDF\
#   .writeStream\
#   .format("kafka")\
#   .foreach(process_row)\
#   .option("checkpointLocation", "checkpoints")\
#   .option("kafka.bootstrap.servers", "localhost:9092")\
#   .option("topic", "topic2")\
#   .start()


spark.streams.awaitAnyTermination()