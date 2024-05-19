package com.gongjakso.server.domain.post.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus {

    RECRUITING("모집 중"),
    CANCEL("모집 취소"),
    CLOSE("모집 마감"),
    ACTIVE("활동 중"),
    COMPLETE("활동 완료"),
    EXTENSION("모집 연장");

    private final String status;
}
