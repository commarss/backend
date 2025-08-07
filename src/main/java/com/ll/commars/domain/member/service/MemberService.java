package com.ll.commars.domain.member.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.favorite.favorite.dto.FavoriteDto;
import com.ll.commars.domain.favorite.favorite.entity.Favorite;
import com.ll.commars.domain.favorite.favorite.service.FavoriteService;
import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.member.dto.MemberDto;
import com.ll.commars.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final FavoriteService favoriteService;

	public void saveUser(Member member) {
		if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
			throw new RuntimeException("이미 등록된 이메일입니다.");
		}
		memberRepository.save(member);
	}

	public boolean isEmailTaken(String email) {
		return memberRepository.findByEmail(email).isPresent();
	}

	public Member authenticate(String email) {
		return memberRepository.findByEmail(email).orElse(null);
	}

	public Member accessionCheck(Member member) {
		System.out.println("accessiom: " + member);
		Optional<Member> findUser = memberRepository.findByEmailAndName(member.getEmail(), member.getName());
		return findUser.orElseGet(() -> memberRepository.save(member));
	}

	public Optional<Member> findByIdAndEmail(Long id, String email) {
		return memberRepository.findByIdAndEmail(id, email);
	}

	public Optional<Member> findById(long l) {
		return memberRepository.findById(l);
	}

	@Transactional
	public MemberDto.UserFavoriteListsResponse getFavoriteLists(Member member) {
		List<FavoriteDto.FavoriteInfo> favorites = favoriteService.getFavoritesByUser(member)
			.stream()
			.map(favoriteService::toFavoriteInfo)
			.collect(Collectors.toList());

		return MemberDto.UserFavoriteListsResponse.builder()
			.id(member.getId())
			.name(member.getName())
			.favoriteLists(favorites)
			.build();
	}

	public void createFavoriteList(Member member, FavoriteDto.CreateFavoriteListRequest request) {
		FavoriteDto.CreateFavoriteListRequest createFavoriteListRequest = FavoriteDto.CreateFavoriteListRequest.builder()
			.name(request.getName())
			.isPublic(request.getIsPublic())
			.build();

		// 찜 리스트 저장
		favoriteService.saveFavoriteList(member, createFavoriteListRequest);
	}

	public ReviewDto.ShowAllReviewsResponse getReviews(Long userId) {
		List<ReviewDto.ReviewInfo> reviews = memberRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("User not found"))
			.getReviews()
			.stream()
			.map(review -> ReviewDto.ReviewInfo.builder()
				.userId(review.getMember().getId())
				.restaurantId(review.getRestaurant().getId())
				.id(review.getId())
				.name(review.getName())
				.body(review.getBody())
				.rate(review.getRate())
				.build())
			.collect(Collectors.toList());

		return ReviewDto.ShowAllReviewsResponse.builder()
			.reviews(reviews)
			.build();
	}

	public Optional<Member> findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	public Member findByEmailOrNull(String email) {
		return memberRepository.findByEmail(email).orElse(null);
	}

	public ResponseEntity<?> createFavoriteList(String favoriteName, String userId) {
		Optional<Member> user = memberRepository.findById(Long.parseLong(userId));
		if (user.isEmpty()) {
			throw new IllegalArgumentException("User not found");
		}

		Optional<Favorite> favorite = favoriteService.findByUserAndName(user.get(), favoriteName);
		if (favorite.isPresent()) {
			return ResponseEntity.badRequest().body("Favorite already exists");
		}

		favoriteService.saveFavorite(Favorite.builder()
			.name(favoriteName)
			.isPublic(false)
			.member(user.get())
			.build());

		return ResponseEntity.ok().build();
	}
}
