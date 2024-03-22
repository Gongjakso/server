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
    Post findWithStackNameAndCategoryUsingFetchJoinByPostId(Long postId);

    Optional<Post> findByPostIdAndDeletedAtIsNull(Long postId);
    List<Post> findAllByEndDateBetweenAndPostIdIn(LocalDateTime endDate, LocalDateTime endDate2, List<Long> postIdList);
    /*
    내가 모집 중인 공모전 공고 개수
     */
    Long countByMemberAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatus
    (Member member, LocalDateTime currentTimestamp, PostStatus status);

    /*
    내가 모집 중인 프로젝트 공고 개수
     */
    Long countByMemberAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatus
    (Member member, LocalDateTime currentTimestamp, PostStatus status);

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
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown,  Pageable pageable);

    /*
    지역, 검색어 기반 공모전 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsOrderByScrapCountDescCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, Pageable pageable);


    /*
    지역, 카테고리, 검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndCategoriesCategoryTypeContainsOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, @Param("stackNameType") String categoryType,Pageable pageable);

    /*
    지역, 카테고리, 검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndCategoriesCategoryTypeContainsOrderByScrapCountDescCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, @Param("stackNameType") String categoryType,Pageable pageable);


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
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, Pageable pageable);

    /*
    지역, 검색어 기반 프로젝트 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsOrderByScrapCountDescCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, Pageable pageable);


    /*
    지역, 스택, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndStackNamesStackNameTypeContainsOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, @Param("stackNameType") String stackNameType,Pageable pageable);

    /*
    지역, 스택, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndStackNamesStackNameTypeContainsOrderByScrapCountDescCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, @Param("stackNameType") String stackNameType,Pageable pageable);

    List<Post> findAllByMemberAndStatusAndDeletedAtIsNull(Member member, PostStatus status);

    List<Post> findAllByPostIdInAndStatusAndDeletedAtIsNull(List<Long> postId, PostStatus status);
}
