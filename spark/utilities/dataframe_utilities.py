import findspark
findspark.init()


def read_dataframe_from_parquet(spark, directory):
    # using SQLContext to read parquet file
    return spark.read.parquet(directory)