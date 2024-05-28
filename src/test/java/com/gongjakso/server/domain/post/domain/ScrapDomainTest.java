package com.gongjakso.server.domain.post.domain;


import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.PostScrap;
import com.gongjakso.server.domain.post.util.PostScrapUtilTest;
import com.gongjakso.server.domain.post.util.PostUtilTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PostScrap 도메인 테스트")
public class ScrapDomainTest{

    private Post post;
    private Member member;

    @BeforeEach
    public void setUp() {
        member = MemberUtilTest.buildMemberAndId(1L);
        post = PostUtilTest.builderPost(1L, member);
    }

    @Nested
    @DisplayName("Builder테스트")
    class BuilderTests {

        @Test
        @DisplayName("Id없이 빌드 되어야 함")
        public void testPostScrapBuilder() {
            PostScrap postScrap = PostScrapUtilTest.builderPostScrap(post, post.getMember());
            System.out.println(postScrap.getMember());
            System.out.println(member);
            assertThat(postScrap.getPost()).isEqualTo(post);
            assertThat(postScrap.getMember()).isEqualTo(member);
            assertThat(postScrap.getScrapStatus()).isTrue();
        }

        @Test
        @DisplayName("Id와 함께 빌드 되어야 함")
        public void testPostScrapBuilderWithId() {
            Long postScrapId = 1L;
            PostScrap postScrap = PostScrapUtilTest.builderPostScrapWithPostAndMember(postScrapId, post, post.getMember());
            assertThat(postScrap.getPostScrapId()).isEqualTo(postScrapId);
            assertThat(postScrap.getPost()).isEqualTo(post);
            assertThat(postScrap.getMember()).isEqualTo(member);
            assertThat(postScrap.getScrapStatus()).isTrue();
        }
    }
}