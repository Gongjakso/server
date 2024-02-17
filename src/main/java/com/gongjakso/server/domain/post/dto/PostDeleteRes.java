package com.gongjakso.server.domain.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDeleteRes {
    private Long postId;
    private Long memberId;

    @Builder
    public PostDeleteRes(Long postId, Long memberId){
        this.postId = postId;
        this.memberId = memberId;
    }
}
