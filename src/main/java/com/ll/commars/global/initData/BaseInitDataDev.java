package com.ll.commars.global.initData;

import com.ll.commars.domain.community.board.service.BoardService;
import com.ll.commars.domain.community.comment.service.CommentService;
import com.ll.commars.domain.community.reaction.service.ReactionService;
import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourDto;
import com.ll.commars.domain.restaurant.businessHour.service.BusinessHourService;
import com.ll.commars.domain.restaurant.category.dto.RestaurantCategoryDto;
import com.ll.commars.domain.restaurant.category.service.RestaurantCategoryService;
import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.menu.service.RestaurantMenuService;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantService;
//import com.ll.commars.domain.restaurant.restaurantDoc.service.RestaurantDocService;
import com.ll.commars.domain.restaurant.restaurantDoc.service.RestaurantDocService;
import com.ll.commars.domain.review.review.service.ReviewService;
//import com.ll.commars.domain.review.reviewDoc.service.ReviewDocService;
import com.ll.commars.domain.review.reviewDoc.service.ReviewDocService;
import com.ll.commars.domain.user.favorite.dto.FavoriteDto;
import com.ll.commars.domain.user.favorite.entity.Favorite;
import com.ll.commars.domain.user.favorite.service.FavoriteService;
import com.ll.commars.domain.user.favoriteRestaurant.service.FavoriteRestaurantService;
import com.ll.commars.domain.user.user.dto.UserDto;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
@Profile("dev") // 개발 환경에서만 실행하도록
public class BaseInitDataDev {
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
    private final FavoriteService favoriteService;
    private final FavoriteRestaurantService favoriteRestaurantService;
    private final ReactionService reactionService;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
//            truncateAll();
//
//            // 테이블 연관관계 순서대로
//            userInit();
            restaurantCategoryInit();
//
//            communityInit();
            restaurantInit();
//
//            reviewInit();
//            restaurantMenuInit();
//            businessHourInit();
//            favoriteInit();
        };
    }

    private void truncateAll(){
        // 연관관계 순서대로 삭제해야함
        reviewDocService.truncate();
        restaurantDocService.truncate();

        favoriteRestaurantService.truncate();
        favoriteService.truncate();

        commentService.truncate();
        reactionService.truncate();
        boardService.truncate();

        reviewService.truncate();
        userService.truncate();

        businessHourService.truncate();
        restaurantMenuService.truncate();
        restaurantService.truncate();
        restaurantCategoryService.truncate();
    }

    // Restaurant 데이터 초기화
    private void restaurantInit() {
        Path csvPath = Paths.get("../api/data/restaurants.csv").toAbsolutePath();

        // 존재하는 카테고리 맵 생성 (카테고리명 -> ID)
        RestaurantCategoryDto.ShowAllCategoriesResponse categories = restaurantCategoryService.getCategories();
        Map<String, Long> categoryMap = categories.getCategories().stream()
                .collect(Collectors.toMap(
                        RestaurantCategoryDto.RestaurantCategoryInfo::getName,
                        RestaurantCategoryDto.RestaurantCategoryInfo::getId
                ));

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toFile()))) {
            // Skip header
            String header = br.readLine();
            String line;
            //int count = 0;

            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                String name = data[0].trim();
                String address = data[1].trim();
                double lat = Double.parseDouble(data[2].trim());
                double lon = Double.parseDouble(data[3].trim());
                String categoryName = data[4].trim();

                // 카테고리 ID 찾기
                Long categoryId = categoryMap.get(categoryName);
                if (categoryId == null) {
                    continue; // Skip if category not found
                }

                RestaurantDto.RestaurantWriteRequest restaurant = RestaurantDto.RestaurantWriteRequest.builder()
                        .name(name)
                        .details("") // Could be added to CSV if needed
                        .averageRate(0.0) // Initial rating
                        .imageUrl("http://example.com/restaurant_default.jpg")
                        .contact("02-1234-5678") // Could be added to CSV if needed
                        .address(address)
                        .lat(lat)
                        .lon(lon)
                        .runningState(true)
                        .summarizedReview("")
                        .categoryId(categoryId)
                        .build();

                restaurantService.write(restaurant);
                //count++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read restaurants from CSV file: " + csvPath.toString(), e);
        }
    }

    // RestaurantCategory 데이터 초기화
    private void restaurantCategoryInit() {
        Path csvPath = Paths.get("../api/data/restaurant_categories.csv").toAbsolutePath();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toFile()))) {
            // Skip header if exists
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                String categoryName = line.trim();
                if (!categoryName.isEmpty()) {
                    RestaurantCategoryDto.RestaurantCategoryEnrollRequest category =
                            RestaurantCategoryDto.RestaurantCategoryEnrollRequest.builder()
                                    .name(categoryName)
                                    .build();

                    restaurantCategoryService.writeCategory(category);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read restaurant categories from CSV file", e);
        }
    }

    private void restaurantMenuInit() {
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

//        IntStream.range(0, 20).forEach(i -> {
//            Long userId = userIds.get(random.nextInt(userIds.size()));
//            Long restaurantId = restaurantIds.get(random.nextInt(restaurantIds.size()));
//            String name = names[random.nextInt(names.length)];
//            String body = bodies[random.nextInt(bodies.length)];
//            Integer rate = random.nextInt(5) + 1; // 1-5 사이 랜덤 점수
////
////            ReviewDto.ReviewWriteRequest review = ReviewDto.ReviewWriteRequest.builder()
////                    .userId(userId)
////                    .reviewName(name)
////                    .body(body)
////                    .rate(rate)
////                    .build();
////
////            restaurantService.writeReview(restaurantId, review);
////        });
    }

    private void userInit() {
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

    private void favoriteInit() {
        // 모든 사용자와 레스토랑 가져오기
        List<UserDto.UserInfo> users = userService.findAllUsers();
        RestaurantDto.RestaurantShowAllResponse restaurants = restaurantService.getRestaurants();

        List<Long> restaurantIds = restaurants.getRestaurants().stream()
                .map(RestaurantDto.RestaurantInfo::getId)
                .toList();

        Random random = new Random();
        String[] favoriteNames = {"맛집 리스트", "가고 싶은 곳", "자주 가는 곳", "친구와 가고 싶은 곳", "데이트 코스"};

        // 각 사용자별로 찜 목록 생성
        users.forEach(user -> {
            // 유저 엔티티 조회
            User userEntity = userService.findById(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // 찜 목록 생성 (1-3개)
            int favoriteCount = random.nextInt(3) + 1;

            IntStream.range(0, favoriteCount).forEach(i -> {
                // 빈 찜 목록 생성
                FavoriteDto.CreateFavoriteListRequest createRequest = FavoriteDto.CreateFavoriteListRequest.builder()
                        .name(favoriteNames[random.nextInt(favoriteNames.length)])
                        .isPublic(random.nextBoolean())
                        .build();

                favoriteService.saveFavoriteList(userEntity, createRequest);

                // 생성된 찜 목록 조회
                List<Favorite> userFavorites = favoriteService.getFavoritesByUser(userEntity);
                Long favoriteId = userFavorites.get(userFavorites.size() - 1).getId();

                // 랜덤한 개수의 레스토랑 추가 (1 ~ restaurants.size)
                int restaurantCount = random.nextInt(restaurantIds.size()) + 1;

                // 중복없이 랜덤하게 레스토랑 선택
                List<Long> selectedRestaurants = new ArrayList<>(restaurantIds);
                java.util.Collections.shuffle(selectedRestaurants);
                selectedRestaurants = selectedRestaurants.subList(0, restaurantCount);

                // 선택된 레스토랑들을 찜 목록에 추가
                selectedRestaurants.forEach(restaurantId -> {
                    favoriteService.addRestaurantToFavorite(favoriteId, restaurantId);
                });
            });
        });
    }
}
