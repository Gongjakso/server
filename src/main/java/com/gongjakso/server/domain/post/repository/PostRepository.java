package com.gongjakso.server.domain.post.repository;

import com.gongjakso.server.domain.post.dto.GetProjectRes;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import com.gongjakso.server.domain.post.enumerate.StackNameType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByPostId(Long postId); //applyService
    @Query("SELECT new com.gongjakso.server.domain.post.dto.GetProjectRes(p.postId, p.title, m.name, p.status, p.startDate, p.finishDate) " +
            "FROM Post p JOIN p.member m WHERE p.postType = true and p.deletedAt is null and p.finishDate >= CURRENT_TIMESTAMP and p.status = 'RECRUITING'")
    Page<GetProjectRes> findAllProjects(Pageable pageable);

    Optional<Post> findByPostIdAndDeletedAtIsNull(Long postId);

    /*
    검색어 기반 조회
     */
    Page<Post> findAllByTitleContainsAndPostTypeIsTrueAndDeletedAtIsNullAndFinishDateAfterAndStatus
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    스택 기반 조회
    @Query("SELECT new com.gongjakso.server.domain.post.dto.GetProjectRes(p.postId, p.title, m.name, p.status, p.startDate, p.finishDate) " +
            "FROM Post p JOIN p.member m JOIN p.stackNames sn " +
            "WHERE LOWER(FUNCTION('REPLACE', p.title, ' ', '')) LIKE %:searchWord% AND p.postType = true AND p.deletedAt IS NULL AND p.finishDate >= CURRENT_TIMESTAMP AND p.status = 'RECRUITING' " +
            "AND LOWER(sn.stackNameType) = LOWER(:stackName)")
    Page<GetProjectRes> findByStackNameAndSearchWord(@Param("stackName") String stackName,
                                                     @Param("searchWord") String searchWord,
                                                     Pageable pageable);
     */
    Page<Post> findAllByTitleContainsAndPostTypeIsTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndStackNamesContaining
            (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("stackName") StackNameType stackName, @Param("pageable") Pageable pageable);

    /*
    지역 기반 조회 -> 스택 기반 조회 기능 완성 시 추가 필요
     */
    Page<Post> findAllByTitleContainsAndPostTypeIsTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContains
            (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingArea") String meetingArea, Pageable pageable);

}
