package com.ll.commars.domain.user.user.service;

// 트랜잭션 단위로 실행될 메소드를 선언하고 있는 클래스
// 스프링이 관리하는 Bean

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.user.favorite.dto.FavoriteDto;
import com.ll.commars.domain.user.favorite.entity.Favorite;
import com.ll.commars.domain.user.favorite.service.FavoriteService;
import com.ll.commars.domain.user.user.controller.ApiV1UserController;
import com.ll.commars.domain.user.user.dto.UserDto;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(ApiV1UserController.class);

	private final UserRepository userRepository;
	private final FavoriteService favoriteService;

	public User createUser(String email, String name, Integer socialProvider, String password, String phoneNumber,
		String profileImageUrl, LocalDateTime birthDate, Integer gender) {
		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setSocialProvider(socialProvider);
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
		return userRepository.save(user);
	}

    /*public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }*/

	public void saveUser(User user) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("이미 등록된 이메일입니다.");
		}
		userRepository.save(user);
	}

	public boolean isEmailTaken(String email) {
		return userRepository.findByEmail(email).isPresent();
	}

	public User authenticate(String email) {
		logger.info("Authenticating user with email: {}", email);
		return userRepository.findByEmail(email).orElse(null);
	}

	public void truncate() {
		userRepository.deleteAll();
	}

	public List<UserDto.UserInfo> findAllUsers() {
		return userRepository.findAll()
			.stream()
			.map(user -> UserDto.UserInfo.builder()
				.id(user.getId())
				.socialProvider(user.getSocialProvider())
				.email(user.getEmail())
				.name(user.getName())
				.phoneNumber(user.getPhoneNumber())
				.profileImageUrl(user.getProfileImageUrl())
				.birthDate(user.getBirthDate())
				.build())
			.collect(Collectors.toList());
	}

	//    public User accessionCheck(User user) {
	//        System.out.println("accessiom: " + user);
	//        Optional<User> findUser = userRepository.findByEmailAndName(user.getEmail(), user.getName());
	//        System.out.println("find: " + findUser.get().getName());
	//        return findUser.orElseGet(() -> userRepository.save(user));
	//    }

	public User accessionCheck(User user) {
		System.out.println("accessiom: " + user);
		Optional<User> findUser = userRepository.findByEmailAndName(user.getEmail(), user.getName());
		return findUser.orElseGet(() -> userRepository.save(user));
	}

	// 카카오 로그인용 신규 사용자 여부 확인 로직
	//    public User accessionKakaoCheck(User user) {
	//        // socialProvider와 kakaoId를 기준으로 기존 사용자를 조회
	//        Optional<User> findUser = userRepository.findBySocialProviderAndKakaoId(
	//                user.getSocialProvider(),
	//                user.getKakaoId()
	//        );
	//        // 기존 사용자가 있다면 반환하고, 없으면 신규 가입 처리
	//        return findUser.orElseGet(() -> userRepository.save(user));
	//    }

	//    public User accessionKakaoCheck(User user) {
	//        Optional<User> findUser = userRepository.findBySocialProviderAndKakaoId(
	//                user.getSocialProvider(),
	//                user.getKakaoId()
	//        );
	//        findUser.ifPresent(u -> System.out.println("find: " + u.getName()));
	//        // 값이 없으면 저장하고, 있으면 기존 사용자를 반환
	//        return findUser.orElseGet(() -> userRepository.save(user));
	//    }

	public Optional<User> findByIdAndEmail(Long id, String email) {
		return userRepository.findByIdAndEmail(id, email);
	}

	public Optional<User> findById(long l) {
		return userRepository.findById(l);
	}

	@Transactional
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
				.userId(review.getUser().getId())
				.restaurantId(review.getRestaurant().getId())
				.id(review.getId())
				.name(review.getName())
				.body(review.getBody())
				.rate(review.getRate())
				.build())
			.collect(Collectors.toList());

		return ReviewDto.ShowAllReviewsResponse.builder()
			.reviews(reviews)
			.build();
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);  // ✅ 중복 제거 + Optional 감싸지 않음
	}

	public User findByEmailOrNull(String email) {
		return userRepository.findByEmail(email).orElse(null);  // ✅ Optional을 벗긴 버전
	}

	public ResponseEntity<?> createFavoriteList(String favoriteName, String userId) {
		Optional<User> user = userRepository.findById(Long.parseLong(userId));
		if (user.isEmpty()) {
			throw new IllegalArgumentException("User not found");
		}

		Optional<Favorite> favorite = favoriteService.findByUserAndName(user.get(), favoriteName);
		if (favorite.isPresent()) {
			return ResponseEntity.badRequest().body("Favorite already exists");
		}

		favoriteService.saveFavorite(Favorite.builder()
			.name(favoriteName)
			.isPublic(false)
			.user(user.get())
			.build());

		return ResponseEntity.ok().build();
	}
}
