package com.gongjakso.server.domain.portfolio.dto.response;

import com.gongjakso.server.domain.portfolio.entity.Portfolio;

public record SimplePortfolioRes(
        Long PortfolioId,
        String PortfolioTitle
) {
    public static SimplePortfolioRes of(Portfolio portfolio) {
        return new SimplePortfolioRes(
                portfolio.getId(),
                portfolio.getTitle()
        );
    }
}
