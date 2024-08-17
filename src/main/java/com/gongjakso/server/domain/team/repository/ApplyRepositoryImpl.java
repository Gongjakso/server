package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.team.entity.Apply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.gongjakso.server.domain.team.entity.QApply.apply;

@Repository
@RequiredArgsConstructor
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Boolean existsByMemberAndTeam(Long memberId, Long teamId) {
        return queryFactory
                .selectOne()
                .from(apply)
                .where(apply.member.id.eq(memberId)
                        .and(apply.team.id.eq(teamId)))
                .fetchFirst() != null;
    }

    @Override
    public Page<Apply> findByMemberAndPage(Member member, Pageable pageable) {
        List<Apply> applyList = queryFactory
                .selectDistinct(apply)
                .from(apply)
                .where(apply.member.eq(member))
                .join(apply.team).fetchJoin()
                .join(apply.member).fetchJoin()
                .join(apply.portfolio).fetchJoin()
                .orderBy(apply.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(apply.count())
                .from(apply)
                .where(apply.member.eq(member))
                .fetchOne();

        return new PageImpl<>(applyList, pageable, total != null ? total : 0L);
    }


}