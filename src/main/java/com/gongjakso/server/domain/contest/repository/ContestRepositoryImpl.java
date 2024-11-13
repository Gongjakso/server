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

import java.time.LocalDate;
import java.util.List;

import static com.gongjakso.server.domain.contest.entity.QContest.contest;

public class ContestRepositoryImpl implements ContestRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public ContestRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Contest> searchList(String word, String sortAt, Pageable pageable) {
        BooleanExpression filterCondition = wordEq(word);

        if ("ACTIVE".equals(sortAt)) {
            filterCondition = (filterCondition == null ? contest.finishedAt.goe(LocalDate.now())
                    : filterCondition.and(contest.finishedAt.goe(LocalDate.now())));
        }

        List<Contest> contestList = queryFactory
                .selectDistinct(contest)
                .from(contest)
                .where(filterCondition)
                .orderBy(arg(sortAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory
                .select(contest.count())
                .from(contest)
                .where(filterCondition)  // Full-Text Search 조건 추가
                .fetchOne();

        return new PageImpl<>(contestList,pageable,total);
    }

    private OrderSpecifier<?> arg(String sortAt){
        if("VIEW".equals(sortAt)){
            return contest.view.desc();//조회순
        }
        if ("ACTIVE".equals(sortAt)) {
            return contest.finishedAt.asc(); // 종료일 기준 오름차순
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
