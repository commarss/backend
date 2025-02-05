package com.ll.commars.domain.restaurant.businessHour.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/restaurant/business-hour", produces = APPLICATION_JSON_VALUE)
public class ApiV1BusinessHourController {
}
