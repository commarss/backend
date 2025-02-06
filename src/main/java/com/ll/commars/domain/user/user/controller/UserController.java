package com.ll.commars.domain.user.user.controller;

import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.user.favorite.dto.FavoriteDto;
import com.ll.commars.domain.user.user.dto.UserDto;
import com.ll.commars.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/users", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "UserController", description = "사용자 API")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<String> signup(@RequestBody User request) {
        if (userService.isEmailTaken(request.getEmail())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 이메일입니다.");
        }

        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setName(request.getName());
        newUser.setGender(request.getGender());

        userService.saveUser(newUser);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<?> login(@RequestBody User user, HttpSession session) {
        logger.info("Login attempt for email: {}", user.getEmail());
        User authenticatedUser = userService.authenticate(user.getEmail(), user.getPassword());

        if (authenticatedUser != null) {
            logger.info("Login successful for email: {}", user.getEmail());

            // 로그인 정보를 DB에 저장하지 않고, 로그만 기록
            logger.info("User {} logged in at {}", authenticatedUser.getEmail(), java.time.LocalDateTime.now());

            // 세션에 사용자 정보 저장
            session.setAttribute("user", authenticatedUser);
            // 확인용 로그 추가
            logger.info("User stored in session: {}", authenticatedUser.getEmail());
            logger.info("Session user set: {}", session.getAttribute("user"));  // 추가 확인 로그
            System.out.println("세션정보: "+session.getAttribute("user"));

            return ResponseEntity.ok(authenticatedUser);
        }

        logger.warn("Login failed for email: {}", user.getEmail());
        return ResponseEntity.status(401).body("이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    @GetMapping("/current-user")
    @Operation(summary = "사용자의 현재 로그인상태여부")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // 이메일을 JSON 형식으로 반환
            Map<String, String> response = new HashMap<>();
            response.put("email", user.getEmail());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body("로그인된 사용자가 없습니다.");
    }



    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    // 내 위치 기반 식당 찾기

    @GetMapping("/favorites")
    @Operation(summary = "회원의 모든 찜 리스트 조회")
    public RsData<UserDto.UserFavoriteListsResponse> getFavoriteLists(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new RsData<>("401", "로그인이 필요합니다.", null);
        }

        UserDto.UserFavoriteListsResponse response = userService.getFavoriteLists(user);
        return new RsData<>("200", "찜 리스트 조회 성공", response);
    }

    @PostMapping("/favorite")
    @Operation(summary = "찜 리스트 생성")
    public RsData<String> addFavorite(
            @RequestBody FavoriteDto.CreateFavoriteListRequest request,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new RsData<>("401", "로그인이 필요합니다.", null);
        }

        userService.createFavoriteList(user, request);
        return new RsData<>("201", "찜 추가 성공", "찜 리스트 생성 성공");
    }

    @GetMapping("/reviews")
    @Operation(summary = "회원이 작성한 리뷰 전체 조회")
    public RsData<ReviewDto.ShowAllReviewsResponse> getReviews(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return new RsData<>("401", "로그인이 필요합니다.", null);
        }

        ReviewDto.ShowAllReviewsResponse response = userService.getReviews(user.getId());

        return new RsData<>("200", "모든 리뷰 조회 성공", response);
    }
}
