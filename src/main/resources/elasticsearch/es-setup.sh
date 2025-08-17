#!/bin/sh

# Elasticsearch가 healthy 상태가 될 때까지 대기
echo "Waiting for Elasticsearch to be healthy..."
until curl -s -f "http://elasticsearch:9200/_cluster/health?wait_for_status=yellow"; do
  printf '.'
  sleep 1
done

echo "Elasticsearch is up!"

# 인덱스 템플릿 생성
# URL과 옵션을 명확히 구분하여 가독성 향상
curl -X PUT "http://elasticsearch:9200/_index_template/restaurants_template" \
     -H "Content-Type: application/json" \
     -d "@/tmp/restaurants_template.json"

# curl 명령어의 성공 여부 확인
if [ $? -eq 0 ]; then
  echo "Index template created successfully."
else
  echo "Failed to create index template."
  exit 1
fi
