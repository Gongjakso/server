package com.gongjakso.server.domain.contest.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Builder
public record ContestReq(
        @Size(min=1,max=150)
        String title,
        String body,
        String contestLink,
        @Size(min=1,max=100)
        String institution,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startedAt,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate finishedAt

) {

}
