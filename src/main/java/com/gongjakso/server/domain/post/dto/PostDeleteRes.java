package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import lombok.Builder;

@Builder
public record PostDeleteRes(
    Long postId,
    Long memberId
){
    public static PostDeleteRes of(Post post, Member member){
        return PostDeleteRes.builder()
                .postId(post.getPostId())
                .memberId(member.getMemberId())
                .build();
    }

}
