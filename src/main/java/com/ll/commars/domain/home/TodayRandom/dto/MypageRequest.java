package com.ll.commars.domain.home.TodayRandom.dto;

import com.ll.commars.domain.home.board.entity.User;
import lombok.Data;

@Data
public class MypageRequest {
    private User user;
    private Long restaurantId;
    private String reviewer;
    private String keywords;
    private String review;
    private double score; // 리뷰 점수 추가
}
