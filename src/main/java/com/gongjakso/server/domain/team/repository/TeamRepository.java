package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * 팀 ID를 조건으로 하여 팀을 조회한다. (논리적 삭제된 데이터는 조회되지 않음)
     * @param teamId 팀 ID
     * @return Optional<Team>
     */
    Optional<Team> findByIdAndDeletedAtIsNull(Long teamId);
}
