package com.gongjakso.server.domain.apply.dto;

import lombok.Getter;

@Getter
public record ApplyMemberRes(
        String application,
        String recruit_part,
        String[] category
) {

}
