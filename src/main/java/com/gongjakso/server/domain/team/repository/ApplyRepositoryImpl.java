package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.entity.QApply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Boolean existsByMemberAndTeam(Long memberId, Long teamId) {
        return queryFactory
                .selectOne()
                .from(QApply.apply)
                .where(QApply.apply.member.id.eq(memberId)
                        .and(QApply.apply.team.id.eq(teamId)))
                .fetchFirst() != null;
    }
}
