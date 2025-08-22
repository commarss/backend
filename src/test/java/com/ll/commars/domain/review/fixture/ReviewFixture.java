package com.ll.commars.domain.review.fixture;

import org.springframework.boot.test.context.TestComponent;

import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.domain.review.repository.jpa.ReviewRepository;

import lombok.RequiredArgsConstructor;

import com.navercorp.fixturemonkey.FixtureMonkey;

@TestComponent
@RequiredArgsConstructor
public class ReviewFixture {

	private final FixtureMonkey fixtureMonkey;
	private final ReviewRepository reviewRepository;

	public Review 리뷰(Member member, Restaurant restaurant, String title, String body, int rate) {
		Review review = fixtureMonkey.giveMeBuilder(Review.class)
			.set("id", null)
			.set("member", member)
			.set("restaurant", restaurant)
			.set("title", title)
			.set("body", body)
			.set("rate", rate)
			.sample();

		restaurant.addReviewAndUpdateAverageRate(review);

		return reviewRepository.save(review);
	}
}
