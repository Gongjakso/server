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
    public  static Post builderPost(Long id, Member member){
        return new Post(
                id,
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

    public static Post builderPosts(Long id, String title, String contents, String link, Boolean postType) {
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
                postType,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public static List<Post> builderMultiplePosts(Boolean postType) {
        return Arrays.asList(
                builderPosts(1L, "Title1", "Content1", "Link1", postType),
                builderPosts(5L, "제목5", "Content5", "Link5", postType),
                builderPosts(3L, "Title3", "Content3", "Link3", postType),
                builderPosts(4L, "제목4", "Content4", "Link4", postType),
                builderPosts(2L, "Title2", "Content2", "Link2", postType),
                builderPosts(6L, "제목6", "Content6", "Link6", postType),
                builderPosts(7L, "제목7", "Content7", "Link7", postType)
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
