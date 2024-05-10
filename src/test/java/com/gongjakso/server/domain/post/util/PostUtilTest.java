package com.gongjakso.server.domain.post.util;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.PostScrap;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostUtilTest {
    public  static Post builderPost(Long id){
        Member member = MemberUtilTest.buildMember();
        return new Post(
                1L,
                "Title1",
                member,
                "Content1",
                "Link1",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(16),
                8L,
                MeetingMethod.ONLINE,
                "서울시",
                "마포구",
                true,
                ".com",
                false,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public static Post builderPosts(Long id, String title, String contents, String link) {
        Member member = MemberUtilTest.buildMember();

        return new Post(
                id,
                title,
                member,
                contents,
                link,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(16),
                8L,
                MeetingMethod.ONLINE,
                "서울시",
                "마포구",
                true,
                ".com",
                false,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public static List<Post> builderMultiplePosts() {
        return Arrays.asList(
                builderPosts(1L, "Title1", "Content1", "Link1"),
                builderPosts(2L, "Title2", "Content2", "Link2"),
                builderPosts(3L, "Title3", "Content3", "Link3")
        );
    }

    public static PostScrap builderPostScrap(Post post, Member member, Boolean postStatus){
        return new PostScrap(
                post,
                member,
                postStatus
        );
    }
}
