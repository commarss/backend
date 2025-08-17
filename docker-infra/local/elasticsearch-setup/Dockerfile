FROM curlimages/curl:8.7.1

USER root

# 3. 파일 복사 및 권한 변경
COPY es-setup.sh /usr/local/bin/es-setup.sh
RUN chmod +x /usr/local/bin/es-setup.sh

# 템플릿 파일도 복사
COPY restaurants_template.json /tmp/restaurants_template.json

USER curl_user

CMD ["/usr/local/bin/es-setup.sh"]
