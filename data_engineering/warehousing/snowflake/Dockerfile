FROM ubuntu:18.04

WORKDIR /app

ENV LC_ALL=C.UTF-8
ENV LANG=C.UTF-8

# Update apt and get curl
RUN apt-get update && apt-get install -y curl

# Get SnowSQL
RUN curl http://s3-us-west-2.amazonaws.com/sfc-snowsql-updates/bootstrap/1.1/linux_x86_64/snowsql-1.1.76-linux_x86_64.bash && \
    -o snowsql-1.1.76-linux_x86_64.bash && \
  touch /app/.profile && \
  SNOWSQL_DEST=/app/ SNOWSQL_LOGIN_SHELL=/app/.profile bash snowsql-1.1.76-linux_x86_64.bash && \
  rm -f snowsql-1.1.76-linux_x86_64.bash

# Let it do its first-time post-install install
RUN /app/snowsql
