package com.gongjakso.server.domain.post.controller;

import com.gongjakso.server.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@Tag(name = "Post", description = "공고 관련 API")
public class PostController {
    private final PostService postService;

//    @PostMapping
//    public ResponseEntity<> createPost() {
//
//    }

    /** 모집글 게시(CREATE) */
//    @PostMapping("/posts")
//    public ResponseEntity<PostCreationResponse> createPost(
//            @RequestBody @Validated PostCreationRequest request,
//            @AuthenticationPrincipal OAuth2User user) {
//
//        String email = user.getName();
//        Post post = postService.createPost(request, email);
//        keywordService.addKeywords(post, request);
//
//        return ResponseEntity.ok(
//                PostCreationResponse.builder()
//                        .id(post.getId())
//                        .title(post.getThumbnail().getTitle())
//                        .createdDate(post.getCreatedDate())
//                        .build());
//    }
}
