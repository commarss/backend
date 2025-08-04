package com.ll.commars.domain.auth.auth.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
