package com.gongjakso.server.global.security.naver.dto;

import jakarta.validation.constraints.Null;
import lombok.Builder;

@Builder
public record NaverToken(
        String access_token,
        @Null
        String refresh_token,
        int expires_in,
        String token_type,
        String error,
        String error_description
) {
}