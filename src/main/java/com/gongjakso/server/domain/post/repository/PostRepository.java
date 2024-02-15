package com.gongjakso.server.domain.post.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    //Page<Post> findAll(Pageable pageable); //전체 조회(페이징)
    //Page<Post> findByCategory(StackName stackname, Pageable pageable);
    Post findByPostId(Long post_id);
    Optional<Post> findByPostIdAndDeletedAtIsNull(Long postId);

    List<Post> findAllByMemberAndDeletedAtIsNull(Member member);
}
