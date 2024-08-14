package com.gongjakso.server.domain.contest.repository;

import com.gongjakso.server.domain.contest.dto.response.ContestListRes;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

public class ContestRepositorylmpl implements ContestRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public ContestRepositorylmpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ContestListRes searchList(String word, String sort, Pageable pageable) {
        return null;
    }
}
