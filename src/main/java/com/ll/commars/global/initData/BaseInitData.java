package com.ll.commars.global.initData;

import com.ll.commars.domain.community.board.service.BoardService;
import com.ll.commars.domain.community.comment.service.CommentService;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantService;
import com.ll.commars.domain.restaurant.restaurantDoc.service.RestaurantDocService;
import com.ll.commars.domain.review.review.service.ReviewService;
import com.ll.commars.domain.review.reviewDoc.service.ReviewDocService;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
 // Lombok 어노테이션
@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    private final ReviewDocService reviewDocService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final BoardService boardService;

    private final RestaurantDocService restaurantDocService;
    private final RestaurantService restaurantService;
    private final CommentService commentService;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
//            work1();
//            work2();
//            work3();
            //work4();
            work5();
            work6();
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

    // Reviews 데이터 초기화
    private void work3() {
        reviewService.truncate();

        reviewService.write("whqtker", "코딩의 즐거움", 5);
        reviewService.write("test", "겨울 여행 계획", 4);
        reviewService.write("hello", "운동 루틴 기록", 1);
        reviewService.write("1q2w3e4r", "영화 리뷰 - 인터스텔라", 4);
        reviewService.write("whqtker", "독서 기록 - 나미야 잡화점의 기적", 5);
    }

    // Restaurants 데이터 초기화
    private void work4() {
        restaurantService.truncate();

        restaurantService.write(
                "이탈리안 레스토랑",
                "Authentic Italian cuisine with a cozy atmosphere.",
                4.5,
                "https://example.com/images/italian-restaurant.jpg",
                "010-1234-5678",
                "서울특별시 강남구 테헤란로 123",
                37.123456,
                127.123456,
                true,
                "Great food and excellent service."
        );

        restaurantService.write(
                "프랑스 레스토랑",
                "Authentic French cuisine with a romantic atmosphere.",
                4.0,
                "https://example.com/images/french-restaurant.jpg",
                "010-2345-6789",
                "서울특별시 강남구 테헤란로 456",
                37.234567,
                127.234567,
                true,
                "Great food and excellent service."
        );

        restaurantService.write(
                "일식 레스토랑",
                "Authentic Japanese cuisine with a traditional atmosphere.",
                4.0,
                "https://example.com/images/japanese-restaurant.jpg",
                "010-3456-7890",
                "서울특별시 강남구 테헤란로 789",
                37.345678,
                127.345678,
                true,
                "Great food and excellent service."
        );

        restaurantService.write(
                "중식 레스토랑",
                "Authentic Chinese cuisine with a modern atmosphere.",
                3.0,
                "https://example.com/images/chinese-restaurant.jpg",
                "010-4567-8901",
                "서울특별시 강남구 테헤란로 012",
                37.456789,
                127.456789,
                true,
                "Great food and excellent service."
        );
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

    // BaseInitData.java
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



}
