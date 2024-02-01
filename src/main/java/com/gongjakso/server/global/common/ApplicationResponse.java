package com.gongjakso.server.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.global.exception.ErrorCode;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApplicationResponse<T>(
        LocalDateTime timestamp,
        Integer code,
        String message,
        T data
) {

    public static <T> ApplicationResponse<T> ok(T data) {
        return ApplicationResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.SUCCESS.getCode())
                .message(ErrorCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApplicationResponse<T> ok() {
        return ApplicationResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.SUCCESS.getCode())
                .message(ErrorCode.SUCCESS.getMessage())
                .build();
    }

    public static <T> ApplicationResponse<T> created(T data) {
        return ApplicationResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.SUCCESS.getCode())
                .message(ErrorCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApplicationResponse<T> created() {
        return ApplicationResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.SUCCESS.getCode())
                .message(ErrorCode.SUCCESS.getMessage())
                .build();
    }
}
