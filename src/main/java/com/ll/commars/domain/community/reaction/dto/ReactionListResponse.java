package com.ll.commars.domain.community.reaction.dto;

import java.util.List;

public record ReactionListResponse(
	List<ReactionResponse> reactions
) {

	public static ReactionListResponse of(List<ReactionResponse> reactions) {
		return new ReactionListResponse(reactions);
	}
}
