package com.gongjakso.server.domain.post.repository;

import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.enumerate.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByPostId(Long postId);

    Optional<Post> findByPostIdAndDeletedAtIsNull(Long postId);
    
    /*
    전체 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllByPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByCreatedAtDesc
    (@Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, Pageable pageable);

    /*
    지역, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingArea") String meetingArea, Pageable pageable);

    /*
    지역, 스택, 검색어 기반 프로젝트 공고 목록 조회 최신순
     */
    Page<Post> findAllPostsJoinedWithStackNamesByTitleContainsAndPostTypeTrueAndDeletedAtIsNullAndFinishDateAfterAndStatusAndMeetingAreaContainsAndStackNamesStackNameTypeContainsOrderByCreatedAtDesc
    (@Param("searchWord") String searchWord, @Param("currentTimestamp") LocalDateTime currentTimestamp, @Param("status") PostStatus status, @Param("meetingArea") String meetingArea, @Param("stackNameType") String stackNameType,Pageable pageable);

}
