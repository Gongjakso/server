package com.gongjakso.server.domain.apply.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.portfolio.entity.Portfolio;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.entity.PortfolioInfo;
import com.gongjakso.server.domain.team.entity.Team;
import com.gongjakso.server.domain.apply.enumerate.ApplyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ApplyReq(

        @Nullable
        @Schema(description = "포트폴리오 ID", example = "1")
        Long portfolioId,

        @NotNull
        @Schema(description = "포트폴리오 공개 설정", example = "false")
        Boolean isPrivate,

        @Size(max = 500)
        @Schema(description = "지원 이유", example = "저는 데이터 사이언스 과목을 수강하며 데이터에 관한 기본적인 내용들을 배우며 이런 데이터를 잘 활용하고, 이용하는 것이 중요한 역량이 될 것 같다고 판단했습니다. 그래서 관련된 역량을 쌓고자 공모전에 출품하고 싶다는 생각을 가지게 되었고, 공공데이터 공모전이 적합하다고 생각했습니다!")
        String body,

        @Size(max = 20)
        @NotNull
        @Schema(description = "지원 상태", example = "COMPLETED")
        ApplyStatus status,

        @Size(max = 20)
        @NotNull
        @Schema(description = "지원 파트", example = "기획")
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
