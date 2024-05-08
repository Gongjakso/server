package com.gongjakso.server.domain.apply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.enumerate.PostType;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import jakarta.validation.constraints.Null;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApplyReq(
        String application,
        String recruit_part,
        String type,
        String applyType,
        @Null
        List<String> stack
) {
    public Apply toEntity(Member member, Post post) {
        return Apply.builder()
                .member(member)
                .post(post)
                .application(application)
                .recruit_part(recruit_part)
                .type(PostType.valueOf(type))
                .applyType(ApplyType.NONE)
                .build();
    }
}
