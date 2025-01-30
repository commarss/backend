package com.ll.commars.domain.auth.google.entity;

import com.ll.commars.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GoogleAuthResponse extends BaseEntity {
    private String token;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private GoogleUser user;

    public void setUser(GoogleUser user) {
        this.user = user;
    }

}
