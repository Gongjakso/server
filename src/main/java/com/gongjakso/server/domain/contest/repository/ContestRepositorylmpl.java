package com.gongjakso.server.domain.contest.repository;

import com.gongjakso.server.domain.contest.dto.response.ContestListRes;
import com.gongjakso.server.domain.contest.entity.Contest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ContestRepositorylmpl implements ContestRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public ContestRepositorylmpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //최신순으로 정렬
    //제목,본문 기준으로 검색
    @Override
    public ContestListRes searchList(String word, String sort, Pageable pageable) {
        List<Contest> contestList = queryFactory
                .selectDistinct(contest)
                .from(contest)
                .where()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return null;
    }
}
