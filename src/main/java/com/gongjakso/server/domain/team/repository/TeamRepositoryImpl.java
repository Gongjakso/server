package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.entity.QTeam;
import com.gongjakso.server.domain.team.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Team> findTeamById(Long id) {
        QTeam team = QTeam.team;
        return Optional.ofNullable(queryFactory
                .selectFrom(team)
                .where(team.id.eq(id)
                        .and(team.deletedAt.isNull()))
                .fetchOne());
    }
}
