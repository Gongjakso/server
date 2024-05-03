package com.gongjakso.server.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record StackNameReq (
    @Schema(
            description = "사용 기술스택을 (REACT | TYPESCRIPT | JAVASCRIPT | NEXTJS | NODEJS | JAVA | SPRING | KOTLIN | SWIFT | FLUTTER | FIGMA | ETC)의 ENUM으로 관리",
            allowableValues = {"REACT","TYPESCRIPT","JAVASCRIPT","NEXTJS","NODEJS","JAVA","SPRING","KOTLIN","SWIFT","FLUTTER","FIGMA","ETC"}
    )
    String stackNameType
){}
