package com.ll.commars.domain.auth.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.time.Duration;
import java.time.Instant;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.auth.dto.TokenReissueResponse;
import com.ll.commars.domain.auth.token.TokenProvider;
import com.ll.commars.domain.auth.token.entity.AccessToken;
import com.ll.commars.domain.auth.token.entity.JwtClaims;
import com.ll.commars.domain.auth.token.entity.RefreshToken;
import com.ll.commars.domain.auth.token.entity.TokenValue;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final TokenProvider tokenProvider;
	private final MemberRepository memberRepository;

	// todo: StringRedisTemplate vs. RedisTemplate<String, String>
	private final RedisTemplate<String, String> redisTemplate;

	@Transactional
	public void signOut(TokenValue accessTokenValue) {
		JwtClaims claims = tokenProvider.parseClaim(accessTokenValue);

		Instant expiration = claims.publicClaims().expiresAt();
		Instant now = Instant.now();
		long remainingMillis = Duration.between(now, expiration).toMillis();

		if (remainingMillis > 0) {
			redisTemplate.opsForValue().set(
				accessTokenValue.value(),
				"logout",
				Duration.ofMillis(remainingMillis)
			);
		}
	}

	@Transactional
	public TokenReissueResponse reissueToken(String refreshTokenValueString) {
		JwtClaims claims = tokenProvider.parseClaim(TokenValue.of(refreshTokenValueString));
		Long userId = claims.privateClaims().userId();

		String savedRefreshToken = redisTemplate.opsForValue().get("refreshToken" + userId);
		if (savedRefreshToken == null || !savedRefreshToken.equals(refreshTokenValueString)) {
			throw new CustomException(INVALID_TOKEN);
		}

		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		AccessToken newAccessToken = tokenProvider.generateAccessToken(member);
		RefreshToken newRefreshToken = tokenProvider.generateRefreshToken(member);

		redisTemplate.opsForValue().set(
			"refreshToken" + userId,
			newRefreshToken.token().value(),
			Duration.ofMillis(newRefreshToken.expiration())
		);

		return new TokenReissueResponse(
			newAccessToken.token().value(),
			newRefreshToken.token().value()
		);
	}

	@Transactional
	public void withdraw(TokenValue accessTokenValue) {
		JwtClaims claims = tokenProvider.parseClaim(accessTokenValue);
		Long userId = claims.privateClaims().userId();

		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		memberRepository.delete(member);

		signOut(accessTokenValue);

		redisTemplate.delete("refreshToken" + userId);
	}

	// public ResponseEntity<?> login(User user) {
	// 	AuthUser authUser = toAuthUser(user);
	//
	// 	String accessToken = jwtProvider.generateAccessToken(user);
	// 	String refreshToken = jwtProvider.generateRefreshToken(user);
	//
	// 	ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
	// 		.httpOnly(true)
	// 		.secure(true)
	// 		.path("/")
	// 		.maxAge(jwtProvider.REFRESH_TOKEN_VALIDITY)
	// 		.sameSite("Strict")
	// 		.build();
	//
	// 	return ResponseEntity.ok()
	// 		.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
	// 		.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
	// 		.body(AuthResponse.builder()
	// 			.accessToken(accessToken)
	// 			.authUser(authUser)
	// 			.build());
	// }
	//
	// public AuthUser toAuthUser(User user) {
	// 	return AuthUser.builder()
	// 		.id(user.getId())  // 🔹 ID 추가
	// 		.name(user.getName())
	// 		.email(user.getEmail())
	// 		.profileImageUrl(user.getProfileImageUrl())
	// 		.gender(user.getGender())
	// 		.phoneNumber(user.getPhoneNumber())
	// 		.birthDate(user.getBirthDate())
	// 		.build();
	// }
	//
	// public Optional<User> loginForGoogle(String idToken) {
	// 	System.out.println("idToken = " + idToken);
	//
	// 	String verificationUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
	// 	RestTemplate restTemplate = new RestTemplate();
	//
	// 	try {
	// 		// Google의 Token 검증 API 호출
	// 		Map<String, Object> response = restTemplate.getForObject(verificationUrl, Map.class);
	//
	// 		User requestUser = User.builder()
	// 			.socialProvider(3)
	// 			.phoneNumber("00000000000")
	// 			.email((String)response.get("email"))
	// 			.name((String)response.get("name"))
	// 			.profileImageUrl((String)response.get("picture"))
	// 			.build();
	//
	// 		// 2. audience(Client ID) 검증
	// 		String clientId = (String)response.get("aud");
	// 		if (!clientId.equals(clientId)) {
	// 			return Optional.empty();
	// 		}
	//
	// 		// 3. 만료 시간 검증 (Optional - 이미 Google에서 해줌)
	// 		Long exp = Long.parseLong((String)response.get("exp"));
	// 		if (System.currentTimeMillis() / 1000 > exp) {
	// 			return Optional.empty();
	// 		}
	//
	// 		return Optional.ofNullable(userService.accessionCheck(requestUser));
	//
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 		return Optional.empty();
	// 	}
	// }
	//
	// public String getAccessToken(String code) {
	// 	String tokenUrl = "https://kauth.kakao.com/oauth/token";
	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	//
	// 	MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	// 	params.add("grant_type", "authorization_code");
	// 	params.add("client_id", clientId);
	// 	params.add("redirect_uri", redirectUri);
	// 	params.add("code", code);
	//
	// 	HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
	//
	// 	try {
	// 		RestTemplate restTemplate = new RestTemplate();
	// 		ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, Map.class);
	//
	// 		if (response.getStatusCodeValue() != 200) {
	// 			System.err.println("토큰 요청 실패: " + response);
	// 			return null;
	// 		}
	//
	// 		return (String)response.getBody().get("access_token");
	//
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 		return null;
	// 	}
	// }
	//
	// public Map<String, Object> getUserProfile(String accessToken) {
	// 	String profileUrl = "https://kapi.kakao.com/v2/user/me";
	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.set("Authorization", "Bearer " + accessToken);
	//
	// 	HttpEntity<String> entity = new HttpEntity<>(headers);
	// 	RestTemplate restTemplate = new RestTemplate();
	//
	// 	ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
	// 		profileUrl,
	// 		HttpMethod.GET,
	// 		entity,
	// 		new ParameterizedTypeReference<Map<String, Object>>() {
	// 		}
	// 	);
	//
	// 	return response.getBody();
	// }
	//
	// /**
	//  * Kakao의 사용자 프로필 정보를 기반으로 내부 User 엔티티로 변환하고, 가입/로그인 처리
	//  */
	// public User loginForKakao(Map<String, Object> userProfile) {
	// 	// Kakao API에서 제공하는 고유 식별자인 id값 추출
	// 	Long kakaoId = ((Number)userProfile.get("id")).longValue();
	//
	// 	// kakao_account와 profile 데이터 추출
	// 	Map<String, Object> kakaoAccount = (Map<String, Object>)userProfile.get("kakao_account");
	// 	Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");
	//
	// 	String nickname = (String)profile.get("nickname");
	// 	String profileImageUrl = (String)profile.get("profile_image_url");
	// 	String email = kakaoId + "@kakao.com"; // 카카오에서 이메일 제공x, 카카오 고유 id로 임시 이메일 생성하기
	//
	// 	// User 엔티티 생성
	// 	User kakaoUser = User.builder()
	// 		.socialProvider(1)
	// 		.email(nickname + kakaoId + "@kakao.com")          // 이메일이 필요하면 사용 (현재 주석 처리)
	// 		.name(nickname)
	// 		.profileImageUrl(profileImageUrl)
	// 		.build();
	//
	// 	return userService.accessionCheck(kakaoUser);
	// }
	//
	// public String getAccessToken(String code, String state) {
	// 	String tokenUrl = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code"
	// 		+ "&client_id=" + clientId
	// 		+ "&client_secret=" + clientSecret
	// 		+ "&code=" + code
	// 		+ "&state=" + state;
	//
	// 	try {
	// 		RestTemplate restTemplate = new RestTemplate();
	// 		ResponseEntity<Map> response = restTemplate.getForEntity(tokenUrl, Map.class);
	//
	// 		if (response.getStatusCodeValue() != 200) {
	// 			System.err.println("response = " + response);
	// 			return null;
	// 		}
	//
	// 		return (String)response.getBody().get("access_token");
	//
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 		return null;
	// 	}
	// }
	//
	// public Map<String, Object> getUserProfile(String accessToken) {
	// 	String profileUrl = "https://openapi.naver.com/v1/nid/me";
	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.set("Authorization", "Bearer " + accessToken);
	//
	// 	HttpEntity<String> entity = new HttpEntity<>(headers);
	// 	RestTemplate restTemplate = new RestTemplate();
	// 	ResponseEntity<Map<String, Object>> response = restTemplate.exchange(profileUrl, HttpMethod.GET, entity,
	// 		new ParameterizedTypeReference<Map<String, Object>>() {
	// 		});
	//
	// 	return (Map<String, Object>)response.getBody().get("response");
	// }
	//
	// public User loginForNaver(Map<String, Object> userProfile) {
	// 	System.out.println(userProfile);
	// 	User naverUser = User.builder()
	// 		.socialProvider(2)
	// 		.phoneNumber((String)userProfile.get("mobile"))
	// 		.email((String)userProfile.get("email"))
	// 		.name((String)userProfile.get("name"))
	// 		.profileImageUrl((String)userProfile.get("profile_image"))
	// 		.build();
	//
	// 	return userService.accessionCheck(naverUser);
	// }
}
