package com.gongjakso.server.domain.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryReq {
    private String categoryType;
    private Integer size;
}
