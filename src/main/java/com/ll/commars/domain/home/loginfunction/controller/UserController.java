package com.ll.commars.domain.home.loginfunction.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ll.commars.domain.home.board.entity.LoginInfo;
import com.ll.commars.domain.home.board.entity.User;
import com.ll.commars.domain.home.board.service.UserService;
import com.ll.commars.domain.home.loginfunction.dto.SignupRequest;
import com.ll.commars.domain.home.loginfunction.repository.LoginInfoRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/signup")
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
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃되었습니다.");
    }
}
