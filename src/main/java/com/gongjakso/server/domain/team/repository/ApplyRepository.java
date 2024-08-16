package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyRepository extends JpaRepository<Apply, Long>, ApplyRepositoryCustom {
}
