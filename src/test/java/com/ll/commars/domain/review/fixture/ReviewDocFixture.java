package com.ll.commars.domain.review.fixture;

import java.util.UUID;

import org.springframework.boot.test.context.TestComponent;

import com.ll.commars.domain.review.entity.ReviewDoc;
import com.ll.commars.domain.review.repository.elasticsearch.ReviewDocRepository;

import lombok.RequiredArgsConstructor;

import com.navercorp.fixturemonkey.FixtureMonkey;

@TestComponent
@RequiredArgsConstructor
public class ReviewDocFixture {

	private final FixtureMonkey fixtureMonkey;
	private final ReviewDocRepository reviewDocRepository;

	public ReviewDoc 리뷰(Long memberId, String title, String body, int rate) {
		ReviewDoc reviewDoc = fixtureMonkey.giveMeBuilder(ReviewDoc.class)
			.set("id", UUID.randomUUID().toString())
			.set("memberId", memberId)
			.set("title", title)
			.set("body", body)
			.set("rate", rate)
			.sample();

		return reviewDocRepository.save(reviewDoc);
	}
}
