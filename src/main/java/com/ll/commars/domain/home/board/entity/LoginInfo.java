package com.ll.commars.domain.home.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Data
@AllArgsConstructor // 모든 필드에 대한 생성자가 자동으로 만들어진다.
public class LoginInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int userId;
    private String email;
    private String name;
    private List<String> roles = new ArrayList<>();

    public LoginInfo(int userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }



    public void addRole(String roleName){
        roles.add(roleName);
    }
    private LocalDateTime loginTime;
}