package com.ll.commars.domain.community.comment.dto;

import com.ll.commars.domain.community.comment.entity.Reply;

import lombok.Getter;

@Getter
public class ReplyDto {

	private Long id;
	private String content;
	private Long userId;
	private String username;

	public ReplyDto(Reply reply) {
		this.id = reply.getId();
		this.content = reply.getContent();
		this.userId = reply.getUser().getId();
		this.username = reply.getUser().getName();
	}
}
