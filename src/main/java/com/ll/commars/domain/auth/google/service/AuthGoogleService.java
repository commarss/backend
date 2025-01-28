package com.ll.commars.domain.auth.google.service;

import com.ll.commars.domain.auth.google.entity.AuthGoogle;
import com.ll.commars.domain.auth.google.repository.AuthGoogleRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/google")
public class AuthGoogleService {

    private AuthGoogleRepository authGoogleRepository;

    public AuthGoogle write(String email, String name, String picture) {
        AuthGoogle authGoogle = AuthGoogle.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .build();

        return authGoogleRepository.save(authGoogle);
    }
}
