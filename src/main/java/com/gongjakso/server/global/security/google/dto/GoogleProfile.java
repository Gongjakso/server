package com.gongjakso.server.global.security.google.dto;

public record GoogleProfile(
        String sub,
        String name,
        String given_name,
        String family_name,
        String picture,
        String email,
        boolean email_verified,
        String locale
) {
}
