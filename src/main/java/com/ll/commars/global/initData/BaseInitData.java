package com.ll.commars.global.initData;

import com.ll.commars.domain.community.board.service.BoardService;
import com.ll.commars.domain.community.comment.service.CommentService;
import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourDto;
import com.ll.commars.domain.restaurant.businessHour.service.BusinessHourService;
import com.ll.commars.domain.restaurant.category.dto.RestaurantCategoryDto;
import com.ll.commars.domain.restaurant.category.service.RestaurantCategoryService;
import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.menu.service.RestaurantMenuService;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantService;
import com.ll.commars.domain.restaurant.restaurantDoc.service.RestaurantDocService;
import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.review.review.service.ReviewService;
import com.ll.commars.domain.review.reviewDoc.service.ReviewDocService;
import com.ll.commars.domain.user.user.dto.UserDto;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ll.commars.domain.reviewerRank.service.ReviewrService;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final UserService userService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final RestaurantCategoryService restaurantCategoryService;
    private final RestaurantMenuService restaurantMenuService;
    private final BusinessHourService businessHourService;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            // ES 초기화
            reviewsDocInit();
            restaurantDocInit();

            // 테이블 연관관계 순서대로
            userInit();
            restaurantCategoryInit();

            communityInit();
            restaurantInit();

            reviewInit();
            restaurantMenuInit();
            businessHourInit();
        };
    }

    // ReviewsDoc 데이터 초기화
    private void reviewsDocInit() {
        reviewDocService.truncate();
    }

    // RestaurantsDoc 데이터 초기화
    private void restaurantDocInit() {
        restaurantDocService.truncate();
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

        // 존재하는 카테고리 ID 가져오기
        RestaurantCategoryDto.ShowAllCategoriesResponse categories = restaurantCategoryService.getCategories();
        List<Long> categoriesId = categories.getCategories().stream()
                .map(RestaurantCategoryDto.RestaurantCategoryInfo::getId)
                .toList();

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
                    .categoryId(categoriesId.get(random.nextInt(categoriesId.size())))
                    .build();

            restaurantService.write(restaurant);
        });
    }

    // RestaurantCategory 데이터 초기화
    private void restaurantCategoryInit(){
        restaurantCategoryService.truncate();

        String[] names = {"한식", "중식", "일식", "양식", "패스트푸드"};

        IntStream.range(0, 5).forEach(i -> {
            RestaurantCategoryDto.RestaurantCategoryEnrollRequest category = RestaurantCategoryDto.RestaurantCategoryEnrollRequest.builder()
                    .name(names[i])
                    .build();

            restaurantCategoryService.writeCategory(category);
        });
    }

    private void restaurantMenuInit() {
        restaurantMenuService.truncate();

        // 모든 음식점 가져오기
        RestaurantDto.RestaurantShowAllResponse restaurants = restaurantService.getRestaurants();
        List<Long> restaurantIds = restaurants.getRestaurants().stream()
                .map(RestaurantDto.RestaurantInfo::getId)
                .toList();

        String[] menuNames = {
                "김치찌개", "짜장면", "초밥", "피자", "치킨", "샐러드", "짜파게티", "탕수육",
                "햄버거", "샌드위치", "커피", "차", "주스", "맥주", "소주", "막걸리"
        };

        Integer[] prices = {
                8000, 7000, 12000, 18000, 18000, 6000, 7000, 16000,
                6500, 6000, 4500, 4000, 5000, 5000, 4000, 4000
        };

        Random random = new Random();

        // 각 레스토랑에 대해
        restaurantIds.forEach(restaurantId -> {
            // 1-5개의 랜덤한 메뉴 선택
            int menuCount = random.nextInt(5) + 1;

            IntStream.range(0, menuCount).forEach(i -> {
                int randomIndex = random.nextInt(menuNames.length);

                RestaurantMenuDto.MenuInfo menuInfo = RestaurantMenuDto.MenuInfo.builder()
                        .name(menuNames[randomIndex])
                        .price(prices[randomIndex])
                        .imageUrl("http://example.com/foods/menu" + (randomIndex + 1) + ".jpg")
                        .build();

                restaurantMenuService.write(restaurantId, menuInfo);
            });
        });
    }

    private void businessHourInit() {
        businessHourService.truncate();

        // 모든 음식점 가져오기
        RestaurantDto.RestaurantShowAllResponse restaurants = restaurantService.getRestaurants();
        List<Long> restaurantIds = restaurants.getRestaurants().stream()
                .map(RestaurantDto.RestaurantInfo::getId)
                .toList();

        Random random = new Random();

        // 각 레스토랑에 대해 영업시간 설정
        restaurantIds.forEach(restaurantId -> {
            List<BusinessHourDto.BusinessHourWriteInfo> businessHours = IntStream.range(1, 8) // 1(월요일) ~ 7(일요일)
                    .mapToObj(dayOfWeek -> {
                        // 랜덤 영업시간 생성 (9-11시 사이 오픈, 20-22시 사이 마감)
                        int openHour = 9 + random.nextInt(3);
                        int closeHour = 20 + random.nextInt(3);

                        return BusinessHourDto.BusinessHourWriteInfo.builder()
                                .dayOfWeek(dayOfWeek)
                                .openTime(LocalDateTime.now()
                                        .withHour(openHour)
                                        .withMinute(0)
                                        .withSecond(0))
                                .closeTime(LocalDateTime.now()
                                        .withHour(closeHour)
                                        .withMinute(0)
                                        .withSecond(0))
                                .build();
                    })
                    .collect(Collectors.toList());

            BusinessHourDto.BusinessHourWriteRequest request = BusinessHourDto.BusinessHourWriteRequest.builder()
                    .businessHours(businessHours)
                    .build();

            restaurantService.writeBusinessHours(restaurantId, request);
        });
    }

    // Reviews 데이터 초기화
    private void reviewInit() {
        reviewService.truncate();

        String[] names = {"맛있네요", "좋아요", "괜찮아요", "별로에요", "맛없어요"};
        String[] bodies = {"맛있어요", "서비스가 좋아요", "가격이 착해요", "재방문 의사 있어요", "별로에요"};

        // 모든 음식점 가져오기
        RestaurantDto.RestaurantShowAllResponse restaurants = restaurantService.getRestaurants();
        List<Long> restaurantIds = restaurants.getRestaurants().stream()
                .map(RestaurantDto.RestaurantInfo::getId)
                .toList();

        // 모든 사용자 가져오기
        List<UserDto.UserInfo> users = userService.findAllUsers();
        List<Long> userIds = users.stream()
                .map(UserDto.UserInfo::getId)
                .toList();

        Random random = new Random();

        IntStream.range(0, 20).forEach(i -> {
            Long userId = userIds.get(random.nextInt(userIds.size()));
            Long restaurantId = restaurantIds.get(random.nextInt(restaurantIds.size()));
            String name = names[random.nextInt(names.length)];
            String body = bodies[random.nextInt(bodies.length)];
            Integer rate = random.nextInt(5) + 1; // 1-5 사이 랜덤 점수

            ReviewDto.ReviewWriteRequest review = ReviewDto.ReviewWriteRequest.builder()
                    .userId(userId)
                    .reviewName(name)
                    .body(body)
                    .rate(rate)
                    .build();

            restaurantService.writeReview(restaurantId, review);
        });
    }

    private void userInit() {
        userService.truncate();

        String[] names = {"김민준", "이서연", "박지호", "최수아", "정우진", "강하은", "윤도현", "임서윤", "한지민", "송민서"};
        String[] domains = {"gmail.com", "naver.com", "kakao.com", "daum.net"};
        String[] phoneNumbers = {"010-1234-", "010-5678-", "010-9012-", "010-3456-"};
        String[] profileImages = {
                "profile1.jpg", "profile2.jpg", "profile3.jpg", "profile4.jpg", "profile5.jpg",
                "profile6.jpg", "profile7.jpg", "profile8.jpg", "profile9.jpg", "profile10.jpg"
        };

        Random random = new Random();

        IntStream.range(0, 20).forEach(i -> {
            String name = names[random.nextInt(names.length)];
            String email = name + (random.nextInt(100) + 1) + "@" + domains[random.nextInt(domains.length)];
            String phoneNumber = phoneNumbers[random.nextInt(phoneNumbers.length)] + String.format("%04d", random.nextInt(10000));
            String password = "password" + (random.nextInt(900) + 100);
            Integer socialProvider = random.nextInt(3) + 1; // 1: 카카오, 2: 네이버, 3: 구글
            String profileImageUrl = "http://example.com/images/" + profileImages[random.nextInt(profileImages.length)];
            Integer gender = random.nextInt(2) + 1; // 1: 남성, 2: 여성

            userService.createUser(
                    email,
                    name,
                    socialProvider,
                    password,
                    phoneNumber,
                    profileImageUrl,
                    LocalDateTime.now().minusYears(random.nextInt(30) + 20), // 20-50살 사이
                    gender
            );
        });
    }

    private void communityInit() {
        // 기존 데이터 삭제
        commentService.truncate();
        boardService.truncate();

        // 게시글 데이터 배열
        String[] titles = {
                "맛집 추천해주세요", "오늘 날씨 좋네요", "여행 계획 중입니다",
                "운동 같이하실 분", "독서모임 하실 분", "영화 추천해주세요",
                "주말에 뭐하세요?", "취미 공유해요", "맛집 탐방기",
                "카페 추천합니다"
        };

        String[] contents = {
                "추천 부탁드립니다!", "정말 좋은 날씨네요~", "여행 코스 추천해주세요",
                "함께 운동하면 더 재미있을 것 같아요", "매주 토요일마다 모여요",
                "재미있는 영화 알려주세요", "주말 계획 공유해요", "취미가 뭐예요?",
                "여기 정말 맛있어요", "분위기 좋은 카페예요"
        };

        String[] tags = {"맛집", "일상", "여행", "운동", "독서", "영화", "취미", "음식", "카페", "문화"};

        String[] comments = {
                "좋은 정보 감사합니다!", "저도 같이 하고 싶어요", "동의합니다",
                "정말 좋네요", "관심 있어요", "잘 보고 갑니다",
                "도움이 되었어요", "재미있네요", "응원합니다",
                "좋은 글이에요"
        };

        String[] imageUrls = {
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
                "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg"
        };

        // 모든 사용자 가져오기
        List<UserDto.UserInfo> users = userService.findAllUsers();
        List<Long> userIds = users.stream()
                .map(UserDto.UserInfo::getId)
                .toList();

        Random random = new Random();
        List<Long> boardIds = new ArrayList<>();

        // 20개의 게시글 생성
        IntStream.range(0, 20).forEach(i -> {
            // 랜덤 태그 2-3개 선택
            int tagCount = random.nextInt(2) + 2;
            List<String> selectedTags = new ArrayList<>();
            for (int j = 0; j < tagCount; j++) {
                selectedTags.add(tags[random.nextInt(tags.length)]);
            }

            Long userId = userIds.get(random.nextInt(userIds.size()));
            String title = titles[random.nextInt(titles.length)];
            String content = contents[random.nextInt(contents.length)];
            String imageUrl = imageUrls[random.nextInt(imageUrls.length)];

            Long boardId = boardService.addBoard(
                    userId,
                    title,
                    content,
                    selectedTags,
                    imageUrl
            );
            boardIds.add(boardId);
        });

        // 20개의 댓글 생성
        IntStream.range(0, 20).forEach(i -> {
            Long userId = userIds.get(random.nextInt(userIds.size()));
            Long boardId = boardIds.get(random.nextInt(boardIds.size()));
            String comment = comments[random.nextInt(comments.length)];

            commentService.addComment(
                    boardId,
                    userId,
                    comment
            );
        });
    }
}
