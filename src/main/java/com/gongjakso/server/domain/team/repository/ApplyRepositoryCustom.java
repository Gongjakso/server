package com.gongjakso.server.domain.team.repository;

public interface ApplyRepositoryCustom {

    Boolean existsByMemberAndTeam(Long memberId, Long teamId);
}
