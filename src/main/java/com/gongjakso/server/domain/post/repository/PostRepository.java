package com.gongjakso.server.domain.post.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByPostId(Long postId);

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
    Page<Post> findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusInOrderByPostIdDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, Pageable pageable);

    /*
    전체 공모전 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusInOrderByScrapCountDescPostIdDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, Pageable pageable);

    /*
    검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusInOrderByPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, Pageable pageable);

    /*
    검색어 기반 공모전 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusInOrderByScrapCountDescPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, Pageable pageable);

    /*
    지역, 검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusInAndMeetingCityContainsAndMeetingTownContainsOrderByPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown,  Pageable pageable);

    /*
    지역, 검색어 기반 공모전 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusInAndMeetingCityContainsAndMeetingTownContainsOrderByScrapCountDescPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, Pageable pageable);


    /*
    지역, 카테고리, 검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusInAndMeetingCityContainsAndMeetingTownContainsAndCategoriesCategoryTypeContainsOrderByPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, @Param("stackNameType") String categoryType,Pageable pageable);

    /*
    지역, 카테고리, 검색어 기반 공모전 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithCategoriesByTitleContainsAndPostTypeFalseAndDeletedAtIsNullAndFinishDateAfterAndStatusInAndMeetingCityContainsAndMeetingTownContainsAndCategoriesCategoryTypeContainsOrderByScrapCountDescPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, @Param("stackNameType") String categoryType,Pageable pageable);


    /*
    전체 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllByPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusInOrderByPostIdDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, Pageable pageable);

    /*
    전체 프로젝트 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusInOrderByScrapCountDescPostIdDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, Pageable pageable);

    /*
    검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusInOrderByPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, Pageable pageable);

    /*
    검색어 기반 프로젝트 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusInOrderByScrapCountDescPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, Pageable pageable);

    /*
    지역, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusInAndMeetingCityContainsAndMeetingTownContainsOrderByPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, Pageable pageable);

    /*
    지역, 검색어 기반 프로젝트 공고 목록 조회 스크랩순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusInAndMeetingCityContainsAndMeetingTownContainsOrderByScrapCountDescPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, Pageable pageable);


    /*
    지역, 스택, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusInAndMeetingCityContainsAndMeetingTownContainsAndStackNamesStackNameTypeContainsOrderByPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, @Param("stackNameType") String stackNameType,Pageable pageable);

    /*
    지역, 스택, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusInAndMeetingCityContainsAndMeetingTownContainsAndStackNamesStackNameTypeContainsOrderByScrapCountDescPostIdDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") List<PostStatus> status, @Param("meetingCity") String meetingCity, @Param("meetingTown") String meetingTown, @Param("stackNameType") String stackNameType,Pageable pageable);

    List<Post> findAllByMemberAndStatusInAndDeletedAtIsNullOrderByCreatedAtDesc(Member member, List<PostStatus> statusList);

    @Query(value = "SELECT DISTINCT p " +
            "FROM Post p " +
            "LEFT JOIN FETCH p.member m " +
            "LEFT JOIN FETCH p.categories c " +
            "LEFT JOIN Apply a ON p.postId = a.post.postId " +
            "WHERE ((a.member.memberId = :memberId " +
            "AND a.deletedAt IS NULL " +
            "AND a.isCanceled = false " +
            "AND a.applyType = 'PASS') OR (p.member.memberId = :memberId)) " +
            "AND p.deletedAt IS NULL " +
            "AND p.status IN :postStatus " +
            "ORDER BY p.createdAt DESC",
            countQuery = "SELECT COUNT(DISTINCT p) " +
                    "FROM Post p " +
                    "LEFT JOIN Apply a ON p.postId = a.post.postId " +
                    "WHERE ((a.member.memberId = :memberId " +
                    "AND a.deletedAt IS NULL " +
                    "AND a.isCanceled = false " +
                    "AND a.applyType = 'PASS') OR (p.member.memberId = :memberId)) " +
                    "AND p.deletedAt IS NULL " +
                    "AND p.status IN :postStatus")
    Page<Post> findPostsByMemberIdAndPostStatusInOrderByCreatedAtDesc(@Param("memberId") Long memberId, @Param("postStatus") List<PostStatus> postStatusList, Pageable pageable);
}
