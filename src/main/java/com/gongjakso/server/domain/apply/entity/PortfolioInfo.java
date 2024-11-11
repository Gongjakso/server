package com.gongjakso.server.domain.apply.entity;

import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", columnDefinition = "bigint")
    private Portfolio portfolio;

    private boolean isPrivate;

    private String dataType;

    public static PortfolioInfo ofPortfolio(Portfolio portfolio, boolean isPrivate, String dataType) {
        return PortfolioInfo.builder()
                .portfolio(portfolio)
                .isPrivate(isPrivate)
                .dataType(dataType)
                .build();
    }
}
