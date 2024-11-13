package com.gongjakso.server.domain.portfolio.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimplePortfolioRes(
        Long PortfolioId,
        String PortfolioName,
        LocalDateTime modifiedAt,
        Boolean isRegistered,
        Boolean isExistedPortfolio,
        String dataType
) {
    public static SimplePortfolioRes of(Portfolio portfolio, Boolean isRegistered, Boolean isExistedPortfolio) {

        String dataType;
        if (portfolio.getFileUri() != null && portfolio.getNotionUri() != null) {
            dataType = "HYBRID";
        } else if (portfolio.getFileUri() != null) {
            dataType = "FILE";
        } else if (portfolio.getNotionUri() != null) {
            dataType = "NOTION";
        } else {
            dataType = null;
        }

        return new SimplePortfolioRes(
                isRegistered ? portfolio.getId() : null,
                isRegistered ? portfolio.getPortfolioName() : null,
                isRegistered ? portfolio.getModifiedAt() : null,
                isRegistered,
                isExistedPortfolio,
                isExistedPortfolio ? dataType : null
        );
    }
}
