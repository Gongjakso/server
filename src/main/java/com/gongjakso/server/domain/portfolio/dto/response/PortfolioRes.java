package com.gongjakso.server.domain.portfolio.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData;
import lombok.Builder;
import java.util.List;
import java.util.Objects;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PortfolioRes (
        Long id,
        String portfolioName,
        List<PortfolioData.Education> educationList,
        List<PortfolioData.Work> workList,
        List<PortfolioData.Activity> activityList,
        List<PortfolioData.Award> awardList,
        List<PortfolioData.Certificate> certificateList,
        List<PortfolioData.Sns> snsList,
        String dataType,
        String fileUri,
        String notionUri
) {
    public static PortfolioRes from(Portfolio portfolio) {
        PortfolioData portfolioData = portfolio.getPortfolioData();
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

        return new PortfolioRes(
                portfolio.getId(),
                portfolio.getPortfolioName(),
                portfolioData != null ? portfolioData.educationList() : null,
                portfolioData != null ? portfolioData.workList() : null,
                portfolioData != null ? portfolioData.activityList() : null,
                portfolioData != null ? portfolioData.awardList() : null,
                portfolioData != null ? portfolioData.certificateList(): null,
                portfolioData != null ? portfolioData.snsList(): null,
                dataType,
                Objects.equals(dataType, "NOTION") ? null : portfolio.getFileUri(),
                Objects.equals(dataType, "FILE") ? null : portfolio.getNotionUri()
        );
    }
}