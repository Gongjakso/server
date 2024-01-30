package com.gongjakso.server.domain.apply.dto;

import com.gongjakso.server.domain.apply.entity.Apply;

public record ApplyList(
    Long apply_id,
    String name,
    String state
) {

}
