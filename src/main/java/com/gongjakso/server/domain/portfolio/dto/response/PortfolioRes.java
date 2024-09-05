package com.gongjakso.server.domain.portfolio.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PortfolioRes (

) {
}

