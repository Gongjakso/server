package com.gongjakso.server.domain.post.util;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
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
}
