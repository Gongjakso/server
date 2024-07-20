package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.member.util.MemberUtilTest;
import com.gongjakso.server.domain.post.dto.GetContestRes;
import com.gongjakso.server.domain.post.dto.GetProjectRes;
import com.gongjakso.server.domain.post.dto.PostScrapRes;
import com.gongjakso.server.domain.post.entity.Category;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.PostScrap;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.domain.post.repository.PostScrapRepository;
import com.gongjakso.server.domain.post.util.PostScrapUtilTest;
import com.gongjakso.server.domain.post.util.PostUtilTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("페이지네이션 및 스크랩 공고 Service 테스트")
@ExtendWith(MockitoExtension.class)
public class PaginationServiceTest {
    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    @Mock
    PostScrapRepository postScrapRepository;

    @Nested
    @DisplayName("공모전 공고 페이지네이션")
    class ContestTests{

        @Nested
        @DisplayName("공모전 공고 전체 조회")
        class ContestTestsAll{

            @Test
            @DisplayName("공모전 공고 전체 조회 최신순")
            void getContestsByCreatedAt() {
                // given
                List<Post> testPosts = PostUtilTest.builderMultiplePosts(false);
                Pageable pageable = PageRequest.of(0, 6);
                Page<Post> testPage = new PageImpl<>(testPosts, pageable, testPosts.size()); // Page 생성

                given(postRepository.findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByPostIdDesc(
                        any(LocalDateTime.class), any(PostStatus.class), any(Pageable.class)
                )).willReturn(testPage);

                // when
                Page<GetContestRes> res = postService.getContests("createdAt", pageable);

                // then
                assertThat(res).isNotNull();
                assertThat(res.getTotalElements()).isEqualTo(7);
                assertThat(res.getTotalPages()).isEqualTo(2);
                assertThat(res.getContent().get(0).title()).isEqualTo("Title1");
                assertThat(res.getContent().get(1).title()).isEqualTo("Title2");
                assertThat(res.getContent().get(2).title()).isEqualTo("Title3");
            }

            @Test
            @DisplayName("공모전 공고 전체 조회 인기순")
            void getContestsByScrapCount() {
                // given
                List<Post> testPosts = PostUtilTest.builderMultiplePosts(false);
                testPosts.get(3).setScrapCount(10); // 제목4
                testPosts.get(6).setScrapCount(8);  // 제목7
                testPosts.sort(Comparator.comparing(Post::getScrapCount)
                        .thenComparing(Post::getPostId).reversed());
                Pageable pageable = PageRequest.of(0, 6);
                Page<Post> testPage = new PageImpl<>(testPosts, pageable, testPosts.size()); // Page 생성


                given(postRepository.findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescPostIdDesc(
                        any(LocalDateTime.class), any(PostStatus.class), any(Pageable.class)
                )).willReturn(testPage);

                // When
                Page<GetContestRes> res = postService.getContests("scrapCount", pageable);

                // then
                assertThat(res).isNotNull();
                assertThat(res.getTotalElements()).isEqualTo(7);
                assertThat(res.getTotalPages()).isEqualTo(2);
                assertThat(res.getContent().get(0).title()).isEqualTo("제목4");
                assertThat(res.getContent().get(1).title()).isEqualTo("제목7");
                // 스크랩 순 -> 최신 순으로 정렬되는 지 확인
                assertThat(res.getContent().get(2).title()).isEqualTo("제목6");
                assertThat(res.getContent().get(3).title()).isEqualTo("제목5");
            }
        }
        @Nested
        @DisplayName("공모전 공고 검색어 기반 조회")
        class ContestTestsWithSearchWord {

            @Test
            @DisplayName("공모전 공고 검색어 기반 조회 최신순")
            void getContestsByCreatedAt() {
                // given
                String searchWord = "제목";
                List<Post> testPosts =  PostUtilTest.builderMultiplePosts(false).stream()
                        .filter(post -> post.getTitle().contains(searchWord))
                        .collect(Collectors.toList());
                Pageable pageable = PageRequest.of(0, 6);
                Page<Post> testPage = new PageImpl<>(testPosts, pageable, testPosts.size());

                given(postRepository.findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByPostIdDesc(
                        any(String.class), any(LocalDateTime.class), any(PostStatus.class), any(Pageable.class)
                )).willReturn(testPage);

                // when
                Page<GetContestRes> res = postService.getContestsBySearchWord("createdAt", searchWord, pageable);

                // then
                assertThat(res).isNotNull();
                assertThat(res.getTotalElements()).isEqualTo(4);
                assertThat(res.getTotalPages()).isEqualTo(1);
                assertThat(res.getContent().get(0).title()).isEqualTo("제목4");
                assertThat(res.getContent().get(1).title()).isEqualTo("제목5");
                assertThat(res.getContent().get(2).title()).isEqualTo("제목6");
            }

            @Test
            @DisplayName("공모전 공고 검색어 기반 조회 인기순")
            void getContestsByScrapCount() {
                // given
                String searchWord = "제목";
                List<Post> testPosts =  PostUtilTest.builderMultiplePosts(false);
                testPosts.get(6).setScrapCount(10); // 제목7
                testPosts.get(5).setScrapCount(8);  // 제목6
                testPosts.sort(Comparator.comparing(Post::getScrapCount)
                        .thenComparing(Post::getPostId).reversed());
                testPosts = testPosts.stream()
                        .filter(post -> post.getTitle().contains(searchWord))
                        .collect(Collectors.toList());
                Pageable pageable = PageRequest.of(0, 6);
                Page<Post> testPage = new PageImpl<>(testPosts, pageable, testPosts.size()); // Page 생성

                given(postRepository.findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescPostIdDesc(
                        any(String.class), any(LocalDateTime.class), any(PostStatus.class), any(Pageable.class)
                )).willReturn(testPage);

                // when
                Page<GetContestRes> res = postService.getContestsBySearchWord("scrapCount", searchWord, pageable);

                // then
                assertThat(res).isNotNull();
                assertThat(res.getTotalElements()).isEqualTo(4);
                assertThat(res.getTotalPages()).isEqualTo(1);
                assertThat(res.getContent().get(0).title()).isEqualTo("제목7");
                assertThat(res.getContent().get(1).title()).isEqualTo("제목6");
                assertThat(res.getContent().get(2).title()).isEqualTo("제목5");
            }
        }

        @Nested
        @DisplayName("공모전 공고 검색어, 지역, 스택 기반 기반 조회")
        class ContestTestsWithMeetingAreaAndCategoryAndSearchWord {
            @Test
            @DisplayName("공모전 공고 검색어 기반 조회 최신순")
            void getContestsByCreatedAt() {
                // given
                String searchWord = "제목";
                String meetingCity = "서울시";
                String meetingTown = "마포구";
                String category = "BE";
                List<Post> testPosts = PostUtilTest.builderMultiplePosts(false);
                testPosts = testPosts.stream()
                        .filter(post -> post.getTitle().contains(searchWord) &&
                                post.getMeetingCity().contains(meetingCity) &&
                                post.getMeetingTown().contains(meetingTown) &&
                                post.getCategories().stream()
                                        .map(Category::getCategoryType)
                                        .anyMatch(catType -> String.valueOf(catType).contains(category)))
                        .collect(Collectors.toList());
                Pageable pageable = PageRequest.of(0, 6);
                Page<Post> testPage = new PageImpl<>(testPosts, pageable, testPosts.size());

                given(postRepository.findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndCategoriesCategoryTypeContainsOrderByPostIdDesc(
                        any(String.class), any(LocalDateTime.class), any(PostStatus.class),  any(String.class), any(String.class), any(String.class), any(Pageable.class)
                )).willReturn(testPage);

                // when
                Page<GetContestRes> res = postService.getContestsByMeetingAreaAndCategoryAndSearchWord("createdAt", meetingCity, meetingTown, category, searchWord, pageable);

                // then
                assertThat(res).isNotNull();
                assertThat(res.getTotalElements()).isEqualTo(2);
                assertThat(res.getTotalPages()).isEqualTo(1);
                assertThat(res.getContent().get(0).title()).isEqualTo("제목4");
                assertThat(res.getContent().get(1).title()).isEqualTo("제목5");
            }

            @Test
            @DisplayName("공모전 공고 검색어 기반 조회 인기순")
            void getContestsByScrapCount() {
                // given
                String searchWord = "제목";
                String meetingCity = "서울시";
                String meetingTown = "마포구";
                String category = "BE";
                List<Post> testPosts = PostUtilTest.builderMultiplePosts(true);
                testPosts.get(4).setScrapCount(10); // 제목7
                testPosts.get(3).setScrapCount(8);  // 제목6
                testPosts.sort(Comparator.comparing(Post::getScrapCount)
                        .thenComparing(Post::getPostId).reversed());
                testPosts = testPosts.stream()
                        .filter(post -> post.getTitle().contains(searchWord) &&
                                post.getMeetingCity().contains(meetingCity) &&
                                post.getMeetingTown().contains(meetingTown) &&
                                post.getCategories().stream()
                                        .map(Category::getCategoryType)
                                        .anyMatch(catType -> String.valueOf(catType).contains(category)))
                        .collect(Collectors.toList());
                Pageable pageable = PageRequest.of(0, 6);
                Page<Post> testPage = new PageImpl<>(testPosts, pageable, testPosts.size());

                given(postRepository.findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndCategoriesCategoryTypeContainsOrderByScrapCountDescPostIdDesc(
                        any(String.class), any(LocalDateTime.class), any(PostStatus.class),  any(String.class), any(String.class), any(String.class), any(Pageable.class)
                )).willReturn(testPage);

                // when
                Page<GetContestRes> res = postService.getContestsByMeetingAreaAndCategoryAndSearchWord("scrapCount", meetingCity, meetingTown, category, searchWord, pageable);

                // then
                assertThat(res).isNotNull();
                assertThat(res.getTotalElements()).isEqualTo(2);
                assertThat(res.getTotalPages()).isEqualTo(1);
                assertThat(res.getContent().get(0).title()).isEqualTo("제목5");
                assertThat(res.getContent().get(1).title()).isEqualTo("제목4");
            }

        }
    }

    @Nested
    @DisplayName("공고 스크랩 테스트")
    class scrapTests {

        @Test
        @DisplayName("공고 스크랩 저장 및 취소 기능")
        void scrapPost() {
            // Given
            Member member = MemberUtilTest.buildMemberAndId(1L);
            Post post = PostUtilTest.builderPost(1L, member);
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

            assertThat(res).isNotNull();
            assertThat(res.postId()).isEqualTo(1L);
            assertThat(res.memberId()).isEqualTo(member.getMemberId());
            assertThat(res.scrapStatus()).isTrue();
            assertThat(savedPostScrap.getPost().getScrapCount()).isEqualTo(1);

            // given
            given(postScrapRepository.findByPostAndMember(any(Post.class), any(Member.class)))
                    .willReturn(savedPostScrap);

            // When
            res = postService.scrapPost(member, post.getPostId());

            // Then
            PostScrap canceledPostScrap = postScrapRepository.findByPostAndMember(post, member);
            assertThat(canceledPostScrap).isNotNull();

            assertThat(res.scrapStatus()).isFalse();
            assertThat(canceledPostScrap.getPost().getScrapCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("공고 스크랩 조회 기능")
        void scrapGet() {
            // Given
            Member member = MemberUtilTest.buildMemberAndId(1L);
            Post post = PostUtilTest.builderPost(1L, member);
            PostScrap postScrap = PostUtilTest.builderPostScrap(post, member, true);

            given(postRepository.findByPostIdAndDeletedAtIsNull(any(Long.class)))
                    .willReturn(Optional.of(post));
            given(postScrapRepository.findByPostAndMember(any(Post.class), any(Member.class)))
                    .willReturn(null)
                    .willReturn(postScrap);

            // When
            PostScrapRes res = postService.scrapPost(member, post.getPostId());
            PostScrap getPostScrap = postScrapRepository.findByPostAndMember(post, member);

            // Then
            assertThat(getPostScrap).isNotNull();
            assertThat(res).isNotNull();
            assertThat(res.postId()).isEqualTo(1L);
            assertThat(res.memberId()).isEqualTo(member.getMemberId());
            assertThat(res.scrapStatus()).isTrue();
            assertThat(getPostScrap.getPost().getScrapCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("내가 스크랩한 프로젝트 목록 조회 및 페이지네이션 기능 테스트")
        void getMyScrapProjectTest() {
            // given
            Member member = MemberUtilTest.buildMemberAndId(1L);
            List<Post> testPosts = PostUtilTest.builderMultiplePosts(false);
            List<PostScrap> testPostScraps= PostScrapUtilTest.builderMultiplePostScraps(testPosts, member).stream()
                    .filter(PostScrap::getScrapStatus)
                    .collect(Collectors.toList());
            Pageable pageable = PageRequest.of(0, 6);
            Page<PostScrap> testPage = new PageImpl<>(testPostScraps, pageable, testPostScraps.size()); // Page 생성


            given(postScrapRepository.findAllByMemberAndPostPostTypeTrueAndPostDeletedAtIsNullAndScrapStatusTrueOrderByPostScrapIdDesc(
                    any(Member.class), any(Pageable.class)
            )).willReturn(testPage);

            // when
            Page<GetProjectRes> res = postService.getMyScrapProject(member, pageable);

            // then
            assertThat(res).isNotNull();
            assertThat(res.getTotalElements()).isEqualTo(7);
            assertThat(res.getTotalPages()).isEqualTo(2);
            assertThat(res.getContent().get(0).title()).isEqualTo("Title1");
            assertThat(res.getContent().get(1).title()).isEqualTo("Title2");
            assertThat(res.getContent().get(2).title()).isEqualTo("Title3");
        }

        @Test
        @DisplayName("내가 스크랩한 공모전 목록 조회 및 페이지네이션 기능 테스트")
        void getMyScrapContestTest() {
            // given
            Member member = MemberUtilTest.buildMemberAndId(1L);
            List<Post> testPosts = PostUtilTest.builderMultiplePosts(false);
            List<PostScrap> testPostScraps= PostScrapUtilTest.builderMultiplePostScraps(testPosts, member).stream()
                    .filter(PostScrap::getScrapStatus)
                    .collect(Collectors.toList());
            Pageable pageable = PageRequest.of(0, 6);
            Page<PostScrap> testPage = new PageImpl<>(testPostScraps, pageable, testPostScraps.size()); // Page 생성


            given(postScrapRepository.findAllByMemberAndPostPostTypeFalseAndPostDeletedAtIsNullAndScrapStatusTrueOrderByPostScrapIdDesc(
                    any(Member.class), any(Pageable.class)
            )).willReturn(testPage);

            // when
            Page<GetContestRes> res = postService.getMyScrapContest(member, pageable);

            // then
            assertThat(res).isNotNull();
            assertThat(res.getTotalElements()).isEqualTo(7);
            assertThat(res.getTotalPages()).isEqualTo(2);
            assertThat(res.getContent().get(0).title()).isEqualTo("Title1");
            assertThat(res.getContent().get(1).title()).isEqualTo("Title2");
            assertThat(res.getContent().get(2).title()).isEqualTo("Title3");
        }
    }
}
