package com.gongjakso.server.global.security.google.dto;

import jakarta.validation.constraints.Null;
import lombok.Builder;

@Builder
public record GoogleToken(
        String access_token,
        @Null
        String refresh_token,
        int expires_in,
        String scope,
        String token_type,
        String id_token
) {
}
