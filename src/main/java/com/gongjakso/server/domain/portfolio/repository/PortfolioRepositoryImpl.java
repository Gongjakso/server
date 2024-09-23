package com.gongjakso.server.domain.portfolio.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.gongjakso.server.domain.portfolio.entity.QPortfolio.portfolio;

@RequiredArgsConstructor
public class PortfolioRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    public List<Portfolio> findByMemberAndDeletedAtIsNull(Member member) {
        return queryFactory
                .selectFrom(portfolio)
                .where(portfolio.member.eq(member)
                        .and(portfolio.deletedAt.isNull()))
                .fetch();
    }

    public Boolean hasExistPortfolioByMember(Member member,String condition){
        return queryFactory
                .selectFrom(portfolio)
                .where(portfolio.member.eq(member)
                        .and(conditionEq(condition))
                        .and(portfolio.deletedAt.isNull()))
                .fetchFirst()!=null;
    }

    public BooleanExpression conditionEq(String condition){
        if(condition.equals("or")){
            return portfolio.fileUri.isNotNull().or(portfolio.notionUri.isNotNull());
        }else if (condition.equals("and")){
            return portfolio.fileUri.isNull().and(portfolio.notionUri.isNull());
        }
        return null;
    }
}
