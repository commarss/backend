package com.ll.commars.domain.member.member.service;


import com.ll.commars.domain.member.member.entity.Member;
import com.ll.commars.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Optional<Member> findByIdAndEmail(Long id, String email) {

        return memberRepository.findByIdAndEmail(id, email);
    }

    public Member accessionCheck(Member member) {
        Optional<Member> findMember = memberRepository.findByEmailAndName(member.getEmail(), member.getName());
        return findMember.orElseGet(() -> memberRepository.save(member));
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Optional<Member> findById(long l) {
        return memberRepository.findById(l);
    }
}
