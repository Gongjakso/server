package com.gongjakso.server.domain.portfolio.repository;

import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long>, PortfolioRepositoryCustom{
    Optional<Portfolio> findByIdAndDeletedAtIsNull(Long portfolioId);
    long countByDeletedAtIsNull();
}
