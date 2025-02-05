package com.ll.commars.global.initData;

import com.ll.commars.domain.community.board.service.BoardService;
import com.ll.commars.domain.community.comment.service.CommentService;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantService;
import com.ll.commars.domain.restaurant.restaurantDoc.service.RestaurantDocService;
import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.review.review.service.ReviewService;
import com.ll.commars.domain.review.reviewDoc.service.ReviewDocService;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ll.commars.domain.reviewerRank.service.ReviewrService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    private final ReviewDocService reviewDocService;
    private final ReviewService reviewService;

    private final RestaurantDocService restaurantDocService;
    private final RestaurantService restaurantService;
    public final UserService userService;
    private final BoardService boardService;
    private final CommentService commentService;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            //work5(); // 유저 데이터 초기화
            //work6(); // 게시글 및 댓글 데이터 초기화
            //work7(); // 리뷰 데이터 초기화


            //restaurantInit();
        };
    }

    // ReviewsDoc 데이터 초기화
    private void work1() {
        reviewDocService.truncate();

//        reviewsDocService.write("하루 일과 정리", 2);
//        reviewsDocService.write("코딩의 즐거움", 5);
//        reviewsDocService.write("겨울 여행 계획", 4);
//        reviewsDocService.write("첫 직장 출근기", 1);
//        reviewsDocService.write("커피 원두 추천", 2);
//        reviewsDocService.write("운동 루틴 기록", 4);
//        reviewsDocService.write("영화 리뷰 - 인터스텔라", 4);
//        reviewsDocService.write("맛집 탐방기", 3);
//        reviewsDocService.write("독서 기록 - 나미야 잡화점의 기적", 5);
//        reviewsDocService.write("코딩 팁 공유", 3);
//        reviewsDocService.write("취미로 배우는 기타", 1);
//        reviewsDocService.write("반려견과의 산책", 5);
//        reviewsDocService.write("다음 프로젝트 아이디어", 5);
    }

    // RestaurantsDoc 데이터 초기화
    private void work2() {
        restaurantDocService.truncate();

//        restaurantsDocService.write("마녀 커피", "마녀 커피는 커피 전문점으로, 커피의 맛이 좋아요.", 4.5);
//        restaurantsDocService.write("피자 알볼로", "피자 알볼로는 피자 전문점으로, 피자의 맛이 좋아요.", 4.0);
//        restaurantsDocService.write("진짜 치킨", "진짜 치킨은 치킨 전문점으로, 치킨의 맛이 좋아요.", 4.0);
//        restaurantsDocService.write("매운 떡볶이", "매운 떡볶이는 떡볶이 전문점으로, 떡볶이의 맛이 좋아요.", 3.0);
    }

    // Restaurant 데이터 초기화
    private void restaurantInit() {
        restaurantService.truncate();

        String[] names = {"마녀커피", "피자알볼로", "스타벅스", "버거킹", "맘스터치", "서브웨이", "홍콩반점", "교촌치킨"};
        String[] details = {"분위기 좋은 카페", "맛있는 피자집", "글로벌 커피 체인", "햄버거 전문점", "치킨 버거 전문점",
                "샌드위치 전문점", "중국 음식점", "치킨 전문점"};
        String[] addresses = {"서울시 강남구", "서울시 서초구", "서울시 송파구", "서울시 마포구", "서울시 종로구"};
        String[] summarizedReviews = {"맛있고 분위기가 좋아요", "가성비가 좋아요", "서비스가 친절해요",
                "음식이 빨리 나와요", "재방문 의사 있어요"};

        Random random = new Random();

        IntStream.range(0, 10).forEach(i -> {
            RestaurantDto.RestaurantWriteRequest restaurant = RestaurantDto.RestaurantWriteRequest.builder()
                    .name(names[random.nextInt(names.length)])
                    .details(details[random.nextInt(details.length)])
                    .averageRate(3.0 + random.nextDouble() * 2.0) // 3.0-5.0 사이 랜덤 점수
                    .imageUrl(String.format("http://example.com/restaurant%d.jpg", i))
                    .contact(String.format("02-%d-%d", 1000 + random.nextInt(9000), 1000 + random.nextInt(9000)))
                    .address(addresses[random.nextInt(addresses.length)])
                    .lat(37.4967 + (random.nextDouble() - 0.5) * 0.1) // 37.4467-37.5467 사이
                    .lng(127.0498 + (random.nextDouble() - 0.5) * 0.1) // 126.9998-127.0998 사이
                    .runningState(random.nextBoolean())
                    .summarizedReview(summarizedReviews[random.nextInt(summarizedReviews.length)])
                    .build();

            restaurantService.write(restaurant);
        });
    }

    // Reviews 데이터 초기화
    private void reviewsInit() {
        reviewService.truncate();

        String[] names = {"맛있네요", "좋아요", "괜찮아요", "별로에요", "맛없어요"};
        String[] bodies = {"맛있어요", "서비스가 좋아요", "가격이 착해요", "재방문 의사 있어요", "별로에요"};

        RestaurantDto.RestaurantShowAllResponse restaurants = restaurantService.getRestaurants();
        List<Long> restaurantIds = restaurants.getRestaurants().stream()
                .map(RestaurantDto.RestaurantInfo::getId)
                .toList();

        Random random = new Random();

        IntStream.range(0, 20).forEach(i -> {
            Long randomRestaurantId = restaurantIds.get(random.nextInt(restaurantIds.size()));

            ReviewDto.ReviewWriteRequest review = ReviewDto.ReviewWriteRequest.builder()
                    .reviewName(names[random.nextInt(names.length)])
                    .body(bodies[random.nextInt(bodies.length)])
                    .rate(1 + random.nextInt(5)) // 1-5 사이 랜덤 점수
                    .build();

            restaurantService.writeReview(randomRestaurantId, review);
        });
    }

    private void work5(){
        userService.truncate();
        // 카카오 유저 5명 생성
        userService.createUser("kakao1@example.com", "카카오유저1", 1, "password123", "010-1111-1111", "profile_image_url1", LocalDateTime.now(), 1);
        userService.createUser("kakao2@example.com", "카카오유저2", 1, "password234", "010-1111-2222", "profile_image_url2", LocalDateTime.now(), 2);
        userService.createUser("kakao3@example.com", "카카오유저3", 1, "password345", "010-1111-3333", "profile_image_url3", LocalDateTime.now(), 1);
        userService.createUser("kakao4@example.com", "카카오유저4", 1, "password456", "010-1111-4444", "profile_image_url4", LocalDateTime.now(), 2);
        userService.createUser("kakao5@example.com", "카카오유저5", 1, "password567", "010-1111-5555", "profile_image_url5", LocalDateTime.now(), 1);

        // 구글 유저 5명 생성
        userService.createUser("google1@example.com", "구글유저1", 3, "password678", "010-2222-1111", "profile_image_url6", LocalDateTime.now(), 1);
        userService.createUser("google2@example.com", "구글유저2", 3, "password789", "010-2222-2222", "profile_image_url7", LocalDateTime.now(), 2);
        userService.createUser("google3@example.com", "구글유저3", 3, "password890", "010-2222-3333", "profile_image_url8", LocalDateTime.now(), 1);
        userService.createUser("google4@example.com", "구글유저4", 3, "password901", "010-2222-4444", "profile_image_url9", LocalDateTime.now(), 2);
        userService.createUser("google5@example.com", "구글유저5", 3, "password012", "010-2222-5555", "profile_image_url10", LocalDateTime.now(), 1);


    }

    private void work6() {
        // 기존 데이터 삭제
        commentService.truncate();
        boardService.truncate();


        // 사용자 가져오기
        User user1 = userService.findByEmail("kakao1@example.com");
        User user2 = userService.findByEmail("google2@example.com");

        // 게시글 추가 (해시태그 포함)
        Long board1Id = boardService.addBoard(user1.getId(), "첫 번째 게시글", "안녕하세요, 첫 번째 게시글입니다.", List.of("첫번째", "게시글", "테스트"));
        System.out.println("board1Id: " + board1Id);
        Long board2Id = boardService.addBoard(user2.getId(), "두 번째 게시글", "반갑습니다!", List.of("두번째", "게시글"));
        System.out.println("board2Id: " + board2Id);
        Long board3Id = boardService.addBoard(user1.getId(), "세 번째 게시글", "이것은 테스트 게시글입니다.", List.of("테스트", "커뮤니티"));
        System.out.println("board3Id: " + board3Id);

        // 댓글 추가
        commentService.addComment(board1Id, user2.getId(), "첫 번째 게시글에 대한 첫 번째 댓글입니다.");
        commentService.addComment(board1Id, user1.getId(), "첫 번째 게시글에 대한 두 번째 댓글입니다.");
        commentService.addComment(board2Id, user1.getId(), "두 번째 게시글에 대한 첫 번째 댓글입니다.");
        commentService.addComment(board3Id, user2.getId(), "세 번째 게시글에 대한 첫 번째 댓글입니다.");
    }

    public final ReviewrService reviewrService;

    // ✅ 10명의 유저가 리뷰를 작성하도록 초기 데이터 설정
    private void work7() {
        System.out.println("🌟 리뷰 초기 데이터 생성 시작!");

        // ✅ 기존 데이터 삭제
        //reviewrService.truncate();
        //userService.truncate();
        //restaurantService.truncate();

        System.out.println("🔹 모든 데이터 삭제 완료!");

        // ✅ 10명의 유저 생성
        IntStream.rangeClosed(1, 10).forEach(i -> {
            String email = "user" + i + "@example.com";
            String name = "유저" + i;
            int provider = (i % 2 == 0) ? 1 : 3;
            String phone = "010-1234-" + (1000 + i);

            userService.createUser(email, name, provider, "password123", phone, "profile" + i + ".jpg", LocalDateTime.now(), (i % 2) + 1);
        });

        // ✅ 유저 확인
        List<User> users = userService.findAllUsers();
        System.out.println("🔹 생성된 유저 수: " + users.size());

        // ✅ 5개의 음식점 추가
        String[] restaurantNames = {"맛있는 식당", "고기 맛집", "해산물 전문점", "이탈리안 레스토랑", "한식 밥집"};
        Random random = new Random();
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Restaurant restaurant = Restaurant.builder()
                    .name(restaurantNames[i - 1])
                    .details("훌륭한 요리를 제공하는 레스토랑입니다.")
                    .averageRate(4.0 + (i % 2))
                    .address("서울시 강남구")
                    .lat(37.5665 + (random.nextDouble() - 0.5) * 0.01) // 서울 근처 랜덤 위도
                    .lng(126.9780 + (random.nextDouble() - 0.5) * 0.01) // 서울 근처 랜덤 경도
                    .build();
            restaurantService.save(restaurant);
        });

        // ✅ 레스토랑 확인
        List<Restaurant> restaurants = restaurantService.findAllRestaurants();
        System.out.println("🔹 생성된 레스토랑 수: " + restaurants.size());


        users.forEach(user -> {
            int reviewCount = random.nextInt(10) + 1; // 유저당 1~10개 리뷰 작성
            for (int i = 0; i < reviewCount; i++) {
                Restaurant randomRestaurant = restaurants.get(random.nextInt(restaurants.size()));

                ReviewDto.ReviewWriteRequest review = ReviewDto.ReviewWriteRequest.builder()
                        .reviewName("리뷰 제목 " + i)
                        .body("이곳은 정말 좋습니다! " + (i + 1))
                        .rate(random.nextInt(5) + 1)
                        .build();

                reviewrService.writeReview(randomRestaurant.getId(), review, user.getEmail());
            }
        });

        System.out.println("✅ 리뷰 데이터 초기화 완료!");
    }



}
