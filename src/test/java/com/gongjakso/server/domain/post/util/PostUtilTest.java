package com.gongjakso.server.domain.post.util;

import com.gongjakso.server.domain.post.entity.Post;

public class PostUtilTest {

    public static Post buildPost() {
        return Post.builder()
                .build();
    }
}
