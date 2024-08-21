package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.dto.response.SimpleTeamRes;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.gongjakso.server.domain.team.entity.QTeam.team;

@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<SimpleTeamRes> findPagination(Long contestId, String province, String district, Pageable pageable) {
        List<SimpleTeamRes> content  = queryFactory
            .select(Projections.constructor(SimpleTeamRes.class,
                team.id,
                team.title,
                team.province,
                team.district,
                team.createdAt
            ))
            .from(team)
            .where(
                team.contest.id.eq(contestId),
                team.province.eq(province),
                team.district.eq(district),
                team.deletedAt.isNull()
            )
            .orderBy(team.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory.select(team.count())
            .from(team)
            .where(
                team.province.eq(province),
                team.district.eq(district),
                team.deletedAt.isNull()
            )
            .fetchOne();

        return new PageImpl<>(content, pageable, (total != null) ? total : 0);
    }
}
