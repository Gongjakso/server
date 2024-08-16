package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.entity.Team;

import java.util.Optional;

public interface TeamRepositoryCustom {

    Optional<Team> findTeamById(Long id);

}
