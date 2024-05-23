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

    Optional<Post> findWithStackNameAndCategoryUsingFetchJoinByPostId(Long postId);

    Optional<Post> findByPostIdAndDeletedAtIsNull(Long postId);
    List<Post> findAllByFinishDateBetweenAndPostIdIn(LocalDateTime finishDate, LocalDateTime finishDate2, List<Long> postIdList);
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
    Page<Post> findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByPostIdDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    전체 공모전 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescPostIdDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    검색어 기반 공모전 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    지역, 검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsOrderByPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown,  Pageable pageable);

    /*
    지역, 검색어 기반 공모전 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsOrderByScrapCountDescPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, Pageable pageable);


    /*
    지역, 카테고리, 검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndCategoriesCategoryTypeContainsOrderByPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, @Param("stackNameType") String categoryType,Pageable pageable);

    /*
    지역, 카테고리, 검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndCategoriesCategoryTypeContainsOrderByScrapCountDescPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, @Param("stackNameType") String categoryType,Pageable pageable);


    /*
    전체 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllByPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByPostIdDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    전체 프로젝트 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescPostIdDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    검색어 기반 프로젝트 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByScrapCountDescPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    지역, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsOrderByPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, Pageable pageable);

    /*
    지역, 검색어 기반 프로젝트 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsOrderByScrapCountDescPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, Pageable pageable);


    /*
    지역, 스택, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndStackNamesStackNameTypeContainsOrderByPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, @Param("stackNameType") String stackNameType,Pageable pageable);

    /*
    지역, 스택, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingCityContainsAndMeetingTownContainsAndStackNamesStackNameTypeContainsOrderByScrapCountDescPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, @Param("stackNameType") String stackNameType,Pageable pageable);

    List<Post> findAllByMemberAndStatusInAndDeletedAtIsNullOrderByCreatedAtDesc(Member member, List<PostStatus> statusList);

    Page<Post> findAllByPostIdInOrMember(List<Long> postIdList, Member member, Pageable pageable);
}
