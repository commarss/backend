package com.ll.commars.domain.member.member.service;


import com.ll.commars.domain.member.member.entity.Member;
import com.ll.commars.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Optional<Member> findByEmail(String email) {

        return memberRepository.findByEmail(email);
    }
}
