package com.gongjakso.server.domain.post.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class StackNameReq {
    private String stackNameType;
    private Integer size;
}
