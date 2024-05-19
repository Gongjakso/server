package com.gongjakso.server.domain.post.util;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.dto.PostScrapRes;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.PostScrap;

import java.util.List;
import java.util.stream.Collectors;

public class PostScrapUtilTest {
    public static PostScrap builderPostScrap(Post post, Member member) {
        return new PostScrap(
                post,
                member,
                true
        );
    }

    public static List<PostScrap> builderMultiplePostScraps(List<Post> posts, Member member){
        return posts.stream()
                .map(post -> new PostScrap(post, member, true))
                .collect(Collectors.toList());
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
