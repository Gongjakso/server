package com.gongjakso.server.domain.post.repository;

import com.gongjakso.server.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByPostId(Long post_id);
}
