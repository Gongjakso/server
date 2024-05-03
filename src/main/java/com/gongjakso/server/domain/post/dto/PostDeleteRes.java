package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record PostDeleteRes(

    @Schema(
            description = "post가 생성된 순서를 관리하며 숫자가 높을수록 최신 공고임"
    )
    Long postId,

    @Schema(
            description = "member를 식별하는 pk값"
    )
    Long memberId
){
    public static PostDeleteRes of(Post post, Member member){
        return PostDeleteRes.builder()
                .postId(post.getPostId())
                .memberId(member.getMemberId())
                .build();
    }

}
