package com.gongjakso.server.domain.post.repository;

import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.domain.post.enumerate.StackNameType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StackNameRepository extends JpaRepository<StackName, Long> {
    List<StackName> findStackNameByPost(Post post);
    StackName findStackNameByPostAndStackNameType(Post post, String stackNameType);
}
