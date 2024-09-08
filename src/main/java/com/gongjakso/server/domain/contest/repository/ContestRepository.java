package com.gongjakso.server.domain.contest.repository;

import com.gongjakso.server.domain.contest.entity.Contest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContestRepository extends JpaRepository<Contest, Long>, ContestRepositoryCustom {

    /**
     * 공모전 ID를 조건으로 하여 공모전을 조회한다. (논리적 삭제된 데이터는 조회되지 않음)
     * @param contestId 공모전 ID
     * @return Optional<Contest>
     */
    Optional<Contest> findByIdAndDeletedAtIsNull(Long contestId);
}
