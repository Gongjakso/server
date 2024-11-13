package com.gongjakso.server.domain.contest.repository;

import com.gongjakso.server.domain.contest.entity.Contest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.gongjakso.server.domain.contest.entity.QContest.contest;

public class ContestRepositoryImpl implements ContestRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public ContestRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Contest> searchList(String word, String sortAt, Pageable pageable) {
        List<Contest> contestList = queryFactory
                .selectDistinct(contest)
                .from(contest)
                .where(wordEq(word))
                .orderBy(arg(sortAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory
                .select(contest.count())
                .from(contest)
                .where(wordEq(word))  // Full-Text Search 조건 추가
                .fetchOne();

        return new PageImpl<>(contestList,pageable,total);
    }

    private OrderSpecifier<?> arg(String sortAt){
        if("VIEW".equals(sortAt)){
            return contest.view.desc();//조회순
        }
        return contest.createdAt.desc(); //최신순
    }

    private BooleanExpression wordEq(String word) {
        if (word == null || word.trim().isEmpty()) {
            return null;
        }

        return Expressions.booleanTemplate(
                "function('match', {0}, {1}, {2})",
                contest.title,
                contest.body,
                word
        );
    }
}
