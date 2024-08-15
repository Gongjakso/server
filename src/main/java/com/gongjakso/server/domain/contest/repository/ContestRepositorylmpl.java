package com.gongjakso.server.domain.contest.repository;

import com.gongjakso.server.domain.contest.dto.response.ContestListRes;
import com.gongjakso.server.domain.contest.dto.response.SearchContent;
import com.gongjakso.server.domain.contest.entity.Contest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.gongjakso.server.domain.contest.entity.QContest.contest;

public class ContestRepositorylmpl implements ContestRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public ContestRepositorylmpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //최신순으로 정렬
    //제목,본문 기준으로 검색
    @Override
    public SearchContent searchList(String word, String arrange, Pageable pageable) {
        List<Contest> contestList = queryFactory
                .selectDistinct(contest)
                .from(contest)
                .where(wordEq(word))
                .orderBy(arg(arrange))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory
                .select(contest.count())
                .from(contest)
                .where(wordEq(word))  // Full-Text Search 조건 추가
                .fetchOne();

        int totalPages = (int) Math.ceil((double) total / pageable.getPageSize());

        return new SearchContent(contestList,total,totalPages);
    }

    private OrderSpecifier<?> arg(String arrange){
//        if("A".equals(arrange)){
//            return //조회순
//        }
        return contest.createdAt.desc(); //최신순
    }

    private BooleanExpression wordEq(String word) {
        if (word == null || word.trim().isEmpty()) {
            return null;
        }

        String expression = "MATCH({0}, {1}) AGAINST ({2} IN NATURAL LANGUAGE MODE)";
        return Expressions.booleanTemplate(expression, contest.title, contest.body, word);
    }
}
