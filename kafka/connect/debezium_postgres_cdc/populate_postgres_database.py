"""
Create & populate users, drivers, ride requests & rides relation data model
"""
# !/usr/bin/env python3
from utils.postgres_connection import get_postgres_connection
import uuid


def create_users_table():
    create_users_table = """
            CREATE TABLE IF NOT EXISTS users(
                    userid VARCHAR(50) PRIMARY KEY
            );
    """
    cur.execute(create_users_table)
    conn.commit()


def populate_users_table():
    insert_into_users_table = """INSERT INTO users(userid) VALUES(%s);"""

    cur.execute(insert_into_users_table, [str(uuid.uuid4())])
    conn.commit()


if __name__ =='__main__':
    conn = get_postgres_connection('postgresdb')
    cur = conn.cursor()

    create_users_table()
    populate_users_table()
    print("THE POSTGRES DATABASE HAS BEEN SEEDED.")
