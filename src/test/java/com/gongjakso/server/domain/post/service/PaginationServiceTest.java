package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import com.gongjakso.server.domain.post.dto.GetContestRes;
import com.gongjakso.server.domain.post.dto.PostScrapRes;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.PostScrap;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.domain.post.repository.PostScrapRepository;
import com.gongjakso.server.domain.post.util.PostUtilTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PaginationServiceTest {
    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    @Mock
    PostScrapRepository postScrapRepository;

    private PostScrap postScrap;
    @Test
    @DisplayName("공모전 공고 전체 조회 최신순")
    void getContestsByCreatedAt() {
        // given
        List<Post> testPosts = PostUtilTest.builderMultiplePosts();
        Pageable pageable = PageRequest.of(0, 6);
        Page<Post> testPage = new PageImpl<>(testPosts, pageable, testPosts.size()); // Page 생성

        given(postRepository.findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByPostIdDesc(
                any(LocalDateTime.class), any(PostStatus.class), any(Pageable.class)
        )).willReturn(testPage);

        // when
        Page<GetContestRes> res= postService.getContests("createdAt", pageable);

        // then
        assertThat(res).isNotNull();
        assertThat(res.getTotalElements() ).isEqualTo(3);
        assertThat(res.getContent().get(0).title()).isEqualTo("Title1");
        assertThat(res.getContent().get(1).title()).isEqualTo("Title2");
        assertThat(res.getContent().get(2).title()).isEqualTo("Title3");
    }

    @Test
    @DisplayName("공모전 공고 전체 조회 인기순")
    void getContestsByScrapCount() {
        // given
        List<Post> testPosts = PostUtilTest.builderMultiplePosts();
        Pageable pageable = PageRequest.of(0, 6);
        Page<Post> testPage = new PageImpl<>(testPosts, pageable, testPosts.size()); // Page 생성

        given(postRepository.findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescPostIdDesc(
                any(LocalDateTime.class), any(PostStatus.class), any(Pageable.class)
        )).willReturn(testPage);

        // when
        Page<GetContestRes> res= postService.getContests("scrapCount", pageable);

        // then
        assertThat(res).isNotNull();
        assertThat(res.getTotalElements() ).isEqualTo(3);
        assertThat(res.getContent().get(0).title()).isEqualTo("Title1");
        assertThat(res.getContent().get(1).title()).isEqualTo("Title2");
        assertThat(res.getContent().get(2).title()).isEqualTo("Title3");
    }

    @Test
    @DisplayName("공고 스크랩 기능")
    void scrapPost() {
        // Given
        Post post = PostUtilTest.builderPost(1L);
        Member member = MemberUtilTest.buildMemberAndId(1L);
        PostScrap postScrap = PostUtilTest.builderPostScrap(post, member, true);

        given(postRepository.findByPostIdAndDeletedAtIsNull(any(Long.class)))
                .willReturn(Optional.of(post));
        given(postScrapRepository.findByPostAndMember(any(Post.class), any(Member.class)))
                .willReturn(null)
                .willReturn(postScrap);

        // When
        PostScrapRes res = postService.scrapPost(member, post.getPostId());

        // Then
        PostScrap savedPostScrap = postScrapRepository.findByPostAndMember(post, member);
        assertThat(savedPostScrap).isNotNull();
        System.out.println("=====================" + savedPostScrap);

        assertThat(res).isNotNull();
        assertThat(res.postId()).isEqualTo(1L);
        assertThat(res.memberId()).isEqualTo(member.getMemberId());
        assertThat(res.ScrapStatus()).isTrue();
        assertThat(savedPostScrap.getPost().getScrapCount()).isEqualTo(1);

        // given
        given(postScrapRepository.findByPostAndMember(any(Post.class), any(Member.class)))
                .willReturn(savedPostScrap);

        // When
        res = postService.scrapPost(member, post.getPostId());

        // Then
        PostScrap canceledPostScrap = postScrapRepository.findByPostAndMember(post, member);
        assertThat(canceledPostScrap).isNotNull();
        System.out.println("=====================" + canceledPostScrap);

        assertThat(res.ScrapStatus()).isFalse();
        assertThat(canceledPostScrap.getPost().getScrapCount()).isEqualTo(0);
    }

}
