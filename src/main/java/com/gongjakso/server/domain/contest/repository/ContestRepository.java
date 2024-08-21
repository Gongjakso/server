package com.gongjakso.server.domain.contest.repository;

import com.gongjakso.server.domain.contest.entity.Contest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContestRepository extends JpaRepository<Contest, Long>, ContestRepositoryCustom {
    @Modifying
    @Query("UPDATE Contest c SET c.view = c.view + 1 WHERE c.id = :id")
    void updateView(@Param("id") Long id);
}
