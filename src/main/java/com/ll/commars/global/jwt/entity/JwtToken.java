package com.ll.commars.global.jwt.entity;


import com.ll.commars.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
public class JwtToken {
    private String token;
    private Long id;
    private String email;
    private Date expiration;
}
