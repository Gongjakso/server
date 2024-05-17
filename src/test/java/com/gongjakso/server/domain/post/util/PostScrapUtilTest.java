package com.gongjakso.server.domain.post.util;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import com.gongjakso.server.domain.post.dto.PostScrapRes;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.PostScrap;

public class PostScrapUtilTest {

    public static PostScrap builderPostScrap() {
        Member member = MemberUtilTest.buildMemberAndId(1L);
        Post post = PostUtilTest.builderPost(1L);
        return new PostScrap(
                1L,
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
