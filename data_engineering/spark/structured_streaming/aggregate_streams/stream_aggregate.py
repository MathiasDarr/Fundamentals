import findspark
findspark.init()
import pyspark as ps
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.serialization import SerializationContext
from pyspark.sql import functions as F
from pyspark.sql.functions import udf, array
from pyspark.sql.types import DoubleType, StringType
from pyspark.sql.functions import explode
from pyspark.sql.functions import split
from pyspark.sql.functions import window

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
    return str(deserialized_row['value'])


def proces_grouped_by_dataframe(row):
    print("THE ROW LOOKS LIKE")
    print(row)


def process_deserialized_row(row):
    print("THE DESERIALIZED ROW LOOKS LIKE ")
    print(row)


streamingDF = spark\
  .readStream\
  .format("kafka")\
  .option("kafka.bootstrap.servers", "localhost:9092")\
  .option("subscribe", "time-series")\
  .option('includeTimestamp', 'true')\
  .load()

# windowedCounts = streamingDF.groupBy(
#     window(streamingDF.timestamp, windowDuration, slideDuration),
#     streamingDF.key
# ).count().orderBy('window')

deserialize_row_udf = udf(lambda x: process_row(x), StringType())

deserialized_value_dataframe = streamingDF.withColumn('deserialized_value', deserialize_row_udf("value"))
deserialized_value_dataframe = deserialized_value_dataframe.select(['key','timestamp','deserialized_value'])

deserialized_value_dataframe.drop('value')
deserialized_value_dataframe = deserialized_value_dataframe.withColumnRenamed('deserialized_value', 'value')

# windowedCounts = deserialized_value_dataframe \
#     .withWatermark("timestamp", "5 seconds")\
#     .groupBy(window(streamingDF.timestamp, windowDuration, slideDuration),deserialized_value_dataframe.key)\
#     .agg(F.avg("deserialized_value"))\

windowedCounts = deserialized_value_dataframe.groupBy(
    window(deserialized_value_dataframe.timestamp, windowDuration, slideDuration),
    deserialized_value_dataframe.key)\
    .count()

# \.agg(F.avg("deserialized_value"))


# windowedCounts = deserialized_value_dataframe.groupBy(
#     window(deserialized_value_dataframe.timestamp, "10 minutes", "5 minutes"),deserialized_value_dataframe.key)).count()
#
#

def print_row(row):
    print("THE ROW LOOKS LIKE")
    print(row)

#   .selectExpr("value", "CAST(key AS STRING)", "CAST(deserialized_value AS DOUBLE)") \
ds = deserialized_value_dataframe \
  .selectExpr("value", "CAST(key AS STRING)", "CAST(value AS STRING)") \
  .writeStream \
  .format("kafka") \
  .option("checkpointLocation","checkpoints")\
  .option("kafka.bootstrap.servers", "localhost:9092") \
  .option("topic","time-series-out")\
  .start()

# windowedCounts.writeStream\
#     .foreach(print_row)\
#     .format("console")\
#     .outputMode("update")\
#     .option("path", "data")\
#     .option("checkpointLocation","checkpoints")\
#     .trigger(processingTime="5 seconds")\
#     .start()

spark.streams.awaitAnyTermination()