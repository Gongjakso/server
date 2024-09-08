package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long>, ApplyRepositoryCustom {
    Boolean existsByMemberIdAndTeamIdAndDeletedAtIsNull(Long memberId, Long teamId);
    Optional<Apply> findByIdAndDeletedAtIsNull(Long applyId);
}
