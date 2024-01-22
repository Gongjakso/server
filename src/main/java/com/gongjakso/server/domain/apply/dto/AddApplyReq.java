package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.PostType;
import com.gongjakso.server.domain.member.entity.Member;

public record AddApplyReq(
        String application,
        String recruit_part,
        PostType type,
        Boolean is_pass,
        Boolean is_open
) {
    public Apply toEntity(Member member){
        return Apply.builder()
                .member(member)
                .application(application)
                .recruit_part(recruit_part)
                .type(type)
                .is_pass(false)
                .is_open(false)
                .build();
    }
}
