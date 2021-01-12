from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider


def createCassandraConnection():
    auth_provider = PlainTextAuthProvider(username='cassandra', password='cassandra')
    try:
        cluster = Cluster(["127.0.0.1"], auth_provider=auth_provider)
        session = cluster.connect()
        return session
    except Exception as e:
        print(e)
        return None


def createKeySpace(keyspace_name, session):
    # Create a Keyspace
    try:
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + keyspace_name + " WITH REPLICATION =  { 'class' : 'SimpleStrategy', 'replication_factor' : 1 } AND durable_writes='true'")
    except Exception as e:
        print(e)


def create_time_series_table():
    create_time_series_table = """CREATE TABLE IF NOT EXISTS time_series(
        processID text, 
        time timestamp,
        value double,
        PRIMARY KEY(processID, time));
    """
    dbsession.execute(create_time_series_table);


if __name__ == '__main__':
    dbsession = createCassandraConnection()
    createKeySpace("ks1", dbsession)
    try:
        dbsession.set_keyspace('ks1')
    except Exception as e:
        print(e)

    create_time_series_table()
    print("CASSANDRA TIME SERIES TABLE HAS BEEN CREATED")



