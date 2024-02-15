package com.gongjakso.server.domain.post.dto;

import com.gongjakso.server.domain.post.entity.Post;

import java.util.List;

public record CalenderRes(
        List<ScrapPost> scrapPosts
) {
    public static CalenderRes of(List<ScrapPost> scrapPosts){
        return new CalenderRes(scrapPosts);
    }
}
