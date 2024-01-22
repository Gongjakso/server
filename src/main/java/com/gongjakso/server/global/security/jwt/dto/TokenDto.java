package com.gongjakso.server.global.security.jwt.dto;

import lombok.Builder;

@Builder
public record TokenDto(String atk, String rtk) {
}
