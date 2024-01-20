package com.gongjakso.server.domain.member.dto;

public record MemberReq(String name,
                        String status,
                        String major,
                        String job) {
}
