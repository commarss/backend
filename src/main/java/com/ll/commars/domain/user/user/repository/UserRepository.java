package com.ll.commars.domain.user.user.repository;

import com.ll.commars.domain.user.user.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndName(String email, String name);

    Optional<User> findByIdAndEmail(Long id, String email);

    Optional<User> findBySocialProviderAndKakaoId(@NotNull Integer socialProvider, Long kakaoId);
}