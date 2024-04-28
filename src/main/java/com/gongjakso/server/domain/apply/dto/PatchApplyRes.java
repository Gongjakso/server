package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.enumerate.PostType;
import com.gongjakso.server.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record PatchApplyRes(
        Long applyId,
        Long memberId,
        String memberName,
        Long postId,
        String application,
        String recruitPart,
        PostType type,
        ApplyType applyType,
        Boolean isCanceled
) {

    public static PatchApplyRes of(Apply apply, Member member) {
        return PatchApplyRes.builder()
                .applyId(apply.getApplyId())
                .memberId(apply.getMember().getMemberId())
                .memberName(member.getName())
                .postId(apply.getPost().getPostId())
                .application(apply.getApplication())
                .applyType(apply.getApplyType())
                .recruitPart(apply.getRecruit_part())
                .build();
    }
}
