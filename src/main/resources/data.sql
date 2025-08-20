INSERT INTO member (auth_type, email, name, phone_number, profile_image_url, birth_date, gender)
VALUES ('KAKAO', 'hong@example.com', '홍길동', '010-1234-5678', 'https://example.com/profiles/hong.jpg',
        '1995-05-10 00:00:00', 1),
       ('GOOGLE', 'lee@example.com', '이순신', '010-8765-4321', 'https://example.com/profiles/lee.png',
        '1988-11-20 00:00:00',
        1),
       ('NAVER', 'yu@example.com', '유관순', '010-1111-2222', 'https://example.com/profiles/yu.gif', '2001-03-01 00:00:00',
        2);

INSERT INTO restaurant (name,
                        details,
                        average_rate,
                        image_url,
                        contact,
                        address,
                        lat,
                        lon,
                        running_state,
                        summarized_review,
                        restaurant_category)
VALUES ('맛있는 한식당', '전통 한식을 맛볼 수 있는 정통 한식당입니다. 신선한 재료로 만든 집밥 같은 맛을 제공합니다.',
        4.5, 'https://example.com/images/korean_restaurant1.jpg', '02-123-4567',
        '서울특별시 강남구 테헤란로 123', 37.5665, 126.9780, true,
        '친절한 서비스와 정갈한 한식이 인상적입니다.', '한식'),

       ('중화루', '50년 전통의 중화요리 전문점입니다. 정통 중국 요리사가 만드는 authentic한 중국 요리를 즐기실 수 있습니다.',
        4.2, 'https://example.com/images/chinese_restaurant1.jpg', '02-234-5678',
        '서울특별시 중구 명동길 45', 37.5636, 126.9834, true,
        '짜장면과 탕수육이 정말 맛있어요. 재방문 의사 100%', '중식'),

       ('파스타 하우스', '이탈리아 정통 파스타와 피자를 선보이는 양식당입니다. 신선한 재료와 정통 레시피로 만든 요리를 제공합니다.',
        4.7, 'https://example.com/images/western_restaurant1.jpg', '02-345-6789',
        '서울특별시 마포구 홍대입구역 근처 와우산로 78', 37.5563, 126.9244, true,
        '분위기 좋고 파스타가 정말 맛있습니다. 데이트 코스로 추천!', '양식'),

       ('스시 야마다', '일본 현지에서 경험을 쌓은 셰프가 직접 만드는 정통 일본 요리를 맛보실 수 있습니다.',
        4.8, 'https://example.com/images/japanese_restaurant1.jpg', '02-456-7890',
        '서울특별시 서초구 강남대로 234', 37.4979, 127.0276, true,
        '신선한 회와 정성스러운 스시가 일품입니다.', '일식'),

       ('고향집', '어머니의 손맛을 그대로 재현한 가정식 한식당입니다. 정성스럽게 만든 밑반찬과 따뜻한 국물 요리가 자랑입니다.',
        4.3, 'https://example.com/images/korean_restaurant2.jpg', '02-567-8901',
        '서울특별시 종로구 인사동길 12', 37.5717, 126.9856, true,
        '집밥 같은 푸근한 맛이 그리울 때 찾는 곳입니다.', '한식'),

       ('드래곤 팰리스', '고급스러운 분위기에서 즐기는 프리미엄 중화요리 전문점입니다. 각종 연회와 회식에 적합합니다.',
        4.0, 'https://example.com/images/chinese_restaurant2.jpg', '02-678-9012',
        '서울특별시 강남구 역삼동 테헤란로 456', 37.5009, 127.0374, false,
        '음식은 맛있지만 가격이 다소 비싸다는 의견이 있습니다.', '중식'),

       ('트라토리아 로마', '로마 현지의 맛을 그대로 재현한 authentic 이탈리안 레스토랑입니다.',
        4.6, 'https://example.com/images/western_restaurant2.jpg', '02-789-0123',
        '서울특별시 용산구 이태원로 89', 37.5344, 126.9794, true,
        '와인과 함께하는 로맨틱한 저녁 식사로 완벽합니다.', '양식'),

       ('사쿠라 정', '계절별 특선 요리와 함께 일본의 사계절을 느낄 수 있는 정통 일식당입니다.',
        4.4, 'https://example.com/images/japanese_restaurant2.jpg', '02-890-1234',
        '서울특별시 강북구 수유동 노원로 567', 37.6542, 127.0258, true,
        '계절 메뉴가 훌륭하고 서비스도 만족스럽습니다.', '일식');

INSERT INTO post (title, content, views, image_url, like_count, member_id, created_at, updated_at)
VALUES ('오늘 저녁 뭐 먹지?', '다들 저녁 메뉴 정하셨나요? 맛있는 메뉴 추천 받습니다!', 25, 'https://example.com/images/food_post1.jpg', 5, 1, NOW(),
        NOW()),
       ('주말에 다녀온 카페 후기', '분위기 좋은 신상 카페에 다녀왔어요. 커피도 맛있고 디저트도 최고였습니다. #카페추천 #주말데이트', 58,
        'https://example.com/images/cafe_post1.jpg', 12, 2, NOW(), NOW()),
       ('요즘 읽기 좋은 책', '가을은 독서의 계절이죠. 최근에 읽은 소설인데 너무 감명 깊어서 공유합니다.', 15, NULL, 3, 3, NOW(), NOW()),
       ('집에서 할 수 있는 홈트레이닝 루틴', '헬스장 가기 힘든 분들을 위해 집에서도 쉽게 따라 할 수 있는 운동 루틴을 만들어봤어요. #홈트 #운동하는직장인', 120,
        'https://example.com/images/hometraining.gif', 33, 1, NOW(), NOW()),
       ('강남역 근처 맛집 공유합니다', '파스타가 정말 맛있는 곳을 찾았어요. 데이트 코스로 강력 추천합니다. 자세한 위치는 본문에!', 77,
        'https://example.com/images/pasta_restaurant.jpg', 21, 2, NOW(), NOW()),
       ('반려동물 자랑 타임', '우리 집 강아지 너무 귀엽지 않나요? 다들 반려동물 사진 보여주세요!', 95, 'https://example.com/images/puppy.jpg', 45, 3,
        NOW(), NOW()),
       ('코딩 테스트 준비 팁', '최근에 코딩 테스트를 봤는데, 제가 공부했던 팁 몇 가지를 공유하려고 합니다. 취준생분들 화이팅!', 210, NULL, 88, 1, NOW(), NOW()),
       ('오늘 날씨 정말 좋네요!', '산책하기 딱 좋은 날씨입니다. 다들 즐거운 하루 보내세요!', 12, 'https://example.com/images/weather_today.jpg', 2, 2,
        NOW(), NOW());

