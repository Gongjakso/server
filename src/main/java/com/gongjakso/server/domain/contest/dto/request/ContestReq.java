package com.gongjakso.server.domain.contest.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Builder
public record ContestReq(
        @NotBlank
        @Size(min=1,max=150)
        String title,
        @NotEmpty
        String body,
        @NotEmpty
        String contestLink,
        @NotNull
        @Size(min=1,max=100)
        String institution,
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startedAt,
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate finishedAt

) {

}
