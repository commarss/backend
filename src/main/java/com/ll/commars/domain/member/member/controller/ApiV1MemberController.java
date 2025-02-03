package com.ll.commars.domain.member.member.controller;


import com.ll.commars.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiV1MemberController {
    private final MemberService memberService;

    @GetMapping("/user/profile")
    public ResponseEntity<?> getProfile() {
        return ResponseEntity.ok().body("profile");
    }

    @GetMapping("/public/data")
    public ResponseEntity<?> getPublicData() {
        return ResponseEntity.ok().body("public data");
    }
}
