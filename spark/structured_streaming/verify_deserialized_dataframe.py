import findspark
findspark.init()
import pyspark as ps
import os
from utilities.dataframe_utilities import read_dataframe_from_parquet


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
parquet_file = os.listdir('data/')[0]

dataframe = read_dataframe_from_parquet(spark,'data/part-00000-2eac8a4b-8d6f-4981-ba13-f5301af80bad-c000.snappy.parquet')


