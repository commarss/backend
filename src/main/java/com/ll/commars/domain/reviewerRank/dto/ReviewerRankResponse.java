package com.ll.commars.domain.reviewerRank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewerRankResponse {
    private List<ReviewerRank> top;     // 상위 3명
    private List<ReviewerRank> others;  // 나머지 유저
}
