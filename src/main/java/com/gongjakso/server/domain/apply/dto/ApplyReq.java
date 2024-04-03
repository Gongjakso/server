package com.gongjakso.server.domain.apply.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.apply.enumerate.ApplyType;
import com.gongjakso.server.domain.apply.enumerate.PostType;
import com.gongjakso.server.domain.apply.enumerate.StackType;
import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.entity.StackName;
import com.gongjakso.server.global.exception.ApplicationException;
import com.gongjakso.server.global.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApplyReq(
        String application,
        String recruit_part,
        String recruit_role,
        String type,
        String applyType,
        List<String> stackNames
) {
    public Apply toEntity(Member member, Post post){

        List<StackType> stackTypeList = new ArrayList<>();
        for (String stackName : stackNames) {
            try {
                StackType stackType = StackType.valueOf(stackName.toUpperCase());
                stackTypeList.add(stackType);
            } catch (IllegalArgumentException e) {
                throw new ApplicationException(ErrorCode.INVALID_STACK_TYPE_EXCEPTION);
            }
        }

        return Apply.builder()
                .member(member)
                .post(post)
                .application(application)
                .recruit_part(recruit_part)
                .recruit_role(recruit_role)
                .type(PostType.valueOf(type))
                .applyType(ApplyType.NONE)
                .stackTypeList(stackTypeList)
                .build();
    }
}
