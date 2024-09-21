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
    @Query("SELECT EXISTS (" +
            "SELECT 1 FROM Portfolio po " +
            "WHERE po.member = :member AND (po.fileUri IS NOT NULL OR po.notionUri IS NOT NULL))")
    Boolean existsExistPortfolioByMember(@Param("member") Member member);
    Optional<Portfolio> findPortfolioById(Long portfolioId);
}
