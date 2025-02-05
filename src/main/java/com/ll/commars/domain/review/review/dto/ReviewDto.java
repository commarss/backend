package com.ll.commars.domain.review.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ReviewDto {
    @Getter
    @Builder
    public static class ReviewInfo{
        private String userName;
        private String restaurantName;
        private String reviewName;
        private String body;
        private Integer rate;
    }

    @Getter
    @Builder
    public static class ReviewWriteRequest{
        private Long userId;
        private String reviewName;
        private String body;
        private Integer rate;
    }

    @Getter
    @Builder
    public static class ReviewWriteResponse{
        private String userName;
        private String restaurantName;
        private String reviewName;
        private String body;
        private Integer rate;
    }

    @Getter
    @Builder
    public static class ReviewShowAllResponse{
        List<ReviewInfo> reviews;
    }
}
