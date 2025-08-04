package com.ll.commars.domain.auth.google.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleService {

	private final UserService userService;

	public Optional<User> loginForGoogle(String idToken) {
		System.out.println("idToken = " + idToken);

		String verificationUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
		RestTemplate restTemplate = new RestTemplate();

		try {
			// Google의 Token 검증 API 호출
			Map<String, Object> response = restTemplate.getForObject(verificationUrl, Map.class);

			User requestUser = User.builder()
				.socialProvider(3)
				.phoneNumber("00000000000")
				.email((String)response.get("email"))
				.name((String)response.get("name"))
				.profileImageUrl((String)response.get("picture"))
				.build();

			// 2. audience(Client ID) 검증
			String clientId = (String)response.get("aud");
			if (!clientId.equals(clientId)) {
				return Optional.empty();
			}

			// 3. 만료 시간 검증 (Optional - 이미 Google에서 해줌)
			Long exp = Long.parseLong((String)response.get("exp"));
			if (System.currentTimeMillis() / 1000 > exp) {
				return Optional.empty();
			}

			return Optional.ofNullable(userService.accessionCheck(requestUser));

		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

}
