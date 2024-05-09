package com.gongjakso.server.domain.post.util;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.MeetingMethod;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostUtilTest {
    public  static Post builderPost(){
        Member member = MemberUtilTest.buildMember();

        return Post.builder()
                .title("Title1")
                .member(member)
                .contents("Content1")
                .contestLink("Link1")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .finishDate(LocalDateTime.now().plusDays(16))
                .maxPerson(8L)
                .meetingMethod(MeetingMethod.ONLINE)
                .meetingCity("서울시")
                .meetingTown("마포구")
                .questionMethod(true)
                .questionLink(".com")
                .postType(false)
                .categories(new ArrayList<>())
                .stackNames(new ArrayList<>())
                .build();
    }

    public static Post builderPosts(String title, String contents, String link) {
        Member member = MemberUtilTest.buildMember();

        return Post.builder()
                .title(title)
                .member(member)
                .contents(contents)
                .contestLink(link)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .finishDate(LocalDateTime.now().plusDays(16))
                .maxPerson(8L)
                .meetingMethod(MeetingMethod.ONLINE)
                .meetingCity("서울시")
                .meetingTown("마포구")
                .questionMethod(true)
                .questionLink(".com")
                .postType(false)
                .categories(new ArrayList<>())
                .stackNames(new ArrayList<>())
                .build();
    }

    public static List<Post> builderMultiplePosts() {
        return Arrays.asList(
                builderPosts("Title1", "Content1", "Link1"),
                builderPosts("Title2", "Content2", "Link2"),
                builderPosts("Title3", "Content3", "Link3")
        );
    }
}
