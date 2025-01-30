package com.ll.commars.domain.auth.auth.entity;

import com.ll.commars.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LoginRequest extends BaseEntity {
    private String token;
    private String name;
    private String email;
    private String profileImageUrl;
}
