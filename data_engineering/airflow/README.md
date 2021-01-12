### Airflow Fundamentals ###

This directory contains demonstrations of fundamental Airflow skills ..


### Airflow Instructions (Assumes airflow 2.0.0 is installed) ###
airflow db init

airflow users create \
    --username airflow \
    --firstname airflow \
    --lastname airflow \
    --role Admin \
    --email mddarr@gmail.com

airflow webserver --port 9090
airflow scheduler

