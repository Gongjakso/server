package com.gongjakso.server.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDeleteRes {
    private Long postId;
    private Long memberId;
    private LocalDateTime deletedAt;

    @Builder
    public PostDeleteRes(Long postId, Long memberId, LocalDateTime deletedAt){
        this.postId = postId;
        this.memberId = memberId;
        this.deletedAt = deletedAt;
    }
}
