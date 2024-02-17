package com.gongjakso.server.domain.post.dto;

import java.util.List;

public record CalendarRes(
        List<ScrapPost> scrapPosts
) {
    public static CalendarRes of(List<ScrapPost> scrapPosts){
        return new CalendarRes(scrapPosts);
    }
}
