package com.ll.commars.domain.member.member.entity;

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
public class Member extends BaseEntity {

    private String name;
    private String email;
    private String general;
}
