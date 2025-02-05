package com.ll.commars.domain.auth.auth.entity;


import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@SuperBuilder
@ToString(callSuper = true)
public class AuthResponse {
    private String accessToken;
    private AuthUser authUser;
}
