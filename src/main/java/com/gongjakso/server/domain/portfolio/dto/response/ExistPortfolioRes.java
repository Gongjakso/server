package com.gongjakso.server.domain.portfolio.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExistPortfolioRes(
        String fileUri,
        String notionUri
) {
    public static ExistPortfolioRes of(Portfolio portfolio) {
        return new ExistPortfolioRes(
                portfolio.getFileUri(),
                portfolio.getNotionUri()
        );
    }
}
