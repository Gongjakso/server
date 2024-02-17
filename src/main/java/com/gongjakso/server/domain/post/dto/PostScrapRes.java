package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.PostScrap;
import lombok.Builder;

@Builder
public record PostScrapRes(
        Long postId,
        Long memberId,
        Boolean ScrapStatus
) {
        public static PostScrapRes of(PostScrap postScrap){
            return PostScrapRes.builder()
                    .postId(postScrap.getPost().getPostId())
                    .memberId(postScrap.getMember().getMemberId())
                    .ScrapStatus(postScrap.getScrapStatus())
                    .build();
        }
}
