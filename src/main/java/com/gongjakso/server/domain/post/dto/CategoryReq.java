package com.gongjakso.server.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
public record CategoryReq (
    @Schema(
            description = "공고 카테고리(역할)를 (PLAN | DESIGN | FE | BE | ETC | LATER)의 ENUM으로 관리",
            allowableValues = {"PLAN","DESIGN","FE","BE","ETC","LATER"}
    )
    String categoryType,

    @Schema(
            description = "공고 카테고리(역할)의 인원수를 size로 관리",
            defaultValue = "0"
    )
    Integer size
){}
