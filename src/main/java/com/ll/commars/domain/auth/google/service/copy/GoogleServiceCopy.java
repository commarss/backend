package com.ll.commars.domain.auth.google.service.copy;

import com.ll.commars.domain.member.member.entity.Member;
import com.ll.commars.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleServiceCopy {
    MemberService memberService;

    public Optional<Member> loginForGoogle(String idToken) {
        System.out.println("idToken = " + idToken);

        String verificationUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Google의 Token 검증 API 호출
            Map<String, Object> response = restTemplate.getForObject(verificationUrl, Map.class);

            // 1. 토큰이 유효하면 사용자 정보 가져오기
            String email = (String) response.get("email");
            String name = (String) response.get("name");
            String picture = (String) response.get("picture");

            // 2. audience(Client ID) 검증
            String clientId = (String) response.get("aud");
            if (!clientId.equals(clientId)) {
                return Optional.empty();
            }

            // 3. 만료 시간 검증 (Optional - 이미 Google에서 해줌)
            Long exp = Long.parseLong((String) response.get("exp"));
            if (System.currentTimeMillis() / 1000 > exp) {
                return Optional.empty();
            }

            Member googleAuth = new Member().builder()
                    .email(email)
                    .name(name)
                    .profile(picture)
                    .build();

            Member member = memberService.accessionCheck(googleAuth);

            return Optional.of(member);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
