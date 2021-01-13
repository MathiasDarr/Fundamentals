from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider


def createCassandraConnection():
    auth_provider = PlainTextAuthProvider(username='cassandra', password='cassandra')
    try:
        cluster = Cluster(["127.0.0.1"], auth_provider=auth_provider)

        # To establish connection and begin executing queries, need a session
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



def create_cassandra_table():
    create_table = """
    CREATE TABLE IF NOT EXISTS UDF_Function_test 
        (
        function_id int,
        num1 int,
        num2 int,
        PRIMARY KEY(function_id)
        );  
    """
    dbsession.execute(create_table)

def populate_cassandra_table():
    populate_table = """
    INSERT INTO UDF_Function_test(function_id, num1, num2)  VALUES(101, 400, 600);
    """
    dbsession.execute(populate_table)
    populate_table = """
    INSERT INTO UDF_Function_test(function_id, num1, num2)  VALUES(102, 500, 700);
    """
    dbsession.execute(populate_table)

    populate_table = """
    INSERT INTO UDF_Function_test(function_id, num1, num2)  VALUES(103, 450, 680);
    """
    dbsession.execute(populate_table)

    populate_table = """
    INSERT INTO UDF_Function_test(function_id, num1, num2)  VALUES(104, 700, 800);
    """
    dbsession.execute(populate_table)


def create_user_defined_function():
    create_udf = """
    CREATE OR REPLACE FUNCTION Max_value(value1 int, value2 int)
    CALLED ON NULL INPUT
    RETURNS int
    LANGUAGE java
    AS 'return Math.max(value1, value2);';
    """
    dbsession.execute(create_udf)


def call_user_defined_function():
    call_udf = """
    SELECT function_id, num1, num2, Max_value(num1, num2) 
    FROM UDF_Function_test WHERE function_id IN(101, 102, 103); 
    """
    results = dbsession.execute(call_udf)
    for r in results:
        print(r)


if __name__ == '__main__':
    dbsession = createCassandraConnection()
    createKeySpace("ks1", dbsession)
    try:
        dbsession.set_keyspace('ks1')
    except Exception as e:
        print(e)

    create_cassandra_table()
    populate_cassandra_table()
    create_user_defined_function()
    call_user_defined_function()
