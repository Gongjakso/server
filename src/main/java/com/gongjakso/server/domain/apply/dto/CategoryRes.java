package com.gongjakso.server.domain.apply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.List;

public record CategoryRes(
        List<String> category
) {
}
