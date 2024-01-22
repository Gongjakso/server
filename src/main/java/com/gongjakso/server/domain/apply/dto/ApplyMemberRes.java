package com.gongjakso.server.domain.apply.dto;

public record ApplyMemberRes(
        String application,
        String recruit_part,
        String[] category
) {

}
