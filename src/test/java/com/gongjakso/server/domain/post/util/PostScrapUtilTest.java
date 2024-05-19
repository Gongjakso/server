package com.gongjakso.server.domain.post.util;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.dto.PostScrapRes;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.PostScrap;

public class PostScrapUtilTest {
    public static PostScrap builderPostScrap(Post post, Member member) {
        return new PostScrap(
                post,
                member,
                true
        );
    }


    public static PostScrap builderPostScrapWithPostAndMember(Long PostScrapId, Post post, Member member) {
        return new PostScrap(
                PostScrapId,
                post,
                member,
                true
        );
    }

    public static PostScrapRes ScrapPostRes(Long postId, Long memberId, boolean scrapStatus){
        return PostScrapRes.builder()
                .postId(postId)
                .memberId(memberId)
                .ScrapStatus(scrapStatus)
                .build();
    }
}
