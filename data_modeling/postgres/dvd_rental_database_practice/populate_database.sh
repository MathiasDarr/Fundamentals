#!/bin/sh

#psql -U postgres -c 'create database if not exists postgresdb'

echo "import dvd rental data..."
pg_restore -U postgres -d postgresdb /tmp/dvdrental.tar

