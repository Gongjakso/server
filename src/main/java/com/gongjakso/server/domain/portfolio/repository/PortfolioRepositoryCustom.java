package com.gongjakso.server.domain.portfolio.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;

import java.util.List;

public interface PortfolioRepositoryCustom {
    List<Portfolio> findByMemberAndDeletedAtIsNull(Member member);
    Boolean hasExistPortfolioByMember(Member member);
    Boolean hasExistPortfolioById(Member member,Long id);
}
