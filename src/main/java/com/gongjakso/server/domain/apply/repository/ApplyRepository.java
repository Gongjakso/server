package com.gongjakso.server.domain.apply.repository;

import com.gongjakso.server.domain.apply.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyRepository extends JpaRepository<Apply,Long> {
}
