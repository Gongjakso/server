package com.gongjakso.server.domain.apply.entity;

import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioInfo {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", columnDefinition = "bigint")
    private Portfolio portfolio;

    private boolean isPrivate;

    public static PortfolioInfo ofPortfolio(Portfolio portfolio) {
        return PortfolioInfo.builder()
                .portfolio(portfolio)
                .isPrivate(false)
                .build();
    }
    
    public static PortfolioInfo ofPrivate() {
        return PortfolioInfo.builder()
                .portfolio(null)
                .isPrivate(true)
                .build();
    }
}
