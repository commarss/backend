package com.ll.commars.domain.member.fixture;

import static com.ll.commars.domain.member.entity.AuthType.*;

import java.util.ArrayList;

import org.springframework.boot.test.context.TestComponent;

import com.ll.commars.domain.member.entity.AuthType;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;

import lombok.RequiredArgsConstructor;

import com.navercorp.fixturemonkey.FixtureMonkey;

@TestComponent
@RequiredArgsConstructor
public class MemberFixture {

	private final FixtureMonkey fixtureMonkey;
	private final MemberRepository memberRepository;

	public Member 이메일_사용자(String email, String encodedPassword) {
		Member member = fixtureMonkey.giveMeBuilder(Member.class)
			.set("id", null)
			.set("email", email)
			.set("name", "테스트유저")
			.set("password", encodedPassword)
			.set("authType", EMAIL)
			.set("reviews", new ArrayList<>())
			.set("favorites", new ArrayList<>())
			.set("posts", new ArrayList<>())
			.set("comments", new ArrayList<>())
			.sample();

		return memberRepository.save(member);
	}

	public Member 사용자(String email, String encodedPassword, AuthType authType) {
		Member member = fixtureMonkey.giveMeBuilder(Member.class)
			.set("id", null)
			.set("email", email)
			.set("name", "테스트유저")
			.set("password", encodedPassword)
			.set("authType", authType)
			.set("reviews", new ArrayList<>())
			.set("favorites", new ArrayList<>())
			.set("posts", new ArrayList<>())
			.set("comments", new ArrayList<>())
			.sample();

		return memberRepository.save(member);
	}
}
