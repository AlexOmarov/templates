FROM postgres:12-alpine

USER root

COPY init.sql /docker-entrypoint-initdb.d/
COPY init.sh  /docker-entrypoint-initdb.d/

RUN sed -i -e 's/\r$//' /docker-entrypoint-initdb.d/init.sh

USER postgres

RUN mkdir -p "/var/lib/postgresql/data" &&\
    initdb /var/lib/postgresql/data  &&\
    chmod 0700 /var/lib/postgresql/data &&\
    pg_ctl start
