package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;
import lombok.Builder;

@Builder
public record ApplyList(
    Long apply_id,
    String name,
    String state,
    Boolean is_canceled
) {
    public static ApplyList of(Apply apply,String state){
        return ApplyList.builder()
                .apply_id(apply.getApplyId())
                .name(apply.getMember().getName())
                .state(state)
                .is_canceled(apply.getIsCanceled())
                .build();
    }
}
