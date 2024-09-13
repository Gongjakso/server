package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.dto.response.SimpleTeamRes;
import com.gongjakso.server.domain.team.entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.gongjakso.server.domain.apply.entity.QApply.apply;
import static com.gongjakso.server.domain.team.entity.QScrap.scrap;
import static com.gongjakso.server.domain.team.entity.QTeam.team;

@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Optional<Team> findTeamById(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(team)
                .join(team.member).fetchJoin()
                .where(team.id.eq(id)
                        .and(team.deletedAt.isNull()))
                .fetchOne());
    }

    public Page<SimpleTeamRes> findPaginationWithContest(Long contestId, String province, String district, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if(province != null) {
            builder.and(team.province.eq(province));
        }
        if(district != null) {
            builder.and(team.district.eq(district));
        }

        List<Team> teamList  = queryFactory
                .select(team)
                .from(team)
                .where(
                        team.contest.id.eq(contestId),
                        team.deletedAt.isNull(),
                        builder
                )
                .orderBy(team.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<SimpleTeamRes> content = teamList.stream()
                .map(SimpleTeamRes::of)
                .toList();

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

    public Page<SimpleTeamRes> findPaginationWithoutContest(String province, String district, String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if(province != null) {
            builder.and(team.province.eq(province));
        }
        if(district != null) {
            builder.and(team.district.eq(district));
        }
        if (keyword != null) {
            builder.and(team.title.containsIgnoreCase(keyword).or(team.body.containsIgnoreCase(keyword)));
        }

        List<Team> teamList = queryFactory
                .select(team)
                .from(team)
                .where(team.deletedAt.isNull()
                        .and(builder))
                .orderBy(team.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<SimpleTeamRes> content = teamList.stream()
                .map(SimpleTeamRes::of)
                .toList();

        Long total = queryFactory.select(team.count())
                .from(team)
                .where(
                        team.province.eq(province),
                        team.district.eq(district),
                        team.title.containsIgnoreCase(keyword),
                        team.deletedAt.isNull()
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, (total != null) ? total : 0);
    }

    public Page<SimpleTeamRes> findRecruitPagination(Long memberId, Pageable pageable) {
        List<Team> teamList = queryFactory
                .select(team)
                .from(team)
                .where(
                        team.member.id.eq(memberId),
                        team.deletedAt.isNull()
                )
                .orderBy(team.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<SimpleTeamRes> content = teamList.stream()
                .map(SimpleTeamRes::of)
                .toList();

        Long total = queryFactory.select(team.count())
                .from(team)
                .where(
                        team.member.id.eq(memberId),
                        team.deletedAt.isNull()
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, (total != null) ? total : 0);
    }

    public Page<SimpleTeamRes> findApplyPagination(Long memberId, Pageable pageable) {
        List<Team> teamList = queryFactory
                .select(team)
                .from(team)
                .innerJoin(apply).on(apply.team.id.eq(team.id).and(apply.deletedAt.isNull()))
                .where(
                        team.deletedAt.isNull()
                )
                .orderBy(team.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<SimpleTeamRes> content = teamList.stream()
                .map(SimpleTeamRes::of)
                .toList();

        Long total = queryFactory.select(team.count())
                .from(team)
                .where(
                        team.deletedAt.isNull()
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, (total != null) ? total : 0);
    }

    public Page<SimpleTeamRes> findScrapPagination(Long memberId, Pageable pageable) {
        List<Long> teamIdList = queryFactory
                .select(scrap.team.id)
                .from(scrap)
                .where(scrap.member.id.eq(memberId))
                .fetch();

        List<Team> teamList = queryFactory
                .select(team)
                .from(team)
                .where(
                        team.id.in(teamIdList),
                        team.deletedAt.isNull()
                )
                .orderBy(team.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<SimpleTeamRes> content = teamList.stream()
                .map(SimpleTeamRes::of)
                .toList();

        Long total = queryFactory.select(team.count())
                .from(team)
                .where(
                        team.deletedAt.isNull()
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, (total != null) ? total : 0);
    }
}
