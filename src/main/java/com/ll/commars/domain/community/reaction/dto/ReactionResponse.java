package com.ll.commars.domain.community.reaction.dto;

import com.ll.commars.domain.community.reaction.entity.Reaction;

public record ReactionResponse(
	Long id
) {

	public static ReactionResponse from(Reaction reaction) {
		return new ReactionResponse(
			reaction.getId()
		);
	}
}
