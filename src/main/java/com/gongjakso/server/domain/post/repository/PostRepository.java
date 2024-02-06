package com.gongjakso.server.domain.post.repository;

import com.gongjakso.server.domain.post.dto.GetProjectRes;
import com.gongjakso.server.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    //Page<Post> findAllOrderByCreatedAtDesc(Pageable pageable); //전체 조회(페이징)

    //Page<Post> findByCategory(StackName stackname, Pageable pageable);
    Post findByPostId(Long postId); //applyService
    @Query("SELECT new com.gongjakso.server.domain.post.dto.GetProjectRes(p.postId, p.title, m.name, p.status, p.startDate, p.finishDate) " +
            "FROM Post p JOIN p.member m WHERE p.postType = true and p.deletedAt is null and p.finishDate >= CURRENT_TIMESTAMP")
    Page<GetProjectRes> findAllProjects(Pageable pageable);

    Optional<Post> findByPostIdAndDeletedAtIsNull(Long postId);
}
