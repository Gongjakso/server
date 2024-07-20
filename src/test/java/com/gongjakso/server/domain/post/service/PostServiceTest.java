package com.gongjakso.server.domain.post.service;

import com.gongjakso.server.domain.post.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PostServiceTest {

    @MockBean
    PostService postService;
    Post post = null;

    @BeforeEach
    void beforeEach() {

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