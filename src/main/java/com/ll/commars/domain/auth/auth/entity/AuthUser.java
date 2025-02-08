package com.ll.commars.domain.auth.auth.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
public class AuthUser {
    private Long id;  // 🔹 ID 필드 추가
    private String name;
    private String email;
    private Integer gender;
    private String profileImageUrl;
    private String phoneNumber;
    private LocalDateTime birthDate;
}
