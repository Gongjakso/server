package com.gongjakso.server.domain.contest.repository;

import com.gongjakso.server.domain.contest.entity.Contest;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContestRepository extends JpaRepository<Contest, Long>, ContestRepositoryCustom {
    @Query("update Contest c set c.view = :view")
    void updateView(@Param("view") int view);
}
