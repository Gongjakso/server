package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;
import com.gongjakso.server.domain.post.entity.Post;

import java.util.List;

public record ApplyList(
    Long apply_id,
    String name,
    String state
) {
    public static ApplyList of(Apply apply,String state){
        return new ApplyList(apply.getApplyId(),apply.getMember().getName(),state);
    }
}
