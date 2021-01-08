
"""
Create & populate users, drivers, ride requests & rides relation data model
"""
# !/usr/bin/env python3
from utils.postgres_connection import get_postgres_connection
import uuid


def create_bank_schema_and_holdings_table():
    create_schema_statement = """
    CREATE SCHEMA bank;
    SET search_path TO bank,public;
    CREATE TABLE bank.holding (
        holding_id int,
        user_id int,
        holding_stock varchar(8),
        holding_quantity int,
        datetime_created timestamp,
        datetime_updated timestamp,
        primary key(holding_id)
    );
    ALTER TABLE bank.holding replica identity FULL;
    insert into bank.holding values (1000, 1, 'VFIAX', 10, now(), now());

    """
    cur.execute(create_schema_statement)
    conn.commit()


if __name__ =='__main__':
    conn = get_postgres_connection('postgresdb')
    cur = conn.cursor()
    create_bank_schema_and_holdings_table()
    print("THE POSTGRES DATABASE HAS BEEN SEEDED.")
