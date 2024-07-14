package com.gongjakso.server.domain.post.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import com.gongjakso.server.domain.post.projection.ContestProjection;
import com.gongjakso.server.domain.post.projection.ProjectProjection;
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
    @Query(value = """
    select p.post_id
    from post p
    left join member m on p.member_id = m.member_id
    left join category c on p.post_id = c.post_id
    where p.post_type = false
    and p.deleted_at is null
    and p.finish_date > :currentTimestamp
    and p.status in (:status)
    and (
        case
            when :searchWord is not null and :searchWord != '' then match(p.title, p.contents) against(:searchWord in natural language mode)
            else true
        end
        )
    and (
        case
            when :meetingCity is not null and :meetingCity != '' then p.meeting_city = :meetingCity
            else true
        end
        )
    and (
        case
            when :meetingTown is not null and :meetingTown != '' then p.meeting_town = :meetingTown
            else true
        end
        )
    group by p.post_id
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM post p
    LEFT JOIN member m ON p.member_id = m.member_id
    LEFT JOIN category c ON p.post_id = c.post_id
    WHERE p.post_type = false
    AND p.deleted_at IS NULL
    AND p.finish_date > :currentTimestamp
    AND p.status IN (:status)
    AND (
        CASE
            WHEN :searchWord IS NOT NULL AND :searchWord != '' THEN MATCH(p.title, p.contents) AGAINST(:searchWord IN natural language mode)
            ELSE 1=1
        END
    )
    AND (
        CASE
            WHEN :meetingCity IS NOT NULL AND :meetingCity != '' THEN p.meeting_city = :meetingCity
            ELSE 1=1
        END
    )
    AND (
        CASE
            WHEN :meetingTown IS NOT NULL AND :meetingTown != '' THEN p.meeting_town = :meetingTown
            ELSE 1=1
        END
    )
    group by p.post_id
    """,
            nativeQuery = true)
    Page<Long> findContestPaginationByFilter(@Param("searchWord") String searchWord,
                                                          @Param("currentTimestamp") LocalDateTime currentTimestamp,
                                                          @Param("status") List<PostStatus> status,
                                                          @Param("meetingCity") String meetingCity,
                                                          @Param("meetingTown") String meetingTown,
                                                          Pageable pageable);

    @Query(value = """
    select p.post_id as postId, p.title as title, m.member_id as memberId, m.name as memberName, p.status as status, p.start_date as startDate, p.end_date as endDate, p.finish_date as finishDate, p.days_remaining as daysRemaining, c.category_id as categoryId, c.category_type as categoryType, c.size as categorySize, p.scrap_count as scrapCount
    from post p
    left join member m on p.member_id = m.member_id
    left join category c on p.post_id = c.post_id
    where p.post_id in (:postIdList)
    order by p.created_at desc
    """, nativeQuery = true
    )
    List<ContestProjection> findContestProjectionListByPostIdListAndCreatedAtDesc(@Param("postIdList") List<Long> postIdList);

    @Query(value = """
    select p.post_id as postId, p.title as title, m.member_id as memberId, m.name as memberName, p.status as status, p.start_date as startDate, p.end_date as endDate, p.finish_date as finishDate, p.days_remaining as daysRemaining, c.category_id as categoryId, c.category_type as categoryType, c.size as categorySize, p.scrap_count as scrapCount
    from post p
    left join member m on p.member_id = m.member_id
    left join category c on p.post_id = c.post_id
    where p.post_id in (:postIdList)
    order by p.scrap_count desc
    """, nativeQuery = true
    )
    List<ContestProjection> findContestProjectionListByPostIdListAndScrapCountAtDesc(@Param("postIdList") List<Long> postIdList);

    /*
    전체 프로젝트 공고 목록 조회 최신순
     */
    @Query(value = """
    select p.post_id
    from post p
    left join member m on p.member_id = m.member_id
    left join category c on p.post_id = c.post_id
    left join stack_name sn on p.post_id = sn.post_id
    WHERE p.post_type = true
    AND p.deleted_at IS NULL
    AND p.finish_date > :currentTimestamp
    AND p.status IN (:status)
    and (
        case
            when :searchWord is not null and :searchWord != '' then match(p.title, p.contents) against(:searchWord in natural language mode)
            else true
        end
        )
    and (
        case
            when :meetingCity is not null and :meetingCity != '' then p.meeting_city = :meetingCity
            else true
        end
        )
    and (
        case
            when :meetingTown is not null and :meetingTown != '' then p.meeting_town = :meetingTown
            else true
            end
        )
    and (
        case
            when :stackName is not null and :stackName != '' then sn.stack_name_type = :stackName
            else true
        end
        )
    group by p.post_id
    """,
            countQuery = """
    select count(*)
    FROM post p
    left join member m on p.member_id = m.member_id
    left join category c on p.post_id = c.post_id
    left join stack_name sn on p.post_id = sn.post_id
    WHERE p.post_type = true
    AND p.deleted_at IS NULL
    AND p.finish_date > :currentTimestamp
    AND p.status IN (:status)
    and (
        case
            when :searchWord is not null and :searchWord != '' then match(p.title, p.contents) against(:searchWord in natural language mode)
            else 1=1
        end
        )
    and (
        case
            when :meetingCity is not null and :meetingCity != '' then p.meeting_city = :meetingCity
            else 1=1
        end
        )
    and (
        case
            when :meetingTown is not null and :meetingTown != '' then p.meeting_town = :meetingTown
            else 1=1
            end
        )
    and (
        case
            when :stackName is not null and :stackName != '' then sn.stack_name_type = :stackName
            else 1=1
        end
        )
    group by p.post_id
    """,
            nativeQuery = true)
    Page<Long> findProjectPaginationByFilter(
            @Param("searchWord") String searchWord,
            @Param("currentTimestamp") LocalDateTime currentTimestamp,
            @Param("status") List<PostStatus> status,
            @Param("meetingCity") String meetingCity,
            @Param("meetingTown") String meetingTown,
            @Param("stackName") String stackName,
            Pageable pageable
    );

    @Query(value = """
    select p.post_id as postId, p.title as title, m.member_id as memberId, m.name as memberName, p.status as status, p.start_date as startDate, p.end_date as endDate, p.finish_date as finishDate, p.days_remaining as daysRemaining, c.category_id as categoryId, c.category_type as categoryType, c.size as categorySize, sn.stack_name_id as stackNameId, sn.stack_name_type as stackNameType, p.scrap_count as scrapCount
    from post p
    left join member m on p.member_id = m.member_id
    left join category c on p.post_id = c.post_id
    left join stack_name sn on p.post_id = sn.post_id
    where p.post_id in (:postIdList)
    order by p.created_at desc
    """, nativeQuery = true)
    List<ProjectProjection> findProjectProjectionListByPostIdListAndCreatedAtDesc(@Param("postIdList") List<Long> postIdList);

    @Query(value = """
    select p.post_id as postId, p.title as title, m.member_id as memberId, m.name as memberName, p.status as status, p.start_date as startDate, p.end_date as endDate, p.finish_date as finishDate, p.days_remaining as daysRemaining, c.category_id as categoryId, c.category_type as categoryType, c.size as categorySize, sn.stack_name_id as stackNameId, sn.stack_name_type as stackNameType, p.scrap_count as scrapCount
    from post p
    left join member m on p.member_id = m.member_id
    left join category c on p.post_id = c.post_id
    left join stack_name sn on p.post_id = sn.post_id
    where p.post_id in (:postIdList)
    order by p.scrap_count desc
    """, nativeQuery = true)
    List<ProjectProjection> findProjectProjectionListByPostIdListAndScrapCountDesc(@Param("postIdList") List<Long> postIdList);


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
