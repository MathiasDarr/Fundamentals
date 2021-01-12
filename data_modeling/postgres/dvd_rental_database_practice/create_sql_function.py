# !/usr/bin/env python3

from utils.postgres_connection import get_postgres_connection


def create_function():
    create_function = """
        create function get_count_of_films(len_from int, len_to int)
        returns int
        language plpgsql
        as
        $$
        declare
           film_count integer;
        begin
           select count(*) 
           into film_count
           from film
           where length between len_from and len_to;
           
           return film_count;
        end;
    $$;
    """
    cur.execute(create_function)
    conn.commit()




if __name__ =='__main__':
    conn = get_postgres_connection('postgresdb')
    cur = conn.cursor()
    create_function()
