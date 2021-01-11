# !/usr/bin/env python3

from utils.postgres_connection import get_postgres_connection


def create_accounts_table():
    create_accounts_table_statement = """
    drop table if exists accounts;

    create table accounts (
        id int generated by default as identity,
        name varchar(100) not null,
        balance dec(15,2) not null,
        primary key(id)
    );

    insert into accounts(name,balance)
    values('Bob',10000);

    insert into accounts(name,balance)
    values('Alice',10000);
    """
    cur.execute(create_accounts_table_statement)
    conn.commit()

def create_transfer_procedure():
    create_transfer_procedure_statemen = """
        create or replace procedure transfer(
           sender int,
           receiver int, 
           amount dec
        )
        language plpgsql    
        as $$
        begin
        -- subtracting the amount from the sender's account 
        update accounts 
        set balance = balance - amount 
        where id = sender;
    
        -- adding the amount to the receiver's account
        update accounts 
        set balance = balance + amount 
        where id = receiver;

    commit;
    end;$$
    """
    cur.execute(create_transfer_procedure_statemen)
    conn.commit()



if __name__ == '__main__':
    conn = get_postgres_connection('postgresdb')
    cur = conn.cursor()
    create_accounts_table()
    create_transfer_procedure()
