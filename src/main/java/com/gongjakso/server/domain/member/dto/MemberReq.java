package com.gongjakso.server.domain.member.dto;

import lombok.Builder;

@Builder
public record MemberReq(String email,
                        String name) {
}
