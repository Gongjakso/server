package com.gongjakso.server.domain.portfolio.vo;

import java.time.LocalDate;
import java.util.List;

public record PortfolioData (
        String portfolioName,
        List<Education> educationList,
        List<Work> workList,
        List<Activity> activityList,
        List<Award> awardList,
        List<Certificate> certificateList,
        List<Sns> snsList
) {
    public record Education (
            String school,
            String grade,
            Boolean isActive
    ) {
    }

    public record Work (
            String company,
            String partition,
            LocalDate enteredAt,
            LocalDate exitedAt,
            Boolean isActive,
            String detail
    ) {
    }

    public record Activity (
            String name,
            Boolean isActive
    ) {
    }

    public record Award (
            String contestName,
            String awardName,
            LocalDate awardDate
    ) {
    }

    public record Certificate (
            String name,
            String rating,
            LocalDate certificationDate
    ) {
    }

    public record Sns (
            String snsLink
    ) {
    }
}
