package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Optional<Scrap> findScrapByMemberIdAndTeamId(Long memberId, Long teamId);

    void deleteScrapByMemberIdAndTeamId(Long memberId, Long teamId);
}
