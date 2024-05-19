package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.repository.PostRepository;
import com.gongjakso.server.domain.post.util.PostUtilTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        Post post = PostUtilTest.buildPost();
    }

    @Test
    void create() {
        
    }

    @Test
    void read() {
    }

    @Test
    void modify() {
    }

    @Test
    void delete() {

    }
}