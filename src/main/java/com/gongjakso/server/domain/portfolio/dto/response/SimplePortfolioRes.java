package com.gongjakso.server.domain.portfolio.dto.response;

import com.gongjakso.server.domain.portfolio.entity.Portfolio;

import java.time.LocalDateTime;

public record SimplePortfolioRes(
        Long PortfolioId,
        String PortfolioName,
        LocalDateTime modifiedAt
) {
    public static SimplePortfolioRes of(Portfolio portfolio) {
        return new SimplePortfolioRes(
                portfolio.getId(),
                portfolio.getPortfolioName(),
                portfolio.getModifiedAt()
        );
    }
}
