package com.gongjakso.server.domain.apply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.enumerate.PostType;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApplyReq(
        @Size(max = 500,message = "지원서 최대 글자 수 500을 넘었습니다.")
        String application,
        @NotNull(message = "지원 파트를 정해야합니다.")
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
