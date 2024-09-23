package com.gongjakso.server.domain.portfolio.repository;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long>, PortfolioRepositoryCustom{
    Optional<Portfolio> findByIdAndDeletedAtIsNull(Long portfolioId);
    long countByDeletedAtIsNull();
    Optional<Portfolio> findPortfolioById(Long portfolioId);
}
