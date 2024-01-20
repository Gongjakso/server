package com.gongjakso.server.domain.member.dto;

import com.gongjakso.server.domain.member.entity.Member;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MemberRes(@NotNull Long memberId,
                        @NotNull String email) {

    public static MemberRes of(Member member) {
        return MemberRes.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .build();
    }
}
