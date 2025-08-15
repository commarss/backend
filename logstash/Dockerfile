FROM docker.elastic.co/logstash/logstash:8.17.2

USER root

# MySQL JDBC Driver 설치
RUN mkdir -p /opt/jdbc && \
    curl -L -o /opt/jdbc/mysql-connector.jar \
    https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar && \
    chmod 644 /opt/jdbc/mysql-connector.jar

# Pipeline 설정 파일을 기존 경로에서 복사
COPY src/main/resources/secret/pipeline/ /usr/share/logstash/pipeline/

# 모든 파일 권한을 한 번에 설정
RUN chown -R logstash:logstash /usr/share/logstash/ && \
    chmod -R 755 /usr/share/logstash/pipeline/

# logstash 사용자로 실행
USER logstash
