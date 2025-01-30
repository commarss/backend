package com.ll.commars.domain.auth.member.service;

import com.ll.commars.domain.auth.google.entity.GoogleUser;
import com.ll.commars.domain.auth.member.entity.User;
import com.ll.commars.domain.auth.member.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveOrUpdateUser(GoogleUser googleUser) {
        return userRepository.findByEmail(googleUser.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(googleUser.getEmail());
                    newUser.setName(googleUser.getName());
                    newUser.setProfileImageUrl(googleUser.getProfileImageUrl());
                    return userRepository.save(newUser);
                });
    }
}
