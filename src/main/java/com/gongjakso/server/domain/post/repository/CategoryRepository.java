package com.gongjakso.server.domain.post.repository;

import com.gongjakso.server.domain.post.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
