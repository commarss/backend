FROM alpine:latest as downloader
ARG MYSQL_DRIVER_URL=https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
ADD ${MYSQL_DRIVER_URL} /tmp/

FROM docker.elastic.co/logstash/logstash:8.17.2

COPY --chown=logstash:logstash --from=downloader /tmp/mysql-connector-j-8.0.33.jar /usr/share/logstash/logstash-core/lib/java/
COPY --chown=logstash:logstash src/main/resources/secret/pipeline/logstash.conf /usr/share/logstash/pipeline/
