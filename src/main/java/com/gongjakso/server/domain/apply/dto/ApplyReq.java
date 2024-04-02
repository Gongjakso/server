package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.enumerate.PostType;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;

public record ApplyReq(
        String application,
        String recruit_part,
        String recruit_role,
        String type,
        String applyType
) {
    public Apply toEntity(Member member, Post post_id){
        return Apply.builder()
                .member(member)
                .post(post_id)
                .application(application)
                .recruit_part(recruit_part)
                .recruit_role(recruit_role)
                .type(PostType.valueOf(type))
                .applyType(ApplyType.NONE)
                .build();
    }
}
