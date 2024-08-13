package com.gongjakso.server.domain.contest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Builder
public record ContestReq(
        @NotBlank(message = "제목을 입력해주세요")
        String title,
        String body,
        String contestLink,
        String institution,
        LocalDate startedAt,
        LocalDate finishedAt

) {

}
