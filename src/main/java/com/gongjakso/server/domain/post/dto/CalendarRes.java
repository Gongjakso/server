package com.gongjakso.server.domain.post.dto;

import java.util.List;

public record CalendarRes(
        List<GetContestRes> scrapPosts
) {
    public static CalendarRes of(List<GetContestRes> scrapPosts){
        return new CalendarRes(scrapPosts);
    }
}
