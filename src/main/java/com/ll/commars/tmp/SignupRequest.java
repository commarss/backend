package com.ll.commars.tmp;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String name;
    private String gender;
}
