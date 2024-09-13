package com.gongjakso.server.domain.apply.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.entity.PortfolioInfo;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.apply.enumerate.ApplyStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ApplyReq(

        @Nullable
        Long portfolioId,

        @NotNull
        Boolean isPrivate,

        @Size(max = 500)
        String body,

        @Size(max = 20)
        @NotNull
        ApplyStatus status,

        @Size(max = 20)
        @NotNull
        String part
) {
    public static Apply toEntity(ApplyReq req, Team team, Member member, @Nullable Portfolio portfolio) {
        PortfolioInfo portfolioInfo = portfolio != null
                ? PortfolioInfo.ofPortfolio(portfolio)
                : PortfolioInfo.ofPrivate();

        return Apply.builder()
                .team(team)
                .member(member)
                .portfolioInfo(portfolioInfo)
                .body(req.body())
                .status(req.status())
                .part(req.part())
                .build();
    }
}
