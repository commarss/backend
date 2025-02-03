package com.ll.commars.domain.home.TodayRandom.controller;

import com.ll.commars.domain.home.TodayRandom.entity.Mypage;
import com.ll.commars.domain.home.TodayRandom.repository.MypageRepository;
import com.ll.commars.domain.home.TodayRandom.service.RestaurantService;
import com.ll.commars.domain.home.board.entity.User;
import com.ll.commars.domain.home.board.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MypageController {
    private final UserRepository userRepository;
    private final MypageRepository mypageRepository;


    public MypageController(UserRepository userRepository, MypageRepository mypageRepository) {
        this.userRepository = userRepository;
        this.mypageRepository = mypageRepository;
    }

    @GetMapping("/score")
    public ResponseEntity<?> getUserReviewScore(@RequestParam String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        int userId = userOptional.get().getUserId();  // int 타입 유지
        Double avgScore = mypageRepository.findAverageScoreByUserId(userId);

        return ResponseEntity.ok(Map.of("score", avgScore != null ? String.format("%.1f", avgScore) : "0.0"));
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> getUserMypageData(@RequestParam String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        int userId = userOptional.get().getUserId();
        List<Mypage> mypageList = mypageRepository.findByUserId(userId);

        return ResponseEntity.ok(mypageList);
    }






}
