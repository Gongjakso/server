package com.gongjakso.server.domain.apply.repository;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplyRepository extends JpaRepository<Apply, Long>, ApplyRepositoryCustom {
    Boolean existsByMemberIdAndTeamIdAndDeletedAtIsNull(Long memberId, Long teamId);
    Optional<Apply> findByIdAndDeletedAtIsNull(Long applyId);
    Optional<Apply> findByTeamIdAndMemberIdAndDeletedAtIsNull(Long teamId, Long memberId);
    List<Apply> findAllByTeamIdAndDeletedAtIsNull(Long teamId);
    int countByTeamIdAndStatusAndDeletedAtIsNull(Long teamId, ApplyStatus status);
}
