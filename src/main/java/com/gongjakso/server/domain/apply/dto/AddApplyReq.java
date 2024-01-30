package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.PostType;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;

public record AddApplyReq(
        String application,
        String recruit_part,
        String type,
        Boolean is_pass,
        Boolean is_open
) {
    public Apply toEntity(Member member, Post post_id){
        return Apply.builder()
                .member(member)
                .post(post_id)
                .application(application)
                .recruit_part(recruit_part)
                .type(PostType.valueOf(type))
                .is_pass(false)
                .is_open(false)
                .build();
    }
}
