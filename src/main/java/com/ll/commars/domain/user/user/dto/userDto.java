package com.ll.commars.domain.user.user.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.commars.domain.community.board.entity.Board;
import com.ll.commars.domain.community.comment.entity.Comment;
import com.ll.commars.domain.review.review.entity.Review;
import com.ll.commars.domain.user.favorite.dto.FavoriteDto;
import com.ll.commars.domain.user.favorite.entity.Favorite;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class userDto {
    @Getter
    @Builder
    public static class UserInfo {
        private Long id;
        private Integer socialProvider;
        private String email;
        private String name;
        private String password;
        private String loginId;
        private String phoneNumber;
        private String profileImageUrl;
        private LocalDateTime birthDate;
    }

    @Getter
    @Builder
    public static class UserFavoriteListsResponse {
        private Long id; // 사용자 ID
        private String name; // 사용자 이름
        private List<FavoriteDto.FavoriteInfo> favoriteLists;
    }
}

