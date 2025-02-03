package com.ll.commars.domain.auth.google.repository;

import com.ll.commars.domain.auth.google.entity.GoogleAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoogleAuthRepository extends JpaRepository<GoogleAuth, Long> {
    Optional<GoogleAuth> findByEmail(String email);
}
