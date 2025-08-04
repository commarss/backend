package com.ll.commars.domain.user.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.user.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByEmailAndName(String email, String name);

	Optional<User> findByIdAndEmail(Long id, String email);

}