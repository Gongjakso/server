package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;

public record ApplyList(
    Long apply_id,
    String name,
    String state
) {
    public static ApplyList of(Apply apply,String state){
        return new ApplyList(apply.getApplyId(),apply.getMember().getName(),state);
    }
}
