package com.gongjakso.server.domain.post.controller;

import com.gongjakso.server.domain.post.dto.PostDeleteRes;
import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.dto.PostRes;
import com.gongjakso.server.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Tag(name = "Post", description = "공고 관련 API")
public class PostController {
    private final PostService postService;

    // @PostMapping
    // public ResponseEntity<> createPost() {
    //
    // }

    /**
     * 모집글 게시(CREATE)
     */
    // @PostMapping("/posts")
    // public ResponseEntity<PostCreationResponse> createPost(
    // @RequestBody @Validated PostCreationRequest request,
    // @AuthenticationPrincipal OAuth2User user) {
    //
    // String email = user.getName();
    // Post post = postService.createPost(request, email);
    // keywordService.addKeywords(post, request);
    //
    // return ResponseEntity.ok(
    // PostCreationResponse.builder()
    // .id(post.getId())
    // .title(post.getThumbnail().getTitle())
    // .createdDate(post.getCreatedDate())
    // .build());
    // }

    @PutMapping("/?{id}")
    public ResponseEntity<PostRes> modify(@PathVariable("id") Long id, @RequestBody PostReq req) {
        return ResponseEntity.ok(postService.modify(id, req));
    }

    @PatchMapping("/?{id}")
    public ResponseEntity<PostDeleteRes> delete(@PathVariable("id") Long id){
        return ResponseEntity.ok(postService.delete(id));
    }
    
}