package com.ll.commars.domain.auth.google.entity;

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
public class GoogleAuth extends BaseEntity {
    private String email;
    private String name;
    private String profileImageUrl;
}
