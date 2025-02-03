package com.ll.commars.domain.home.loginfunction.controller;


/*
import com.ll.commars.domain.home.loginfunction.dto.KakaoTokenResponseDto;

import com.ll.commars.domain.home.loginfunction.dto.KakaoUserInfoResponseDto;

import com.ll.commars.domain.home.loginfunction.service.KakaoService;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@CrossOrigin(origins = "http://localhost:5173")

@Slf4j

@Controller

@RequiredArgsConstructor

@RequestMapping("")

public class KakaoLoginController {


    private final KakaoService kakaoService;


// 카카오 로그아웃 API 호출

    public void kakaoLogout(String accessToken) {

        String url = "https://kapi.kakao.com/v1/user/logout";


        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + accessToken);


        HttpEntity<String> entity = new HttpEntity<>(headers);


        RestTemplate restTemplate = new RestTemplate();

        try {

// 로그아웃 요청을 전송

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            log.info("카카오 로그아웃 API 호출 성공: " + response.getStatusCode()); // 로그아웃 성공 로그

        } catch (Exception e) {

            log.error("카카오 로그아웃 API 호출 실패", e); // 실패시 에러 로그

        }

    }


    @GetMapping("/callback")

    public String callback(@RequestParam("code") String code, HttpSession session) {

// 카카오에서 access token을 얻음

        String accessToken = kakaoService.getAccessTokenFromKakao(code);


// KakaoTokenResponseDto 객체에 accessToken 저장

        KakaoTokenResponseDto tokenResponse = new KakaoTokenResponseDto();

        tokenResponse.setAccessToken(accessToken);


// 사용자 정보 얻기

        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);


// 세션에 사용자 정보와 KakaoTokenResponseDto 저장

        session.setAttribute("user", userInfo);

        session.setAttribute("kakaoToken", tokenResponse); // accessToken을 KakaoTokenResponseDto로 세션에 저장


// 로그인 후 성공 페이지로 리다이렉트

        return "redirect:/login/success";

    }


    @GetMapping("/login/success")

    public String loginSuccessPage(HttpSession session, Model model) {

        KakaoUserInfoResponseDto userInfo = (KakaoUserInfoResponseDto) session.getAttribute("user");


        if (userInfo != null && userInfo.getKakaoAccount() != null && userInfo.getKakaoAccount().getProfile() != null) {

            model.addAttribute("user", userInfo);

            return "login_success"; // login_success.html 템플릿을 반환

        } else {

            return "redirect:/"; // 세션에 정보가 없으면 로그인 페이지로 리다이렉트

        }

    }


    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // 세션에서 KakaoTokenResponseDto 객체 가져오기
        KakaoTokenResponseDto tokenResponse = (KakaoTokenResponseDto) session.getAttribute("kakaoToken");

        if (tokenResponse != null && tokenResponse.getAccessToken() != null) {
            // 카카오 로그아웃 처리
            String accessToken = tokenResponse.getAccessToken(); // accessToken을 가져옴
            log.info("로그아웃 요청, accessToken: " + accessToken);
            kakaoLogout(accessToken); // 카카오 로그아웃 호출
        } else {
            log.info("로그아웃 시 accessToken이 존재하지 않음");
        }

        // 세션에서 사용자 정보와 token 삭제
        session.removeAttribute("user");
        session.removeAttribute("kakaoToken"); // KakaoTokenResponseDto 삭제

        // 세션 무효화 (세션을 완전히 종료)
        session.invalidate();
        log.info("세션 무효화 완료");

        // 로그아웃 메시지 추가
        redirectAttributes.addFlashAttribute("message", "로그아웃되었습니다.");
        log.info("로그아웃 처리 완료");

        return "redirect:" + "/login/page"; // 카카오 로그인 페이지로 리다이렉트
    }



}

 */