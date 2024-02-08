package com.gongjakso.server.domain.post.controller;

import com.gongjakso.server.domain.member.entity.Member;
import com.gongjakso.server.domain.post.dto.PostReq;
import com.gongjakso.server.domain.post.dto.PostRes;
import com.gongjakso.server.domain.post.entity.Post;
import com.gongjakso.server.domain.post.service.PostService;
import com.gongjakso.server.global.security.PrincipalDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Tag(name = "Post", description = "공고 관련 API")
public class PostController {

    private final PostService postService;

//    @PostMapping("")
//    public ResponseEntity<Post> create(Member member, @RequestBody PostReq req) {
//        return ResponseEntity.ok(postService.create(member, req));
//    }

    @PostMapping("")
    public ResponseEntity<PostRes> create(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody PostReq req) {
        return ResponseEntity.ok(postService.create(principalDetails.getMember(), req));
    }

    @GetMapping("/?{post_id}")
    public ResponseEntity<PostRes> read(@PathVariable("id") Long id, @RequestBody PostReq req) {
        return ResponseEntity.ok(postService.read(id, req));
    }

    @PutMapping("/?{id}")
    public ResponseEntity<PostRes> modify(@PathVariable("id") Long id, @RequestBody PostReq req) {
        return ResponseEntity.ok(postService.modify(id, req));
    }
}