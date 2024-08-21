package com.gongjakso.server.domain.team.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SimpleTeamRes(
    @Schema(description = "팀 ID", example = "1")
    Long id,

    @Schema(description = "팀 제목", example = "광화문광장 숏폼 공모전 참여자 모집")
    String title,

    @Schema(description = "멤버 ID", example = "1")
    Long memberId,

    @Schema(description = "멤버 이름", example = "홍길동")
    Long memberName,

    @Schema(description = "모집 마감일", example = "2024-12-31")
    LocalDate recruitFinishedAt,

    @Schema(description = "활동 시작일", example = "2025-01-01")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDate startedAt,

    @Schema(description = "활동 종료일", example = "2025-01-31")
    LocalDate finishedAt,

    @Schema(description = "D-day", example = "10")
    int dDay,

    @Schema(description = "스크랩 수", example = "10")
    int scrapCount
) {

//    public SimpleTeamRes(Long id, String title, Long memberId, Long memberName, LocalDate recruitFinishedAt, LocalDate startedAt, LocalDate finishedAt, int scrapCount) {
//        this.id = id;
//        this.title = title;
//        this.memberId = memberId;
//        this.memberName = memberName;
//        this.recruitFinishedAt = recruitFinishedAt;
//        this.startedAt = startedAt;
//        this.finishedAt = finishedAt;
//        this.dDay = 10; //LocalDate.now() - recruitFinishedAt;
//        this.scrapCount = scrapCount;
//    }
}
