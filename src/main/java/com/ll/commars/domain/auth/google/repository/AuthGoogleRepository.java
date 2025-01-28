package com.ll.commars.domain.auth.google.repository;

import com.ll.commars.domain.auth.google.entity.AuthGoogle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthGoogleRepository extends JpaRepository<AuthGoogle, Long> {
    AuthGoogle findByEmail(String email);
}
