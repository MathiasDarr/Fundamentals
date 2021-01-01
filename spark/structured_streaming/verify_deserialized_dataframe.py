import findspark
findspark.init()
import pyspark as ps
import os
from utilities.dataframe_utilities import read_dataframe_from_parquet
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.avro import AvroDeserializer
from confluent_kafka.serialization import SerializationContext
from pyspark.sql.functions import udf
from pyspark.sql.types import DoubleType


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


spark = getSparkInstance()
parquet_file = os.listdir('data/')[0]

dataframe = read_dataframe_from_parquet(spark,'data/part-00000-45f3488f-8d9a-4cb7-b68b-a1deb9a3bb28-c000.snappy.parquet')

deserialize_row_udf = udf(lambda x: process_row(x), DoubleType())

deserialized_value_dataframe = dataframe.withColumn('deserialized_value', deserialize_row_udf(dataframe['value']))
