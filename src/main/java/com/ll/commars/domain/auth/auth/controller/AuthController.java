package com.ll.commars.domain.auth.auth.controller;


import com.ll.commars.domain.auth.authUserInfo.entity.AuthUserInfo;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;
import com.ll.commars.domain.user.user.service.UserService;
import com.ll.commars.domain.user.user.dto.userDTO;
import com.ll.commars.domain.auth.authUserInfo.repository.AuthUserInfoRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private UserRepository userRepository;
    private AuthUserInfoRepository authUserInfoRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody userDTO userDTO, HttpSession session) {
        try {
            User user = userService.findByEmail(userDTO.getEmail());
            if (user != null && user.getPassword().equals(userDTO.getPassword())) {
                AuthUserInfo authUserInfo = new AuthUserInfo(user.getId(), user.getEmail(), user.getName());
                authUserInfo.setLoginTime(LocalDateTime.now());

                // 로그인 기록을 DB에 저장
                authUserInfoRepository.save(authUserInfo);

                session.setAttribute("user", authUserInfo); // 세션에 사용자 정보 저장
                return ResponseEntity.ok(authUserInfo);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 일치하지 않습니다.");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 실패: " + ex.getMessage());
        }
    }

    // 로그아웃 처리
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // 세션 초기화
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    // 현재 사용자 정보 조회
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        AuthUserInfo authUserInfo = (AuthUserInfo) session.getAttribute("user");
        if (authUserInfo != null) {
            // DB에서 사용자 정보 조회
            User user = userService.findByEmail(authUserInfo.getEmail());
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인된 사용자가 없습니다.");
    }

}

/*

// 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpSession session) {
        try {
            User user = userService.findByEmail(loginDto.getEmail());
            if (user != null && user.getPassword().equals(loginDto.getPassword())) {
                LoginInfo loginInfo = new LoginInfo(user.getUserId(), user.getEmail(), user.getName());
                session.setAttribute("user", loginInfo); // 세션에 사용자 정보 저장
                return ResponseEntity.ok(loginInfo);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일 또는 비밀번호가 일치하지 않습니다.");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 실패: " + ex.getMessage());
        }
    }


    // 현재 사용자 정보 조회
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        LoginInfo user = (LoginInfo) session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인된 사용자가 없습니다.");
        }
    }
 */
