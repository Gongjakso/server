package com.gongjakso.server.domain.team.repository;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyStatus;
import com.gongjakso.server.domain.apply.repository.ApplyRepository;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
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

import java.time.LocalDate;
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
    private final ApplyRepository applyRepository;

    public Optional<Team> findTeamById(Long id) {
        return Optional.ofNullable(queryFactory
                .selectFrom(team)
                .join(team.member).fetchJoin()
                .where(team.id.eq(id)
                        .and(team.deletedAt.isNull()))
                .fetchOne());
    }

    public Page<SimpleTeamRes> findPaginationWithContest(Long contestId, String province, String district, Pageable pageable) {
        List<TeamStatus> teamStatusList = Arrays.asList(TeamStatus.RECRUITING, TeamStatus.EXTENSION);

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
                        team.status.in(teamStatusList),
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
        List<TeamStatus> teamStatusList = Arrays.asList(TeamStatus.RECRUITING, TeamStatus.EXTENSION);

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
                        .and(team.status.in(teamStatusList))
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
        List<TeamStatus> teamStatusList = Arrays.asList(TeamStatus.RECRUITING, TeamStatus.EXTENSION, TeamStatus.CANCELED, TeamStatus.CLOSED);

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
        List<TeamStatus> teamStatusList = Arrays.asList(TeamStatus.ACTIVE, TeamStatus.FINISHED);

        List<Team> teamList = queryFactory
                .select(team)
                .from(team)
                .leftJoin(apply).on(apply.team.id.eq(team.id)
                        .and(apply.member.id.eq(memberId))
                        .and(apply.deletedAt.isNull()))
                .where(
                        (
                                team.member.id.eq(memberId)
                                        .and(team.status.in(teamStatusList))
                                        .and(team.deletedAt.isNull())
                        )
                                .or(
                                        apply.member.id.eq(memberId)
                                                .and(apply.status.eq(ApplyStatus.ACCEPTED))
                                                .and(apply.team.status.in(teamStatusList))
                                                .and(apply.team.deletedAt.isNull())
                                )
                )
                .orderBy(team.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<SimpleTeamRes> content = teamList.stream()
                .map(team -> {
                    Optional<Apply> apply = applyRepository
                            .findByTeamIdAndMemberIdAndDeletedAtIsNull(team.getId(), memberId);

                    String applyPart = apply.map(Apply::getPart).orElse(null);

                    return SimpleTeamRes.of(team, applyPart);
                })
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

    public Boolean equalsLeaderIdAndMember(Portfolio portfolio, Member member) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(apply.portfolioInfo.portfolio.isNotNull())
                .and(apply.portfolioInfo.portfolio.eq(portfolio))
                .and(apply.deletedAt.isNull())
                .and(team.deletedAt.isNull());

        booleanBuilder.and(team.member.eq(member));

        return queryFactory
                .selectOne()
                .from(apply)
                .join(apply.team, team)
                .where(booleanBuilder)
                .fetchFirst() != null;
    }

    public List<Team> findAllByRecruitFinishedAtAndStartedAtBeforeAndFinishedAtAfter(LocalDate today) {
        return queryFactory
                .selectFrom(team)
                .where(
                        team.deletedAt.isNull(),
                        team.recruitFinishedAt.before(LocalDate.from(today)),
                        team.startedAt.before(LocalDate.from(today)),
                        team.finishedAt.after(LocalDate.from(today))
                )
                .fetch();
    }

    public List<Team> findAllByTeamStatusAndFinishedAtBefore(LocalDate today) {
        return queryFactory
                .selectFrom(team)
                .where(
                        team.deletedAt.isNull(),
                        team.status.eq(TeamStatus.valueOf(TeamStatus.ACTIVE.name())),
                        team.finishedAt.before(LocalDate.from(today))
                )
                .fetch();
    }
}
