package com.ll.commars.domain.popularKeywords.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/popular-keywords", produces = APPLICATION_JSON_VALUE)
@Tag(name = "ApiV1PopularKeywordsController", description = "인기 키워드 API")
public class ApiV1PopularKeywordsController {
}
