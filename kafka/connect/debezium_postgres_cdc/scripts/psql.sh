#!/bin/bash
#docker exec -it postgres psql 'dbname=postgresdb user=postgres options=--search_path=snowpack'
docker exec -it postgres psql 'dbname=postgresdb user=postgres'