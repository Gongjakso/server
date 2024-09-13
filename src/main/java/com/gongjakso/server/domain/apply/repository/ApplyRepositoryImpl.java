package com.gongjakso.server.domain.apply.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.apply.dto.response.ApplyRes;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.gongjakso.server.domain.apply.entity.QApply.apply;

@RequiredArgsConstructor
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<ApplyRes> findByMemberAndPage(Member member, Pageable pageable) {
        List<Apply> applyList = queryFactory
                .selectFrom(apply)
                .leftJoin(apply.team).fetchJoin()
                .leftJoin(apply.member).fetchJoin()
                .leftJoin(apply.portfolioInfo.portfolio).fetchJoin()
                .leftJoin(apply.team.member).fetchJoin()
                .where(apply.member.eq(member), apply.deletedAt.isNull())
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

    public Optional<Apply> findApplyDetails(Long applyId) {
        return Optional.ofNullable(queryFactory
                .select(apply)
                .from(apply)
                .leftJoin(apply.member).fetchJoin()
                .leftJoin(apply.team).fetchJoin()
                .leftJoin(apply.team.member).fetchJoin()
                .leftJoin(apply.portfolioInfo.portfolio).fetchJoin()
                .where(apply.id.eq(applyId), apply.deletedAt.isNull())
                .fetchOne());
    }

}