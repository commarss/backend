package com.ll.commars.domain.home.main.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/main", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1MainController", description = "메인 화면 API")
public class ApiV1MainController {
    // 상단 바, 왼쪽 바 네비게이션(오늘뭐먹지, 찜, 커뮤니티, ...)
}
