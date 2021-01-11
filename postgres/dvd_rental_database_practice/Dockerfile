FROM postgres:12
COPY data/dvdrental.tar /tmp/dvdrental.tar
COPY populate_database.sh /docker-entrypoint-initdb.d/
