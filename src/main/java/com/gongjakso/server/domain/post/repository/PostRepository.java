package com.gongjakso.server.domain.post.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByPostId(Long postId);

    Optional<Post> findByPostIdAndDeletedAtIsNull(Long postId);

    /*
    전체 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByCreatedAtDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    전체 공모전 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescCreatedAtDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    검색어 기반 공모전 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    지역, 검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingArea") String meetingArea, Pageable pageable);

    /*
    지역, 검색어 기반 공모전 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByScrapCountDescCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingArea") String meetingArea, Pageable pageable);


    /*
    지역, 카테고리, 검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsAndCategoriesCategoryTypeContainsOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingArea") String meetingArea, @Param("stackNameType") String categoryType,Pageable pageable);

    /*
    지역, 카테고리, 검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsAndCategoriesCategoryTypeContainsOrderByScrapCountDescCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingArea") String meetingArea, @Param("stackNameType") String categoryType,Pageable pageable);


    /*
    전체 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllByPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByCreatedAtDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    전체 프로젝트 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescCreatedAtDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    검색어 기반 프로젝트 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    지역, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingArea") String meetingArea, Pageable pageable);

    /*
    지역, 검색어 기반 프로젝트 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByScrapCountDescCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingArea") String meetingArea, Pageable pageable);


    /*
    지역, 스택, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsAndStackNamesStackNameTypeContainsOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingArea") String meetingArea, @Param("stackNameType") String stackNameType,Pageable pageable);

    /*
    지역, 스택, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsAndStackNamesStackNameTypeContainsOrderByScrapCountDescCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingArea") String meetingArea, @Param("stackNameType") String stackNameType,Pageable pageable);

    List<Post> findAllByMemberAndStatusAndDeletedAtIsNull(Member member, PostStatus status);

    List<Post> findAllByPostIdInAndStatusAndDeletedAtIsNull(List<Long> postId, PostStatus status);
}
