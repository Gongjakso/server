package com.gongjakso.server.global.exception;

import java.time.LocalDateTime;


public record ErrorResponse(
        LocalDateTime timestamp,
        Integer code,
        String message) {

    public ErrorResponse(ErrorCode errorcode) {
        this(LocalDateTime.now().withNano(0), errorcode.getCode(), errorcode.getMessage());
    }
}
