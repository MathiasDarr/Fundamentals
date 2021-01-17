"""
Create & populate the ride coorinates table
"""
# !/usr/bin/env python3


import os
import csv
from utilities.cassandra_utilities import createCassandraConnection, createKeySpace


def populate_coordinates_table():
    create_ride_coordiantes_table = """CREATE TABLE IF NOT EXISTS coordinates(
        rideid text, 
        time bigint,
        latitude float,
        longitude float,
        PRIMARY KEY(rideid, time));
    """
    dbsession.execute(create_ride_coordiantes_table)

    insert_trip_data_point = """INSERT INTO coordinates(rideid, time, latitude, longitude) VALUES(%s,%s,%s,%s);"""


if __name__ == '__main__':
    dbsession = createCassandraConnection()
    createKeySpace("ks1", dbsession)
    try:
        dbsession.set_keyspace('ks1')
    except Exception as e:
        print(e)

    populate_coordinates_table()
    print("CASSANDRA COORDINATE TABLE CREATED")
