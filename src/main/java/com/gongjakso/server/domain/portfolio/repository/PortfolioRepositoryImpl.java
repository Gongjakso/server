package com.gongjakso.server.domain.portfolio.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.gongjakso.server.domain.portfolio.entity.QPortfolio.portfolio;

@RequiredArgsConstructor
public class PortfolioRepositoryImpl implements PortfolioRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<Portfolio> findByMemberAndDeletedAtIsNull(Member member) {
        return queryFactory
                .selectFrom(portfolio)
                .where(portfolio.member.eq(member)
                        .and(portfolio.deletedAt.isNull()))
                .fetch();
    }

    public Boolean hasExistPortfolioByMember(Member member){
        return queryFactory
                .selectFrom(portfolio)
                .where(portfolio.member.eq(member)
                        .and(portfolio.fileUri.isNotNull().or(portfolio.notionUri.isNotNull()))
                        .and(portfolio.deletedAt.isNull()))
                .fetchFirst()!=null;
    }

    public Boolean hasExistPortfolioById(Member member,Long id){
        return queryFactory
                .selectFrom(portfolio)
                .where(portfolio.member.eq(member)
                        .and(portfolio.id.eq(id))
                        .and(portfolio.fileUri.isNull())
                        .and(portfolio.notionUri.isNull())
                        .and(portfolio.portfolioData.isNull())
                        .and(portfolio.deletedAt.isNull()))
                .fetchFirst()!=null;
    }
}
