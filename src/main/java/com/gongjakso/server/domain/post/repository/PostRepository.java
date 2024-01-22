package com.gongjakso.server.domain.post.repository;

import com.gongjakso.server.domain.post.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Contest, Long> {

}
