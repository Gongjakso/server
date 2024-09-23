package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.team.dto.response.SimpleTeamRes;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.team.enumerate.TeamStatus;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
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

        if(province != null && !province.isEmpty()) {
            builder.and(team.province.eq(province));
        }
        if(district != null && !district.isEmpty()) {
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
                .orderBy(getSort(pageable).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<SimpleTeamRes> content = teamList.stream()
                .map(SimpleTeamRes::of)
                .toList();

        Long total = queryFactory.select(team.count())
                .from(team)
                .where(
                        team.contest.id.eq(contestId),
                        team.deletedAt.isNull(),
                        builder
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, (total != null) ? total : 0);
    }

    public Page<SimpleTeamRes> findPaginationWithoutContest(String province, String district, String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if(province != null && !province.isEmpty()) {
            builder.and(team.province.eq(province));
        }
        if(district != null && !district.isEmpty()) {
            builder.and(team.district.eq(district));
        }
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(team.title.containsIgnoreCase(keyword).or(team.body.containsIgnoreCase(keyword)));
        }

        List<Team> teamList = queryFactory
                .select(team)
                .from(team)
                .where(team.deletedAt.isNull()
                        .and(builder))
                .orderBy(getSort(pageable).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<SimpleTeamRes> content = teamList.stream()
                .map(SimpleTeamRes::of)
                .toList();

        Long total = queryFactory.select(team.count())
                .from(team)
                .where(
                        team.deletedAt.isNull(),
                        builder
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, (total != null) ? total : 0);
    }

    public Page<SimpleTeamRes> findRecruitPagination(Long memberId, Pageable pageable) {
        List<TeamStatus> teamStatusList = Arrays.asList(TeamStatus.RECRUITING, TeamStatus.EXTENSION);

        List<Team> teamList = queryFactory
                .select(team)
                .from(team)
                .where(
                        team.member.id.eq(memberId),
                        team.status.in(teamStatusList),
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
        List<TeamStatus> teamStatusList = Arrays.asList(TeamStatus.ACTIVE, TeamStatus.FINISHED);

        List<Team> teamList = queryFactory
                .select(team)
                .from(team)
                .innerJoin(apply).on(apply.team.id.eq(team.id).and(apply.deletedAt.isNull()))
                .where(
                        team.status.in(teamStatusList),
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
                        team.status.in(teamStatusList),
                        team.deletedAt.isNull()
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, (total != null) ? total : 0);
    }

    public Page<SimpleTeamRes> findParticipatePagination(Long memberId, Pageable pageable) {
        List<TeamStatus> teamStatusList = Arrays.asList(TeamStatus.RECRUITING, TeamStatus.EXTENSION, TeamStatus.CANCELED, TeamStatus.CLOSED);

        List<Team> teamList = queryFactory
                .select(team)
                .from(team)
                .innerJoin(apply).on(apply.team.id.eq(team.id).and(apply.deletedAt.isNull()))
                .where(
                        team.status.in(teamStatusList),
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
                        team.status.in(teamStatusList),
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
                        team.id.in(teamIdList),
                        team.deletedAt.isNull()
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, (total != null) ? total : 0);
    }

    private List<OrderSpecifier<?>> getSort(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();

        if(pageable.getSort().isEmpty()) {
            orderSpecifierList.add(team.createdAt.desc());
            return orderSpecifierList;
        }

        for(Sort.Order order : pageable.getSort()) {
            System.out.println(order.getProperty());
            switch (order.getProperty())  {
                case "createdAt":
                    orderSpecifierList.add((order.isAscending()) ? team.createdAt.asc() : team.createdAt.desc());
                    break;
                case "scrap":
                    orderSpecifierList.add((order.isAscending()) ? team.scrapCount.asc() : team.scrapCount.desc());
                    break;
                default:
                    throw new ApplicationException(ErrorCode.INVALID_SORT_EXCEPTION);
            }
        }

        return orderSpecifierList;
    }
}
