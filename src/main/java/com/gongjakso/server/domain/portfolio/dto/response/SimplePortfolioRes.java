package com.gongjakso.server.domain.portfolio.dto.response;

import com.gongjakso.server.domain.portfolio.entity.Portfolio;

import java.time.LocalDateTime;

public record SimplePortfolioRes(
        Long PortfolioId,
        String PortfolioTitle,
        LocalDateTime modifiedAt
) {
    public static SimplePortfolioRes of(Portfolio portfolio) {
        return new SimplePortfolioRes(
                portfolio.getId(),
                portfolio.getTitle(),
                portfolio.getModifiedAt()
        );
    }
}
