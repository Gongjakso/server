package com.gongjakso.server.domain.portfolio.dto.response;

import com.gongjakso.server.domain.portfolio.entity.Portfolio;

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
