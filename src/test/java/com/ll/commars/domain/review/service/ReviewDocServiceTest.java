package com.ll.commars.domain.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import com.ll.commars.domain.review.dto.ReviewSearchResponse;
import com.ll.commars.domain.review.entity.ReviewDoc;
import com.ll.commars.domain.review.fixture.ReviewDocFixture;
import com.ll.commars.domain.review.repository.elasticsearch.ReviewDocRepository;
import com.ll.commars.global.annotation.IntegrationTest;

import com.navercorp.fixturemonkey.FixtureMonkey;

@IntegrationTest
@DisplayName("ReviewDocService 테스트")
class ReviewDocServiceTest {

	@Autowired
	private ReviewDocService reviewDocService;

	@Autowired
	private ReviewDocRepository reviewDocRepository;

	@Autowired
	private FixtureMonkey fixtureMonkey;

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	private ReviewDocFixture reviewDocFixture;

	@BeforeEach
	void setUp() {
		this.reviewDocFixture = new ReviewDocFixture(fixtureMonkey, reviewDocRepository);
	}

	@AfterEach
	void tearDown() {
		reviewDocRepository.deleteAll();
	}

	@Nested
	class 리뷰_키워드_검색_테스트 {

		@BeforeEach
		void setUp() {
			reviewDocFixture.리뷰(1L, "정말 맛있는 맛집", "인생 최고의 맛집입니다. 꼭 가보세요.", 5);
			reviewDocFixture.리뷰(2L, "분위기 좋은 곳", "데이트하기 좋은 맛집이에요. 분위기가 다 했어요.", 4);
			reviewDocFixture.리뷰(3L, "가성비 최고", "가격도 저렴하고 맛도 최고입니다.", 5);
		}

		@Test
		void 키워드로_리뷰를_성공적으로_검색한다() {
			// given
			String keyword = "맛집";

			// when
			ReviewSearchResponse response = reviewDocService.searchByKeyword(keyword);

			// then
			List<ReviewSearchResponse.ReviewSearchInfo> reviews = response.reviews();
			assertAll(
				() -> assertThat(reviews).hasSize(2),
				() -> assertThat(reviews).extracting("title").contains("정말 맛있는 맛집", "분위기 좋은 곳")
			);
		}

		@Test
		void 검색_결과가_없으면_빈_목록을_반환한다() {
			// given
			String keyword = "존재하지 않는 키워드";

			// when
			ReviewSearchResponse response = reviewDocService.searchByKeyword(keyword);

			// then
			assertThat(response.reviews()).isEmpty();
		}
	}

	@Nested
	class 리뷰_검색_결과_정렬_테스트 {

		private ReviewDoc highRateReview;
		private ReviewDoc lowRateReview;

		@BeforeEach
		void setUp() {
			highRateReview = reviewDocFixture.리뷰(1L, "최고의 경험", "내용", 5);
			lowRateReview = reviewDocFixture.리뷰(2L, "나름 최고의 선택", "내용", 3);
			reviewDocFixture.리뷰(3L, "관련 없는 문서", "아무 내용", 1);
		}

		@Test
		void 평점이_높은_리뷰가_검색_결과_상위에_위치한다() {
			// given
			String keyword = "최고";

			// when
			ReviewSearchResponse response = reviewDocService.searchByKeyword(keyword);

			// then
			List<ReviewSearchResponse.ReviewSearchInfo> reviews = response.reviews();
			assertAll(
				() -> assertThat(reviews).hasSize(2),
				() -> assertThat(reviews.get(0).title()).isEqualTo(highRateReview.getTitle()),
				() -> assertThat(reviews.get(1).title()).isEqualTo(lowRateReview.getTitle())
			);
		}
	}
}
