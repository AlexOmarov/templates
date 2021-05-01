FROM yandex/clickhouse-server:latest

COPY init.sql /docker-entrypoint-initdb.d/
COPY init.sh  /docker-entrypoint-initdb.d/

COPY user.xml /etc/clickhouse-server/users.d/
COPY config.xml /etc/clickhouse-server/config.d/

RUN sed -i -e 's/\r$//' /docker-entrypoint-initdb.d/init.sh

RUN /etc/init.d/clickhouse-server start
