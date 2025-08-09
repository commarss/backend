package com.ll.commars.domain.member.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ll.commars.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);

	Optional<Member> findByEmailAndName(String email, String name);

	Optional<Member> findByIdAndEmail(Long id, String email);

}