package com.ll.commars.domain.home.loginfunction.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String gender;
}
