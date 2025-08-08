INSERT INTO member (social_provider, email, name, phone_number, profile_image_url, birth_date, gender)
VALUES (3, 'hong@example.com', '홍길동', '010-1234-5678', 'https://example.com/profiles/hong.jpg',
        '1995-05-10 00:00:00', 1),
       (1, 'lee@example.com', '이순신', '010-8765-4321', 'https://example.com/profiles/lee.png', '1988-11-20 00:00:00',
        1),
       (2, 'yu@example.com', '유관순', '010-1111-2222', 'https://example.com/profiles/yu.gif', '2001-03-01 00:00:00',
        2);

INSERT INTO post (member_id, title, content, image_url, views, like_count)
VALUES (1, '첫 번째 게시글 제목입니다.', 'JPA와 Spring Boot를 사용한 게시판 만들기 예제입니다. 내용은 자유롭게 채워주세요.',
        'https://example.com/images/post1.jpg', 150, 0),
       (2, '이순신 장군의 게시글', '두 번째 게시글입니다. 이 글은 다른 사용자가 작성했습니다.', 'https://example.com/images/post2.png', 75, 0),
       (1, '세 번째 게시글, 이미지 없음', '이 게시글은 이미지가 없는 텍스트 전용 게시글입니다.', NULL, 20, 0);

INSERT INTO comment (post_id, member_id, content)
VALUES (1, 2, '정말 유용한 정보네요! 감사합니다.'),
       (1, 3, '관리자입니다. 좋은 글 잘 보고 갑니다.'),
       (2, 1, '멋진 글입니다!');

INSERT INTO post_hash_tag (post_id, name)
VALUES (1, 'Java'),
       (1, 'Spring'),
       (1, 'JPA'),
       (2, '역사'),
       (2, '이순신');

INSERT INTO post_like (post_id, member_id)
VALUES (1, 2),
       (1, 3),
       (2, 1);

UPDATE post p
SET p.like_count = (SELECT COUNT(*) FROM post_like pl WHERE pl.post_id = p.id);
