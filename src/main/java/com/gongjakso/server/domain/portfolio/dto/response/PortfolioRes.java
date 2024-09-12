package com.gongjakso.server.domain.portfolio.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.gongjakso.server.domain.portfolio.vo.PortfolioData;
import lombok.Builder;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PortfolioRes (
        Long id,
        List<PortfolioData.Education> educationList,
        List<PortfolioData.Work> workList,
        List<PortfolioData.Activity> activityList,
        List<PortfolioData.Award> awardList,
        List<PortfolioData.Certificate> certificateList,
        List<PortfolioData.Sns> snsList
) {
    public static PortfolioRes from(Portfolio portfolio) {
        PortfolioData portfolioData = portfolio.getPortfolioData();
        return new PortfolioRes(
                portfolio.getId(),
                portfolioData.educationList(),
                portfolioData.workList(),
                portfolioData.activityList(),
                portfolioData.awardList(),
                portfolioData.certificateList(),
                portfolioData.snsList()
        );
    }
}