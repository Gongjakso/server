package com.gongjakso.server.domain.portfolio.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimplePortfolioRes(
        Long PortfolioId,
        String PortfolioName,
        LocalDateTime modifiedAt,
        Boolean isRegistered
) {
    public static SimplePortfolioRes of(Portfolio portfolio, Boolean isRegistered) {
        return new SimplePortfolioRes(
                isRegistered ? portfolio.getId() : null,
                isRegistered ? portfolio.getPortfolioName() : null,
                isRegistered ? portfolio.getModifiedAt() : null,
                isRegistered
        );
    }
}
