package com.gongjakso.server.global.security.naver.dto;

import lombok.Builder;

@Builder
public record NaverProfile(
        String resultcode,
        String message,
        NaverProfileResponse response
) {
    public record NaverProfileResponse(
            String id,
            String name,
            String nickname,
            String profile_image,
            String email,
            String age,
            String birthday,
            String gender,
            String mobile,
            String mobile_e164,
            String birthyear
    ) {
    }
}
