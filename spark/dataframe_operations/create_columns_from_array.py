"""
This pyspark example makes use of the ArrayType indexing to create a new column for each of the two array indices.

"""



import findspark
findspark.init()
import pyspark as ps


def getSparkInstance():
    """
    @return: Return Spark session
    """
    spark = ps.sql.SparkSession.builder \
        .master("local[4]") \
        .appName("individual") \
        .getOrCreate()
    return spark


spark = getSparkInstance()


arrayData = [
        ('James',[12.0,12.1]),
        ('Erik', [19.0, 12.1])
]
df = spark.createDataFrame(data=arrayData, schema=['name','coordinates'])

df.printSchema()
df.show()


split_df = df.select(df.name, df.coordinates[0], df.coordinates[1])


split_df = df.selectExpr("name as name", "coordinates[0] as lat", "coordinates[1] as lng")
split_df.show()



# from pyspark.sql.functions import explode
# df2 = df.select(df.name,explode(df.knownLanguages))
# df2.printSchema()
# df2.show()
