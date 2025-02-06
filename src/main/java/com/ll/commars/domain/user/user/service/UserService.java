package com.ll.commars.domain.user.user.service;

// 트랜잭션 단위로 실행될 메소드를 선언하고 있는 클래스
// 스프링이 관리하는 Bean

import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.user.favorite.dto.FavoriteDto;
import com.ll.commars.domain.user.favorite.service.FavoriteService;
import com.ll.commars.domain.user.user.dto.UserDto;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;
import com.ll.commars.domain.user.user.controller.UserController;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final FavoriteService favoriteService;

    public User createUser(String email, String name, Integer socialProvider, String password, String phoneNumber, String profileImageUrl, LocalDateTime birthDate,Integer gender) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setSocialProvider(socialProvider);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setProfileImageUrl(profileImageUrl);
        user.setBirthDate(birthDate);
        user.setGender(gender);
        // ... 기타 필드 설정 ...
        return userRepository.save(user);
    }

    public User addUser(String email, String name, String password) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password); // 평문 비밀번호 저장
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public void saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("이미 등록된 이메일입니다.");
        }

        // 비밀번호 해싱 (Spring Security 사용 고려)
        user.setPassword(user.getPassword());

        userRepository.save(user);
    }


    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User authenticate(String email, String password) {
        logger.info("Authenticating user with email: {}", email);
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }

    public void truncate() {
        userRepository.deleteAll();
    }

    // ✅ 모든 유저 조회 메서드 추가
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


    public User accessionCheck(User user) {
        Optional<User> findUser = userRepository.findByEmailAndName(user.getEmail(), user.getName());
        return findUser.orElseGet(() -> userRepository.save(user));
    }

    public Optional<User> findByIdAndEmail(Long id, String email) {
        return userRepository.findByIdAndEmail(id, email);
    }

    public Optional<User> findById(long l) {
        return userRepository.findById(l);
    }

    public UserDto.UserFavoriteListsResponse getFavoriteLists(User user) {
        List<FavoriteDto.FavoriteInfo> favorites = favoriteService.getFavoritesByUser(user)
                .stream()
                .map(favoriteService::toFavoriteInfo)
                .collect(Collectors.toList());

        return UserDto.UserFavoriteListsResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .favoriteLists(favorites)
                .build();
    }

    // 찜 리스트 생성(식당 추가 X)
    public void createFavoriteList(User user, FavoriteDto.CreateFavoriteListRequest request) {
        // 찜 리스트 생성
        FavoriteDto.CreateFavoriteListRequest createFavoriteListRequest = FavoriteDto.CreateFavoriteListRequest.builder()
                .name(request.getName())
                .isPublic(request.getIsPublic())
                .build();

        // 찜 리스트 저장
        favoriteService.saveFavoriteList(user, createFavoriteListRequest);
    }

    public ReviewDto.ShowAllReviewsResponse getReviews(Long userId) {
        List<ReviewDto.ReviewInfo> reviews = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"))
                .getReviews()
                .stream()
                .map(review -> ReviewDto.ReviewInfo.builder()
                        .userName(review.getUser().getName())
                        .restaurantName(review.getRestaurant().getName())
                        .reviewName(review.getName())
                        .body(review.getBody())
                        .rate(review.getRate())
                        .build())
                .collect(Collectors.toList());

        return ReviewDto.ShowAllReviewsResponse.builder()
                .reviews(reviews)
                .build();
    }
}
