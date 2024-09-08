package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.team.dto.ApplyRes;
import com.gongjakso.server.domain.team.entity.Apply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.gongjakso.server.domain.team.entity.QApply.apply;

@RequiredArgsConstructor
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<ApplyRes> findByMemberAndPage(Member member, Pageable pageable) {
        List<Apply> applyList = queryFactory
                .selectFrom(apply)
                .where(apply.member.eq(member), apply.deletedAt.isNull())
                .join(apply.team).fetchJoin()
                .join(apply.member).fetchJoin()
                .join(apply.portfolio).fetchJoin()
                .orderBy(apply.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<ApplyRes> content = applyList.stream()
                .map(ApplyRes::of)
                .toList();

        Long total = queryFactory
                .select(apply.count())
                .from(apply)
                .where(apply.member.eq(member), apply.deletedAt.isNull())
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    public Optional<Apply> findApplyWithTeam(Long applyId) {
        return Optional.ofNullable(queryFactory
                .select(apply)
                .from(apply)
                .join(apply.team).fetchJoin()
                .where(apply.id.eq(applyId), apply.deletedAt.isNull())
                .fetchOne());
    }

}