package com.ll.commars.global.jwt.controller.copy;


import com.ll.commars.domain.member.member.service.MemberService;
import com.ll.commars.global.jwt.component.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jwt")
@RequiredArgsConstructor
public class ApiV1JwtControllerCopy {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

//    @GetMapping("/refresh")
//    public ResponseEntity<?> refresh(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
//
//        String requestToken = extractRefreshTokenFromCookies(request);
//
//        if (requestToken == null) {
//            return ResponseEntity.badRequest().body("Refresh Token이 존재하지 않습니다.");
//        }
//
//        Optional<Member> member = memberService.findById(Long.parseLong(userDetails.getUsername()));
//
//        String accessToken = jwtProvider.generateAccessToken(member.get());
//        String refreshToken = jwtProvider.generateRefreshToken(member.get());
//
//        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
//                .httpOnly(true)
//                .secure(true)
//                .path("/")
//                .maxAge(jwtProvider.REFRESH_TOKEN_VALIDITY)
//                .sameSite("Strict")
//                .build();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
//                .header("Authorization", "Bearer " + accessToken)
//                .body(Map.of("server", "refresh ok"));
//    }
//
//
//    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
//        if (request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                if (cookie.getName().equals("refreshToken")) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
}
