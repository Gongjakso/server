package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.PostScrap;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record PostScrapRes(

        @Schema(
                description = "post가 생성된 순서를 관리하며 숫자가 높을수록 최신 공고임"
        )
        Long postId,

        @Schema(
                description = "member를 식별하는 pk값"
        )
        Long memberId,

        @Schema(
                description = "사용자가 공고를 스크랩 했는지 boolean값으로 판단",
                defaultValue = "0"
        )
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
